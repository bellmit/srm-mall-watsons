package org.srm.mall.other.domain.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.validation.constraints.NotBlank;

import io.choerodon.mybatis.domain.AuditDomain;
import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.VersionAudit;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hzero.boot.platform.lov.annotation.LovValue;
import org.hzero.export.annotation.ExcelColumn;
import org.hzero.starter.keyencrypt.core.Encrypt;

import java.math.BigDecimal;

/**
 * 屈臣氏费用分配表
 *
 * @author yuewen.wei@hand-china.com 2020-12-21 15:35:27
 */
@ApiModel("屈臣氏费用分配表")
@VersionAudit
@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "scux_watsons_allocation_info")
public class AllocationInfo extends AuditDomain {

    public static final String FIELD_ALLOCATION_ID = "allocationId";
    public static final String FIELD_CART_ID = "cartId";
    public static final String FIELD_DELIVERY_TYPE = "deliveryType";
    public static final String FIELD_COST_SHOP_ID = "costShopId";
    public static final String FIELD_COST_SHOP_CODE = "costShopCode";
    public static final String FIELD_COST_SHOP_NAME = "costShopName";
    public static final String FIELD_COST_DEPARTMENT_ID = "costDepartmentId";
    public static final String FIELD_COST_DEPARTMENT_CODE = "costDepartmentCode";
    public static final String FIELD_COST_DEPARTMENT_NAME = "costDepartmentName";
    public static final String FIELD_RECEIVE_WAREHOUSE_ID = "receiveWarehouseId";
    public static final String FIELD_RECEIVE_WAREHOUSE_CODE = "receiveWarehouseCode";
    public static final String FIELD_RECEIVE_WAREHOUSE_NAME = "receiveWarehouseName";
    public static final String FIELD_QUANTITY = "quantity";
    public static final String FIELD_PRICE = "price";
    public static final String FIELD_AMOUNT = "amount";
    public static final String FIELD_PROJECT_COST_CODE = "projectCostCode";
    public static final String FIELD_PROJECT_COST_NAME = "projectCostName";
    public static final String FIELD_PROJECT_COST_ID = "projectCostId";
    public static final String FIELD_PROJECT_COST_SUBCATEGORY_CODE = "projectCostSubcategoryCode";
    public static final String FIELD_PROJECT_COST_SUBCATEGORY_NAME = "projectCostSubcategoryName";
    public static final String FIELD_PROJECT_COST_SUBCATEGORY_ID = "projectCostSubcategoryId";
    public static final String FIELD_ADDRESS_REGION = "addressRegion";
    public static final String FIELD_FULL_ADDRESS = "fullAddress";



//
// 业务方法(按public protected private顺序排列)
// ------------------------------------------------------------------------------
    public String groupKey(){
        return this.costShopId + "-" +
                this.costDepartmentId + "-" +
                this.receiveWarehouseId;
    }

//
// 数据库字段
// ------------------------------------------------------------------------------


    @ApiModelProperty("")
    @Id
    @GeneratedValue
    @Encrypt
    private Long allocationId;
    @ApiModelProperty(value = "购物车id", required = true)
    @NotNull
    @Encrypt
    private Long cartId;

    @ApiModelProperty(value = "送货方式", required = true)
    @LovValue(lovCode = "SCUX.WATSONS.DELIVERY_METHOD", meaningField = "deliveryTypeMeaning")
    @NotBlank
    private String deliveryType;

    @ApiModelProperty(value = "地址id", required = true)
    @NotNull
    @Encrypt
    private Long addressId;
    @ApiModelProperty(value = "费用承担店铺/写字楼id", required = true)
    @NotNull
    @Encrypt
    private Long costShopId;
    @ApiModelProperty(value = "费用承担店铺/写字楼编码", required = true)
    @NotBlank
    private String costShopCode;
    @ApiModelProperty(value = "费用承担店铺/写字楼名称", required = true)
    @NotBlank
    private String costShopName;
    @ApiModelProperty(value = "费用承担部门id", required = true)
    @NotNull
    @Encrypt
    private Long costDepartmentId;
    @ApiModelProperty(value = "费用承担部门编码", required = true)
    @NotBlank
    private String costDepartmentCode;
    @ApiModelProperty(value = "费用承担部门名称", required = true)
    @NotBlank
    private String costDepartmentName;
    @ApiModelProperty(value = "仓转店收获仓id")
    @Encrypt
    private Long receiveWarehouseId;
    @ApiModelProperty(value = "仓转店收获仓编码")
    private String receiveWarehouseCode;
    @ApiModelProperty(value = "仓转店收获仓名称")
    private String receiveWarehouseName;
    @ApiModelProperty(value = "数量", required = true)
    @NotNull
    private Long quantity;
    @ApiModelProperty(value = "单价", required = true)
    @NotNull
    private BigDecimal price;
    @ApiModelProperty(value = "费用项目编码",required = true)
    private String projectCostCode;
    @ApiModelProperty(value = "费用项目名称",required = true)
    private String projectCostName;
    @ApiModelProperty(value = "费用项目id",required = true)
    private Long projectCostId;
    @ApiModelProperty(value = "费用项目子分类编码", required = true)
    private String projectCostSubcategoryCode;
    @ApiModelProperty(value = "费用项目子分类名称", required = true)
    private String projectCostSubcategoryName;
    @ApiModelProperty(value = "费用项目子分类id", required = true)
    private Long projectCostSubcategoryId;
    @ApiModelProperty(value = "地址区域", required = true)
    private String addressRegion;
    @ApiModelProperty(value = "详细地址", required = true)
    private String fullAddress;



//
// 非数据库字段
// ------------------------------------------------------------------------------

