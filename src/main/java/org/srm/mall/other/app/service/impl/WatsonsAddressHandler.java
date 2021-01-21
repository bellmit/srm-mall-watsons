package org.srm.mall.other.app.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.hzero.boot.scheduler.infra.annotation.JobHandler;
import org.hzero.boot.scheduler.infra.enums.ReturnT;
import org.hzero.boot.scheduler.infra.handler.IJobHandler;
import org.hzero.boot.scheduler.infra.tool.SchedulerTool;
import org.hzero.core.base.BaseConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.srm.mall.infra.constant.WatsonsConstants;
import org.srm.mall.other.api.dto.WatsonsAddressDTO;
import org.srm.mall.other.async.WatsonsAddressAsyncTask;
import org.srm.mall.other.domain.repository.WatsonsAddressRepository;
import org.srm.web.annotation.Tenant;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@JobHandler("watsonsUpdateAddress")
@Tenant(WatsonsConstants.TENANT_NUMBER)
@Service
public class WatsonsAddressHandler implements IJobHandler {

    @Autowired
    private WatsonsAddressRepository watsonsAddressRepository;

    @Autowired
    private WatsonsAddressAsyncTask addressAsyncTask;

    @Override
    public ReturnT execute(Map<String, String> map, SchedulerTool tool) {
        //查询当前时间前25小时之内的更新数据
        Date date = getSelectParamDate(tool);
        //查询屈臣氏的租户id
        Long tenantId = watsonsAddressRepository.selectTenantId(WatsonsConstants.TENANT_NUMBER);
        List<WatsonsAddressDTO> addressList = watsonsAddressRepository.selectUpdateAddressInfo(tenantId, date);
        if (CollectionUtils.isEmpty(addressList)){
            return ReturnT.SUCCESS;
        }
        List<WatsonsAddressDTO> successList = new ArrayList<>();
        List<WatsonsAddressDTO> errorList = new ArrayList<>();
        //查询详细地址和公司id
        selectInvOrgAddress(tenantId, addressList);
        for (WatsonsAddressDTO watsonsAddressDTO : addressList){
            if (StringUtils.isEmpty(watsonsAddressDTO.getAddress()) || ObjectUtils.isEmpty(watsonsAddressDTO.getCompanyId())){
                watsonsAddressDTO.setSuccess(false);
                watsonsAddressDTO.setResultMsg("该库存组织"+ watsonsAddressDTO.getInvOrganizationId() +"对应的公司id或地址为空:" + watsonsAddressDTO);
                errorList.add(watsonsAddressDTO);
            } else {
                watsonsAddressDTO.setSuccess(true);
                successList.add(watsonsAddressDTO);
            }
        }
        //判断是否有公司id和地址是否为空
        //查询经纬度
        List<WatsonsAddressDTO> resultList = selectBaiduReverseGeocoding(successList, tenantId);
        successList = resultList.stream().filter(WatsonsAddressDTO::getSuccess).collect(Collectors.toList());
        errorList.addAll(resultList.stream().filter(s -> !s.getSuccess()).collect(Collectors.toList()));
        if (CollectionUtils.isEmpty(successList)){
            tool.error("更新商城地址失败，成功数量： 0 , 失败数量：" + errorList.size());
            createErrorInfo(errorList, tool);
            return ReturnT.FAILURE;
        }
        //查询对应的映射表，找出商城地址编码
        List<WatsonsAddressDTO> list = selectRegionMapping(successList, tool);
        if (CollectionUtils.isEmpty(list)){
            return ReturnT.FAILURE;
        }
        //更新
        List<WatsonsAddressDTO> result = updateMallAddress(list, tenantId);
        List<WatsonsAddressDTO> successResults = result.stream().filter(WatsonsAddressDTO::getSuccess).collect(Collectors.toList());
        List<WatsonsAddressDTO> errorResults = result.stream().filter(s -> !s.getSuccess()).collect(Collectors.toList());
        //生成更新失败的日志信息
        errorList.addAll(errorResults);
        createErrorInfo(errorList, tool);
        //生成成功数据的信息
        tool.info("成功数据条数：" + successResults.size() + ", 失败数据条数：" + errorResults.size());
        return ReturnT.SUCCESS;
    }

