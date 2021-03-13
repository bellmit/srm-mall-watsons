package org.srm.mall.common.feign.dto.product;

import io.choerodon.mybatis.domain.AuditDomain;
import io.swagger.annotations.ApiModelProperty;
import org.hzero.boot.platform.lov.annotation.LovValue;
import org.hzero.core.base.BaseConstants;
import org.hzero.starter.keyencrypt.core.Encrypt;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.util.List;

/**
 * sku审批  DTO
 * 
 * @author yuhao.guo@hand-china.com  2021年1月5日15:32:51
 */
public class SkuApproveDTO extends AuditDomain {

    private Long skuTemporaryId;
    @ApiModelProperty(value = "租户ID  hpfm_tenant.tenant_id")
    private Long tenantId;
    @ApiModelProperty(value = "SKU ID  smpc_sku.sku_id")
    @Encrypt
    private Long skuId;
    @ApiModelProperty(value = "供应商租户ID  hpfm_tenant.tenant_id")
    private Long supplierTenantId;
    @ApiModelProperty(value = "商品信息，JSON内容")
    private String skuContent;
    @ApiModelProperty(value = "审批状态  SMPC.SKU_APPROVE_STATUS NEW/APPROVED/REJECTED")
    @LovValue(meaningField = "approveStatusMeaning", lovCode = "SMAL.AGREEMENT_DETAIL_STATUS")
    private String approveStatus;
    private String approveStatusMeaning;
    @ApiModelProperty(value = "备注")
    private String remark;
    @ApiModelProperty(value = "待审批商品来源")
    private String approvalFrom;

    private String supplierTenantName;
    private String skuCode;
    private String skuName;
    private String spuCode;
    private String categoryPath;
    private String currencyName;
    private String taxRate;
    private String imagePath;
    @Encrypt
    private Long categoryId;
    private Integer changeFlag;
    private List<String> keyList;
    //todo  价格（阶梯价）&价格类型，需对接价格服务获取
    private String priceType;
    private BigDecimal taxPrice;
    private List<LadderPriceDTO> ladderPriceDTOS;


    private Integer approvalFlag;
    @Encrypt
    private List<SkuApproveDTO> skuApproveDTOS;
    private List<Long> skuIds;


    public SkuApproveDTO() {}

    public SkuApproveDTO approvalInit(String spuCode, String imagePath, SpuPublishDTO spuPublishDTO, SkuPublishDTO skuPublishDTO) {
        this.spuCode = spuCode;
        this.skuCode = skuPublishDTO.getSkuCode();
        this.skuName = skuPublishDTO.getSkuName();
        this.categoryPath = spuPublishDTO.getCategoryNamePath();
        this.categoryId = spuPublishDTO.getCategoryId();
        String skuImage = ObjectUtils.isEmpty(skuPublishDTO.getSkuImageList()) ? null : skuPublishDTO.getSkuImageList().get(0).getMediaPath();
        this.imagePath = ObjectUtils.isEmpty(imagePath) ? skuImage : imagePath;
        skuPublishDTO.getSkuAttrList().forEach(skuAttr -> {
            if (BasicAttributeEnum.CURRENCY_CODE.getCode().equals(skuAttr.getAttributeCode())) {
                this.currencyName = skuAttr.getAttrValueName();
            } else if (BasicAttributeEnum.TAX_CODE.getCode().equals(skuAttr.getAttributeCode())) {
                this.taxRate = skuAttr.getAttrValueName();
            }
        });
        this.changeFlag = BaseConstants.Flag.YES;
        return this;
    }

    public Long getSkuTemporaryId() {
        return skuTemporaryId;
    }

    public void setSkuTemporaryId(Long skuTemporaryId) {
        this.skuTemporaryId = skuTemporaryId;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    public Long getSupplierTenantId() {
        return supplierTenantId;
    }

    public void setSupplierTenantId(Long supplierTenantId) {
        this.supplierTenantId = supplierTenantId;
    }

    public String getSkuContent() {
        return skuContent;
    }

    public void setSkuContent(String skuContent) {
        this.skuContent = skuContent;
    }

    public String getApproveStatus() {
        return approveStatus;
    }

    public void setApproveStatus(String approveStatus) {
        this.approveStatus = approveStatus;
    }

    public String getApproveStatusMeaning() {
        return approveStatusMeaning;
    }

    public void setApproveStatusMeaning(String approveStatusMeaning) {
        this.approveStatusMeaning = approveStatusMeaning;
    }

    public String getSupplierTenantName() {
        return supplierTenantName;
    }

    public void setSupplierTenantName(String supplierTenantName) {
        this.supplierTenantName = supplierTenantName;
    }

    public String getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
    }

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    public String getSpuCode() {
        return spuCode;
    }

    public void setSpuCode(String spuCode) {
        this.spuCode = spuCode;
    }

    public String getCategoryPath() {
        return categoryPath;
    }

    public void setCategoryPath(String categoryPath) {
        this.categoryPath = categoryPath;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    public String getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(String taxRate) {
        this.taxRate = taxRate;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getChangeFlag() {
        return changeFlag;
    }

    public void setChangeFlag(Integer changeFlag) {
        this.changeFlag = changeFlag;
    }

    public List<String> getKeyList() {
        return keyList;
    }

    public void setKeyList(List<String> keyList) {
        this.keyList = keyList;
    }

    public String getPriceType() {
        return priceType;
    }

    public void setPriceType(String priceType) {
        this.priceType = priceType;
    }

    public BigDecimal getTaxPrice() {
        return taxPrice;
    }

    public void setTaxPrice(BigDecimal taxPrice) {
        this.taxPrice = taxPrice;
    }

    public List<LadderPriceDTO> getLadderPriceDTOS() {
        return ladderPriceDTOS;
    }

    public void setLadderPriceDTOS(List<LadderPriceDTO> ladderPriceDTOS) {
        this.ladderPriceDTOS = ladderPriceDTOS;
    }

    public Integer getApprovalFlag() {
        return approvalFlag;
    }

    public void setApprovalFlag(Integer approvalFlag) {
        this.approvalFlag = approvalFlag;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<SkuApproveDTO> getSkuApproveDTOS() {
        return skuApproveDTOS;
    }

    public void setSkuApproveDTOS(List<SkuApproveDTO> skuApproveDTOS) {
        this.skuApproveDTOS = skuApproveDTOS;
    }

    public List<Long> getSkuIds() {
        return skuIds;
    }

    public void setSkuIds(List<Long> skuIds) {
        this.skuIds = skuIds;
    }

    public String getApprovalFrom() {
        return approvalFrom;
    }

    public void setApprovalFrom(String approvalFrom) {
        this.approvalFrom = approvalFrom;
    }
}
