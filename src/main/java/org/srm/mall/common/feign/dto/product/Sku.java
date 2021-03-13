package org.srm.mall.common.feign.dto.product;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.MultiLanguage;
import io.choerodon.mybatis.annotation.MultiLanguageField;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.choerodon.mybatis.domain.AuditDomain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hzero.boot.platform.code.builder.CodeRuleBuilder;
import org.hzero.core.base.BaseConstants;
import org.hzero.starter.keyencrypt.core.Encrypt;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * SKU
 *
 * @author yuhao.guo@hand-china.com 2020-12-15 11:18:00
 */
@ApiModel("SKU")
@VersionAudit
@ModifyAudit
@MultiLanguage
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "smpc_sku")
public class Sku extends AuditDomain {

    public static final String FIELD_SKU_ID = "skuId";
    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_SPU_ID = "spuId";
    public static final String FIELD_SKU_CODE = "skuCode";
    public static final String FIELD_SKU_NAME = "skuName";
    public static final String FIELD_CATEGORY_ID = "categoryId";
    public static final String FIELD_INTRODUCTION_URL = "introductionUrl";
    public static final String FIELD_SOURCE_FROM = "sourceFrom";
    public static final String FIELD_SOURCE_FROM_TYPE = "sourceFromType";
    public static final String FIELD_THIRD_SKU_CODE = "thirdSkuCode";
    public static final String FIELD_PACKING_LIST = "packingList";
    public static final String FIELD_PRIMARY_FLAG = "primaryFlag";
    public static final String FIELD_SKU_STATUS = "skuStatus";
    public static final String FIELD_SUPPLIER_TENANT_ID = "supplierTenantId";
    public static final String FIELD_SUPPLIER_COMPANY_ID = "supplierCompanyId";
    public static final String FIELD_UNIT_PRICE = "unitPrice";
    public static final String FIELD_MARKET_PRICE = "marketPrice";
    public static final String FIELD_SALE_PRICE = "salePrice";
    public static final String FIELD_VERSION = "version";
    public static final String FIELD_SUPPLIER_ITEM_CODE = "supplierItemCode";
    public static final String FIELD_SUPPLIER_ITEM_NAME = "supplierItemName";
    public static final String FIELD_MANUFACTURER_ITEM_CODE = "manufacturerItemCode";
    public static final String FIELD_MANUFACTURER_ITEM_NAME = "manufacturerItemName";
    public static final String FIELD_MANUFACTURER_INFO = "manufacturerInfo";

    //
    // 业务方法(按public protected private顺序排列)
    // ------------------------------------------------------------------------------

	public void init(Long spuId, Long categoryId) {
		this.spuId = spuId;
		this.categoryId = categoryId;
		this.sourceFrom = ObjectUtils.isEmpty(this.sourceFrom) ? "CATA" : this.sourceFrom;
		this.sourceFromType = ObjectUtils.isEmpty(this.sourceFromType) ? SmpcConstants.SourceFromType.MANUAL : this.sourceFromType;
		this.skuStatus = 0;
		if (this.supplierTenantId == null || this.supplierTenantId.equals(-1L)) {
			this.supplierTenantId = -1L;
			this.supplierCompanyId = -1L;
		}
		this.version = 1L;
	}

	//商品导入初始化
	public Sku importInit(Long tenantId, Long spuId, Long categoryId, SkuImportDTO skuImportDTO, CodeRuleBuilder codeRuleBuilder, SkuService skuService) {
		this.skuCode = codeRuleBuilder.generateCode(SmpcConstants.CodeRule.CODE_RULE_SKU_CODE, null);
		this.tenantId = tenantId;
		this.skuName = skuImportDTO.getSkuName();
		this.spuId = spuId;
		this.categoryId = categoryId;
		this.sourceFrom = ObjectUtils.isEmpty(this.sourceFrom) ? "CATA" : this.sourceFrom;
		this.sourceFromType = ObjectUtils.isEmpty(this.sourceFromType) ? SmpcConstants.SourceFromType.MANUAL : this.sourceFromType;
		this.supplierCompanyId = ObjectUtils.isEmpty(skuImportDTO.getSupplierCompanyId()) ? -1L : skuImportDTO.getSupplierCompanyId();
		this.supplierTenantId = ObjectUtils.isEmpty(skuImportDTO.getSupplierTenantId()) ? -1L : skuImportDTO.getSupplierTenantId();
		this.version = 1L;
		this.primaryFlag = BaseConstants.Flag.YES;
		if (skuService.checkPlatformApprove()) {
			this.skuStatus = SmpcConstants.SkuStatus.WAITING;
		} else {
			this.skuStatus = SmpcConstants.SkuStatus.VALID;
		}
		if (!ObjectUtils.isEmpty(skuImportDTO.getPrice())) {
			this.unitPrice = skuImportDTO.getPrice();
		} else {
			this.unitPrice = BigDecimal.ZERO;
		}
		return this;
	}
    //
    // 数据库字段
    // ------------------------------------------------------------------------------


