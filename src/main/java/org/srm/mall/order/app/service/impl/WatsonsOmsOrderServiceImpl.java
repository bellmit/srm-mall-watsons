package org.srm.mall.order.app.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import io.choerodon.core.exception.CommonException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.hzero.boot.platform.code.builder.CodeRuleBuilder;
import org.hzero.core.util.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.srm.mall.common.constant.ScecConstants;
import org.srm.mall.common.feign.SmodrRemoteService;
import org.srm.mall.infra.constant.WatsonsConstants;
import org.srm.mall.order.api.dto.*;
import org.srm.mall.order.app.service.OmsOrderService;
import org.srm.mall.order.app.service.WatsonsOmsOrderService;
import org.srm.mall.order.domain.vo.PurchaseRequestVO;
import org.srm.mall.other.api.dto.WatsonsPreRequestOrderDTO;
import org.srm.mall.other.api.dto.WatsonsShoppingCartDTO;
import org.srm.mall.other.domain.entity.AllocationInfo;
import org.srm.mall.platform.api.dto.PrHeaderCreateDTO;
import org.srm.mall.platform.api.dto.PrLineCreateDTO;
import org.srm.web.annotation.Tenant;

import java.util.*;
import java.util.stream.Collectors;

@Service("watsonsOmsOrderService")
@Tenant(WatsonsConstants.TENANT_NUMBER)
@Slf4j
public class WatsonsOmsOrderServiceImpl extends OmsOrderServiceImpl implements WatsonsOmsOrderService {
    @Autowired
    private CodeRuleBuilder codeRuleBuilder;
    @Autowired
    private SmodrRemoteService smodrRemoteService;

    @Override
    public PurchaseRequestVO watsonsCreateOrder(Long tenantId, List<WatsonsPreRequestOrderDTO> preRequestOrderDTOs) {
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
            OmsOrderDto omsOrderDto = super.omsOrderDtoBuilder(tenantId, preRequestOrderDTO, batchNum);
            List<WatsonsShoppingCartDTO> watsonsShoppingCartDTOList = preRequestOrderDTO.getWatsonsShoppingCartDTOList();
            if(Objects.nonNull(watsonsShoppingCartDTOList)){
                Map<Long, List<WatsonsShoppingCartDTO>> groupBy = watsonsShoppingCartDTOList.stream()
                        .collect(Collectors.groupingBy(WatsonsShoppingCartDTO::getProductId));
                omsOrderDto.getOrderEntryList().forEach(omsOrderEntry -> {
                    if(Objects.nonNull(omsOrderEntry.getSkuId())){
                        List<WatsonsShoppingCartDTO> watsonsShoppingCartDTOS = groupBy.get(omsOrderEntry.getSkuId());
                        if(CollectionUtils.isNotEmpty(watsonsShoppingCartDTOS)){
                            omsOrderEntry.setAttributeLongtext1(JSONObject.toJSONString(watsonsShoppingCartDTOS.get(0).getAllocationInfoList()));
                            log.debug("watsons allocationInfo:" + JSONObject.toJSONString(omsOrderEntry.getAttributeLongtext1()));
                        }
                    }
                });
            }
            omsOrderDtos.add(omsOrderDto);
        }
        log.info("屈臣氏oms创建订单入参:" + JSONObject.toJSONString(omsOrderDtos));
        ResponseEntity<String> result = smodrRemoteService.create(tenantId, omsOrderDtos);
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
        if (!omsResultDTO.getHasErrorFlag()) {
            PurchaseRequestVO purchaseRequestVO = new PurchaseRequestVO();
            purchaseRequestVO.setErrorNum(omsResultDTO.getFailedNum());
            purchaseRequestVO.setHasErrorFlag(omsResultDTO.getHasErrorFlag() ? 1 : 0);
            purchaseRequestVO.setSuccessNum(omsResultDTO.getSuccessNum());
            purchaseRequestVO.setNeedPaymentAmount(omsResultDTO.getNeedPaymentAmount());
            purchaseRequestVO.setLotNum(batchNumMap.entrySet().iterator().next().getValue());
            purchaseRequestVO.setOrderJson(omsResultDTO.getOrderJson());

            //设置回传协同参数
            AdaptorCommonDTO<List<OmsOrderDto>> ret = JSONObject.parseObject(omsResultDTO.getOrderJson(),new com.alibaba.fastjson.TypeReference<AdaptorCommonDTO<List<OmsOrderDto>>>(){});
            List<OmsOrderDto> retOrderDtoList = ret.getKey();
            List<PrHeaderCreateDTO> prHeaderCreateDTOS = new ArrayList<>();
            for(OmsOrderDto omsOrderDto : retOrderDtoList){
                PrHeaderCreateDTO prHeaderCreateDTO = new PrHeaderCreateDTO();
                prHeaderCreateDTO.setMallOrderNum(omsOrderDto.getOrder().getOrderCode());
                List<PrLineCreateDTO> prLineCreateDTOList = new ArrayList<>();
                for(OmsOrderEntry omsOrderEntry : omsOrderDto.getOrderEntryList()){
                    PrLineCreateDTO prLineCreateDTO = new PrLineCreateDTO();
                    prLineCreateDTO.setBudgetInfoList(omsOrderEntry.getBudgetInfoList());
                    prLineCreateDTOList.add(prLineCreateDTO);
                }
                prHeaderCreateDTO.setPrLineCreateDTOList(prLineCreateDTOList);
                prHeaderCreateDTOS.add(prHeaderCreateDTO);
            }
            purchaseRequestVO.setPrHeaderList(prHeaderCreateDTOS);
            return purchaseRequestVO;
        } else {
            throw new CommonException(ScecConstants.ErrorCode.PRE_REQUEST_EC_ORDER_FAILED);
        }
    }
}
