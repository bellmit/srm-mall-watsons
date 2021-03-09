package org.srm.mall.common.feign.dto.product;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.choerodon.mybatis.domain.AuditDomain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * sku 销售信息
 *
 * @author jianqiang.qiu@hand-china.com 2021-02-14 22:38:31
 */
@ApiModel("sku 销售信息")
@VersionAudit
@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "smpc_sku_sales_info")
public class SkuSalesInfo extends AuditDomain {

    public static final String FIELD_SALES_ID = "salesId";
    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_AGREEMENT_LINE_ID = "agreementLineId";
    public static final String FIELD_SKU_ID = "skuId";
    public static final String FIELD_PLAT_PRICE = "platPrice";
    public static final String FIELD_AGREEMENT_TAXED_PRICE = "agreementTaxedPrice";
    public static final String FIELD_AGREEMENT_PRICE = "agreementPrice";
    public static final String FIELD_PRICE_TYPE = "priceType";
    public static final String FIELD_PRICE_HIDDEN_FLAG = "priceHiddenFlag";
    public static final String FIELD_VALID_DATE_FROM = "validDateFrom";
    public static final String FIELD_INVALID_DATE_TO = "invalidDateTo";
    public static final String FIELD_UOM_ID = "uomId";
    public static final String FIELD_TAX_ID = "taxId";
    public static final String FIELD_CURRENCY_ID = "currencyId";
    public static final String FIELD_FREE_SHIPPING_FLAG = "freeShippingFlag";
    public static final String FIELD_SHIPPING_RULE_ID = "shippingRuleId";
    public static final String FIELD_SOURCE_FROM = "sourceFrom";
	public static final String FIELD_ORDER_QUANTITY = "orderQuantity";
	public static final String FIELD_MIN_PACKAGE_QUANTITY = "minPackageQuantity";
	public static final String FIELD_ALL_UNIT_FLAG = "allUnitFlag";

    //
    // 业务方法(按public protected private顺序排列)
    // ------------------------------------------------------------------------------

    //
    // 数据库字段
    // ------------------------------------------------------------------------------


    @ApiModelProperty("销售信息ID")
    @Id
    @GeneratedValue
    private Long salesId;
    @ApiModelProperty(value = "租户ID  hpfm_tenant.tenant_id",required = true)
    @NotNull
    private Long tenantId;
   @ApiModelProperty(value = "协议行 ID")    
    private Long agreementLineId;
    @ApiModelProperty(value = "SKU ID  smpc_sku.sku_id",required = true)
    @NotNull
    private Long skuId;
    @ApiModelProperty(value = "平台价格(含税)",required = true)
    @NotNull
    private BigDecimal platPrice;
    @ApiModelProperty(value = "协议价格(含税)",required = true)
    @NotNull
    private BigDecimal agreementTaxedPrice;
    @ApiModelProperty(value = "协议价格(不含税)",required = true)
    @NotNull
    private BigDecimal agreementPrice;
    @ApiModelProperty(value = "价格类型 值集 SMPC.PRICE_TYPE  阶梯价/实时价/固定价",required = true)
    @NotBlank
    private String priceType;
    @ApiModelProperty(value = "是否隐藏价格",required = true)
    @NotNull
    private Integer priceHiddenFlag;
   @ApiModelProperty(value = "有效期从")    
    private Date validDateFrom;
   @ApiModelProperty(value = "有效期至")    
    private Date invalidDateTo;
   @ApiModelProperty(value = "单位")
   //@LovValue(meaningField = "uomName", lovCode = "SMDM.UOM")
    private Long uomId;
   @ApiModelProperty(value = "税率")
   //@LovValue(meaningField = "tax", lovCode = "SMDM.TAX")
    private Long taxId;
   @ApiModelProperty(value = "币种")
   //@LovValue(meaningField = "currencyName", lovCode = "SMDM.CURRENCY")
    private Long currencyId;
    @ApiModelProperty(value = "是否包邮",required = true)
    @NotNull
    private Integer freeShippingFlag;
   @ApiModelProperty(value = "运费规则 ID")
   //@LovValue(meaningField = "shippingRuleName", lovCode = "SMAL.POSTAGE_SUPPLIER")
    private Long shippingRuleId;
    @ApiModelProperty(value = "来源 SMPC.SKU_SALES_FROM  MANUAL-手工/AGREEMENT-协议",required = true)
    @NotBlank
    private String sourceFrom;
    @ApiModelProperty(value = "起订量")
	private BigDecimal orderQuantity;
	@ApiModelProperty(value = "最小包装量")
	private BigDecimal minPackageQuantity;
	@ApiModelProperty(value = "采买组织全选")
	private Integer allUnitFlag;

	//
    // 非数据库字段
    // ------------------------------------------------------------------------------
	//价格阶梯
	@Transient
	private List<SkuSalesLadder> skuSalesLadders;
	//采买组织
	@Transient
	private List<SkuSalesUnit> skuSalesUnits;

	@ApiModelProperty(value = "单位名称")
	@Transient
	private String uomName;
	@ApiModelProperty(value = "税率")
	@Transient
	private String tax;
	@ApiModelProperty(value = "币种名称")
	@Transient
	private String currencyName;
	@ApiModelProperty(value = "运费规则名称")
	@Transient
	private String shippingRuleName;
    //
    // getter/setter
    // ------------------------------------------------------------------------------


	public String getUomName() {
		return uomName;
	}

	public void setUomName(String uomName) {
		this.uomName = uomName;
	}

	public String getTax() {
		return tax;
	}

	public void setTax(String tax) {
		this.tax = tax;
	}

	public String getCurrencyName() {
		return currencyName;
	}

