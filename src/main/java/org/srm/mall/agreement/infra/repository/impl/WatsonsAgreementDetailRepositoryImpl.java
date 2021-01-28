package org.srm.mall.agreement.infra.repository.impl;

import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.srm.mall.agreement.domain.entity.WatsonsAgreementDetail;
import org.srm.mall.common.enums.WatsonsAuthDimensionEnum;
import org.srm.mall.agreement.domain.entity.AgreementDetail;
import org.srm.mall.agreement.domain.entity.WatsonsAuthorityList;
import org.srm.mall.agreement.domain.entity.WatsonsPriceLibScope;
import org.srm.mall.agreement.domain.repository.WastonsAgreementDetailRepository;
import org.srm.mall.agreement.infra.mapper.WatsonsAgreementDetailMapper;
import org.srm.mall.infra.constant.WatsonsConstants;
import org.srm.web.annotation.Tenant;

import java.util.List;

/**
 * 资源库实现
 *
 * @author fu.ji@hand-china.com 2021-1-27 14:13:04
 */
@Component
@Tenant(WatsonsConstants.TENANT_NUMBER)
public class WatsonsAgreementDetailRepositoryImpl extends BaseRepositoryImpl<WatsonsAgreementDetail> implements WastonsAgreementDetailRepository {

    @Autowired
    private WatsonsAgreementDetailMapper watsonsAgreementDetailMapper;


    @Override
    public List<WatsonsAuthorityList> queryExistsAuth(List<AgreementDetail> agreementDetails, WatsonsAuthDimensionEnum[] values) {
        return watsonsAgreementDetailMapper.queryExistsAuth(agreementDetails,values);
    }

    @Override
    public List<WatsonsPriceLibScope> queryPriceScope(List<AgreementDetail> agreementDetails) {
        return watsonsAgreementDetailMapper.queryPriceScope(agreementDetails);
    }
}
