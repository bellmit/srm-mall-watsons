package org.srm.mall.common.feign.dto.product;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.choerodon.mybatis.domain.AuditDomain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * sku销售阶梯价
 *
 * @author jianqiang.qiu@hand-china.com 2021-02-14 22:38:31
 */
@ApiModel("sku销售阶梯价")
@VersionAudit
@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "smpc_sku_sales_ladder")
public class SkuSalesLadder extends AuditDomain {

    public static final String FIELD_LADDER_ID = "ladderId";
    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_SALES_ID = "salesId";
    public static final String FIELD_SKU_ID = "skuId";
    public static final String FIELD_LADDER_FROM = "ladderFrom";
    public static final String FIELD_LADDER_TO = "ladderTo";
    public static final String FIELD_UNIT_PRICE = "unitPrice";
    public static final String FIELD_TAX_PRICE = "taxPrice";

    //
    // 业务方法(按public protected private顺序排列)
    // ------------------------------------------------------------------------------

    //
    // 数据库字段
    // ------------------------------------------------------------------------------


    @ApiModelProperty("阶梯价ID")
    @Id
    @GeneratedValue
    private Long ladderId;
    @ApiModelProperty(value = "租户ID  hpfm_tenant.tenant_id",required = true)
    @NotNull
    private Long tenantId;
    @ApiModelProperty(value = "销售信息 ID smpc_sku_sales_info.sales_id",required = true)
    @NotNull
    private Long salesId;
    @ApiModelProperty(value = "SKU ID  smpc_sku.sku_id",required = true)
    @NotNull
    private Long skuId;
    @ApiModelProperty(value = "数量从",required = true)
    @NotNull
    private BigDecimal ladderFrom;
   @ApiModelProperty(value = "数量至")    
    private BigDecimal ladderTo;
    @ApiModelProperty(value = "不含税单价",required = true)
    @NotNull
    private BigDecimal unitPrice;
    @ApiModelProperty(value = "含税单价",required = true)
    @NotNull
    private BigDecimal taxPrice;

	//
    // 非数据库字段
    // ------------------------------------------------------------------------------

    //
    // getter/setter
    // ------------------------------------------------------------------------------

    /**
     * @return 阶梯价ID
     */
	public Long getLadderId() {
		return ladderId;
	}

	public void setLadderId(Long ladderId) {
		this.ladderId = ladderId;
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
     * @return 销售信息 ID smpc_sku_sales_info.sales_id
     */
	public Long getSalesId() {
		return salesId;
	}

	public void setSalesId(Long salesId) {
		this.salesId = salesId;
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
     * @return 数量从
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
     * @return 不含税单价
     */
	public BigDecimal getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(BigDecimal unitPrice) {
		this.unitPrice = unitPrice;
	}
    /**
     * @return 含税单价
     */
	public BigDecimal getTaxPrice() {
		return taxPrice;
	}

	public void setTaxPrice(BigDecimal taxPrice) {
		this.taxPrice = taxPrice;
	}

}
