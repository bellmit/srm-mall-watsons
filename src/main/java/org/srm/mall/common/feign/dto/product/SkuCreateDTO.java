package org.srm.mall.common.feign.dto.product;

import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class SkuCreateDTO {

    /**
     *商品编码
     */
    @NotBlank(message = "sku编码不能为空")
    private String skuId;
    /**
     *商品名称
     */
    @NotBlank(message = "商品名称不能为空")
    private String skuName;
    /**
     *销售价格
     */
    @NotNull(message ="销售价格不能为空")
    private BigDecimal sellPrice;
    /**
     *市场价
     */
    @NotNull(message ="市场价格不能为空")
    private BigDecimal marketPrice;
    /**
     *上下架状态，0-下架，1-上架
     */
    @NotNull(message = "上下架状态不能为空")
    private Boolean saleStatus;
    /**
     * 商品主图
     */
    @NotBlank(message = "主图不能为空")
    private String primaryImage;
    /**
     * 商品附图
     */
    private String otherImage;
    /**
     * 商品详情描述
     */
    @NotBlank(message = "商品详情不能为空")
    private String introduction;
    /**
     * 商品的包装清单
     */
    @NotBlank(message = "包装清单不能为空")
    private String packinglist;
    /**
     * 售后服务信息
     */
    @NotBlank(message = "售后服务不能为空")
    private String afsServiceInfo;
    @NotBlank(message = "销售价的官网链接不能为空")
    private String officialUrl;
    /**
     * 供货周期
     */
    private String deliveryCycle;
    /**
     * 最小购买量
     */
    private Integer limitQuantity;
    /**
     * 商品属性集合
     */
    private List<SkuAttrCreateDTO> attributes;

    @ApiModelProperty(value = "不含税价")
    private BigDecimal nakedPrice;

    /**
     * 此字段仅做免税商品标识
     * 增值税特殊管理，0：免税、1：不征税，2：普通零税率，3：非零税率，4：特殊税收政策
     */
    private Integer taxSpecial;

    /**
     * 备注，当增值税特殊管理为 4特殊税收政策 时，备注字段必输，填写具体的特殊税收政策是什么
     */
    private String remark;

    public String getSkuId() {
        return skuId;
    }

    public void setSkuId(String skuId) {
        this.skuId = skuId;
    }

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    public BigDecimal getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(BigDecimal sellPrice) {
        this.sellPrice = sellPrice;
    }

    public BigDecimal getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(BigDecimal marketPrice) {
        this.marketPrice = marketPrice;
    }

    public Boolean getSaleStatus() {
        return saleStatus;
    }

    public void setSaleStatus(Boolean saleStatus) {
        this.saleStatus = saleStatus;
    }

    public String getPrimaryImage() {
        return primaryImage;
    }

    public void setPrimaryImage(String primaryImage) {
        this.primaryImage = primaryImage;
    }

    public String getOtherImage() {
        return otherImage;
    }

    public void setOtherImage(String otherImage) {
        this.otherImage = otherImage;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getPackinglist() {
        return packinglist;
    }

    public void setPackinglist(String packinglist) {
        this.packinglist = packinglist;
    }

    public String getAfsServiceInfo() {
        return afsServiceInfo;
    }

    public void setAfsServiceInfo(String afsServiceInfo) {
        this.afsServiceInfo = afsServiceInfo;
    }

    public String getOfficialUrl() {
        return officialUrl;
    }

    public void setOfficialUrl(String officialUrl) {
        this.officialUrl = officialUrl;
    }

    public String getDeliveryCycle() {
        return deliveryCycle;
    }

    public void setDeliveryCycle(String deliveryCycle) {
        this.deliveryCycle = deliveryCycle;
    }

    public Integer getLimitQuantity() {
        return limitQuantity;
    }

    public void setLimitQuantity(Integer limitQuantity) {
        this.limitQuantity = limitQuantity;
    }

    public List<SkuAttrCreateDTO> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<SkuAttrCreateDTO> attributes) {
        this.attributes = attributes;
    }

    public BigDecimal getNakedPrice() {
        return nakedPrice;
    }

    public void setNakedPrice(BigDecimal nakedPrice) {
        this.nakedPrice = nakedPrice;
    }

    public Integer getTaxSpecial() {
        return taxSpecial;
    }

    public void setTaxSpecial(Integer taxSpecial) {
        this.taxSpecial = taxSpecial;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
