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

    public static final String WATSONS_SHOPPINGCART_DTO_LIST  = "body.watsonsShoppingCartDTOList.allocationInfoList";

    @ApiModelProperty(value = "物料品类id/采购品类id")
    @Encrypt
    private Long itemCategoryId;

    @ApiModelProperty(value = "CE号")
    private String ceNumber;

    @ApiModelProperty(value = "分组名称key检查")
    private String keyForView;

    @ApiModelProperty(value = "联系电话")
    private String mobile;

    @ApiModelProperty(value = "用于预算分配返回的shoppingCartDTO")
    @Compare
    @IsList
    private List<WatsonsShoppingCartDTO> watsonsShoppingCartDTOList;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getKeyForView() {
        return keyForView;
    }

    public void setKeyForView(String keyForView) {
        this.keyForView = keyForView;
    }

    public Long getItemCategoryId() {
        return itemCategoryId;
    }

    public void setItemCategoryId(Long itemCategoryId) {
        this.itemCategoryId = itemCategoryId;
    }

    public String getCeNumber() {
        return ceNumber;
    }

    public void setCeNumber(String ceNumber) {
        this.ceNumber = ceNumber;
    }

    public List<WatsonsShoppingCartDTO> getWatsonsShoppingCartDTOList() {
        return watsonsShoppingCartDTOList;
    }

    public void setWatsonsShoppingCartDTOList(List<WatsonsShoppingCartDTO> watsonsShoppingCartDTOList) {
        this.watsonsShoppingCartDTOList = watsonsShoppingCartDTOList;
    }

}
