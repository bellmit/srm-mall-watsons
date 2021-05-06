package org.srm.mall.other.api.dto;

import io.swagger.annotations.ApiModelProperty;
import org.hzero.starter.keyencrypt.core.Encrypt;
import org.springframework.util.ObjectUtils;
import org.srm.mall.common.utils.snapshot.annotation.Compare;
import org.srm.mall.common.utils.snapshot.annotation.IsList;
import org.srm.mall.order.api.dto.PreRequestOrderDTO;
import org.srm.starter.mall.precision.annotation.LocaleValue;
import org.srm.starter.mall.precision.enums.PrecisionTypeEnum;

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

    @Encrypt
    @ApiModelProperty("收货联系人id")
    private Long receiverContactId;


    @ApiModelProperty(value = "拆单完成后每个单子的费用承担店铺的code")
    private String storeNo;

    @ApiModelProperty(value = "商品不含税运费")
    @LocaleValue(value = "financialPrecision",type = PrecisionTypeEnum.FINANCIAL)
    private BigDecimal withoutTaxFreightPrice;

    @ApiModelProperty(value = "SRM币种默认精度")
    private Integer defaultPrecision;

    @ApiModelProperty(value = "SRM币种财务精度")
    private Integer financialPrecision;
    /**
     * 根据财务精度设置价格
     */
    public void priceFinancialPrecisionSetting( ){
        this.withoutTaxFreightPrice = this.setPriceByFinancialPrecision(this.withoutTaxFreightPrice);
    }

    private BigDecimal setPriceByFinancialPrecision(BigDecimal price){
        //如果财务精度为空，不做四舍五入，原样返回
        if (ObjectUtils.isEmpty(this.financialPrecision)) {
            return price;
        }
        return ObjectUtils.isEmpty(price) ? null : price.setScale(this.financialPrecision, BigDecimal.ROUND_HALF_UP);
    }


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

    public Long getReceiverContactId() {
        return receiverContactId;
    }

    public void setReceiverContactId(Long receiverContactId) {
        this.receiverContactId = receiverContactId;
    }

    public BigDecimal getWithoutTaxFreightPrice() {
        return withoutTaxFreightPrice;
    }

    public void setWithoutTaxFreightPrice(BigDecimal withoutTaxFreightPrice) {
        this.withoutTaxFreightPrice = withoutTaxFreightPrice;
    }

    @Override
    public Integer getDefaultPrecision() {
        return defaultPrecision;
    }

    @Override
    public void setDefaultPrecision(Integer defaultPrecision) {
        this.defaultPrecision = defaultPrecision;
    }

    @Override
    public Integer getFinancialPrecision() {
        return financialPrecision;
    }

    @Override
    public void setFinancialPrecision(Integer financialPrecision) {
        this.financialPrecision = financialPrecision;
    }
}
