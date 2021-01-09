package org.srm.mall.other.domain.entity;

import java.util.Date;
import java.util.List;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import javax.validation.constraints.NotBlank;
import io.choerodon.mybatis.domain.AuditDomain;
import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hzero.core.base.BaseConstants;
import org.hzero.export.annotation.ExcelColumn;
import org.hzero.export.annotation.ExcelSheet;

/**
 * 屈臣氏费用项目表
 *
 * @author ronghu.kou@hand-china.com 2020-12-29 14:40:14
 */
@ApiModel("屈臣氏费用项目表")
@VersionAudit
@ModifyAudit
@ExcelSheet(zh= "费用项目定义导出")
@Table(name = "scux_watsons_project_cost")
public class ProjectCost extends AuditDomain {

    public static final String FIELD_PROJECT_COST_ID = "projectCostId";
    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_PROJECT_COST_CODE = "projectCostCode";
    public static final String FIELD_PROJECT_COST_NAME = "projectCostName";
    public static final String FIELD_CATEGORY_ID = "categoryId";
    public static final String FIELD_CATEGORY_CODE = "categoryCode";
    public static final String FIELD_CATEGORY_NAME = "categoryName";
    public static final String FIELD_SECONDARY_CATEGORY_ID = "secondaryCategoryId";
    public static final String FIELD_SECONDARY_CATEGORY_CODE = "secondaryCategoryCode";
    public static final String FIELD_SECONDARY_CATEGORY_NAME = "secondaryCategoryName";
    public static final String FIELD_PROJECT_COST_SUBCATEGORY = "projectCostSubcategory";
    public static final String FIELD_LAST_UPDATED_PERSON = "lastUpdatedPerson";

    //
    // 业务方法(按public protected private顺序排列)
    // ------------------------------------------------------------------------------

    //
    // 数据库字段
    // ------------------------------------------------------------------------------



    @ApiModelProperty(value = "租户ID,hpfm_tenant.tenant_id",required = true)
    @NotNull
    private Long tenantId;
   @ApiModelProperty(value = "一级品类ID")    
    private Long categoryId;
	@ExcelColumn(zh = "一级品类编码")
   @ApiModelProperty(value = "一级品类编码")    
    private String categoryCode;
	@ExcelColumn(zh = "一级品类名称")
   @ApiModelProperty(value = "一级品类名称")    
    private String categoryName;
   @ApiModelProperty(value = "二级品类ID")    
    private Long secondaryCategoryId;
	@ExcelColumn(zh = "二级品类编码")
   @ApiModelProperty(value = "二级品类编码")    
    private String secondaryCategoryCode;
	@ExcelColumn(zh = "二级品类名称")
   @ApiModelProperty(value = "二级品类名称")
    private String secondaryCategoryName;
	@ApiModelProperty("费用项目id")
	@Id
	@GeneratedValue
	private Long projectCostId;
	@ExcelColumn(zh = "费用项目编码")
	@ApiModelProperty(value = "费用项目编码",required = true)
	@NotBlank
	private String projectCostCode;
	@ExcelColumn(zh = "费用项目名称")
	@ApiModelProperty(value = "费用项目名称",required = true)
	@NotBlank
	private String projectCostName;
	@ExcelColumn(zh = "费用项目子分类")
    @ApiModelProperty(value = "费用项目子分类",required = true)
    @NotBlank
    private String projectCostSubcategory;
	@ApiModelProperty(value = "费用项目子分类",required = true)
	private Boolean hasProjectCostSubcategory;

	//
    // 非数据库字段
    // ------------------------------------------------------------------------------
	@ExcelColumn(zh = "最后更新人")
	@ApiModelProperty(value = "最后更新人")
	@Transient
	private String lastUpdatedPerson;
	@Transient
	private List<ProjectCostSubcategory> projectCostSubcategoryList;
	@Transient
	@ExcelColumn(zh = "最后更新日期",pattern = BaseConstants.Pattern.DATETIME)
	@ApiModelProperty(value = "最后更新日期")
	private Date lastUpdateDate;
	@Transient
	private Date lastUpdateDateFrom;
	@Transient
	private Date lastUpdateDateTo;
	@Transient
	private List<Long> projectSubcategoryCodes;
	@Transient
	private List<Long> projectCostIds;
    //
    // getter/setter
    // ------------------------------------------------------------------------------

