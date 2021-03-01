package org.srm.mall.common.feign.dto.product;

import io.choerodon.mybatis.domain.AuditDomain;
import io.swagger.annotations.ApiModelProperty;
import org.hzero.starter.keyencrypt.core.Encrypt;

import java.util.List;
import javax.validation.constraints.NotNull;

public class SpuPublishDTO extends AuditDomain {

    public static final String FIELD_SKU_LIST="body.skuList";

    @ApiModelProperty("spu主键")
    @Encrypt
    private Long spuId;
    @ApiModelProperty("采购方租户id")
    private Long tenantId;
    @ApiModelProperty("spu名称")
    private String spuCode;
    @ApiModelProperty("spu名称")
    private String spuName;
    @ApiModelProperty("三级类目id")
    @NotNull(message = "categoryId不能为空")
    @Encrypt
    private Long categoryId;
    @ApiModelProperty("商品分类名称--三级名称")
    private String categoryNamePath;
    @ApiModelProperty("商品来源，CATA目录化，JD/CG/...电商")
    private String sourceFrom;
    @ApiModelProperty("商品创建方式，手工：MANUAL；协议：AGREEMENT；电商同步：SYNCHRONIZE")
    private String sourceFromType;
    @ApiModelProperty("三级类目id")
    private String thirdSpuCode;
    @ApiModelProperty("商品请求时的状态。0:未发布，1:已发布")
    private Integer spuStatus;
    @ApiModelProperty("供应商租户")
    private Long supplierTenantId;
    private String supplierTenantName;
    @ApiModelProperty("供应商公司")
    @NotNull(message = "公司id不能为空")
    @Encrypt(ignoreValue = {"3"})
    private Long supplierCompanyId;
    private String supplierCompanyName;

    @ApiModelProperty(value = "商品组下商品主图路径")
    private String primaryImagePath;
    @ApiModelProperty(value = "商品组下商品视频路径")
    private String primaryVideoPath;

    @ApiModelProperty("sku列表")
    @Encrypt
    private List<SkuPublishDTO> skuList;
    @ApiModelProperty("spu销售属性")
    @Encrypt
    private List<SkuAttr> spuAttrList;
    @ApiModelProperty("spu扩展销售属性（自定义）")
    @Encrypt
    private List<SkuAttrExtend> spuAttrExtendList;

    @ApiModelProperty("供应商可编辑商品模块")
    private List<String> approveType;

    // 商品创建协议需求字段 ============ START

    @ApiModelProperty("采购方租户id（采购方代发布商品）")
    private Long purchaseTenantId;
    @ApiModelProperty("采购方公司id（采购方代发布商品）")
    @Encrypt
    private Long purchaseCompanyId;
    private String purchaseCompanyName;

    @ApiModelProperty(value = "物料id")
    @Encrypt
    private Long itemId;
    @ApiModelProperty(value = "物料编码")
    private String itemCode;
    @ApiModelProperty(value = "物料名称")
    private String itemName;
    @ApiModelProperty(value = "目录id")
    @Encrypt
    private Long catalogId;
    @ApiModelProperty(value = "目录编码")
    private String catalogCode;
    @ApiModelProperty(value = "目录名称")
    private String catalogName;
    @ApiModelProperty(value = "协议id")
    @Encrypt
    private Long agreementId;
    @ApiModelProperty(value = "协议编码")
    private String agreementNumber;
    @ApiModelProperty(value = "协议名称")
    private String agreementName;

    @ApiModelProperty(value = "品类id")
    @Encrypt
    private Long itemCategoryId;
    @ApiModelProperty(value = "品类编码")
    private String itemCategoryCode;
    @ApiModelProperty(value = "品类名称")
    private String itemCategoryName;

    @Encrypt
    private Long agreementLineId;

    // 协议需求字段 ============ END

    private List<String> keyList;


    public SpuPublishDTO() {
    }

    public SpuPublishDTO initComparison() {
        return this;
    }

    public Long getSpuId() {
        return spuId;
    }

