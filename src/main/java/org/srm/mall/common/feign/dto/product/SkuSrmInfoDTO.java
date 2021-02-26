package org.srm.mall.common.feign.dto.product;

import io.swagger.annotations.ApiModelProperty;
import org.hzero.starter.keyencrypt.core.Encrypt;

import java.math.BigDecimal;
import java.util.List;

public class SkuSrmInfoDTO {

    @ApiModelProperty(value = "商品ID")
    @Encrypt
    private Long skuId;
    @ApiModelProperty(value = "商品编码")
    private String skuCode;
    @ApiModelProperty(value = "商品名称")
    private String skuName;
    @ApiModelProperty(value = "物料ID")
    @Encrypt
    private Long itemId;
    @ApiModelProperty(value = "物料编码")
    private String itemCode;
    @ApiModelProperty(value = "物料名称")
    private String itemName;
    @ApiModelProperty(value = "物料品类ID")
    @Encrypt
    private Long itemCategoryId;
    @ApiModelProperty(value = "物料品类编码")
    private String itemCategoryCode;
    @ApiModelProperty(value = "物料品类名称")
    private String itemCategoryName;
    @ApiModelProperty(value = "物料单位ID")
    @Encrypt
    private Long itemUomId;
    @ApiModelProperty(value = "物料单位编码")
    private String itemUomCode;
    @ApiModelProperty(value = "物料单位名称")
    private String itemUomName;
    @ApiModelProperty(value = "SRM单位ID")
    @Encrypt
    private Long uomId;
    @ApiModelProperty(value = "SRM单位编码")
    private String uomCode;
    @ApiModelProperty(value = "SRM单位名称")
    private String uomName;
    @ApiModelProperty(value = "SRM币种ID")
    @Encrypt
    private Long currencyId;
    @ApiModelProperty(value = "SRM币种编码")
    private String currencyCode;
    @ApiModelProperty(value = "SRM币种名称")
    private String currencyName;
    @ApiModelProperty(value = "SRM税率ID")
    @Encrypt
    private Long taxId;
    @ApiModelProperty(value = "SRM税率编码")
    private String taxCode;
    @ApiModelProperty(value = "SRM税率名称")
    private BigDecimal taxRate;

    @ApiModelProperty(value = "查询参数，商品ID List")
    @Encrypt
    private List<Long> skuIds;
    @ApiModelProperty(value = "查询参数，租户ID")
    private Long tenantId;
    @ApiModelProperty(value = "查询参数，商品组ID")
    private Long spuId;

    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    public String getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
    }

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Long getItemCategoryId() {
        return itemCategoryId;
    }

    public void setItemCategoryId(Long itemCategoryId) {
        this.itemCategoryId = itemCategoryId;
    }

    public String getItemCategoryCode() {
        return itemCategoryCode;
    }

    public void setItemCategoryCode(String itemCategoryCode) {
        this.itemCategoryCode = itemCategoryCode;
    }

    public String getItemCategoryName() {
        return itemCategoryName;
    }

    public void setItemCategoryName(String itemCategoryName) {
        this.itemCategoryName = itemCategoryName;
    }

    public Long getUomId() {
        return uomId;
    }

    public void setUomId(Long uomId) {
        this.uomId = uomId;
    }

    public String getUomCode() {
        return uomCode;
    }

    public void setUomCode(String uomCode) {
        this.uomCode = uomCode;
    }

    public String getUomName() {
        return uomName;
    }

    public void setUomName(String uomName) {
        this.uomName = uomName;
    }

    public Long getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(Long currencyId) {
        this.currencyId = currencyId;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    public Long getTaxId() {
        return taxId;
    }

    public void setTaxId(Long taxId) {
        this.taxId = taxId;
    }

    public String getTaxCode() {
        return taxCode;
    }

    public void setTaxCode(String taxCode) {
        this.taxCode = taxCode;
    }

    public BigDecimal getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(BigDecimal taxRate) {
        this.taxRate = taxRate;
    }

    public List<Long> getSkuIds() {
        return skuIds;
    }

    public void setSkuIds(List<Long> skuIds) {
        this.skuIds = skuIds;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public Long getSpuId() {
        return spuId;
    }

    public void setSpuId(Long spuId) {
        this.spuId = spuId;
    }

    public Long getItemUomId() {
        return itemUomId;
    }

    public SkuSrmInfoDTO setItemUomId(Long itemUomId) {
        this.itemUomId = itemUomId;
        return this;
    }

    public String getItemUomCode() {
        return itemUomCode;
    }

    public SkuSrmInfoDTO setItemUomCode(String itemUomCode) {
        this.itemUomCode = itemUomCode;
        return this;
    }

    public String getItemUomName() {
        return itemUomName;
    }

    public SkuSrmInfoDTO setItemUomName(String itemUomName) {
        this.itemUomName = itemUomName;
        return this;
    }
}
