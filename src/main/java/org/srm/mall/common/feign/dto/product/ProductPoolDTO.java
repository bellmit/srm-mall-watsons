package org.srm.mall.common.feign.dto.product;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.core.base.BaseConstants;
import org.hzero.starter.keyencrypt.core.Encrypt;
import org.springframework.util.ObjectUtils;
import org.srm.mall.agreement.domain.entity.AgreementDetail;
import org.srm.mall.agreement.domain.entity.AgreementLine;
import org.srm.mall.common.feign.dto.agreement.Agreement;
import org.srm.mall.product.domain.entity.spuPublish.SkuPublish;

import java.math.BigDecimal;
import java.util.List;

/**
 * 商品池DTO，用作商品池方法入参对象
 */
@Data
public class ProductPoolDTO {
    /**
     * 商品池id
     */
    @Encrypt
    private Long recordId;
    /**
     * 租户id
     */
    private Long tenantId;
    /**
     * 公司id
     */
    @Encrypt
    private Long companyId;

    /**
     * 供应商租户id
     */
    private Long supplierTenantId;
    /**
     * 供应商公司id
     */
    @Encrypt
    private Long supplierCompanyId;
    /**
     * 商品id
     */
    @Encrypt
    private Long productId;
    /**
     * 协议id
     */
    @Encrypt
    private Long agreementId;
    /**
     * 协议编号
     */
    private String agreementNumber;
    /**
     * 协议池行id
     */
    private Long agreementPoolLineId;
    /**
     * 协议类型
     */
    private Integer agreementBelongType;
    /**
     * 供货周期
     */
    private String deliveryCycle;
    /**
     * 是否分配所有公司
     */
    private Integer assignAllCompanyFlag;

    /**
     * 是否启用阶梯价
     */
    private Integer ladderEnableFlag;

    /**
     * 平台价
     */
    private BigDecimal marketPrice;

    /**
     * 协议价
     */
    private BigDecimal agreementPrice;

    /**
     * 最小购买数量
     */
    private BigDecimal limitQuantity;

    /**
     * 最小包装数量
     */
    private BigDecimal minPackageQuantity;

    /**
     * 商品类型
     * 1:电商
     * 2:目录化
     */
    private Integer productType;

    /**
     * 阶梯价
     */
    private List<ProductPoolLadderDTO> productPoolLadders;

    /**
     * 供方上下架状态
     */
    private Integer supplierShelfFlag;
    @ApiModelProperty(value = "商品供应商")
    private String supplierCompanyName;

    /**
     * 协议行id
     */
    @Encrypt
    private Long agreementLineId;

    /**
     * 上架校验错误原因
     */
    private String shelveErrorMessage;

    private Integer shelfFlag;
    private Integer purShelfFlag;
    @ApiModelProperty(value = "商品映射方式，值集 SMAL.SKU_REF_TYPE 物料/平台分类", required = true)
    private String skuRefType;


    @ApiModelProperty(value = "平台分类id")
    @Encrypt
    private Long cid;

    @ApiModelProperty(value = "物料分类id")
    @Encrypt
    private Long itemId;
    @ApiModelProperty(value = "目录ID")
    @Encrypt
    private Long catalogId;

    @ApiModelProperty(value = "不含税价")
    private BigDecimal nakedPrice;

    private SkuPublish skuPublish;

    private AgreementLine agreementLine;

    /**
     * 此字段仅做免税商品标识
     * 增值税特殊管理，0：免税、1：不征税，2：普通零税率，3：非零税率，4：特殊税收政策
     */
    private Integer taxSpecial;

    /**
     * 备注，当增值税特殊管理为 4特殊税收政策 时，备注字段必输，填写具体的特殊税收政策是什么
     */
    private String remark;

    public ProductPoolDTO() {
    }

