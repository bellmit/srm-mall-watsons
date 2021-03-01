package org.srm.mall.common.feign.dto.product;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.choerodon.mybatis.domain.AuditDomain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * sku 销售可采买组织
 *
 * @author jianqiang.qiu@hand-china.com 2021-02-14 22:38:30
 */
@ApiModel("sku 销售可采买组织")
@VersionAudit
@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "smpc_sku_sales_unit")
public class SkuSalesUnit extends AuditDomain {

    public static final String FIELD_SALES_UNIT_ID = "salesUnitId";
    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_SALES_ID = "salesId";
    public static final String FIELD_SKU_ID = "skuId";
    public static final String FIELD_UNIT_ID = "unitId";

    //
    // 业务方法(按public protected private顺序排列)
    // ------------------------------------------------------------------------------

    //
    // 数据库字段
    // ------------------------------------------------------------------------------


    @ApiModelProperty("表ID")
    @Id
    @GeneratedValue
    private Long salesUnitId;
    @ApiModelProperty(value = "租户ID  hpfm_tenant.tenant_id",required = true)
    @NotNull
    private Long tenantId;
    @ApiModelProperty(value = "销售信息 ID smpc_sku_sales_info.sales_id",required = true)
    @NotNull
    private Long salesId;
    @ApiModelProperty(value = "SKU ID  smpc_sku.sku_id",required = true)
    @NotNull
    private Long skuId;
   @ApiModelProperty(value = "组织 ID  hpfm_unit.unit_id")    
    private Long unitId;

	//
    // 非数据库字段
    // ------------------------------------------------------------------------------

    //
    // getter/setter
    // ------------------------------------------------------------------------------

    /**
     * @return 表ID
     */
	public Long getSalesUnitId() {
		return salesUnitId;
	}

	public void setSalesUnitId(Long salesUnitId) {
		this.salesUnitId = salesUnitId;
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
     * @return 组织 ID  hpfm_unit.unit_id
     */
	public Long getUnitId() {
		return unitId;
	}

	public void setUnitId(Long unitId) {
		this.unitId = unitId;
	}

}