    @ApiModelProperty("")
    @Id
    @GeneratedValue
    @Encrypt
    private Long skuId;
    @ApiModelProperty(value = "租户ID，hpfm_tenant.tenant_id", required = true)
    @NotNull
    private Long tenantId;
    @ApiModelProperty(value = "商品组ID,smpc_spu.spu_id", required = true)
    @NotNull
    @Encrypt
    private Long spuId;
    @ApiModelProperty(value = "商品编码")
    private String skuCode;
    @ApiModelProperty(value = "商品名称", required = true)
    @NotBlank
    @MultiLanguageField
    private String skuName;
    @ApiModelProperty(value = "商品分类ID，smpc_category.category_id", required = true)
    @NotNull
    @Encrypt
    private Long categoryId;
    @ApiModelProperty(value = "介绍地址")
    private String introductionUrl;
    @ApiModelProperty(value = "目录化：CATA；电商：EC", required = true)
    @NotBlank
    private String sourceFrom;
    @ApiModelProperty(value = "商品来源类型；手工：MANUAL；协议：AGREEMENT", required = true)
    @NotBlank
    private String sourceFromType;
    @ApiModelProperty(value = "第三方商品编码")
    private String thirdSkuCode;
    @ApiModelProperty(value = "包装清单")
    private String packingList;
    @ApiModelProperty(value = "主SKU； 1:主sku,0:非主sku", required = true)
    @NotNull
    private Integer primaryFlag;
    @ApiModelProperty(value = "sku状态；0:新建 1:审批通过 2:审批拒绝 3:锁定 4:解锁", required = true)
    @NotNull
    private Integer skuStatus;
    @ApiModelProperty(value = "供应商租户ID  hpfm_tenant.tenant_id", required = true)
    @NotNull
    private Long supplierTenantId;
    @ApiModelProperty(value = "供应商公司ID，hpfm_company.company_id", required = true)
    @NotNull
    @Encrypt
    private Long supplierCompanyId;
    @ApiModelProperty(value = "单价")
    private BigDecimal unitPrice;
    @ApiModelProperty(value = "市场价")
    private BigDecimal marketPrice;
    @ApiModelProperty(value = "销售价")
    private BigDecimal salePrice;
    @ApiModelProperty(value = "版本号", required = true)
    @NotNull
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

    //
    // 非数据库字段
    // ------------------------------------------------------------------------------
    @Transient
    @ApiModelProperty(value = "是否供应商")
    private Integer supFlag;
    @Transient
    @ApiModelProperty(value = "商品主图")
    private String mediaPath;
    @Transient
    @ApiModelProperty(value = "spu编码")
    private String spuCode;
    @Transient
    @ApiModelProperty(value = "spu名称")
    private String spuName;
    @Transient
    @ApiModelProperty(value = "商品库存")
    private Long skuStock;
    @Transient
    @ApiModelProperty(value = "供应商租户名称（采方查询）")
    private String supplierTenantName;
    @Transient
    @ApiModelProperty(value = "供应商公司名称（采方查询）")
    private String supplierCompanyName;
    @Transient
    @ApiModelProperty(value = "三级分类名称")
    private String categoryNamePath;
    //
    // getter/setter
    // ------------------------------------------------------------------------------

    /**
     * @return
     */
    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    /**
     * @return 租户ID，hpfm_tenant.tenant_id
     */
    public Long getTenantId() {
        return tenantId;
    }

    public Sku setTenantId(Long tenantId) {
        this.tenantId = tenantId;
        return this;
    }

    /**
     * @return 商品组ID, smpc_spu.spu_id
     */
    public Long getSpuId() {
        return spuId;
    }

    public void setSpuId(Long spuId) {
        this.spuId = spuId;
    }

    /**
     * @return 商品编码
     */
    public String getSkuCode() {
        return skuCode;
    }

    public Sku setSkuCode(String skuCode) {
        this.skuCode = skuCode;
        return this;
    }

    /**
     * @return 商品名称
     */
    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    /**
     * @return 商品分类ID，smpc_category.category_id
     */
    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    /**
     * @return 介绍地址
     */
    public String getIntroductionUrl() {
        return introductionUrl;
    }

    public void setIntroductionUrl(String introductionUrl) {
        this.introductionUrl = introductionUrl;
    }

