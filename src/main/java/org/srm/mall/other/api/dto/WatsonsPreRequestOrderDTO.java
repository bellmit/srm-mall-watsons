package org.srm.mall.other.api.dto;

import io.swagger.annotations.ApiModelProperty;
import org.hzero.starter.keyencrypt.core.Encrypt;
import org.srm.mall.common.utils.snapshot.annotation.Compare;
import org.srm.mall.common.utils.snapshot.annotation.IsList;
import org.srm.mall.order.api.dto.PreRequestOrderDTO;

import java.math.BigDecimal;
import java.util.List;

/**
 * 屈臣氏二开预采购申请view返回值
 *
 * @author jianhao.zhang01@hand-china.com 2020/12/23 17:15
 */
public class WatsonsPreRequestOrderDTO extends PreRequestOrderDTO {

    public static final String WATSONS_SHOPPINGCART_DTO_LIST  = "body.watsonsShoppingCartDTOList.allocationInfoList";

    @ApiModelProperty(value = "CE编码")
    private String ceNumber;

    @ApiModelProperty(value = "CE编码描述")
    private String discription;

    @ApiModelProperty(value = "CE明细名称")
    private String itemName;

    @ApiModelProperty(value = "CEid")
    private Integer ceId;

    @ApiModelProperty(value = "分组名称key检查")
    private String keyForView;

    @ApiModelProperty(value = "联系电话")
    private String mobile;

    @ApiModelProperty(value = "用于费用分配返回的shoppingCartDTO")
    @Compare
    @IsList
    private List<WatsonsShoppingCartDTO> watsonsShoppingCartDTOList;


    @ApiModelProperty(value = "拆单完成后每个单子的费用承担店铺的code")
    private String storeNo;

    @ApiModelProperty(value = "商品不含税运费")
    private BigDecimal withoutTaxFreightPrice;


    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }


    public String getStoreNo() {
        return storeNo;
    }

    public void setStoreNo(String storeNo) {
        this.storeNo = storeNo;
    }

    public String getDiscription() {
        return discription;
    }
    public void setDiscription(String discription) {
        this.discription = discription;
    }


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

    public Integer getCeId() {
        return ceId;
    }

    public void setCeId(Integer ceId) {
        this.ceId = ceId;
    }

    public BigDecimal getWithoutTaxFreightPrice() {
        return withoutTaxFreightPrice;
    }

    public void setWithoutTaxFreightPrice(BigDecimal withoutTaxFreightPrice) {
        this.withoutTaxFreightPrice = withoutTaxFreightPrice;
    }
}
