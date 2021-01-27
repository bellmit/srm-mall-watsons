package org.srm.mall.other.api.dto;
import java.math.BigDecimal;

/**
 *  查询ceInfoDTO
 *
 * @author jianhao.zhang01@hand-china.com 2021/01/26 00:07
 */
public class CheckCeInfoDTO {

    /*
        ceId
     */
    private Integer ceId;

    /*
        该订单未含税价格
     */
    private BigDecimal changeAmount;

    /*
        ce明细名称
     */
    private String itemName;


    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }


    public Integer getCeId() {
        return ceId;
    }

    public void setCeId(Integer ceId) {
        this.ceId = ceId;
    }

    public BigDecimal getChangeAmount() {
        return changeAmount;
    }

    public void setChangeAmount(BigDecimal changeAmount) {
        this.changeAmount = changeAmount;
    }

}
