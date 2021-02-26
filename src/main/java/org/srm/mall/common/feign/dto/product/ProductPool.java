package org.srm.mall.common.feign.dto.product;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.choerodon.mybatis.domain.AuditDomain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.starter.keyencrypt.core.Encrypt;
import org.springframework.util.ObjectUtils;
import org.srm.mall.common.annotation.TargetField;
import org.srm.mall.product.api.dto.LadderPriceResultDTO;

import java.math.BigDecimal;
import java.util.*;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

/**
 * 采方商品池表
 *
 * @author xin.li13@hand-china.com 2019-11-25 14:59:43
 */
@ApiModel("采方商品池表")
@Data
@VersionAudit
@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "scec_product_pool")
public class ProductPool extends AuditDomain {

    public static final String FIELD_RECORD_ID = "recordId";
    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_COMPANY_ID = "companyId";
    public static final String FIELD_CATALOG_ID = "catalogId";
    public static final String FIELD_PRODUCT_TYPE = "productType";
    public static final String FIELD_PRODUCT_ID = "productId";
    public static final String FIELD_SHELF_FLAG = "shelfFlag";
    public static final String FIELD_MARKET_PRICE = "marketPrice";
    public static final String FIELD_AGREEMENT_PRICE = "agreementPrice";
    public static final String FIELD_SUPPLIER_TENANT_ID = "supplierTenantId";
    public static final String FIELD_SUPPLIER_COMPANY_ID = "supplierCompanyId";
    public static final String FIELD_FATHER_ID = "fatherId";
    public static final String FIELD_AGREEMENT_ID = "agreementId";
    public static final String FIELD_AGREEMENT_DETAIL_ID = "agreementDetailId";
    public static final String FIELD_AGREEMENT_NUMBER = "agreementNumber";
    public static final String FIELD_ASSIGN_ALL_COMPANY_FLAG = "assignAllCompanyFlag";
    public static final String FIELD_AGREEMENT_BELONG_TYPE = "agreementBelongType";
    public static final String FIELD_AGREEMENT_POOL_LINE_ID = "agreementPoolLineId";
    public static final String FIELD_PUR_SHELF_FLAG = "purShelfFlag";
    public static final String FIELD_SUPPLIER_SHELF_FLAG = "supplierShelfFlag";
    public static final String FIELD_BENCHMARK_PRICE = "benchmarkPrice";
    public static final String FIELD_LOGIC_DELETE_FLAG = "logicDeleteFlag";
    public static final String FIELD_AGREEMENT_LINE_ID = "agreementLineId";
    public static final String FIELD_SHELF_ERROR_MESSAGE = "shelfErrorMessage";
    public static final String FIELD_MIN_PACKAGE_QUANTITY = "minPackageQuantity";
    public static final String SKU = "SKU";
    public static final String FIELD_CID = "cid";
    public static final String FIELD_SHELF_REMARK = "shelfRemark";

    public ProductPool() {
    }

    public ProductPool(Long productId, Long itemId, Long tenantId) {
        this.productId = productId;
        this.itemId = itemId;
        this.tenantId = tenantId;
    }
    //
    // 业务方法(按public protected private顺序排列)
    // ------------------------------------------------------------------------------

    /**
     * 更换协议
     *
     * @param agreementId          协议id
     * @param agreementNumber      协议号
     * @param agreementPoolLineId  协议池行id
     * @param agreementBelongType  协议所属类型
     * @param deliveryCycle        供货周期
     * @param assignAllCompanyFlag 是否分配所有公司
     * @param marketPrice          市场价
     * @param agreementPrice       协议价
     * @param limitQuantity        最小购买量
     * @param assignedFlag         是否被分配 0 原始数据 1 被分配
     */
    public void changeAgreement(Long agreementId, String agreementNumber, Long agreementPoolLineId, Integer agreementBelongType, String deliveryCycle, Integer assignAllCompanyFlag, BigDecimal marketPrice, BigDecimal agreementPrice, BigDecimal limitQuantity, Integer assignedFlag) {
        this.agreementId = agreementId;
        this.agreementNumber = agreementNumber;
        this.agreementPoolLineId = agreementPoolLineId;
        this.agreementBelongType = agreementBelongType;
        this.deliveryCycle = deliveryCycle;
        this.assignAllCompanyFlag = assignAllCompanyFlag;
        this.marketPrice = marketPrice;
        this.agreementPrice = agreementPrice;
        this.limitQuantity = limitQuantity;
        this.assignedFlag = assignedFlag;
        this.logicDeleteFlag = 0;
        //协议更换后默认采购方上架
        this.purchaserShelf();
    }

