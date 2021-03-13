package org.srm.mall.common.feign.dto.product;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.choerodon.mybatis.domain.AuditDomain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hzero.starter.keyencrypt.core.Encrypt;
import org.srm.mall.product.api.dto.LadderPriceResultDTO;

import java.math.BigDecimal;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

/**
 * 采方商品池阶梯价格表
 *
 * @author xin.li13@hand-china.com 2019-11-25 14:59:43
 */
@ApiModel("采方商品池阶梯价格表")
@VersionAudit
@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "scec_product_pool_ladder")
public class ProductPoolLadder extends AuditDomain {

    public static final String FIELD_LADDER_ID = "ladderId";
    public static final String FIELD_RECORD_ID = "recordId";
    public static final String FIELD_PRODUCT_ID = "productId";
    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_LINE_NUM = "lineNum";
    public static final String FIELD_LADDER_FROM = "ladderFrom";
    public static final String FIELD_LADDER_TO = "ladderTo";
    public static final String FIELD_UNIT_PRICE = "unitPrice";
    public static final String FIELD_TAX_PRICE = "taxPrice";
    public static final String FIELD_REMARK = "remark";
    public static final String FIELD_AGREEMENT_POOL_LINE_ID = "agreementPoolLineId";

    //
    // 业务方法(按public protected private顺序排列)
    // ------------------------------------------------------------------------------

    //
    // 数据库字段
    // ------------------------------------------------------------------------------


    @ApiModelProperty("")
    @Id
    @GeneratedValue
    @Encrypt
    private Long ladderId;
    @ApiModelProperty(value = "商品池表scec_product_pool.record_id",required = true)
    @NotNull
    @Encrypt
    private Long recordId;
    @ApiModelProperty(value = "商品ID",required = true)
    @NotNull
    @Encrypt
    private Long productId;
    @ApiModelProperty(value = "租户ID",required = true)
    @NotNull
    private Long tenantId;
    @ApiModelProperty(value = "行号",required = true)
    @NotNull
    private Long lineNum;
    @ApiModelProperty(value = "数量从（含）")
    private BigDecimal ladderFrom;
    @ApiModelProperty(value = "数量至",required = true)
    @NotNull
    private BigDecimal ladderTo;
    @ApiModelProperty(value = "单价")
    private BigDecimal unitPrice;
    @ApiModelProperty(value = "备注说明")
    private String remark;

    @ApiModelProperty(value = "协议池行id",required = true)
    @NotNull
    private Long agreementPoolLineId;

    @ApiModelProperty(value = "含税单价")
    private BigDecimal taxPrice;

    //
    // 非数据库字段
    // ------------------------------------------------------------------------------

    @Transient
    private Long agreementDetailId;

    public ProductPoolLadder() {


    }

    public ProductPoolLadder(LadderPriceResultDTO ladderPriceResultDTO) {
        this.ladderFrom = ladderPriceResultDTO.getLadderFrom();
        this.ladderTo = ladderPriceResultDTO.getLadderTo();
        this.taxPrice = ladderPriceResultDTO.getSalePrice();
        this.remark = ladderPriceResultDTO.getRemark();
    }

    //
    // getter/setter
    // ------------------------------------------------------------------------------

    /**
     * @return
     */
    public Long getLadderId() {
        return ladderId;
    }

    public void setLadderId(Long ladderId) {
        this.ladderId = ladderId;
    }
    /**
     * @return 商品池表scec_product_pool.record_id
     */
    public Long getRecordId() {
        return recordId;
    }

    public void setRecordId(Long recordId) {
        this.recordId = recordId;
    }
    /**
     * @return 商品ID
     */
    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }
    /**
     * @return 租户ID
     */
    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }
    /**
     * @return 行号
     */
    public Long getLineNum() {
        return lineNum;
    }

    public void setLineNum(Long lineNum) {
        this.lineNum = lineNum;
    }
    /**
     * @return 数量从（含）
     */
    public BigDecimal getLadderFrom() {
        return ladderFrom;
    }

    public void setLadderFrom(BigDecimal ladderFrom) {
        this.ladderFrom = ladderFrom;
    }
    /**
     * @return 数量至
     */
    public BigDecimal getLadderTo() {
        return ladderTo;
    }

    public void setLadderTo(BigDecimal ladderTo) {
        this.ladderTo = ladderTo;
    }
    /**
     * @return 单价
     */
    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }
    /**
     * @return 备注说明
     */
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Long getAgreementDetailId() {
        return agreementDetailId;
    }

    public void setAgreementDetailId(Long agreementDetailId) {
        this.agreementDetailId = agreementDetailId;
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
