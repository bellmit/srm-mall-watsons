package org.srm.mall.common.feign.dto.product;

import io.choerodon.mybatis.domain.AuditDomain;
import io.swagger.annotations.ApiModelProperty;
import org.hzero.boot.platform.lov.annotation.LovValue;
import org.hzero.core.base.BaseConstants;
import org.hzero.export.annotation.ExcelColumn;
import org.hzero.export.annotation.ExcelSheet;
import org.hzero.mybatis.domian.SecurityToken;
import org.hzero.starter.keyencrypt.core.Encrypt;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;

@ExcelSheet(promptKey = "smpc.common", promptCode = "smpc.common.excel.skuList", title = "SKU列表导出")
public class SkuPublishDTO extends AuditDomain {

    @ApiModelProperty(value = "sku主键")
    @Encrypt
    private Long skuId;
    @ApiModelProperty(value = "采方租户ID，hpfm_tenant.tenant_id")
    private Long tenantId;
    @ApiModelProperty(value = "商品组ID,smpc_spu.spu_id")
    @Encrypt
    private Long spuId;
    @ExcelColumn(promptKey = "smpc.common", promptCode = "smpc.common.excel.spuCode", order = 2)
    private String spuCode;
    @ExcelColumn(promptKey = "smpc.common", promptCode = "smpc.common.excel.spuName", order = 3)
    private String spuName;
    @ApiModelProperty(value = "商品编码")
    @ExcelColumn(promptKey = "smpc.common", promptCode = "smpc.common.excel.skuCode", order = 6)
    private String skuCode;
    @ApiModelProperty(value = "商品名称")
    @NotBlank
    @ExcelColumn(promptKey = "smpc.common", promptCode = "smpc.common.excel.skuName", order = 7)
    private String skuName;
    @ApiModelProperty(value = "商品分类ID，smpc_category.category_id")
    @Encrypt
    private Long categoryId;
    @ApiModelProperty(value = "商品分类名称")
    @ExcelColumn(promptKey = "smpc.common", promptCode = "smpc.common.excel.categoryName", order = 1)
    private String categoryName;
    @ApiModelProperty(value = "商品分类三级名称")
    private String categoryNamePath;
    @ApiModelProperty(value = "商品主图Id")
    @Encrypt
    private String mediaId;
    @ApiModelProperty(value = "商品主图路径")
    private String mediaPath;
    @ApiModelProperty(value = "介绍地址")
    private String introductionUrl;
    @ApiModelProperty(value = "目录化：CATA；电商：EC")
    @LovValue(meaningField = "sourceFromMeaning", lovCode = "SMPC.SOURCE_FROM")
    private String sourceFrom;
    private String sourceFromMeaning;
    @ApiModelProperty(value = "商品来源类型；手工：MANUAL；协议：AGREEMENT")
    @LovValue(meaningField = "sourceFromTypeMeaning", lovCode = "SMPC.SOURCE_FROM_TYPE")
    private String sourceFromType;
    private String sourceFromTypeMeaning;
    @ApiModelProperty(value = "第三方商品编码")
    private String thirdSkuCode;
    @ApiModelProperty(value = "包装清单")
    private String packingList;
    @ApiModelProperty(value = "主SKU； 1:主sku,0:非主sku")
    private Integer primaryFlag;
    @ApiModelProperty(value = "sku状态；0:新建 1:审批通过 2:审批拒绝 3:锁定 4:解锁")
    @LovValue(meaningField = "skuStatusMeaning", lovCode = "SMPC.SKU_STATUS")
    private Integer skuStatus;
    private String skuStatusMeaning;
    @ApiModelProperty(value = "供应商租户ID  hpfm_tenant.tenant_id")
    private Long supplierTenantId;
    private String supplierTenantName;
    @ApiModelProperty(value = "供应商公司ID，hpfm_company.company_id")
    @Encrypt
    private Long supplierCompanyId;
    private String supplierCompanyName;
    @ApiModelProperty(value = "单价")
    private BigDecimal unitPrice;
    @ApiModelProperty(value = "市场价")
    private BigDecimal marketPrice;
    @ApiModelProperty(value = "销售价")
    private BigDecimal salePrice;
    @ApiModelProperty(value = "库存量")
    private BigDecimal skuStock;
    @ApiModelProperty(value = "消耗库存")
    private BigDecimal consumedStock;
    @ApiModelProperty(value = "总库存")
    private BigDecimal totalStock;
    @ApiModelProperty(value = "版本号")
    private Long version;
    @ApiModelProperty(value = "供应商物料号")
    private String supplierItemCode;
    @ApiModelProperty(value = "供应商物料名称")
    private String supplierItemName;
    @ApiModelProperty(value = "制造商物料号")
    private String manufacturerItemCode;
    @ApiModelProperty(value = "制造商物料名称")
    private String manufacturerItemName;
    @ApiModelProperty(value = "制造商信息")
    private String manufacturerInfo;

