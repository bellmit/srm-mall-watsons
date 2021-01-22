package org.srm.mall.agreement.app.service.impl;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.srm.mall.agreement.domain.entity.PriceLibMatch;
import org.srm.mall.infra.constant.WatsonsConstants;
import org.srm.mall.platform.api.dto.PriceLibLnDTO;
import org.srm.web.annotation.Tenant;

import java.util.Map;

/**
 * 新版价格库消息处理
 */
@Service
@Tenant(WatsonsConstants.TENANT_NUMBER)
public class WatsonsPriceLibMatchServiceImpl extends PriceLibMatchServiceImpl {


    private static final Logger logger = LoggerFactory.getLogger(WatsonsPriceLibMatchServiceImpl.class);

//
//    @Override
//    protected void handExpandField(PriceLibMatch priceLibMatch, Map<String, PriceLibLnDTO> priceLibLnDTOMap) {
//        super.handExpandField(priceLibMatch, priceLibLnDTOMap);
//        if (priceLibLnDTOMap.containsKey(WatsonsConstants.PriceExpandField.PC_NUM)) {
//            String pcNum = priceLibMatch.getAttributeVarchar1();
//            try {
//                pcNum = priceLibLnDTOMap.get(WatsonsConstants.PriceExpandField.PC_NUM).getDimensionValue();
//            } catch (Exception e) {
//                logger.error("error is {}", ExceptionUtils.getMessage(e));
//            }
//            priceLibMatch.setAttributeVarchar1(pcNum);
//        }
//    }

}