    /**
     * 采购方下架设置
     *
     * @return boolean
     */
    public void purchaserUnshelf() {
        this.purShelfFlag = PurConstants.ShelfStatus.UNSHELF;
        this.shelfFlag = PurConstants.ShelfStatus.MANUAL_UNSHELF;
    }

    /**
     * 采购方上架设置
     */
    public void purchaserShelf() {
        //若当前供方下架，不能上架，直接返回
        this.purShelfFlag = PurConstants.ShelfStatus.SHELF;
        this.shelfFlag = PurConstants.ShelfStatus.SHELF == this.supplierShelfFlag ? PurConstants.ShelfStatus.SHELF : PurConstants.ShelfStatus.UNSHELF;
        this.logicDeleteFlag = PurConstants.ShelfStatus.DEFAULT_LOGIC_DELETE_FLAG;
    }

    /**
     * 供应方下架设置
     */
    public void supplierUnshelf() {
        this.supplierShelfFlag = PurConstants.ShelfStatus.UNSHELF;
        this.shelfFlag = PurConstants.ShelfStatus.UNSHELF;
    }

    /**
     * 上下架处理
     */
    public void selfFlag() {
        if (!ObjectUtils.isEmpty(shelfErrorMessage)) {
            this.shelfFlag = PurConstants.ShelfStatus.SHELF_FAILED;
        } else {
            if (PurConstants.ShelfStatus.SHELF == purShelfFlag && PurConstants.ShelfStatus.SHELF == supplierShelfFlag) {
                this.shelfFlag = PurConstants.ShelfStatus.SHELF;
            } else {
                //进入商品池，为待上架状态
                this.shelfFlag = PurConstants.ShelfStatus.WAITING_SHELF;
            }
        }
    }

    /**
     * 逻辑删除
     */
    public void logicDelete() {
        this.logicDeleteFlag = PurConstants.ShelfStatus.LOGIC_DELETE_FLAG;
    }

    public void unlogicDelete() {
        this.logicDeleteFlag = PurConstants.ShelfStatus.DEFAULT_LOGIC_DELETE_FLAG;
    }


    /**
     * 判断是否上下架
     *
     * @return boolean
     */
    public boolean checkShelf() {
        return PurConstants.ShelfStatus.SHELF == this.shelfFlag;
    }

    //
    // 数据库字段
    // ------------------------------------------------------------------------------


    @ApiModelProperty("")
    @Id
    @GeneratedValue
    @Encrypt
    private Long recordId;
    @ApiModelProperty(value = "租户ID", required = true)
    @NotNull
    private Long tenantId;
    @ApiModelProperty(value = "公司ID")
    @Encrypt
    private Long companyId;
    @ApiModelProperty(value = "目录ID")
    @Encrypt
    private Long catalogId;
    @ApiModelProperty(value = "商品类型（1线上 2线下）")
    @NotNull
    private Integer productType;
    @ApiModelProperty(value = "商品ID")
    @NotNull
    @Encrypt
    private Long productId;
    @ApiModelProperty(value = "上下架状态（采方状态）")
    @NotNull
    private Integer shelfFlag;
    @ApiModelProperty(value = "上下架状态（供方状态）")
    @NotNull
    private Integer supplierShelfFlag;
    @ApiModelProperty(value = "平台价")
    private BigDecimal marketPrice;
    @ApiModelProperty(value = "协议价")
    @NotNull
    private BigDecimal agreementPrice;
    @ApiModelProperty(value = "供应商租户ID")
    private Long supplierTenantId;
    @ApiModelProperty(value = "供应商公司ID")
    @Encrypt
    private Long supplierCompanyId;

    @ApiModelProperty(value = "是否启用阶梯价格")
    private Integer ladderEnableFlag;

    @ApiModelProperty(value = "最小购买量")
    private BigDecimal limitQuantity;

    @ApiModelProperty(value = "父级id")
    private Long fatherId;

