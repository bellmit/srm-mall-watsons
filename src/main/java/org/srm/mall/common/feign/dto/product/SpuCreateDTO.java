package org.srm.mall.common.feign.dto.product;

import io.swagger.annotations.ApiModelProperty;
import org.hzero.core.base.BaseConstants;
import org.hzero.starter.keyencrypt.core.Encrypt;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 商品创建/更新  DTO
 *
 * @author yuhao.guo  2021年1月19日20:01:59
 */
public class SpuCreateDTO {

    /**
     * 供应商简码
     */
    @NotBlank(message = "supplierCode不能为空")
    private String supplierCode;
    /**
     * 平台租户ID
     */
    @NotNull(message = "平台租户id不能为空")
    private Long tenantId;
    /**
     * 商品SPU编码
     */
    @NotBlank(message = "spu编码不能为空")
    @Encrypt
    private String spuId;
    /**
     * 商品SPU名称
     */
    private String spuName;
    /**
     * 采购平台三级类目编码
     */
    @NotBlank(message = "分类编码不能为空")
    private String categoryId;
    /**
     * 客户采购目录编码
     */
    private String clientCatalogId;
    /**
     * 销售属性集合
     */
    private String spuSaleAttrs;
    /**
     * 商品列表
     */
    @NotNull(message = "商品列表不能为空")
    @Valid
    private List<SkuCreateDTO> skuInfos;
    /**
     * 供应商的tenantId
     */
    private Long supplierTenantId;
    @ApiModelProperty(value = "数量")
    private Long quantity;
    @ApiModelProperty(value = "报价单头号")
    private String qtHeaderNo;
    @ApiModelProperty(value = "报价单行号")
    private String qtLineNo;
    @DateTimeFormat(pattern = BaseConstants.Pattern.DATE)
    private LocalDate qtEndDate;
    @ApiModelProperty(value = "供应商客户编码")
    private String custCode;
    @ApiModelProperty(value = "总额")
    private BigDecimal totalAmount;
    @ApiModelProperty(value = "不含税总额")
    private BigDecimal nakedTotalAmount;
    @ApiModelProperty(value = "punchout所需字段")
    private String customize1;
    private String customize2;
    private String customize3;
    private String customize4;
    private String customize5;

    public String getSupplierCode() {
        return supplierCode;
    }

    public void setSupplierCode(String supplierCode) {
        this.supplierCode = supplierCode;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public String getSpuId() {
        return spuId;
    }

    public void setSpuId(String spuId) {
        this.spuId = spuId;
    }

    public String getSpuName() {
        return spuName;
    }

    public void setSpuName(String spuName) {
        this.spuName = spuName;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getClientCatalogId() {
        return clientCatalogId;
    }

    public void setClientCatalogId(String clientCatalogId) {
        this.clientCatalogId = clientCatalogId;
    }

    public String getSpuSaleAttrs() {
        return spuSaleAttrs;
    }

    public void setSpuSaleAttrs(String spuSaleAttrs) {
        this.spuSaleAttrs = spuSaleAttrs;
    }

    public List<SkuCreateDTO> getSkuInfos() {
        return skuInfos;
    }

    public void setSkuInfos(List<SkuCreateDTO> skuInfos) {
        this.skuInfos = skuInfos;
    }

    public Long getSupplierTenantId() {
        return supplierTenantId;
    }

    public void setSupplierTenantId(Long supplierTenantId) {
        this.supplierTenantId = supplierTenantId;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public String getQtHeaderNo() {
        return qtHeaderNo;
    }

    public void setQtHeaderNo(String qtHeaderNo) {
        this.qtHeaderNo = qtHeaderNo;
    }

    public String getQtLineNo() {
        return qtLineNo;
    }

    public void setQtLineNo(String qtLineNo) {
        this.qtLineNo = qtLineNo;
    }

    public LocalDate getQtEndDate() {
        return qtEndDate;
    }

    public void setQtEndDate(LocalDate qtEndDate) {
        this.qtEndDate = qtEndDate;
    }

    public String getCustCode() {
        return custCode;
    }

    public void setCustCode(String custCode) {
        this.custCode = custCode;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getNakedTotalAmount() {
        return nakedTotalAmount;
    }

    public void setNakedTotalAmount(BigDecimal nakedTotalAmount) {
        this.nakedTotalAmount = nakedTotalAmount;
    }

    public String getCustomize1() {
        return customize1;
    }

    public void setCustomize1(String customize1) {
        this.customize1 = customize1;
    }

    public String getCustomize2() {
        return customize2;
    }

    public void setCustomize2(String customize2) {
        this.customize2 = customize2;
    }

    public String getCustomize3() {
        return customize3;
    }

    public void setCustomize3(String customize3) {
        this.customize3 = customize3;
    }

    public String getCustomize4() {
        return customize4;
    }

    public void setCustomize4(String customize4) {
        this.customize4 = customize4;
    }

    public String getCustomize5() {
        return customize5;
    }

    public void setCustomize5(String customize5) {
        this.customize5 = customize5;
    }
}
