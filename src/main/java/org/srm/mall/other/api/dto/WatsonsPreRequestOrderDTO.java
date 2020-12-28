package org.srm.mall.other.api.dto;

import io.swagger.annotations.ApiModelProperty;
import org.hzero.starter.keyencrypt.core.Encrypt;
import org.srm.mall.common.utils.snapshot.annotation.Compare;
import org.srm.mall.common.utils.snapshot.annotation.IsList;
import org.srm.mall.order.api.dto.PreRequestOrderDTO;

import java.util.List;

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

    @ApiModelProperty(value = "品类名称")
    private String itemCategoryName;

    @ApiModelProperty(value = "分组名称key检查")
    private String keyForView;

    @ApiModelProperty(value = "用于预算分配返回的shoppingCartDTO")
    @Compare
    @IsList
    private List<WatsonsShoppingCartDTO> watsonsShoppingCartDTOList;

    public String getItemCategoryName() {
        return itemCategoryName;
    }

    public String getKeyForView() {
        return keyForView;
    }

    public void setKeyForView(String keyForView) {
        this.keyForView = keyForView;
    }

    public void setItemCategoryName(String itemCategoryName) {
        this.itemCategoryName = itemCategoryName;
    }

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

    public List<WatsonsShoppingCartDTO> getWatsonsShoppingCartDTOList() {
        return watsonsShoppingCartDTOList;
    }

    public void setWatsonsShoppingCartDTOList(List<WatsonsShoppingCartDTO> watsonsShoppingCartDTOList) {
        this.watsonsShoppingCartDTOList = watsonsShoppingCartDTOList;
    }

}
