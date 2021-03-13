package org.srm.mall.order.app.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import io.choerodon.core.exception.CommonException;
import io.choerodon.core.oauth.DetailsHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.code.builder.CodeRuleBuilder;
import org.hzero.core.base.BaseConstants;
import org.hzero.core.util.ResponseUtils;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.srm.mall.common.constant.ScecConstants;
import org.srm.mall.common.feign.SmdmRemoteNewService;
import org.srm.mall.common.feign.SmodrRemoteService;
import org.srm.mall.infra.constant.WatsonsConstants;
import org.srm.mall.order.api.dto.*;
import org.srm.mall.order.app.service.OmsOrderService;
import org.srm.mall.order.app.service.WatsonsOmsOrderService;
import org.srm.mall.order.domain.vo.PurchaseRequestVO;
import org.srm.mall.other.api.dto.ShoppingCartDTO;
import org.srm.mall.other.api.dto.WatsonsPreRequestOrderDTO;
import org.srm.mall.other.api.dto.WatsonsShoppingCartDTO;
import org.srm.mall.other.domain.entity.AllocationInfo;
import org.srm.mall.platform.api.dto.PrHeaderCreateDTO;
import org.srm.mall.platform.api.dto.PrLineCreateDTO;
import org.srm.mall.product.api.dto.QueryItemCodeDTO;
import org.srm.mall.region.domain.entity.Address;
import org.srm.mall.region.domain.entity.MallRegion;
import org.srm.mall.region.domain.repository.MallRegionRepository;
import org.srm.web.annotation.Tenant;

import javax.transaction.Transactional;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service("watsonsOmsOrderService")
@Tenant(WatsonsConstants.TENANT_NUMBER)
@Slf4j
public class WatsonsOmsOrderServiceImpl extends OmsOrderServiceImpl implements WatsonsOmsOrderService {
    @Autowired
    private CodeRuleBuilder codeRuleBuilder;
    @Autowired
    private SmodrRemoteService smodrRemoteService;
    @Autowired
    private MallRegionRepository mallRegionRepository;
    @Autowired
    private SmdmRemoteNewService smdmRemoteNewService;