    private Date getSelectParamDate(SchedulerTool tool){
        String schedulePram = tool.getJobDataDTO().getParam();
        Date date = null;
        try{
            if (!StringUtils.isEmpty(schedulePram)){
                JSONObject jsonObject = JSON.parseObject(schedulePram);
                String time = (String) jsonObject.get("time");
                if (!StringUtils.isEmpty(time)){
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(BaseConstants.Pattern.DATETIME);
                    date = simpleDateFormat.parse(time);
                }
            }
        } catch (ParseException e) {
            log.error("时间转换错误");
            e.printStackTrace();
        }
        if (date == null){
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) - 25);
            date = calendar.getTime();
        }
        return date;
    }

    private void selectInvOrgAddress(Long tenantId, List<WatsonsAddressDTO> list){
        List<Long> invOrganizationIdList = list.stream().map(WatsonsAddressDTO::getInvOrganizationId).collect(Collectors.toList());
        List<WatsonsAddressDTO> resultList = watsonsAddressRepository.selectInvorgAddress(invOrganizationIdList, tenantId);
        resultList = resultList.stream().filter(s -> !StringUtils.isEmpty(s.getAddress()) && !ObjectUtils.isEmpty(s.getCompanyId())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(resultList)){
            return;
        }
        Map<Long, WatsonsAddressDTO> map = resultList.stream().collect(Collectors.toMap(WatsonsAddressDTO::getInvOrganizationId, Function.identity(), (k1, k2) -> k1));
        for (WatsonsAddressDTO address : list){
            WatsonsAddressDTO watsonsAddressDTO = map.get(address.getInvOrganizationId());
            if (watsonsAddressDTO != null){
                address.setAddress(watsonsAddressDTO.getAddress());
                address.setCompanyId(watsonsAddressDTO.getCompanyId());
            }
        }
    }

    private List<WatsonsAddressDTO> selectBaiduReverseGeocoding(List<WatsonsAddressDTO> addressList, Long tenantId){
        List<WatsonsAddressDTO> resultList = new ArrayList<>();
        List<Future<WatsonsAddressDTO>> asyncCallbackList = new ArrayList<>();
        for (WatsonsAddressDTO address : addressList){
            Future<WatsonsAddressDTO> result = addressAsyncTask.asyncReverseGeocoding(tenantId, address);
            asyncCallbackList.add(result);
        }
        for (Future<WatsonsAddressDTO> future : asyncCallbackList){
            try{
                resultList.add(future.get(60, TimeUnit.SECONDS));
            }catch (InterruptedException | ExecutionException | TimeoutException e){
                log.error("异步查询百度逆地理编码接口异常:" + e);
            }
        }
        return resultList;
    }


    private List<WatsonsAddressDTO> updateMallAddress(List<WatsonsAddressDTO> addressList, Long tenantId){
        List<WatsonsAddressDTO> resultList = new ArrayList<>();
        List<Future<WatsonsAddressDTO>> asyncCallbackList = new ArrayList<>();
        for (WatsonsAddressDTO address : addressList){
            Future<WatsonsAddressDTO> result = addressAsyncTask.asyncUpdateMallAddress(tenantId, address);
            asyncCallbackList.add(result);
        }
        for (Future<WatsonsAddressDTO> future : asyncCallbackList){
            try{
                resultList.add(future.get(60, TimeUnit.SECONDS));
            }catch (InterruptedException | ExecutionException | TimeoutException e){
                log.error("更新商城地址失败:" + e);
            }
        }
        return resultList;
    }

    private List<WatsonsAddressDTO> selectRegionMapping(List<WatsonsAddressDTO> successList, SchedulerTool tool){
        //查询对应的映射表，找出商城地址编码
        List<Integer> adCodeList = successList.stream().map(s -> s.getAddressResult().getAddressComponent().getAdcode()).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(adCodeList)){
            tool.error("更新商城地址失败，查询百度逆地理编码返回的adcode为空");
            return new ArrayList<>();
        }
        //查询对应的映射
        List<WatsonsAddressDTO> mappingAddressList = watsonsAddressRepository.selectRegionMapping(adCodeList);
        if (CollectionUtils.isEmpty(mappingAddressList)){
            tool.error("更新商城地址失败，百度adcode与商城地址无映射，无映射的百度adcodes为：" + adCodeList);
            return new ArrayList<>();
        }
        Map<Integer, WatsonsAddressDTO> mappingAddressMap = mappingAddressList.stream().collect(Collectors.toMap(WatsonsAddressDTO::getAdCode, Function.identity(), (k1, k2) -> k1));
        for (WatsonsAddressDTO externalAddress : successList){
            //将查询出来的商城地址信息复制到结果对象中
            externalAddress.setAdCode(externalAddress.getAddressResult().getAddressComponent().getAdcode());
            WatsonsAddressDTO temp = mappingAddressMap.get(externalAddress.getAdCode());
            if (temp == null){
                externalAddress.setSuccess(false);
                externalAddress.setResultMsg("更新商城地址失败，百度adcode与商城地址无映射，无映射的百度adcodes为：" + adCodeList);
            } else {
                externalAddress.setMallLevelPath(temp.getMallLevelPath());
                externalAddress.setMallRegionId(temp.getMallRegionId());
                externalAddress.setMallRegionLevel(temp.getMallRegionLevel());
            }
        }
        return successList;
    }

    private void createErrorInfo(List<WatsonsAddressDTO> errorList, SchedulerTool tool){
        if (!CollectionUtils.isEmpty(errorList)){
            for (WatsonsAddressDTO watsonsAddressDTO : errorList){
                tool.error("查询库存组织Id为:" + watsonsAddressDTO.getInvOrganizationId() + "数据失败，经度为: " + watsonsAddressDTO.getLongitude() +
                        "纬度为：" +watsonsAddressDTO.getLatitude() + ",失败原因：" + watsonsAddressDTO.getResultMsg());
            }
        }
    }
}