    @Transient
    private BigDecimal totalAmount;

    @Transient
    private BigDecimal percent;

    @Transient
    private String deliveryTypeMeaning;

    @Transient
    private String fromWhichShoppingCart;

//
// getter/setter
// ------------------------------------------------------------------------------

    public String getFromWhichShoppingCart() {
        return fromWhichShoppingCart;
    }

    public void setFromWhichShoppingCart(String fromWhichShoppingCart) {
        this.fromWhichShoppingCart = fromWhichShoppingCart;
    }


    public String getAddressRegion() {
        return addressRegion;
    }

    public void setAddressRegion(String addressRegion) {
        this.addressRegion = addressRegion;
    }

    public String getFullAddress() {
        return fullAddress;
    }

    public void setFullAddress(String fullAddress) {
        this.fullAddress = fullAddress;
    }


    public Long getProjectCostId() {
        return projectCostId;
    }

    public void setProjectCostId(Long projectCostId) {
        this.projectCostId = projectCostId;
    }

    public Long getProjectCostSubcategoryId() {
        return projectCostSubcategoryId;
    }

    public void setProjectCostSubcategoryId(Long projectCostSubcategoryId) {
        this.projectCostSubcategoryId = projectCostSubcategoryId;
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

    public String getProjectCostSubcategoryCode() {
        return projectCostSubcategoryCode;
    }

    public void setProjectCostSubcategoryCode(String projectCostSubcategoryCode) {
        this.projectCostSubcategoryCode = projectCostSubcategoryCode;
    }

    public String getProjectCostSubcategoryName() {
        return projectCostSubcategoryName;
    }

    public void setProjectCostSubcategoryName(String projectCostSubcategoryName) {
        this.projectCostSubcategoryName = projectCostSubcategoryName;
    }


    public BigDecimal getPercent() {
        return percent;
    }

    public void setPercent(BigDecimal percent) {
        this.percent = percent;
    }

    public String getDeliveryTypeMeaning() {
        return deliveryTypeMeaning;
    }

    public void setDeliveryTypeMeaning(String deliveryTypeMeaning) {
        this.deliveryTypeMeaning = deliveryTypeMeaning;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }

    /**
     * @return
     */
    public Long getAllocationId() {
        return allocationId;
    }

    public AllocationInfo setAllocationId(Long allocationId) {
        this.allocationId = allocationId;
        return this;
    }

    /**
     * @return 购物车id
     */
    public Long getCartId() {
        return cartId;
    }

    public AllocationInfo setCartId(Long cartId) {
        this.cartId = cartId;
        return this;
    }

    /**
     * @return 送货方式
     */
    public String getDeliveryType() {
        return deliveryType;
    }

    public void setDeliveryType(String deliveryType) {
        this.deliveryType = deliveryType;
    }

    /**
     * @return 费用承担店铺/写字楼id
     */
    public Long getCostShopId() {
        return costShopId;
    }

    public AllocationInfo setCostShopId(Long costShopId) {
        this.costShopId = costShopId;
        return this;
    }

    /**
     * @return 费用承担店铺/写字楼编码
     */
    public String getCostShopCode() {
        return costShopCode;
    }

    public AllocationInfo setCostShopCode(String costShopCode) {
        this.costShopCode = costShopCode;
        return this;
    }

    /**
     * @return 费用承担店铺/写字楼名称
     */
    public String getCostShopName() {
        return costShopName;
    }

    public AllocationInfo setCostShopName(String costShopName) {
        this.costShopName = costShopName;
        return this;
    }

    /**
     * @return 费用承担部门id
     */
    public Long getCostDepartmentId() {
        return costDepartmentId;
    }

    public AllocationInfo setCostDepartmentId(Long costDepartmentId) {
        this.costDepartmentId = costDepartmentId;
        return this;
    }

    /**
     * @return 费用承担部门编码
     */
    public String getCostDepartmentCode() {
        return costDepartmentCode;
    }

    public AllocationInfo setCostDepartmentCode(String costDepartmentCode) {
        this.costDepartmentCode = costDepartmentCode;
        return this;
    }

    /**
     * @return 费用承担部门名称
     */
    public String getCostDepartmentName() {
        return costDepartmentName;
    }

    public AllocationInfo setCostDepartmentName(String costDepartmentName) {
        this.costDepartmentName = costDepartmentName;
        return this;
    }

    /**
     * @return 仓转店收获仓id
     */
    public Long getReceiveWarehouseId() {
        return receiveWarehouseId;
    }

    public AllocationInfo setReceiveWarehouseId(Long receiveWarehouseId) {
        this.receiveWarehouseId = receiveWarehouseId;
        return this;
    }

    /**
     * @return 仓转店收获仓编码
     */
    public String getReceiveWarehouseCode() {
        return receiveWarehouseCode;
    }

    public AllocationInfo setReceiveWarehouseCode(String receiveWarehouseCode) {
        this.receiveWarehouseCode = receiveWarehouseCode;
        return this;
    }

    /**
     * @return 仓转店收获仓名称
     */
    public String getReceiveWarehouseName() {
        return receiveWarehouseName;
    }

    public AllocationInfo setReceiveWarehouseName(String receiveWarehouseName) {
        this.receiveWarehouseName = receiveWarehouseName;
        return this;
    }

    /**
     * @return 数量
     */
    public Long getQuantity() {
        return quantity;
    }

    public AllocationInfo setQuantity(Long quantity) {
        this.quantity = quantity;
        return this;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
