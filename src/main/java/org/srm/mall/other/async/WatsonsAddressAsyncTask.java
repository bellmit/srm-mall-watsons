package org.srm.mall.other.async;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.srm.amkt.client.api.dto.InterfaceResponse;
import org.srm.amkt.client.api.dto.ReverseGeocoderDTO;
import org.srm.amkt.client.service.GeocoderService;
import org.srm.mall.common.constant.ScecConstants;
import org.srm.mall.other.api.dto.WatsonsAddressDTO;
import org.srm.mall.other.api.dto.WatsonsAddressResultDTO;
import org.srm.mall.platform.domain.repository.InvOrganizationRepository;
import org.srm.mall.platform.domain.vo.InvOrganizationVO;
import org.srm.mall.region.app.service.AddressService;
import org.srm.mall.region.app.service.MallRegionService;
import org.srm.mall.region.domain.entity.Address;
import org.srm.mall.region.domain.repository.AddressRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

@Slf4j
@Component
public class WatsonsAddressAsyncTask {

    @Autowired
    private GeocoderService geocoderService;

    @Autowired
    private MallRegionService mallRegionService;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private AddressService addressService;

    /**
     * 多线程查询百度经纬度信息
     */
    @Async("addressExecutor")
    public Future<WatsonsAddressDTO> asyncReverseGeocoding(Long tenantId, WatsonsAddressDTO address){
        ReverseGeocoderDTO reverseGeocoderDTO = new ReverseGeocoderDTO();
        reverseGeocoderDTO.setLocation(address.getLatitude() + "," + address.getLongitude());
        reverseGeocoderDTO.setModuleCode("SMAL");
        reverseGeocoderDTO.setCurrentUserDetail(false);
        try{
            log.info("调用百度逆向地理编码接口：" + reverseGeocoderDTO);
            InterfaceResponse response = geocoderService.reverseGeocoding(tenantId, reverseGeocoderDTO);
            if (response.getSuccess()){
                address.setSuccess(true);
                HashMap map = (HashMap) response.getData();
                String jsonString = JSON.toJSONString(map);
                WatsonsAddressResultDTO resultDTO = JSON.parseObject(jsonString, WatsonsAddressResultDTO.class);
                address.setAddressResult(resultDTO);
            } else {
                address.setSuccess(false);
                address.setResultMsg(response.getMsg());
            }
        }catch (Exception e){
            log.error("调用百度逆向地理编码接口错误：" + e);
            address.setSuccess(false);
            address.setResultMsg(e.getMessage());
        }
        return new AsyncResult<>(address);
    }

    /**
     * 多线程更新商城地址
     */
    @Async("addressExecutor")
    public Future<WatsonsAddressDTO> asyncUpdateMallAddress(Long tenantId, WatsonsAddressDTO watsonsAddressDTO){
        if (watsonsAddressDTO.getSuccess()){
            try{
                String parentRegionName = mallRegionService.getParentNames(watsonsAddressDTO.getMallRegionId(), StringUtils.EMPTY);
                List<Address> dbAddress = addressRepository.selectByCondition(Condition.builder(Address.class).andWhere(Sqls.custom().andEqualTo(Address.FIELD_INV_ORGANIZATION_ID,watsonsAddressDTO.getInvOrganizationId())).build());

                Address address = new Address();
                if (!ObjectUtils.isEmpty(dbAddress)){
                    address.setAddressId(dbAddress.get(0).getAddressId());
                    address.setObjectVersionNumber(dbAddress.get(0).getObjectVersionNumber());
                }
                address.setTenantId(tenantId);
                address.setCompanyId(watsonsAddressDTO.getCompanyId());
                address.setAddressType(ScecConstants.AdressType.RECEIVER);
                address.setRegionId(watsonsAddressDTO.getMallRegionId());
                address.setContactName("屈臣氏虚拟人员");
                address.setAddress(watsonsAddressDTO.getAddress());
                address.setMobile("131xxxx8888");
                address.setFullAddress(parentRegionName+watsonsAddressDTO.getAddress());
                address.setBelongType(1);
                address.setEnabledFlag(1);
                address.setInvOrganizationId(watsonsAddressDTO.getInvOrganizationId());
                addressService.insertAndUpdateAddress(address);
            }catch (Exception e){
                watsonsAddressDTO.setSuccess(false);
                watsonsAddressDTO.setResultMsg(e.getMessage());
            }
        }
        return new AsyncResult<>(watsonsAddressDTO);
    }

}
