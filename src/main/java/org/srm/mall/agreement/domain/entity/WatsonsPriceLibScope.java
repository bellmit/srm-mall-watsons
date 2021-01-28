package org.srm.mall.agreement.domain.entity;

/**
 * 权限范围
 *
 * @author fu.ji@hand-china.com 2020-10-03 16:01:18
 */
public class WatsonsPriceLibScope extends PriceLibScope {

    private Long agreementId;
    private Long agreementDetailId;

    public Long getAgreementDetailId() {
        return agreementDetailId;
    }

    public WatsonsPriceLibScope setAgreementDetailId(Long agreementDetailId) {
        this.agreementDetailId = agreementDetailId;
        return this;
    }

    public Long getAgreementId() {
        return agreementId;
    }

    public WatsonsPriceLibScope setAgreementId(Long agreementId) {
        this.agreementId = agreementId;
        return this;
    }

    public String createGroupByKey(WatsonsPriceLibScope watsonsPriceLibScope) {
        StringBuilder stringBuilder =new StringBuilder();


        return stringBuilder.toString();
    }
}
