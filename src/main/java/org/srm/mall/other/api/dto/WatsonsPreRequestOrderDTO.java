package org.srm.mall.other.api.dto;

import io.swagger.annotations.ApiModelProperty;
import org.hzero.starter.keyencrypt.core.Encrypt;
import org.srm.mall.order.api.dto.PreRequestOrderDTO;

/**
 * 屈臣氏二开预采购申请view返回值
 *
 * @author jianhao.zhang01@hand-china.com 2020/12/23 17:15
 */
public class WatsonsPreRequestOrderDTO extends PreRequestOrderDTO {

    @ApiModelProperty(value = "物料品类id/采购品类id")
    @Encrypt
    private Long itemCategoryId;

    @ApiModelProperty(value = "CE号")
    private String ceNum;

    @ApiModelProperty(value = "联系电话")
    private String mobile;

    @ApiModelProperty(value = "送货方式")
    private String deliveryType;

    public Long getItemCategoryId() {
        return itemCategoryId;
    }

    public void setItemCategoryId(Long itemCategoryId) {
        this.itemCategoryId = itemCategoryId;
    }

    public String getCeNum() {
        return ceNum;
    }

    public void setCeNum(String ceNum) {
        this.ceNum = ceNum;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getDeliveryType() {
        return deliveryType;
    }

    public void setDeliveryType(String deliveryType) {
        this.deliveryType = deliveryType;
    }
}