    @ApiModelProperty(value = "协议头id")
    @Encrypt
    private Long agreementId;

    @ApiModelProperty(value = "协议明细id")
    @Encrypt
    private Long agreementDetailId;

    @ApiModelProperty(value = "协议编号")
    private String agreementNumber;

    @ApiModelProperty(value = "分配所有公司标识(0否/1是)")
    private Integer assignAllCompanyFlag;

    @ApiModelProperty(value = "协议所属类型(-1集团/1公司)")
    private Integer agreementBelongType;

    @ApiModelProperty(value = "被分配标志，1 被集团分配而来的sku/0 集团或公司原始sku")
    private Integer assignedFlag;

    @ApiModelProperty(value = "协议池行id")
    @Encrypt
    private Long agreementPoolLineId;

    @ApiModelProperty(value = "供货周期")
    private String deliveryCycle;

    @ApiModelProperty(value = "上下架状态（采方状态）")
    @NotNull
    private Integer purShelfFlag;

    @ApiModelProperty(value = "基准价")
    private BigDecimal benchmarkPrice;

    @ApiModelProperty(value = "逻辑删除标识")
    private Integer logicDeleteFlag;

    @ApiModelProperty(value = "协议行id")
    @TargetField(ignore = true)
    @Encrypt
    private Long agreementLineId;

    @ApiModelProperty(value = "上架失败原因")
    private String shelfErrorMessage;

    @ApiModelProperty(value = "平台分类id")
    @Encrypt
    private Long cid;

    @ApiModelProperty(value = "物料分类id")
    @Encrypt
    private Long itemId;

    @ApiModelProperty(value = "最小包装量")
    private BigDecimal minPackageQuantity;

    @Transient
    private BigDecimal minPurchaseQuantity;

    @ApiModelProperty(value = "不含税价")
    private BigDecimal nakedPrice;

    /**
     * 此字段仅做免税商品标识
     * 增值税特殊管理，0：免税、1：不征税，2：普通零税率，3：非零税率，4：特殊税收政策
     */
    private Integer taxSpecial;

    /**
     * 备注，当增值税特殊管理为 4特殊税收政策 时，备注字段必输，填写具体的特殊税收政策是什么
     */
    private String remark;

    // 非数据库字段
    // ------------------------------------------------------------------------------

    @Transient
    @ApiModelProperty(value = "有效期从")
    private Date dateFrom;

    @Transient
    @ApiModelProperty(value = "有效期至")
    private Date dateTo;

    @Transient
    @ApiModelProperty(value = "商品名称")
    private String skuCode;

    @Transient
    @ApiModelProperty(value = "商品名称")
    private String skuName;


    @Transient
    @ApiModelProperty(value = "协议类型（公司|集团）")
    private String agreementType;

    @Transient
    @ApiModelProperty(value = "sku上下架状态")
    private Long skuShelfStatus;
    @Transient
    @TargetField("agreementLineId")
    @Encrypt
    private String esAgreementLine;

    @Transient
    @ApiModelProperty(value = "上架失败状态，1.全部成功 0.全部失败 2.部分成功")
    private Integer shelfFailureStatus;

    @Transient
    private String shelfFailureMessage;

    @ApiModelProperty(value = "商品映射方式，值集 SMAL.SKU_REF_TYPE 物料/平台分类", required = true)
    @Transient
    private String skuRefType;
    @Transient
    private String companyName;

    @Transient
    private String supplierCompanyName;
    @Transient
    @ApiModelProperty(value = "按分类更新商品池目录")
    private List<Long> categoryIds;
    @Transient
    @ApiModelProperty(value = "按物料更新商品池目录")
    private List<Long> itemIds;
    @Transient
    @Encrypt
    private Long currentCatalogId;

