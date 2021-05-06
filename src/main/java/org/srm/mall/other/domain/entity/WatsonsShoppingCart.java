package org.srm.mall.other.domain.entity;

import io.swagger.annotations.ApiModelProperty;
import org.hzero.starter.keyencrypt.core.Encrypt;

import java.util.List;

public class WatsonsShoppingCart extends ShoppingCart {

    //物料品类id
    @Encrypt
    private Long itemCategoryId;
    //物料id
    @Encrypt
    private Long itemId;

    @ApiModelProperty("费用项目id")
    private Long projectCostId;
    @ApiModelProperty(value = "费用项目编码",required = false)
    private String projectCostCode;
    @ApiModelProperty(value = "费用项目名称",required = false)
    private String projectCostName;
    private List<ProjectCostSubcategory> projectCostSubcategoryList;
    //可以默认批量分配标识
    private Integer projectCostBatchFlag;
    //可以默认批量分配子分类的标识
    private Integer projectCostSubCategoryBatchFlag;
    private List<AllocationInfo> allocationInfoList;
    public List<AllocationInfo> getAllocationInfoList() {
        return allocationInfoList;
    }

    public void setAllocationInfoList(List<AllocationInfo> allocationInfoList) {
        this.allocationInfoList = allocationInfoList;
    }

    public Long getItemCategoryId() {
        return itemCategoryId;
    }

    public void setItemCategoryId(Long itemCategoryId) {
        this.itemCategoryId = itemCategoryId;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public Long getProjectCostId() {
        return projectCostId;
    }

    public void setProjectCostId(Long projectCostId) {
        this.projectCostId = projectCostId;
    }

    public String getProjectCostCode() {
        return projectCostCode;
    }

    public void setProjectCostCode(String projectCostCode) {
        this.projectCostCode = projectCostCode;
    }

    public String getProjectCostName() {
        return projectCostName;
    }

    public void setProjectCostName(String projectCostName) {
        this.projectCostName = projectCostName;
    }

    public List<ProjectCostSubcategory> getProjectCostSubcategoryList() {
        return projectCostSubcategoryList;
    }

    public void setProjectCostSubcategoryList(List<ProjectCostSubcategory> projectCostSubcategoryList) {
        this.projectCostSubcategoryList = projectCostSubcategoryList;
    }

    public Integer getProjectCostBatchFlag() {
        return projectCostBatchFlag;
    }

    public void setProjectCostBatchFlag(Integer projectCostBatchFlag) {
        this.projectCostBatchFlag = projectCostBatchFlag;
    }

    public Integer getProjectCostSubCategoryBatchFlag() {
        return projectCostSubCategoryBatchFlag;
    }

    public void setProjectCostSubCategoryBatchFlag(Integer projectCostSubCategoryBatchFlag) {
        this.projectCostSubCategoryBatchFlag = projectCostSubCategoryBatchFlag;
    }
}
