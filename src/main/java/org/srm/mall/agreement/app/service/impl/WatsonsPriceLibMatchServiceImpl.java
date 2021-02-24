package org.srm.mall.agreement.app.service.impl;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.srm.mall.agreement.domain.entity.AgreementLine;
import org.srm.mall.agreement.domain.entity.AgreementUnit;
import org.srm.mall.agreement.domain.entity.PriceLibMatch;
import org.srm.mall.infra.constant.WatsonsConstants;
import org.srm.mall.platform.api.dto.PriceLibLnDTO;
import org.srm.web.annotation.Tenant;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 新版价格库消息处理
 */
@Service
@Tenant(WatsonsConstants.TENANT_NUMBER)
public class WatsonsPriceLibMatchServiceImpl extends PriceLibMatchServiceImpl {


    private static final Logger logger = LoggerFactory.getLogger(WatsonsPriceLibMatchServiceImpl.class);


    @Override
    protected void handExpandField(PriceLibMatch priceLibMatch, Map<String, PriceLibLnDTO> priceLibLnDTOMap) {
        super.handExpandField(priceLibMatch, priceLibLnDTOMap);
        if (priceLibLnDTOMap.containsKey(WatsonsConstants.PriceExpandField.CONTRACT_NUM)) {
            String pcNum = priceLibMatch.getAttributeVarchar1();
            try {
                pcNum = priceLibLnDTOMap.get(WatsonsConstants.PriceExpandField.CONTRACT_NUM).getDimensionValue();
            } catch (Exception e) {
                logger.error("error is {}", ExceptionUtils.getMessage(e));
            }
            priceLibMatch.setAttributeVarchar1(pcNum);
        }

        if (priceLibLnDTOMap.containsKey(WatsonsConstants.PriceExpandField.LAST_PURCHASE_QUANTITY)) {
            BigDecimal lastPurchaseQuantity = priceLibMatch.getAttributeDecimal1();
            try {
                lastPurchaseQuantity =new BigDecimal(priceLibLnDTOMap.get(WatsonsConstants.PriceExpandField.LAST_PURCHASE_QUANTITY).getDimensionValue());
            } catch (Exception e) {
                logger.error("error is {}", ExceptionUtils.getMessage(e));
            }
            priceLibMatch.setAttributeDecimal1(lastPurchaseQuantity);
        }
    }

    @Override
    protected void convertData(AgreementLine agreementLine, PriceLibMatch priceLibMatch) {
        super.convertData(agreementLine, priceLibMatch);
        // 协议自动升级处理
        agreementLine.setAttributeVarchar1(priceLibMatch.getAttributeVarchar1());
        agreementLine.setPurchaseQuantityLimit(priceLibMatch.getAttributeDecimal1());
    }

    @Override
    public AgreementLine generateAgreementLineFromPriceLibray(Long tenantId, List<AgreementUnit> agreementUnits, AgreementLine agreementLine, PriceLibMatch priceLibMatch) {
        AgreementLine line = super.generateAgreementLineFromPriceLibray(tenantId, agreementUnits, agreementLine, priceLibMatch);
        // 协议自动创建和界面创建转换
        line.setAttributeVarchar1(priceLibMatch.getAttributeVarchar1());
        line.setPurchaseQuantityLimit(priceLibMatch.getAttributeDecimal1());
        return line;
    }
}