    public void setSpuId(Long spuId) {
        this.spuId = spuId;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public String getSpuCode() {
        return spuCode;
    }

    public void setSpuCode(String spuCode) {
        this.spuCode = spuCode;
    }

    public String getSpuName() {
        return spuName;
    }

    public void setSpuName(String spuName) {
        this.spuName = spuName;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryNamePath() {
        return categoryNamePath;
    }

    public void setCategoryNamePath(String categoryNamePath) {
        this.categoryNamePath = categoryNamePath;
    }

    public String getSourceFrom() {
        return sourceFrom;
    }

    public void setSourceFrom(String sourceFrom) {
        this.sourceFrom = sourceFrom;
    }

    public String getSourceFromType() {
        return sourceFromType;
    }

    public void setSourceFromType(String sourceFromType) {
        this.sourceFromType = sourceFromType;
    }

    public String getThirdSpuCode() {
        return thirdSpuCode;
    }

    public void setThirdSpuCode(String thirdSpuCode) {
        this.thirdSpuCode = thirdSpuCode;
    }

    public Integer getSpuStatus() {
        return spuStatus;
    }

    public void setSpuStatus(Integer spuStatus) {
        this.spuStatus = spuStatus;
    }

    public Long getSupplierTenantId() {
        return supplierTenantId;
    }

    public void setSupplierTenantId(Long supplierTenantId) {
        this.supplierTenantId = supplierTenantId;
    }

    public String getSupplierTenantName() {
        return supplierTenantName;
    }

    public void setSupplierTenantName(String supplierTenantName) {
        this.supplierTenantName = supplierTenantName;
    }

    public Long getSupplierCompanyId() {
        return supplierCompanyId;
    }

    public void setSupplierCompanyId(Long supplierCompanyId) {
        this.supplierCompanyId = supplierCompanyId;
    }

    public String getSupplierCompanyName() {
        return supplierCompanyName;
    }

    public void setSupplierCompanyName(String supplierCompanyName) {
        this.supplierCompanyName = supplierCompanyName;
    }

    public String getPrimaryImagePath() {
        return primaryImagePath;
    }

    public void setPrimaryImagePath(String primaryImagePath) {
        this.primaryImagePath = primaryImagePath;
    }

    public String getPrimaryVideoPath() {
        return primaryVideoPath;
    }

    public void setPrimaryVideoPath(String primaryVideoPath) {
        this.primaryVideoPath = primaryVideoPath;
    }

    public List<SkuPublishDTO> getSkuList() {
        return skuList;
    }

    public void setSkuList(List<SkuPublishDTO> skuList) {
        this.skuList = skuList;
    }

    public List<SkuAttr> getSpuAttrList() {
        return spuAttrList;
    }

    public void setSpuAttrList(List<SkuAttr> spuAttrList) {
        this.spuAttrList = spuAttrList;
    }

    public List<SkuAttrExtend> getSpuAttrExtendList() {
        return spuAttrExtendList;
    }

    public void setSpuAttrExtendList(List<SkuAttrExtend> spuAttrExtendList) {
        this.spuAttrExtendList = spuAttrExtendList;
    }

    public Long getPurchaseTenantId() {
        return purchaseTenantId;
    }

    public void setPurchaseTenantId(Long purchaseTenantId) {
        this.purchaseTenantId = purchaseTenantId;
    }

    public Long getPurchaseCompanyId() {
        return purchaseCompanyId;
    }

    public void setPurchaseCompanyId(Long purchaseCompanyId) {
        this.purchaseCompanyId = purchaseCompanyId;
    }

    public String getPurchaseCompanyName() {
        return purchaseCompanyName;
    }

    public void setPurchaseCompanyName(String purchaseCompanyName) {
        this.purchaseCompanyName = purchaseCompanyName;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Long getCatalogId() {
        return catalogId;
    }

    public void setCatalogId(Long catalogId) {
        this.catalogId = catalogId;
    }

    public String getCatalogCode() {
        return catalogCode;
    }

    public void setCatalogCode(String catalogCode) {
        this.catalogCode = catalogCode;
    }

    public String getCatalogName() {
        return catalogName;
    }

    public void setCatalogName(String catalogName) {
        this.catalogName = catalogName;
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

    public String getAgreementName() {
        return agreementName;
    }

    public void setAgreementName(String agreementName) {
        this.agreementName = agreementName;
    }

    public Long getItemCategoryId() {
        return itemCategoryId;
    }

    public void setItemCategoryId(Long itemCategoryId) {
        this.itemCategoryId = itemCategoryId;
    }

    public String getItemCategoryCode() {
        return itemCategoryCode;
    }

    public void setItemCategoryCode(String itemCategoryCode) {
        this.itemCategoryCode = itemCategoryCode;
    }

    public String getItemCategoryName() {
        return itemCategoryName;
    }

    public void setItemCategoryName(String itemCategoryName) {
        this.itemCategoryName = itemCategoryName;
    }

    public Long getAgreementLineId() {
        return agreementLineId;
    }

    public void setAgreementLineId(Long agreementLineId) {
        this.agreementLineId = agreementLineId;
    }

    public List<String> getApproveType() {
        return approveType;
    }

    public void setApproveType(List<String> approveType) {
        this.approveType = approveType;
    }

    public List<String> getKeyList() {
        return keyList;
    }

    public void setKeyList(List<String> keyList) {
        this.keyList = keyList;
    }
}