    @ApiModelProperty("sku属性&属性值")
    @Encrypt
    private List<SkuAttr> skuAttrList;
    @ApiModelProperty("图片信息")
    @Encrypt
    private List<SkuMedia> skuImageList;
    @ApiModelProperty("商品详情")
    private String introduction;
    @ApiModelProperty("售后")
    @Encrypt
    private AfterSale afterSale;
    @ApiModelProperty("sku扩展（自定义）属性&属性值")
    @Encrypt
    private List<SkuAttrExtend> skuAttrExtendList;
    @ApiModelProperty("采购方租户id（采购方代发布商品）")
    private Long purchaseTenantId;
    @ApiModelProperty(value = "目录id")
    @Encrypt
    private Long catalogId;
    private String catalogName;
    @ApiModelProperty(value = "物料id")
    @Encrypt
    private Long itemId;
    private String itemName;
    @ApiModelProperty(value = "品类id")
    @Encrypt
    private Long itemCategoryId;
    private String itemCategoryName;

    //查询参数
    private Integer effectFlag;
    private List<Long> skuIds;
    private List<Long> itemSkuIds;
    private Integer supFlag;
    private Integer updateFlag;
    private String labelName;
    private String itemCode;
    private Long createdBy;
    private String skuCodeOrName;
    private Date validDateFrom;
    private Date validDateTo;
    @LovValue(meaningField = "shelfFlagMeaning", lovCode = "SMAL.SHELF_FLAG")
    private Integer shelfFlag;
    private List<Integer> shelfFlags;
    private String shelfFlagMeaning;
    private String shelfRemark;
    private String publisher;
    private String publisherId;

    // 协议需求字段 ============ START
    @ApiModelProperty("单位id")
    @Encrypt
    private Long uomId;
    @ApiModelProperty("税率id")
    @Encrypt
    private Long taxId;
    @ApiModelProperty("税率值")
    @ExcelColumn(promptKey = "smpc.common", promptCode = "smpc.common.excel.tax", order = 9)
    private BigDecimal tax;
    @ApiModelProperty("币种编码")
    private String currencyCode;
    @ApiModelProperty("币种名称")
    @ExcelColumn(promptKey = "smpc.common", promptCode = "smpc.common.excel.currencyName", order = 10)
    private String currencyName;
    @ApiModelProperty("含税价")
    private BigDecimal taxPrice;
    @ApiModelProperty("起订量")
    private BigDecimal orderQuantity;
    @ApiModelProperty("采购量上限")
    private BigDecimal purchaseQuantityLimit;
    @ApiModelProperty("最小包装量")
    private BigDecimal minPackageQuantity;
    @Encrypt
    private List<SkuLabel> labels;

    // 协议需求字段 ============ END

    private List<String> keyList;
    @Encrypt
    private List<Long> attrIdList;
    @ApiModelProperty("销售规格属性名称")
    @ExcelColumn(promptKey = "smpc.common", promptCode = "smpc.common.excel.attributeName", order = 4)
    private String attributeName;
    @ApiModelProperty("销售规格属性值")
    @ExcelColumn(promptKey = "smpc.common", promptCode = "smpc.common.excel.attrValueName", order = 5)
    private String attrValueName;
    @ExcelColumn(promptKey = "smpc.common", promptCode = "smpc.common.excel.uomName", order = 8)
    private String uomName;
    @Encrypt
    @Transient
    private Long purSkuId;

