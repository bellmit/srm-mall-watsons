package org.srm.mall.common.feign.dto.product;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * 商品属性  DTO
 *
 * @author yuhao.guo  2021年1月18日17:11:34
 */
public class SkuAttrInfoDTO {

    @ApiModelProperty("商品基础属性&属性值")
    private List<SkuAttr> skuAttr;
    @ApiModelProperty("商品自定义属性&属性值")
    private List<SkuAttrExtend> skuAttrExtends;

    public SkuAttrInfoDTO() {}

    public SkuAttrInfoDTO(List<SkuAttr> skuAttr, List<SkuAttrExtend> skuAttrExtends) {
        this.skuAttr = skuAttr;
        this.skuAttrExtends = skuAttrExtends;
    }

    public List<SkuAttr> getSkuAttr() {
        return skuAttr;
    }

    public void setSkuAttr(List<SkuAttr> skuAttr) {
        this.skuAttr = skuAttr;
    }

    public List<SkuAttrExtend> getSkuAttrExtends() {
        return skuAttrExtends;
    }

    public void setSkuAttrExtends(List<SkuAttrExtend> skuAttrExtends) {
        this.skuAttrExtends = skuAttrExtends;
    }
}