    public ProductPoolDTO(Agreement agreement, AgreementLine agreementLine, AgreementDetail agreementDetail, ECProductSku ecProductSku) {
        this.setCompanyId(agreement.getCompanyId());
        this.setTenantId(agreement.getTenantId());
        this.setAgreementNumber(agreement.getAgreementNumber());
        this.setAgreementId(agreement.getAgreementId());
        this.setSupplierCompanyId(agreement.getSupplierCompanyId());
        this.setSupplierTenantId(agreement.getSupplierTenantId());
        this.setAssignAllCompanyFlag(agreementLine.getAllCompanyFlag());
        this.setLimitQuantity(agreementLine.getOrderQuantity());
        this.setAgreementBelongType(PurConstants.AgreementType.AGREEMENT_BELONG_TYPE_GROUP);
        this.setProductId(agreementDetail.getSkuId());
        this.setProductType(PurConstants.SourceType.CATALOGUE.equals(ecProductSku.getSourceFrom()) ? 2 : 1);
        this.setSupplierShelfFlag(ecProductSku.getShelfStatus().intValue());
        this.setLadderEnableFlag(PurConstants.PriceType.LADDER_PRICE.equals(agreementLine.getPriceType()) ? 1 : 0);
        this.setAgreementNumber(agreement.getAgreementNumber());
        this.setAssignAllCompanyFlag(agreementLine.getAllCompanyFlag());
        this.setAgreementLineId(agreementLine.getAgreementLineId());
        this.setMarketPrice(ecProductSku.getMarketPrice());
        this.setSkuRefType(agreementLine.getSkuRefType());
        this.setDeliveryCycle(ObjectUtils.isEmpty(agreementLine.getDeliveryDay()) ? null : String.valueOf(agreementLine.getDeliveryDay()));
        this.setCid(ecProductSku.getCid());
        this.setMinPackageQuantity(agreementLine.getMinPackageQuantity());
        if (!ObjectUtils.isEmpty(agreementDetail.getApproveFlag()) && Integer.valueOf(BaseConstants.Digital.TWO).equals(agreementDetail.getApproveFlag())) {
            this.setPurShelfFlag(PurConstants.ShelfStatus.MANUAL_UNSHELF);
        }
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getAgreementId() {
        return agreementId;
    }

    public void setAgreementId(Long agreementId) {
        this.agreementId = agreementId;
    }

    public String getAgreementNumber() {
        return agreementNumber;
    }

    public void setAgreementNumber(String agreementNumber) {
        this.agreementNumber = agreementNumber;
    }

    public Long getAgreementPoolLineId() {
        return agreementPoolLineId;
    }

    public void setAgreementPoolLineId(Long agreementPoolLineId) {
        this.agreementPoolLineId = agreementPoolLineId;
    }

    public Integer getAgreementBelongType() {
        return agreementBelongType;
    }

    public void setAgreementBelongType(Integer agreementBelongType) {
        this.agreementBelongType = agreementBelongType;
    }

    public String getDeliveryCycle() {
        return deliveryCycle;
    }

    public void setDeliveryCycle(String deliveryCycle) {
        this.deliveryCycle = deliveryCycle;
    }

    public Integer getAssignAllCompanyFlag() {
        return assignAllCompanyFlag;
    }

    public void setAssignAllCompanyFlag(Integer assignAllCompanyFlag) {
        this.assignAllCompanyFlag = assignAllCompanyFlag;
    }

    public Integer getLadderEnableFlag() {
        return ladderEnableFlag;
    }

    public void setLadderEnableFlag(Integer ladderEnableFlag) {
        this.ladderEnableFlag = ladderEnableFlag;
    }

    public Long getSupplierTenantId() {
        return supplierTenantId;
    }

    public void setSupplierTenantId(Long supplierTenantId) {
        this.supplierTenantId = supplierTenantId;
    }

    public Long getSupplierCompanyId() {
        return supplierCompanyId;
    }

    public void setSupplierCompanyId(Long supplierCompanyId) {
        this.supplierCompanyId = supplierCompanyId;
    }

    public BigDecimal getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(BigDecimal marketPrice) {
        this.marketPrice = marketPrice;
    }

    public BigDecimal getAgreementPrice() {
        return agreementPrice;
    }

    public void setAgreementPrice(BigDecimal agreementPrice) {
        this.agreementPrice = agreementPrice;
    }

    public BigDecimal getLimitQuantity() {
        return limitQuantity;
    }

    public void setLimitQuantity(BigDecimal limitQuantity) {
        this.limitQuantity = limitQuantity;
    }

    public Integer getProductType() {
        return productType;
    }

    public void setProductType(Integer productType) {
        this.productType = productType;
    }

    public List<ProductPoolLadderDTO> getProductPoolLadders() {
        return productPoolLadders;
    }

    public void setProductPoolLadders(List<ProductPoolLadderDTO> productPoolLadders) {
        this.productPoolLadders = productPoolLadders;
    }

    public Integer getSupplierShelfFlag() {
        return supplierShelfFlag;
    }

    public void setSupplierShelfFlag(Integer supplierShelfFlag) {
        this.supplierShelfFlag = supplierShelfFlag;
    }

    public Long getRecordId() {
        return recordId;
    }

    public void setRecordId(Long recordId) {
        this.recordId = recordId;
    }
    public Long getAgreementLineId() {
        return agreementLineId;
    }

    public void setAgreementLineId(Long agreementLineId) {
        this.agreementLineId = agreementLineId;
    }

    public String getShelveErrorMessage() {
        return shelveErrorMessage;
    }

    public void setShelveErrorMessage(String shelveErrorMessage) {
        this.shelveErrorMessage = shelveErrorMessage;
    }

    public Integer getShelfFlag() {
        return shelfFlag;
    }

    public void setShelfFlag(Integer shelfFlag) {
        this.shelfFlag = shelfFlag;
    }

    public Integer getPurShelfFlag() {
        return purShelfFlag;
    }

    public void setPurShelfFlag(Integer purShelfFlag) {
        this.purShelfFlag = purShelfFlag;
    }

    public String getSkuRefType() {
        return skuRefType;
    }

    public void setSkuRefType(String skuRefType) {
        this.skuRefType = skuRefType;
    }

    public String getSupplierCompanyName() {
        return supplierCompanyName;
    }

    public void setSupplierCompanyName(String supplierCompanyName) {
        this.supplierCompanyName = supplierCompanyName;
    }

    public Long getCid() {
        return cid;
    }

    public void setCid(Long cid) {
        this.cid = cid;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public BigDecimal getNakedPrice() {
        return nakedPrice;
    }

    public void setNakedPrice(BigDecimal nakedPrice) {
        this.nakedPrice = nakedPrice;
    }

    public BigDecimal getMinPackageQuantity() {
        return minPackageQuantity;
    }

    public ProductPoolDTO setMinPackageQuantity(BigDecimal minPackageQuantity) {
        this.minPackageQuantity = minPackageQuantity;
        return this;
    }

    public Long getCatalogId() {
        return catalogId;
    }

    public ProductPoolDTO setCatalogId(Long catalogId) {
        this.catalogId = catalogId;
        return this;
    }

    public SkuPublish getSkuPublish() {
        return skuPublish;
    }

    public ProductPoolDTO setSkuPublish(SkuPublish skuPublish) {
        this.skuPublish = skuPublish;
        return this;
    }

    public AgreementLine getAgreementLine() {
        return agreementLine;
    }

    public ProductPoolDTO setAgreementLine(AgreementLine agreementLine) {
        this.agreementLine = agreementLine;
        return this;
    }

    public Integer getTaxSpecial() {
        return taxSpecial;
    }

    public void setTaxSpecial(Integer taxSpecial) {
        this.taxSpecial = taxSpecial;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