    @Override
    @Transactional
    public PurchaseRequestVO watsonsCreateOrder(Long tenantId, String customizeUnitCode, List<WatsonsPreRequestOrderDTO> preRequestOrderDTOs) {
        //批次号
        Map<Optional<Long>,String> batchNumMap = new HashMap<>();
        List<OmsOrderDto> omsOrderDtos = new ArrayList<>();
        for (WatsonsPreRequestOrderDTO preRequestOrderDTO : preRequestOrderDTOs) {
            String batchNum;
            //根据品类分组批次号
            if(batchNumMap.containsKey(Optional.ofNullable(preRequestOrderDTO.getShoppingCartDTOList().get(0).getItemCategoryId()))){
                batchNum = batchNumMap.get(Optional.ofNullable(preRequestOrderDTO.getShoppingCartDTOList().get(0).getItemCategoryId()));
            }else {
                batchNum = codeRuleBuilder.generateCode(ScecConstants.RuleCode.S2FUL_ORDER_BATCH_CODE, null);
                batchNumMap.put(Optional.ofNullable(preRequestOrderDTO.getShoppingCartDTOList().get(0).getItemCategoryId()),batchNum);
            }
            OmsOrderDto omsOrderDto = self().omsOrderDtoBuilder(tenantId, preRequestOrderDTO, batchNum);
            //一级品类id
            omsOrderDto.getOrder().setAttributeBigint10(preRequestOrderDTO.getShoppingCartDTOList().get(0).getItemCategoryId());
            //feign调用mdm查询一级品类信息
            ResponseEntity<String> responseEntity = smdmRemoteNewService.queryItemById(tenantId,preRequestOrderDTO.getShoppingCartDTOList().get(0).getItemCategoryId());
            if(!ObjectUtils.isEmpty(responseEntity)){
                QueryItemCodeDTO queryItemCodeDTO = ResponseUtils.getResponse(responseEntity, new com.fasterxml.jackson.core.type.TypeReference<QueryItemCodeDTO>() {
                });
                //一级品类code
                omsOrderDto.getOrder().setAttributeVarchar10(queryItemCodeDTO.getCategoryCode());
                //一级品类编码
                omsOrderDto.getOrder().setAttributeVarchar11(queryItemCodeDTO.getCategoryName());
            }
            //设置个性化字段
            List<WatsonsShoppingCartDTO> watsonsShoppingCartDTOList = preRequestOrderDTO.getWatsonsShoppingCartDTOList();
            if(Objects.nonNull(watsonsShoppingCartDTOList)){
                Map<Long, List<WatsonsShoppingCartDTO>> groupBy = watsonsShoppingCartDTOList.stream()
                        .collect(Collectors.groupingBy(WatsonsShoppingCartDTO::getProductId));
                omsOrderDto.getOrderEntryList().forEach(omsOrderEntry -> {
                    if(Objects.nonNull(omsOrderEntry.getSkuId())){
                        List<WatsonsShoppingCartDTO> watsonsShoppingCartDTOS = groupBy.get(omsOrderEntry.getSkuId());
                        if(CollectionUtils.isNotEmpty(watsonsShoppingCartDTOS)){
                            WatsonsShoppingCartDTO watsonsShoppingCartDTO = watsonsShoppingCartDTOS.get(0);
                            //库存组织
                            omsOrderEntry.setInvOrganizationId(watsonsShoppingCartDTO.getAllocationInfoList().get(0).getCostShopId());
                            omsOrderEntry.setInvOrganizationCode(watsonsShoppingCartDTO.getAllocationInfoList().get(0).getCostShopCode());
                            omsOrderEntry.setInvOrganizationName(watsonsShoppingCartDTO.getAllocationInfoList().get(0).getCostShopName());
                            //费用承担部门
                            omsOrderEntry.setAttributeBigint7(watsonsShoppingCartDTO.getAllocationInfoList().get(0).getCostDepartmentId());
                            //仓转店收货仓
                            omsOrderEntry.setAttributeVarchar8(watsonsShoppingCartDTO.getAllocationInfoList().get(0).getReceiveWarehouseCode());
                            //费用项目
                            omsOrderEntry.setAttributeVarchar5(watsonsShoppingCartDTO.getAllocationInfoList().get(0).getProjectCostCode());
                            //费用项目子分类
                            omsOrderEntry.setAttributeVarchar6(watsonsShoppingCartDTO.getAllocationInfoList().get(0).getProjectCostSubcategoryCode());
                            //合同号
                            omsOrderEntry.setAttributeVarchar7(watsonsShoppingCartDTO.getCmsNumber());
                            //费用分配
                            omsOrderEntry.setAttributeLongtext1(JSONObject.toJSONString(watsonsShoppingCartDTO.getAllocationInfoList()));
                            log.debug("watsons allocationInfo:" + JSONObject.toJSONString(omsOrderEntry.getAttributeLongtext1()));
                        }
                    }
                });
            }
            //设置ce号
            omsOrderDto.getOrder().setAttributeVarchar2(preRequestOrderDTO.getCeNumber());
            //设置送货方式
            omsOrderDto.getOrder().setAttributeVarchar3(preRequestOrderDTO.getWatsonsShoppingCartDTOList().get(0).getAllocationInfoList().get(0).getDeliveryType());
            omsOrderDtos.add(omsOrderDto);
        }
        log.info("屈臣氏oms创建订单入参:" + JSONObject.toJSONString(omsOrderDtos));
        ResponseEntity<String> result = smodrRemoteService.create(tenantId, customizeUnitCode, omsOrderDtos);
        log.info("屈臣氏oms创建订单出参:" + JSONObject.toJSONString(result));
        if (ResponseUtils.isFailed(result)) {
            //获取异常信息，如果有异常，则会直接抛出
            String message = null;
            try {
                Exception exception = JSONObject.parseObject(result.getBody(), Exception.class);
                message = exception.getMessage();
            } catch (Exception e) {
                message = result.getBody();
            }
            throw new CommonException(message);
        }
        OmsResultDTO omsResultDTO = ResponseUtils.getResponse(result, OmsResultDTO.class);
        return self().returnVOBuilder(omsResultDTO,batchNumMap.entrySet().iterator().next().getValue());
    }

