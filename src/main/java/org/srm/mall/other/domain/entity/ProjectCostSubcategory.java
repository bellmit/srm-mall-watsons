package org.srm.mall.other.domain.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import javax.validation.constraints.NotBlank;
import io.choerodon.mybatis.domain.AuditDomain;
import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 屈臣氏项目费用子分类表
 *
 * @author ronghu.kou@hand-china.com 2020-12-29 16:42:58
 */
@ApiModel("屈臣氏项目费用子分类表")
@VersionAudit
@ModifyAudit
@Table(name = "scux_watsons_project_cost_subcategory")
public class ProjectCostSubcategory extends AuditDomain {

    public static final String FIELD_PROJECT_COST_SUBCATEGORY_ID = "projectCostSubcategoryId";
    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_PROJECT_COST_ID = "projectCostId";
    public static final String FIELD_PROJECT_COST_SUBCATEGORY = "projectCostSubcategory";

    //
    // 业务方法(按public protected private顺序排列)
    // ------------------------------------------------------------------------------

    //
    // 数据库字段
    // ------------------------------------------------------------------------------


    @ApiModelProperty("")
    @Id
    @GeneratedValue
    private Long projectCostSubcategoryId;
    @ApiModelProperty(value = "租户ID,hpfm_tenant.tenant_id",required = true)
    @NotNull
    private Long tenantId;
   @ApiModelProperty(value = "费用项目ID")    
    private Long projectCostId;
	@ApiModelProperty(value = "费用项目子分类编码 取lov_value_id")
	private Long subcategoryCode;
   @ApiModelProperty(value = "费用项目子分类")    
    private String projectCostSubcategory;

	//
    // 非数据库字段
    // ------------------------------------------------------------------------------

    //
    // getter/setter
    // ------------------------------------------------------------------------------

    /**
     * @return 
     */
	public Long getProjectCostSubcategoryId() {
		return projectCostSubcategoryId;
	}

	public void setProjectCostSubcategoryId(Long projectCostSubcategoryId) {
		this.projectCostSubcategoryId = projectCostSubcategoryId;
	}
    /**
     * @return 租户ID,hpfm_tenant.tenant_id
     */
	public Long getTenantId() {
		return tenantId;
	}

	public void setTenantId(Long tenantId) {
		this.tenantId = tenantId;
	}
    /**
     * @return 费用项目ID
     */
	public Long getProjectCostId() {
		return projectCostId;
	}

	public void setProjectCostId(Long projectCostId) {
		this.projectCostId = projectCostId;
	}
    /**
     * @return 费用项目子分类
     */
	public String getProjectCostSubcategory() {
		return projectCostSubcategory;
	}

	public void setProjectCostSubcategory(String projectCostSubcategory) {
		this.projectCostSubcategory = projectCostSubcategory;
	}

	public Long getSubcategoryCode() {
		return subcategoryCode;
	}

	public void setSubcategoryCode(Long subcategoryCode) {
		this.subcategoryCode = subcategoryCode;
	}
}
