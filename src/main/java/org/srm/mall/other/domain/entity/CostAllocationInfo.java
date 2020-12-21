package org.srm.mall.other.domain.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.validation.constraints.NotBlank;

import io.choerodon.mybatis.domain.AuditDomain;
import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.VersionAudit;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 屈臣氏费用分配表
 *
 * @author yuewen.wei@hand-china.com 2020-12-21 15:35:27
 */
@ApiModel("屈臣氏费用分配表")
@VersionAudit
@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "smal_cost_allocation_info")
public class CostAllocationInfo extends AuditDomain{

    public static final String FIELD_ALLOCATION_ID ="allocationId";
    public static final String FIELD_CART_ID ="cartId";
    public static final String FIELD_RECEIVE_TYPE ="receiveType";
    public static final String FIELD_COST_SHOP_ID ="costShopId";
    public static final String FIELD_COST_SHOP_CODE ="costShopCode";
    public static final String FIELD_COST_SHOP_NAME ="costShopName";
    public static final String FIELD_COST_DEPARTMENT_ID ="costDepartmentId";
    public static final String FIELD_COST_DEPARTMENT_CODE ="costDepartmentCode";
    public static final String FIELD_COST_DEPARTMENT_NAME ="costDepartmentName";
    public static final String FIELD_RECEIVE_WAREHOUSE_ID ="receiveWarehouseId";
    public static final String FIELD_RECEIVE_WAREHOUSE_CODE ="receiveWarehouseCode";
    public static final String FIELD_RECEIVE_WAREHOUSE_NAME ="receiveWarehouseName";
    public static final String FIELD_PERCENT ="percent";
    public static final String FIELD_QUANTITY ="quantity";
    public static final String FIELD_AMOUNT ="amount";

//
// 业务方法(按public protected private顺序排列)
// ------------------------------------------------------------------------------

//
// 数据库字段
// ------------------------------------------------------------------------------


                @ApiModelProperty("")
    @Id
    @GeneratedValue
            private Long allocationId;
                                @ApiModelProperty(value = "购物车id", required = true)
                    @NotNull
                                        private Long cartId;
                                @ApiModelProperty(value = "收货方式", required = true)
                    @NotBlank
                                        private String receiveType;
                                @ApiModelProperty(value = "费用承担店铺/写字楼id", required = true)
                    @NotNull
                                        private Long costShopId;
                                @ApiModelProperty(value = "费用承担店铺/写字楼编码", required = true)
                    @NotBlank
                                        private String costShopCode;
                                @ApiModelProperty(value = "费用承担店铺/写字楼名称", required = true)
                    @NotBlank
                                        private String costShopName;
                                @ApiModelProperty(value = "费用承担部门id", required = true)
                    @NotNull
                                        private Long costDepartmentId;
                                @ApiModelProperty(value = "费用承担部门编码", required = true)
                    @NotBlank
                                        private String costDepartmentCode;
                                @ApiModelProperty(value = "费用承担部门名称", required = true)
                    @NotBlank
                                        private String costDepartmentName;
                                @ApiModelProperty(value = "仓转店收获仓id")
                        private Long receiveWarehouseId;
                                @ApiModelProperty(value = "仓转店收获仓编码")
                        private String receiveWarehouseCode;
                                @ApiModelProperty(value = "仓转店收获仓名称")
                        private String receiveWarehouseName;
                                @ApiModelProperty(value = "百分比", required = true)
                    @NotNull
                                        private unknowType percent;
                                @ApiModelProperty(value = "数量", required = true)
                    @NotNull
                                        private Long quantity;
                                @ApiModelProperty(value = "金额", required = true)
                    @NotNull
                                        private unknowType amount;
                        
//
// 非数据库字段
// ------------------------------------------------------------------------------

//
// getter/setter
// ------------------------------------------------------------------------------

        /**
     * @return 
     */
    public Long getAllocationId(){
        return allocationId;
    }

    public CostAllocationInfo setAllocationId(Long allocationId) {
        this.allocationId = allocationId;
        return this;
    }
            /**
     * @return 购物车id
     */
    public Long getCartId(){
        return cartId;
    }

    public CostAllocationInfo setCartId(Long cartId) {
        this.cartId = cartId;
        return this;
    }
            /**
     * @return 收货方式
     */
    public String getReceiveType(){
        return receiveType;
    }

    public CostAllocationInfo setReceiveType(String receiveType) {
        this.receiveType = receiveType;
        return this;
    }
            /**
     * @return 费用承担店铺/写字楼id
     */
    public Long getCostShopId(){
        return costShopId;
    }

    public CostAllocationInfo setCostShopId(Long costShopId) {
        this.costShopId = costShopId;
        return this;
    }
            /**
     * @return 费用承担店铺/写字楼编码
     */
    public String getCostShopCode(){
        return costShopCode;
    }

    public CostAllocationInfo setCostShopCode(String costShopCode) {
        this.costShopCode = costShopCode;
        return this;
    }
            /**
     * @return 费用承担店铺/写字楼名称
     */
    public String getCostShopName(){
        return costShopName;
    }

    public CostAllocationInfo setCostShopName(String costShopName) {
        this.costShopName = costShopName;
        return this;
    }
            /**
     * @return 费用承担部门id
     */
    public Long getCostDepartmentId(){
        return costDepartmentId;
    }

    public CostAllocationInfo setCostDepartmentId(Long costDepartmentId) {
        this.costDepartmentId = costDepartmentId;
        return this;
    }
            /**
     * @return 费用承担部门编码
     */
    public String getCostDepartmentCode(){
        return costDepartmentCode;
    }

    public CostAllocationInfo setCostDepartmentCode(String costDepartmentCode) {
        this.costDepartmentCode = costDepartmentCode;
        return this;
    }
            /**
     * @return 费用承担部门名称
     */
    public String getCostDepartmentName(){
        return costDepartmentName;
    }

    public CostAllocationInfo setCostDepartmentName(String costDepartmentName) {
        this.costDepartmentName = costDepartmentName;
        return this;
    }
            /**
     * @return 仓转店收获仓id
     */
    public Long getReceiveWarehouseId(){
        return receiveWarehouseId;
    }

    public CostAllocationInfo setReceiveWarehouseId(Long receiveWarehouseId) {
        this.receiveWarehouseId = receiveWarehouseId;
        return this;
    }
            /**
     * @return 仓转店收获仓编码
     */
    public String getReceiveWarehouseCode(){
        return receiveWarehouseCode;
    }

    public CostAllocationInfo setReceiveWarehouseCode(String receiveWarehouseCode) {
        this.receiveWarehouseCode = receiveWarehouseCode;
        return this;
    }
            /**
     * @return 仓转店收获仓名称
     */
    public String getReceiveWarehouseName(){
        return receiveWarehouseName;
    }

    public CostAllocationInfo setReceiveWarehouseName(String receiveWarehouseName) {
        this.receiveWarehouseName = receiveWarehouseName;
        return this;
    }
            /**
     * @return 百分比
     */
    public unknowType getPercent(){
        return percent;
    }

    public CostAllocationInfo setPercent(unknowType percent) {
        this.percent = percent;
        return this;
    }
            /**
     * @return 数量
     */
    public Long getQuantity(){
        return quantity;
    }

    public CostAllocationInfo setQuantity(Long quantity) {
        this.quantity = quantity;
        return this;
    }
            /**
     * @return 金额
     */
    public unknowType getAmount(){
        return amount;
    }

    public CostAllocationInfo setAmount(unknowType amount) {
        this.amount = amount;
        return this;
    }
                        }