	public Boolean getHasProjectCostSubcategory() {
		return hasProjectCostSubcategory;
	}

	public void setHasProjectCostSubcategory(Boolean hasProjectCostSubcategory) {
		this.hasProjectCostSubcategory = hasProjectCostSubcategory;
	}

	/**
     * @return 
     */
	public Long getProjectCostId() {
		return projectCostId;
	}

	public void setProjectCostId(Long projectCostId) {
		this.projectCostId = projectCostId;
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
     * @return 费用项目编码
     */
	public String getProjectCostCode() {
		return projectCostCode;
	}

	public void setProjectCostCode(String projectCostCode) {
		this.projectCostCode = projectCostCode;
	}
    /**
     * @return 费用项目名称
     */
	public String getProjectCostName() {
		return projectCostName;
	}

	public void setProjectCostName(String projectCostName) {
		this.projectCostName = projectCostName;
	}
    /**
     * @return 一级品类ID
     */
	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}
    /**
     * @return 一级品类编码
     */
	public String getCategoryCode() {
		return categoryCode;
	}

	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}
    /**
     * @return 一级品类名称
     */
	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
    /**
     * @return 二级品类ID
     */
	public Long getSecondaryCategoryId() {
		return secondaryCategoryId;
	}

	public void setSecondaryCategoryId(Long secondaryCategoryId) {
		this.secondaryCategoryId = secondaryCategoryId;
	}
    /**
     * @return 二级品类编码
     */
	public String getSecondaryCategoryCode() {
		return secondaryCategoryCode;
	}

	public void setSecondaryCategoryCode(String secondaryCategoryCode) {
		this.secondaryCategoryCode = secondaryCategoryCode;
	}
    /**
     * @return 二级品类名称
     */
	public String getSecondaryCategoryName() {
		return secondaryCategoryName;
	}

	public void setSecondaryCategoryName(String secondaryCategoryName) {
		this.secondaryCategoryName = secondaryCategoryName;
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
    /**
     * @return 最后更新人
     */
	public String getLastUpdatedPerson() {
		return lastUpdatedPerson;
	}

	public void setLastUpdatedPerson(String lastUpdatedPerson) {
		this.lastUpdatedPerson = lastUpdatedPerson;
	}

	public List<ProjectCostSubcategory> getProjectCostSubcategoryList() {
		return projectCostSubcategoryList;
	}

	public void setProjectCostSubcategoryList(List<ProjectCostSubcategory> projectCostSubcategoryList) {
		this.projectCostSubcategoryList = projectCostSubcategoryList;
	}

	@Override
	public Date getLastUpdateDate() {
		return lastUpdateDate;
	}

	@Override
	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}

	public Date getLastUpdateDateFrom() {
		return lastUpdateDateFrom;
	}

	public void setLastUpdateDateFrom(Date lastUpdateDateFrom) {
		this.lastUpdateDateFrom = lastUpdateDateFrom;
	}

	public Date getLastUpdateDateTo() {
		return lastUpdateDateTo;
	}

	public void setLastUpdateDateTo(Date lastUpdateDateTo) {
		this.lastUpdateDateTo = lastUpdateDateTo;
	}

	public List<Long> getProjectSubcategoryCodes() {
		return projectSubcategoryCodes;
	}

	public void setProjectSubcategoryCodes(List<Long> projectSubcategoryCodes) {
		this.projectSubcategoryCodes = projectSubcategoryCodes;
	}

	public List<Long> getProjectCostIds() {
		return projectCostIds;
	}

	public void setProjectCostIds(List<Long> projectCostIds) {
		this.projectCostIds = projectCostIds;
	}
}