    @Transient
    private List<Long> skuIdList;
    @Transient
    private Map<String, Boolean> queryFlag = new HashMap<>();
    @Transient
    private BigDecimal stockQuantity;
    @Transient
    private Long regionId;
    @Transient
    private String taxRate;
    @Transient
    @ApiModelProperty(value = "物料品类id")
    private Long itemCategoryId;
    /**
     * 阶梯价
     */
    @Transient
    private List<LadderPriceResultDTO> ladderPriceList;
    /**
     * 可售标识：1/0 可售/不可售 如果匹配不到协议则为0
     */
    @Transient
    private Integer saleEnable;
    /**
     * 采购价
     */
    @Transient
    private BigDecimal purchasePrice;
    /**
     * 销售价
     */
    @Transient
    private BigDecimal sellPrice;
    /**
     * 协议业务类型:SAGM.AGREEMENT_HEADER_TYPE
     */
    @Transient
    private String agreementBusinessType;
    /**
     * 显示供应商id
     */
    @Transient
    private Long showSupplierCompanyId;
    /**
     * 显示供应商名称
     */
    @Transient
    private String showSupplierName;
    /**
     * 代理供应商id
     */
    @Transient
    private Long proxySupplierCompanyId;
    /**
     * 代理供应商名称
     */
    @Transient
    private String proxySupplierCompanyName;
    /**
     * 销售协议行id
     */
    @Transient
    private Long saleAgreementLineId;
    @ApiModelProperty(value = "备注")
    private String shelfRemark;

    @Transient
    private Integer priceHiddenFlag;

    @Transient
    @ApiModelProperty(value = "最大购买量")
    private BigDecimal purchaseQuantityLimit;
    //
    // getter/setter
    // ------------------------------------------------------------------------------


    public String getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(String taxRate) {
        this.taxRate = taxRate;
    }

    /**
     * @return
     */
    public Long getRecordId() {
        return recordId;
    }