    //商品审批相关
    private String skuContent;
    private Long skuTemporaryId;
    @LovValue(meaningField = "approveStatusMeaning", lovCode = "SMAL.AGREEMENT_DETAIL_STATUS")
    private String approveStatus;
    private String approveStatusMeaning;
    private String remark;
    private String approvalFrom;
    private String categoryPath;
    private String imagePath;
    private String taxRate;
    private Integer changeFlag;

    public SkuPublishDTO approvalInit(String spuCode, String imagePath, SpuPublishDTO spuPublishDTO, SkuPublishDTO skuPublishDTO) {
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

    //销售信息
    @Transient
    private List<SkuSalesInfo> skuSalesInfos;

    public Long getPurSkuId() {
        return purSkuId;
    }

    public void setPurSkuId(Long purSkuId) {
        this.purSkuId = purSkuId;
    }

    public String getSkuContent() {
        return skuContent;
    }

    public void setSkuContent(String skuContent) {
        this.skuContent = skuContent;
    }

    public Long getSkuTemporaryId() {
        return skuTemporaryId;
    }

    public void setSkuTemporaryId(Long skuTemporaryId) {
        this.skuTemporaryId = skuTemporaryId;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getApprovalFrom() {
        return approvalFrom;
    }

    public void setApprovalFrom(String approvalFrom) {
        this.approvalFrom = approvalFrom;
    }

    public List<SkuSalesInfo> getSkuSalesInfos() {
        return skuSalesInfos;
    }

    public void setSkuSalesInfos(List<SkuSalesInfo> skuSalesInfos) {
        this.skuSalesInfos = skuSalesInfos;
    }

    private String itemCategoryCode;

    public SkuPublishDTO() {
    }

    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public Long getSpuId() {
        return spuId;
    }

    public void setSpuId(Long spuId) {
        this.spuId = spuId;
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

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryNamePath() {
        return categoryNamePath;
    }

    public void setCategoryNamePath(String categoryNamePath) {
        this.categoryNamePath = categoryNamePath;
    }

    public String getMediaId() {
        return mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }

    public String getMediaPath() {
        return mediaPath;
    }

    public void setMediaPath(String mediaPath) {
        this.mediaPath = mediaPath;
    }

    public String getIntroductionUrl() {
        return introductionUrl;
    }

    public void setIntroductionUrl(String introductionUrl) {
        this.introductionUrl = introductionUrl;
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

    public String getThirdSkuCode() {
        return thirdSkuCode;
    }

    public void setThirdSkuCode(String thirdSkuCode) {
        this.thirdSkuCode = thirdSkuCode;
    }

    public String getPackingList() {
        return packingList;
    }

    public void setPackingList(String packingList) {
        this.packingList = packingList;
    }

    public Integer getPrimaryFlag() {
        return primaryFlag;
    }

    public void setPrimaryFlag(Integer primaryFlag) {
        this.primaryFlag = primaryFlag;
    }

    public Integer getSkuStatus() {
        return skuStatus;
    }

    public void setSkuStatus(Integer skuStatus) {
        this.skuStatus = skuStatus;
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

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(BigDecimal marketPrice) {
        this.marketPrice = marketPrice;
    }

    public BigDecimal getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(BigDecimal salePrice) {
        this.salePrice = salePrice;
    }

    public BigDecimal getSkuStock() {
        return skuStock;
    }

    public void setSkuStock(BigDecimal skuStock) {
        this.skuStock = skuStock;
    }

    public BigDecimal getConsumedStock() {
        return consumedStock;
    }

    public void setConsumedStock(BigDecimal consumedStock) {
        this.consumedStock = consumedStock;
    }

    public BigDecimal getTotalStock() {
        return totalStock;
    }

    public void setTotalStock(BigDecimal totalStock) {
        this.totalStock = totalStock;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public String getSupplierItemCode() {
        return supplierItemCode;
    }

    public void setSupplierItemCode(String supplierItemCode) {
        this.supplierItemCode = supplierItemCode;
    }

    public String getSupplierItemName() {
        return supplierItemName;
    }

    public void setSupplierItemName(String supplierItemName) {
        this.supplierItemName = supplierItemName;
    }

    public String getManufacturerItemCode() {
        return manufacturerItemCode;
    }

    public void setManufacturerItemCode(String manufacturerItemCode) {
        this.manufacturerItemCode = manufacturerItemCode;
    }

    @Override
    public Long getCreatedBy() {
        return createdBy;
    }

    @Override
    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public String getSkuCodeOrName() {
        return skuCodeOrName;
    }

    public void setSkuCodeOrName(String skuCodeOrName) {
        this.skuCodeOrName = skuCodeOrName;
    }

    public Date getValidDateFrom() {
        return validDateFrom;
    }

    public void setValidDateFrom(Date validDateFrom) {
        this.validDateFrom = validDateFrom;
    }

    public Date getValidDateTo() {
        return validDateTo;
    }

    public void setValidDateTo(Date validDateTo) {
        this.validDateTo = validDateTo;
    }

    public Integer getShelfFlag() {
        return shelfFlag;
    }

    public void setShelfFlag(Integer shelfFlag) {
        this.shelfFlag = shelfFlag;
    }

    public List<Integer> getShelfFlags() {
        return shelfFlags;
    }

    public void setShelfFlags(List<Integer> shelfFlags) {
        this.shelfFlags = shelfFlags;
    }

    public String getCategoryPath() {
        return categoryPath;
    }

    public void setCategoryPath(String categoryPath) {
        this.categoryPath = categoryPath;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(String taxRate) {
        this.taxRate = taxRate;
    }

    public Integer getChangeFlag() {
        return changeFlag;
    }

    public void setChangeFlag(Integer changeFlag) {
        this.changeFlag = changeFlag;
    }

    public String getShelfFlagMeaning() {
        return shelfFlagMeaning;
    }

    public void setShelfFlagMeaning(String shelfFlagMeaning) {
        this.shelfFlagMeaning = shelfFlagMeaning;
    }

    public String getShelfRemark() {
        return shelfRemark;
    }

    public void setShelfRemark(String shelfRemark) {
        this.shelfRemark = shelfRemark;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getPublisherId() {
        return publisherId;
    }

    public void setPublisherId(String publisherId) {
        this.publisherId = publisherId;
    }

    public String getManufacturerItemName() {
        return manufacturerItemName;
    }

    public void setManufacturerItemName(String manufacturerItemName) {
        this.manufacturerItemName = manufacturerItemName;
    }

    public String getManufacturerInfo() {
        return manufacturerInfo;
    }

    public void setManufacturerInfo(String manufacturerInfo) {
        this.manufacturerInfo = manufacturerInfo;
    }

    public List<SkuAttr> getSkuAttrList() {
        return skuAttrList;
    }

    public void setSkuAttrList(List<SkuAttr> skuAttrList) {
        this.skuAttrList = skuAttrList;
    }

    public List<SkuMedia> getSkuImageList() {
        return skuImageList;
    }

    public void setSkuImageList(List<SkuMedia> skuImageList) {
        this.skuImageList = skuImageList;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public AfterSale getAfterSale() {
        return afterSale;
    }

    public void setAfterSale(AfterSale afterSale) {
        this.afterSale = afterSale;
    }

    public List<SkuAttrExtend> getSkuAttrExtendList() {
        return skuAttrExtendList;
    }

    public void setSkuAttrExtendList(List<SkuAttrExtend> skuAttrExtendList) {
        this.skuAttrExtendList = skuAttrExtendList;
    }

    public Long getPurchaseTenantId() {
        return purchaseTenantId;
    }

    public void setPurchaseTenantId(Long purchaseTenantId) {
        this.purchaseTenantId = purchaseTenantId;
    }

    public Long getCatalogId() {
        return catalogId;
    }

    public void setCatalogId(Long catalogId) {
        this.catalogId = catalogId;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public Long getItemCategoryId() {
        return itemCategoryId;
    }

    public void setItemCategoryId(Long itemCategoryId) {
        this.itemCategoryId = itemCategoryId;
    }

    public String getCatalogName() {
        return catalogName;
    }

    public void setCatalogName(String catalogName) {
        this.catalogName = catalogName;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemCategoryName() {
        return itemCategoryName;
    }

    public void setItemCategoryName(String itemCategoryName) {
        this.itemCategoryName = itemCategoryName;
    }

    public Long getUomId() {
        return uomId;
    }

    public void setUomId(Long uomId) {
        this.uomId = uomId;
    }

    public Long getTaxId() {
        return taxId;
    }

    public void setTaxId(Long taxId) {
        this.taxId = taxId;
    }

    public BigDecimal getTax() {
        return tax;
    }

    public void setTax(BigDecimal tax) {
        this.tax = tax;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    public BigDecimal getTaxPrice() {
        return taxPrice;
    }

    public void setTaxPrice(BigDecimal taxPrice) {
        this.taxPrice = taxPrice;
    }

    public BigDecimal getOrderQuantity() {
        return orderQuantity;
    }

    public void setOrderQuantity(BigDecimal orderQuantity) {
        this.orderQuantity = orderQuantity;
    }

    public BigDecimal getPurchaseQuantityLimit() {
        return purchaseQuantityLimit;
    }

    public void setPurchaseQuantityLimit(BigDecimal purchaseQuantityLimit) {
        this.purchaseQuantityLimit = purchaseQuantityLimit;
    }

    public BigDecimal getMinPackageQuantity() {
        return minPackageQuantity;
    }

    public void setMinPackageQuantity(BigDecimal minPackageQuantity) {
        this.minPackageQuantity = minPackageQuantity;
    }

    public Integer getEffectFlag() {
        return effectFlag;
    }

    public void setEffectFlag(Integer effectFlag) {
        this.effectFlag = effectFlag;
    }

    public List<Long> getSkuIds() {
        return skuIds;
    }

    public void setSkuIds(List<Long> skuIds) {
        this.skuIds = skuIds;
    }

    public Integer getSupFlag() {
        return supFlag;
    }

    public void setSupFlag(Integer supFlag) {
        this.supFlag = supFlag;
    }

    public String getSourceFromMeaning() {
        return sourceFromMeaning;
    }

    public void setSourceFromMeaning(String sourceFromMeaning) {
        this.sourceFromMeaning = sourceFromMeaning;
    }

    public String getSourceFromTypeMeaning() {
        return sourceFromTypeMeaning;
    }

    public void setSourceFromTypeMeaning(String sourceFromTypeMeaning) {
        this.sourceFromTypeMeaning = sourceFromTypeMeaning;
    }

    public String getSkuStatusMeaning() {
        return skuStatusMeaning;
    }

    public void setSkuStatusMeaning(String skuStatusMeaning) {
        this.skuStatusMeaning = skuStatusMeaning;
    }

    public List<SkuLabel> getLabels() {
        return labels;
    }

    public void setLabels(List<SkuLabel> labels) {
        this.labels = labels;
    }

    public Integer getUpdateFlag() {
        return updateFlag;
    }

    public void setUpdateFlag(Integer updateFlag) {
        this.updateFlag = updateFlag;
    }

    public List<Long> getItemSkuIds() {
        return itemSkuIds;
    }

    public void setItemSkuIds(List<Long> itemSkuIds) {
        this.itemSkuIds = itemSkuIds;
    }

    public String getLabelName() {
        return labelName;
    }

    public void setLabelName(String labelName) {
        this.labelName = labelName;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public List<String> getKeyList() {
        return keyList;
    }

    public void setKeyList(List<String> keyList) {
        this.keyList = keyList;
    }

    public List<Long> getAttrIdList() {
        return attrIdList;
    }

    public void setAttrIdList(List<Long> attrIdList) {
        this.attrIdList = attrIdList;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public String getAttrValueName() {
        return attrValueName;
    }

    public void setAttrValueName(String attrValueName) {
        this.attrValueName = attrValueName;
    }

    public String getUomName() {
        return uomName;
    }

    public void setUomName(String uomName) {
        this.uomName = uomName;
    }

    public String getItemCategoryCode() {
        return itemCategoryCode;
    }

    public void setItemCategoryCode(String itemCategoryCode) {
        this.itemCategoryCode = itemCategoryCode;
    }

    /**
     * 返回关联的实体类型，自定义 DTO 可以重写这个方法，返回对应的<strong>实体类型</strong>
     *
     * @return 实体类型
     */
    @Override
    public Class<? extends SecurityToken> associateEntityClass() {
        return Sku.class;
    }
}