	public void setCurrencyName(String currencyName) {
		this.currencyName = currencyName;
	}

	public String getShippingRuleName() {
		return shippingRuleName;
	}

	public void setShippingRuleName(String shippingRuleName) {
		this.shippingRuleName = shippingRuleName;
	}

	public List<SkuSalesLadder> getSkuSalesLadders() {
		return skuSalesLadders;
	}

	public void setSkuSalesLadders(List<SkuSalesLadder> skuSalesLadders) {
		this.skuSalesLadders = skuSalesLadders;
	}

	public List<SkuSalesUnit> getSkuSalesUnits() {
		return skuSalesUnits;
	}

	public void setSkuSalesUnits(List<SkuSalesUnit> skuSalesUnits) {
		this.skuSalesUnits = skuSalesUnits;
	}

	/**
     * @return 销售信息ID
     */
	public Long getSalesId() {
		return salesId;
	}

	public void setSalesId(Long salesId) {
		this.salesId = salesId;
	}
    /**
     * @return 租户ID  hpfm_tenant.tenant_id
     */
	public Long getTenantId() {
		return tenantId;
	}

	public void setTenantId(Long tenantId) {
		this.tenantId = tenantId;
	}
    /**
     * @return 协议行 ID
     */
	public Long getAgreementLineId() {
		return agreementLineId;
	}

	public void setAgreementLineId(Long agreementLineId) {
		this.agreementLineId = agreementLineId;
	}
    /**
     * @return SKU ID  smpc_sku.sku_id
     */
	public Long getSkuId() {
		return skuId;
	}

	public void setSkuId(Long skuId) {
		this.skuId = skuId;
	}
    /**
     * @return 平台价格(含税)
     */
	public BigDecimal getPlatPrice() {
		return platPrice;
	}

	public void setPlatPrice(BigDecimal platPrice) {
		this.platPrice = platPrice;
	}
    /**
     * @return 协议价格(含税)
     */
	public BigDecimal getAgreementTaxedPrice() {
		return agreementTaxedPrice;
	}

	public void setAgreementTaxedPrice(BigDecimal agreementTaxedPrice) {
		this.agreementTaxedPrice = agreementTaxedPrice;
	}
    /**
     * @return 协议价格(不含税)
     */
	public BigDecimal getAgreementPrice() {
		return agreementPrice;
	}

	public void setAgreementPrice(BigDecimal agreementPrice) {
		this.agreementPrice = agreementPrice;
	}
    /**
     * @return 价格类型 值集 SMPC.PRICE_TYPE  阶梯价/实时价/固定价
     */
	public String getPriceType() {
		return priceType;
	}

	public void setPriceType(String priceType) {
		this.priceType = priceType;
	}
    /**
     * @return 是否隐藏价格
     */
	public Integer getPriceHiddenFlag() {
		return priceHiddenFlag;
	}

	public void setPriceHiddenFlag(Integer priceHiddenFlag) {
		this.priceHiddenFlag = priceHiddenFlag;
	}
    /**
     * @return 有效期从
     */
	public Date getValidDateFrom() {
		return validDateFrom;
	}

	public void setValidDateFrom(Date validDateFrom) {
		this.validDateFrom = validDateFrom;
	}
    /**
     * @return 有效期至
     */
	public Date getInvalidDateTo() {
		return invalidDateTo;
	}

	public void setInvalidDateTo(Date invalidDateTo) {
		this.invalidDateTo = invalidDateTo;
	}
    /**
     * @return 单位
     */
	public Long getUomId() {
		return uomId;
	}

	public void setUomId(Long uomId) {
		this.uomId = uomId;
	}
    /**
     * @return 税率
     */
	public Long getTaxId() {
		return taxId;
	}

	public void setTaxId(Long taxId) {
		this.taxId = taxId;
	}
    /**
     * @return 币种
     */
	public Long getCurrencyId() {
		return currencyId;
	}

	public void setCurrencyId(Long currencyId) {
		this.currencyId = currencyId;
	}
    /**
     * @return 是否包邮
     */
	public Integer getFreeShippingFlag() {
		return freeShippingFlag;
	}

	public void setFreeShippingFlag(Integer freeShippingFlag) {
		this.freeShippingFlag = freeShippingFlag;
	}
    /**
     * @return 运费规则 ID
     */
	public Long getShippingRuleId() {
		return shippingRuleId;
	}

	public void setShippingRuleId(Long shippingRuleId) {
		this.shippingRuleId = shippingRuleId;
	}
    /**
     * @return 来源 SMPC.SKU_SALES_FROM  MANUAL-手工/AGREEMENT-协议
     */
	public String getSourceFrom() {
		return sourceFrom;
	}

	public void setSourceFrom(String sourceFrom) {
		this.sourceFrom = sourceFrom;
	}

	public BigDecimal getOrderQuantity() {
		return orderQuantity;
	}

	public void setOrderQuantity(BigDecimal orderQuantity) {
		this.orderQuantity = orderQuantity;
	}

	public BigDecimal getMinPackageQuantity() {
		return minPackageQuantity;
	}

	public void setMinPackageQuantity(BigDecimal minPackageQuantity) {
		this.minPackageQuantity = minPackageQuantity;
	}

	public Integer getAllUnitFlag() {
		return allUnitFlag;
	}

	public void setAllUnitFlag(Integer allUnitFlag) {
		this.allUnitFlag = allUnitFlag;
	}

	public void init(Long tenantId, Long skuId) {
		this.tenantId = tenantId;
		this.skuId = skuId;
		this.sourceFrom = SmpcConstants.SourceFromType.MANUAL;
	}
}