    /**
     * @return 目录化：CATA；电商：EC
     */
    public String getSourceFrom() {
        return sourceFrom;
    }

    public void setSourceFrom(String sourceFrom) {
        this.sourceFrom = sourceFrom;
    }

    /**
     * @return 商品来源类型；手工：MANUAL；协议：AGREEMENT
     */
    public String getSourceFromType() {
        return sourceFromType;
    }

    public void setSourceFromType(String sourceFromType) {
        this.sourceFromType = sourceFromType;
    }

    /**
     * @return 第三方商品编码
     */
    public String getThirdSkuCode() {
        return thirdSkuCode;
    }

    public void setThirdSkuCode(String thirdSkuCode) {
        this.thirdSkuCode = thirdSkuCode;
    }

    /**
     * @return 包装清单
     */
    public String getPackingList() {
        return packingList;
    }

    public void setPackingList(String packingList) {
        this.packingList = packingList;
    }

    /**
     * @return 主SKU； 1:主sku,0:非主sku
     */
    public Integer getPrimaryFlag() {
        return primaryFlag;
    }

    public void setPrimaryFlag(Integer primaryFlag) {
        this.primaryFlag = primaryFlag;
    }

    /**
     * @return sku状态；0:新建 1:审批通过 2:审批拒绝 3:锁定 4:解锁
     */
    public Integer getSkuStatus() {
        return skuStatus;
    }

    public void setSkuStatus(Integer skuStatus) {
        this.skuStatus = skuStatus;
    }

    /**
     * @return 供应商租户ID  hpfm_tenant.tenant_id
     */
    public Long getSupplierTenantId() {
        return supplierTenantId;
    }

    public void setSupplierTenantId(Long supplierTenantId) {
        this.supplierTenantId = supplierTenantId;
    }

    /**
     * @return 供应商公司ID，hpfm_company.company_id
     */
    public Long getSupplierCompanyId() {
        return supplierCompanyId;
    }

    public void setSupplierCompanyId(Long supplierCompanyId) {
        this.supplierCompanyId = supplierCompanyId;
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
     * @return 市场价
     */
    public BigDecimal getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(BigDecimal marketPrice) {
        this.marketPrice = marketPrice;
    }

    /**
     * @return 销售价
     */
    public BigDecimal getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(BigDecimal salePrice) {
        this.salePrice = salePrice;
    }

    /**
     * @return 版本号
     */
    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    /**
     * @return 供应商物料号
     */
    public String getSupplierItemCode() {
        return supplierItemCode;
    }

    public void setSupplierItemCode(String supplierItemCode) {
        this.supplierItemCode = supplierItemCode;
    }

    /**
     * @return 供应商物料名称
     */
    public String getSupplierItemName() {
        return supplierItemName;
    }

    public void setSupplierItemName(String supplierItemName) {
        this.supplierItemName = supplierItemName;
    }

    /**
     * @return 制造商物料号
     */
    public String getManufacturerItemCode() {
        return manufacturerItemCode;
    }

    public void setManufacturerItemCode(String manufacturerItemCode) {
        this.manufacturerItemCode = manufacturerItemCode;
    }

    /**
     * @return 制造商物料名称
     */
    public String getManufacturerItemName() {
        return manufacturerItemName;
    }

    public void setManufacturerItemName(String manufacturerItemName) {
        this.manufacturerItemName = manufacturerItemName;
    }

    /**
     * @return 制造商信息
     */
    public String getManufacturerInfo() {
        return manufacturerInfo;
    }

    public void setManufacturerInfo(String manufacturerInfo) {
        this.manufacturerInfo = manufacturerInfo;
    }

    public Integer getSupFlag() {
        return supFlag;
    }

    public void setSupFlag(Integer supFlag) {
        this.supFlag = supFlag;
    }

    public String getMediaPath() {
        return mediaPath;
    }

    public void setMediaPath(String mediaPath) {
        this.mediaPath = mediaPath;
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

    public Long getSkuStock() {
        return skuStock;
    }

    public void setSkuStock(Long skuStock) {
        this.skuStock = skuStock;
    }

    public String getSupplierTenantName() {
        return supplierTenantName;
    }

    public void setSupplierTenantName(String supplierTenantName) {
        this.supplierTenantName = supplierTenantName;
    }

    public String getSupplierCompanyName() {
        return supplierCompanyName;
    }

    public void setSupplierCompanyName(String supplierCompanyName) {
        this.supplierCompanyName = supplierCompanyName;
    }

    public String getCategoryNamePath() {
        return categoryNamePath;
    }

    public void setCategoryNamePath(String categoryNamePath) {
        this.categoryNamePath = categoryNamePath;
    }
}
