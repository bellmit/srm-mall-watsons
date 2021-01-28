package org.srm.mall.agreement.domain.repository;

import org.hzero.mybatis.base.BaseRepository;
import org.srm.mall.agreement.domain.entity.WatsonsAgreementDetail;
import org.srm.mall.common.enums.WatsonsAuthDimensionEnum;
import org.srm.mall.agreement.domain.entity.AgreementDetail;
import org.srm.mall.agreement.domain.entity.WatsonsAuthorityList;
import org.srm.mall.agreement.domain.entity.WatsonsPriceLibScope;

import java.util.List;

/**
 * 资源库
 *
 * @author fu.ji@hand-china.com 2020-05-22 10:50:31
 */
public interface WastonsAgreementDetailRepository extends BaseRepository<WatsonsAgreementDetail> {

    /**
     * 查询是否存在权限集
     *
     * @param agreementDetails 价格库商品信息
     * @param values           枚举类
     * @return 存在的权限集
     */
    List<WatsonsAuthorityList> queryExistsAuth(List<AgreementDetail> agreementDetails, WatsonsAuthDimensionEnum[] values);

    /**
     * 价格库适用范围
     *
     * @param agreementDetails 协议行
     * @return 价格库适用范围
     */
    List<WatsonsPriceLibScope> queryPriceScope(List<AgreementDetail> agreementDetails);
}