    public void setRecordId(Long recordId) {
        this.recordId = recordId;
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
     * @return 公司ID
     */
    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    /**
     * @return 目录ID
     */
    public Long getCatalogId() {
        return catalogId;
    }

    public void setCatalogId(Long catalogId) {
        this.catalogId = catalogId;
    }

    /**
     * @return 商品类型（1线上 2线下）
     */
    public Integer getProductType() {
        return productType;
    }

    public void setProductType(Integer productType) {
        this.productType = productType;
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
     * @return 上下架状态（采方状态）
     */
    public Integer getShelfFlag() {
        return shelfFlag;
    }

    public void setShelfFlag(Integer shelfFlag) {
        this.shelfFlag = shelfFlag;
    }

    /**
     * @return 平台价
     */
    public BigDecimal getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(BigDecimal marketPrice) {
        this.marketPrice = marketPrice;
    }

    /**
     * @return 协议价
     */
    public BigDecimal getAgreementPrice() {
        return agreementPrice;
    }

    public void setAgreementPrice(BigDecimal agreementPrice) {
        this.agreementPrice = agreementPrice;
    }

    /**
     * @return 供应商租户ID
     */
    public Long getSupplierTenantId() {
        return supplierTenantId;
    }

    public void setSupplierTenantId(Long supplierTenantId) {
        this.supplierTenantId = supplierTenantId;
    }

    /**
     * @return 供应商公司ID
     */
    public Long getSupplierCompanyId() {
        return supplierCompanyId;
    }

    public void setSupplierCompanyId(Long supplierCompanyId) {
        this.supplierCompanyId = supplierCompanyId;
    }

    public Integer getLadderEnableFlag() {
        return ladderEnableFlag;
    }

    public void setLadderEnableFlag(Integer ladderEnableFlag) {
        this.ladderEnableFlag = ladderEnableFlag;
    }

    public BigDecimal getLimitQuantity() {
        return limitQuantity;
    }

    public void setLimitQuantity(BigDecimal limitQuantity) {
        this.limitQuantity = limitQuantity;
    }

    public Long getFatherId() {
        return fatherId;
    }

    public void setFatherId(Long fatherId) {
        this.fatherId = fatherId;
    }

    public Long getAgreementId() {
        return agreementId;
    }

    public void setAgreementId(Long agreementId) {
        this.agreementId = agreementId;
    }

    public Long getAgreementDetailId() {
        return agreementDetailId;
    }

    public void setAgreementDetailId(Long agreementDetailId) {
        this.agreementDetailId = agreementDetailId;
    }

    public String getAgreementNumber() {
        return agreementNumber;
    }

    public void setAgreementNumber(String agreementNumber) {
        this.agreementNumber = agreementNumber;
    }

    public Integer getAssignAllCompanyFlag() {
        return assignAllCompanyFlag;
    }

    public void setAssignAllCompanyFlag(Integer assignAllCompanyFlag) {
        this.assignAllCompanyFlag = assignAllCompanyFlag;
    }

    public Integer getAgreementBelongType() {
        return agreementBelongType;
    }

    public void setAgreementBelongType(Integer agreementBelongType) {
        this.agreementBelongType = agreementBelongType;
    }

    public Date getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }

    public Date getDateTo() {
        return dateTo;
    }

    public void setDateTo(Date dateTo) {
        this.dateTo = dateTo;
    }

    public Integer getAssignedFlag() {
        return assignedFlag;
    }

    public void setAssignedFlag(Integer assignedFlag) {
        this.assignedFlag = assignedFlag;
    }

    public Long getAgreementPoolLineId() {
        return agreementPoolLineId;
    }

    public void setAgreementPoolLineId(Long agreementPoolLineId) {
        this.agreementPoolLineId = agreementPoolLineId;
    }

    public Integer getSupplierShelfFlag() {
        return supplierShelfFlag;
    }

    public void setSupplierShelfFlag(Integer supplierShelfFlag) {
        this.supplierShelfFlag = supplierShelfFlag;
    }

    public String getDeliveryCycle() {
        return deliveryCycle;
    }

    public void setDeliveryCycle(String deliveryCycle) {
        this.deliveryCycle = deliveryCycle;
    }

    public Integer getPurShelfFlag() {
        return purShelfFlag;
    }

    public void setPurShelfFlag(Integer purShelfFlag) {
        this.purShelfFlag = purShelfFlag;
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

    public BigDecimal getBenchmarkPrice() {
        return benchmarkPrice;
    }

    public void setBenchmarkPrice(BigDecimal benchmarkPrice) {
        this.benchmarkPrice = benchmarkPrice;
    }

    public Integer getLogicDeleteFlag() {
        return logicDeleteFlag;
    }

    public void setLogicDeleteFlag(Integer logicDeleteFlag) {
        this.logicDeleteFlag = logicDeleteFlag;
    }

    public String getAgreementType() {
        return agreementType;
    }

    public void setAgreementType(String agreementType) {
        this.agreementType = agreementType;
    }

    public Long getSkuShelfStatus() {
        return skuShelfStatus;
    }

    public void setSkuShelfStatus(Long skuShelfStatus) {
        this.skuShelfStatus = skuShelfStatus;
    }

    public String getSupplierCompanyName() {
        return supplierCompanyName;
    }

    public void setSupplierCompanyName(String supplierCompanyName) {
        this.supplierCompanyName = supplierCompanyName;
    }

    public Long getAgreementLineId() {
        return agreementLineId;
    }

    public void setAgreementLineId(Long agreementLineId) {
        this.agreementLineId = agreementLineId;
    }

    public String getShelfErrorMessage() {
        return shelfErrorMessage;
    }

    public void setShelfErrorMessage(String shelfErrorMessage) {
        this.shelfErrorMessage = shelfErrorMessage;
    }


    public String getEsAgreementLine() {
        return esAgreementLine;
    }

    public void setEsAgreementLine(String esAgreementLine) {
        this.esAgreementLine = esAgreementLine;
    }

    public void initEsAgreementLine() {
        String agreementLine = ObjectUtils.isEmpty(agreementLineId) || agreementLineId == -1 ? "ALL" : String.valueOf(agreementLineId);
        this.esAgreementLine = "T" + tenantId + "-" + agreementLine;
    }


    public Integer getShelfFailureStatus() {
        return shelfFailureStatus;
    }

    public void setShelfFailureStatus(Integer shelfFailureStatus) {
        this.shelfFailureStatus = shelfFailureStatus;
    }

    public String getShelfFailureMessage() {
        return shelfFailureMessage;
    }

    public void setShelfFailureMessage(String shelfFailureMessage) {
        this.shelfFailureMessage = shelfFailureMessage;
    }

    public String getSkuRefType() {
        return skuRefType;
    }

    public void setSkuRefType(String skuRefType) {
        this.skuRefType = skuRefType;
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

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public List<Long> getCategoryIds() {
        return categoryIds;
    }

    public void setCategoryIds(List<Long> categoryIds) {
        this.categoryIds = categoryIds;
    }

    public List<Long> getItemIds() {
        return itemIds;
    }

    public void setItemIds(List<Long> itemIds) {
        this.itemIds = itemIds;
    }

    public Long getCurrentCatalogId() {
        return currentCatalogId;
    }

    public void setCurrentCatalogId(Long currentCatalogId) {
        this.currentCatalogId = currentCatalogId;
    }

    public List<Long> getSkuIdList() {
        return skuIdList;
    }

    public void setSkuIdList(List<Long> skuIdList) {
        this.skuIdList = skuIdList;
    }

    public Map<String, Boolean> getQueryFlag() {
        return queryFlag;
    }

    public void setQueryFlag(Map<String, Boolean> queryFlag) {
        this.queryFlag = queryFlag;
    }

    public BigDecimal getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(BigDecimal stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductPool that = (ProductPool) o;
        return Objects.equals(tenantId, that.tenantId) &&
                Objects.equals(productId, that.productId) &&
                Objects.equals(regionId, that.regionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tenantId, productId, regionId);
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

    public void setMinPackageQuantity(BigDecimal minPackageQuantity) {
        this.minPackageQuantity = minPackageQuantity;
    }

    public BigDecimal getMinPurchaseQuantity() {
        return minPurchaseQuantity;
    }

    public void setMinPurchaseQuantity(BigDecimal minPurchaseQuantity) {
        this.minPurchaseQuantity = minPurchaseQuantity;
    }

    public Long getRegionId() {
        return regionId;
    }

    public void setRegionId(Long regionId) {
        this.regionId = regionId;
    }

    public Long getItemCategoryId() {
        return itemCategoryId;
    }

    public void setItemCategoryId(Long itemCategoryId) {
        this.itemCategoryId = itemCategoryId;
    }

    public List<LadderPriceResultDTO> getLadderPriceList() {
        return ladderPriceList;
    }

    public void setLadderPriceList(List<LadderPriceResultDTO> ladderPriceList) {
        this.ladderPriceList = ladderPriceList;
    }

    public Integer getSaleEnable() {
        return saleEnable;
    }

    public void setSaleEnable(Integer saleEnable) {
        this.saleEnable = saleEnable;
    }

    public BigDecimal getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(BigDecimal purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public BigDecimal getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(BigDecimal sellPrice) {
        this.sellPrice = sellPrice;
    }

    public String getAgreementBusinessType() {
        return agreementBusinessType;
    }

    public void setAgreementBusinessType(String agreementBusinessType) {
        this.agreementBusinessType = agreementBusinessType;
    }

    public Long getShowSupplierCompanyId() {
        return showSupplierCompanyId;
    }

    public void setShowSupplierCompanyId(Long showSupplierCompanyId) {
        this.showSupplierCompanyId = showSupplierCompanyId;
    }

    public String getShowSupplierName() {
        return showSupplierName;
    }

    public void setShowSupplierName(String showSupplierName) {
        this.showSupplierName = showSupplierName;
    }

    public Long getProxySupplierCompanyId() {
        return proxySupplierCompanyId;
    }

    public void setProxySupplierCompanyId(Long proxySupplierCompanyId) {
        this.proxySupplierCompanyId = proxySupplierCompanyId;
    }

    public String getProxySupplierCompanyName() {
        return proxySupplierCompanyName;
    }

    public void setProxySupplierCompanyName(String proxySupplierCompanyName) {
        this.proxySupplierCompanyName = proxySupplierCompanyName;
    }

    public Long getSaleAgreementLineId() {
        return saleAgreementLineId;
    }

    public void setSaleAgreementLineId(Long saleAgreementLineId) {
        this.saleAgreementLineId = saleAgreementLineId;
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

    public Integer getPriceHiddenFlag() {
        return priceHiddenFlag;
    }

    public void setPriceHiddenFlag(Integer priceHiddenFlag) {
        this.priceHiddenFlag = priceHiddenFlag;
    }

    public String getShelfRemark() {
        return shelfRemark;
    }

    public void setShelfRemark(String shelfRemark) {
        this.shelfRemark = shelfRemark;
    }

    public BigDecimal getPurchaseQuantityLimit() {
        return purchaseQuantityLimit;
    }

    public ProductPool setPurchaseQuantityLimit(BigDecimal purchaseQuantityLimit) {
        this.purchaseQuantityLimit = purchaseQuantityLimit;
        return this;
    }
}
