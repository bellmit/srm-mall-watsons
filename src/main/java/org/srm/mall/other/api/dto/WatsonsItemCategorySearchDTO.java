package org.srm.mall.other.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import org.hzero.starter.keyencrypt.core.Encrypt;
import org.srm.common.mybatis.domain.ExpandDomain;

import javax.persistence.Transient;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * <p>
 * WatsonsItemCategorySearchDTO
 * </p>
 *
 * @author linqing.wang@hand-china.com 2020/12/25 15:54
 */
@ApiModel
public class WatsonsItemCategorySearchDTO extends ExpandDomain {

    @ApiParam(hidden = true)
    @Encrypt
    private Long categoryId;
    @ApiParam(hidden = true)
    private Long tenantId;


    @ApiModelProperty("类别编码")
    private String categoryCode;
    @ApiModelProperty("查询类别Id")
    private String queryCategoryId;
    @ApiModelProperty("查询类别编码")
    private String queryCategoryCode;
    @ApiModelProperty("查询类别名称")
    private String queryCategoryName;
    @ApiModelProperty("类别名称")
    private String categoryName;
    @Max(1)
    @Min(0)
    @ApiModelProperty("启用标识")
    private Integer enabledFlag;
    @Max(1)
    @Min(0)
    @ApiModelProperty("模板启用标识")
    private Integer templateEnabledFlag;

    //
    // 非数据库字段
    // ------------------------------------------------------------------------------

    @Transient
    @ApiModelProperty("物料ID")
    @JsonIgnore
    @Encrypt
    private Long itemId;


    @Transient
    private Integer defaultFlag;
    //
    // getter/setter
    // ------------------------------------------------------------------------------

    @ApiModelProperty("查询层级匹配")
    private Integer categoryLevel;

    @ApiModelProperty("查询层级匹配")
    private String queryLevelPath;

    @ApiModelProperty("查询层级排除")
    private String queryLevelPathExclude;


    public Integer getDefaultFlag() {
        return defaultFlag;
    }

    public void setDefaultFlag(Integer defaultFlag) {
        this.defaultFlag = defaultFlag;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public String getQueryCategoryCode() {
        return queryCategoryCode;
    }

    public void setQueryCategoryCode(String queryCategoryCode) {
        this.queryCategoryCode = queryCategoryCode;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Integer getEnabledFlag() { return enabledFlag; }

    public void setEnabledFlag(Integer enabledFlag) { this.enabledFlag = enabledFlag; }

    public Integer getTemplateEnabledFlag() { return templateEnabledFlag; }

    public void setTemplateEnabledFlag(Integer templateEnabledFlag) { this.templateEnabledFlag = templateEnabledFlag; }

    public Integer getCategoryLevel() {
        return categoryLevel;
    }

    public void setCategoryLevel(Integer categoryLevel) {
        this.categoryLevel = categoryLevel;
    }

    public String getQueryLevelPath() {
        return queryLevelPath;
    }

    public void setQueryLevelPath(String queryLevelPath) {
        this.queryLevelPath = queryLevelPath;
    }

    public String getQueryLevelPathExclude() {
        return queryLevelPathExclude;
    }

    public void setQueryLevelPathExclude(String queryLevelPathExclude) {
        this.queryLevelPathExclude = queryLevelPathExclude;
    }

    public String getQueryCategoryId() {
        return queryCategoryId;
    }

    public void setQueryCategoryId(String queryCategoryId) {
        this.queryCategoryId = queryCategoryId;
    }

    public String getQueryCategoryName() {
        return queryCategoryName;
    }

    public void setQueryCategoryName(String queryCategoryName) {
        this.queryCategoryName = queryCategoryName;
    }
}