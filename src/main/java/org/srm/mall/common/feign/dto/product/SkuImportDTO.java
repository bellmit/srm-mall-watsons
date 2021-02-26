package org.srm.mall.common.feign.dto.product;

import io.swagger.annotations.ApiModelProperty;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * sku导入对象
 */
public class SkuImportDTO {

    @ApiModelProperty("三级分类名称")
    private String categoryName;

    @ApiModelProperty("spu编码")
    private String spuCode;

    @ApiModelProperty("spu名称")
    private String spuName;

    @ApiModelProperty("销售规格属性名称")
    private String attributeName;

    @ApiModelProperty("销售规格属性值")
    private String attrValueName;

    @ApiModelProperty("sku编码")
    private String skuCode;

    @ApiModelProperty("sku名称")
    private String skuName;

    @ApiModelProperty("sku销售单位")
    private String uomName;

    @ApiModelProperty("sku税率")
    private String tax;

    @ApiModelProperty("sku币种")
    private String currencyName;

    @ApiModelProperty("品牌")
    private String brandName;

    @ApiModelProperty("重量")
    private String weight;

    @ApiModelProperty("型号")
    private String model;

    @ApiModelProperty("sku图片")
    private String image;

    @ApiModelProperty("sku图片地址")
    private String imagePath;

    @ApiModelProperty("sku介绍")
    private String introduction;

    @ApiModelProperty("sku条码")
    private String upc;

    @ApiModelProperty("售后-退货（天）")
    private Long returnDuration;

    @ApiModelProperty("售后-换货（天）")
    private Long changeDuration;

    @ApiModelProperty("售后-退货（月）")
    private Long qualityDuration;

    @ApiModelProperty("售后-特殊售后说明")
    private String afsSpecial;

    @ApiModelProperty("sku价格")
    private BigDecimal price;

    @ApiModelProperty("sku库存")
    private String skuStock;

    @ApiModelProperty("采方sku-供应商公司名称")
    private String supplierCompanyName;

    @ApiModelProperty("目录编码")
    private String catalogCode;

    private Long supplierCompanyId;
    private Long supplierTenantId;

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getSpuCode() {
        return spuCode;
    }

    public void setSpuCode(String spuCode) {
        this.spuCode = spuCode;
    }

    public String getSpuName() {
        return spuName;
    }

    public void setSpuName(String spuName) {
        this.spuName = spuName;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public String getAttrValueName() {
        return attrValueName;
    }

    public void setAttrValueName(String attrValueName) {
        this.attrValueName = attrValueName;
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

    public String getUomName() {
        return uomName;
    }

    public void setUomName(String uomName) {
        this.uomName = uomName;
    }

    public String getTax() {
        return tax;
    }

    public void setTax(String tax) {
        this.tax = tax;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getUpc() {
        return upc;
    }

    public void setUpc(String upc) {
        this.upc = upc;
    }

    public Long getReturnDuration() {
        return returnDuration;
    }

    public void setReturnDuration(Long returnDuration) {
        this.returnDuration = returnDuration;
    }

    public Long getChangeDuration() {
        return changeDuration;
    }

    public void setChangeDuration(Long changeDuration) {
        this.changeDuration = changeDuration;
    }

    public Long getQualityDuration() {
        return qualityDuration;
    }

    public void setQualityDuration(Long qualityDuration) {
        this.qualityDuration = qualityDuration;
    }

    public String getAfsSpecial() {
        return afsSpecial;
    }

    public void setAfsSpecial(String afsSpecial) {
        this.afsSpecial = afsSpecial;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getSkuStock() {
        return skuStock;
    }

    public void setSkuStock(String skuStock) {
        this.skuStock = skuStock;
    }

    public String getSupplierCompanyName() {
        return supplierCompanyName;
    }

    public void setSupplierCompanyName(String supplierCompanyName) {
        this.supplierCompanyName = supplierCompanyName;
    }

    public String getCatalogCode() {
        return catalogCode;
    }

    public void setCatalogCode(String catalogCode) {
        this.catalogCode = catalogCode;
    }

    public Long getSupplierCompanyId() {
        return supplierCompanyId;
    }

    public void setSupplierCompanyId(Long supplierCompanyId) {
        this.supplierCompanyId = supplierCompanyId;
    }

    public Long getSupplierTenantId() {
        return supplierTenantId;
    }

    public void setSupplierTenantId(Long supplierTenantId) {
        this.supplierTenantId = supplierTenantId;
    }

    public List<String> getImages() {
        List<String> images = new ArrayList<>();
        if (!ObjectUtils.isEmpty(this.getImage())) {
            List<String> path = Arrays.asList(this.getImage().split(";"));
            images.addAll(path);
        }
        if (!ObjectUtils.isEmpty(this.getImagePath())) {
            List<String> path = Arrays.asList(this.getImagePath().split(";"));
            images.addAll(path);
        }
        return images;
    }
}