    @Override
    @Transactional
    public OmsOrderAddress getOmsOrderAddress(PreRequestOrderDTO preRequestOrderDTO, OmsOrder omsOrder){
        if(!(preRequestOrderDTO instanceof WatsonsPreRequestOrderDTO)){
            throw new CommonException(BaseConstants.ErrorCode.DATA_INVALID);
        }
        WatsonsPreRequestOrderDTO watsonsPreRequestOrderDTO = (WatsonsPreRequestOrderDTO)preRequestOrderDTO;
        OmsOrderAddress omsOrderAddress = new OmsOrderAddress();
        AllocationInfo allocationInfo = watsonsPreRequestOrderDTO.getWatsonsShoppingCartDTOList().get(0).getAllocationInfoList().get(0);
        if(Objects.isNull(allocationInfo.getLastRegionId())){
            throw new CommonException(BaseConstants.ErrorCode.NOT_NULL);
        }
        MallRegion mallRegion = mallRegionRepository.selectByPrimaryKey(allocationInfo.getLastRegionId());
        List<String> regionCode = Arrays.asList(mallRegion.getLevelPath().split("\\."));
        List<MallRegion> mallRegionList = mallRegionRepository.selectByCondition(Condition.builder(MallRegion.class).andWhere(Sqls.custom()
                .andIn(MallRegion.FIELD_REGION_CODE, regionCode)).build());
        Map<String, MallRegion> mallRegionMap = mallRegionList.stream().collect(Collectors.toMap(MallRegion::getRegionCode, Function.identity()));
        omsOrderAddress.setTenantId(omsOrder.getTenantId());
        omsOrderAddress.setRegionId(mallRegionMap.get(regionCode.get(0)).getRegionId());
        omsOrderAddress.setRegionCode(mallRegionMap.get(regionCode.get(0)).getRegionCode());
        omsOrderAddress.setRegionName(mallRegionMap.get(regionCode.get(0)).getRegionName());
        omsOrderAddress.setCityId(mallRegionMap.get(regionCode.get(1)).getRegionId());
        omsOrderAddress.setCityCode(mallRegionMap.get(regionCode.get(1)).getRegionCode());
        omsOrderAddress.setCityName(mallRegionMap.get(regionCode.get(1)).getRegionName());
        omsOrderAddress.setDistrictId(mallRegionMap.get(regionCode.get(2)).getRegionId());
        omsOrderAddress.setDistrictCode(mallRegionMap.get(regionCode.get(2)).getRegionCode());
        omsOrderAddress.setDistrictName(mallRegionMap.get(regionCode.get(2)).getRegionName());
        omsOrderAddress.setStreetId((regionCode.size() == 4) ? mallRegionMap.get(regionCode.get(3)).getRegionId() : null);
        omsOrderAddress.setStreetCode((regionCode.size() == 4) ? mallRegionMap.get(regionCode.get(3)).getRegionCode() : null);
        omsOrderAddress.setStreetName((regionCode.size() == 4) ? mallRegionMap.get(regionCode.get(3)).getRegionName() : null);
        omsOrderAddress.setAddress(allocationInfo.getFullAddress());
        omsOrderAddress.setFullAddress(allocationInfo.getAddressRegion()+allocationInfo.getFullAddress());
        omsOrderAddress.setContactName(StringUtils.defaultIfBlank(watsonsPreRequestOrderDTO.getReceiverContactName(),DetailsHelper.getUserDetails().getRealName()));
        omsOrderAddress.setMobilePhone(watsonsPreRequestOrderDTO.getMobile());
        omsOrderAddress.setCompanyId(omsOrder.getPurchaseCompanyId());
        omsOrderAddress.setCompanyName(omsOrder.getPurchaseCompanyName());
        omsOrderAddress.setAddressTypeCode("COMPANY");
        omsOrder.setReceiverAddressId(allocationInfo.getAddressId());
        return omsOrderAddress;
    }
}
