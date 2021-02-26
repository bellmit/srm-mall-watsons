package org.srm.mall.common.feign.dto.product;

import org.hzero.starter.keyencrypt.core.Encrypt;

import java.math.BigDecimal;

public class ProductPoolLadderDTO {
    /**
     * 商品池id
     */
    @Encrypt
    private Long recordId;
    /**
     * 商品id
     */
    @Encrypt
    private Long productId;
    /**
     * 租户id
     */
    private Long tenantId;

    /**
     * 协议池行id
     */
    private Long agreementPoolLineId;
    /**
     * 行号
     */
    private Long lineNum;
    /**
     * 数量从
     */
    private BigDecimal ladderFrom;
    /**
     * 数量至
     */
    private BigDecimal ladderTo;
    /**
     * 单价
     */
    private BigDecimal unitPrice;

    /**
     * 含税单价
     */
    private BigDecimal taxPrice;
    /**
     * 备注
     */
    private String remark;

    public Long getRecordId() {
        return recordId;
    }

    public void setRecordId(Long recordId) {
        this.recordId = recordId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public Long getLineNum() {
        return lineNum;
    }

    public void setLineNum(Long lineNum) {
        this.lineNum = lineNum;
    }

    public BigDecimal getLadderFrom() {
        return ladderFrom;
    }

    public void setLadderFrom(BigDecimal ladderFrom) {
        this.ladderFrom = ladderFrom;
    }

    public BigDecimal getLadderTo() {
        return ladderTo;
    }

    public void setLadderTo(BigDecimal ladderTo) {
        this.ladderTo = ladderTo;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Long getAgreementPoolLineId() {
        return agreementPoolLineId;
    }

    public void setAgreementPoolLineId(Long agreementPoolLineId) {
        this.agreementPoolLineId = agreementPoolLineId;
    }

    public BigDecimal getTaxPrice() {
        return taxPrice;
    }

    public void setTaxPrice(BigDecimal taxPrice) {
        this.taxPrice = taxPrice;
    }
}
