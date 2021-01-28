package org.srm.mall.agreement.infra.mapper;

import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.srm.mall.agreement.domain.entity.WatsonsAgreementDetail;
import org.srm.mall.common.enums.WatsonsAuthDimensionEnum;
import org.srm.mall.agreement.domain.entity.AgreementDetail;
import org.srm.mall.agreement.domain.entity.WatsonsAuthorityList;
import org.srm.mall.agreement.domain.entity.WatsonsPriceLibScope;

import java.util.List;

/**
 * Mapper
 *
 * @author fu.ji@hand-china.com 2020-05-22 10:50:31
 */
public interface WatsonsAgreementDetailMapper extends BaseMapper<WatsonsAgreementDetail> {

    /**
     * 查询是否存在权限集
     *
     * @param agreementDetails 价格库商品信息
     * @param values 枚举类
     * @return 存在的权限集
     */
    List<WatsonsAuthorityList> queryExistsAuth(@Param("agreementDetails") List<AgreementDetail> agreementDetails, @Param("values") WatsonsAuthDimensionEnum[] values);

    /**
     * 价格库适用范围
     *
     * @param agreementDetails 协议行
     * @return 价格库适用范围
     */
    List<WatsonsPriceLibScope> queryPriceScope(@Param("agreementDetails") List<AgreementDetail> agreementDetails);
}
