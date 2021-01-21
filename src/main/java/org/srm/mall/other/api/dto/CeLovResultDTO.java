package org.srm.mall.other.api.dto;

import org.srm.mall.product.api.dto.BaseDto;

import java.math.BigDecimal;

/**
 * CE信息接口返回值
 *
 * @author jianhao.zhang01@hand-china.com 2021/01/18 22:38
 */
public class CeLovResultDTO extends BaseDto {

    /**
     * ceid
     */
    private int ceId;

    /**
     * ce编号
     */

    private String ceNumber;

    /**
     * 项目名称
     */

    private String projectName;

    /**
     * ce描述
     */

    private String description;

    /**
     * ce明细名称
     */

    private String itemName;

    /**
     * 市场名称
     */
    private String marketName;

    /**
     * CE可用余额
     */

    private BigDecimal amountBalance;

    /**
     * 店铺号
     */

    private String storeNo;


    public int getCeId() {
        return ceId;
    }

    public void setCeId(int ceId) {
        this.ceId = ceId;
    }

    public String getCeNumber() {
        return ceNumber;
    }

    public void setCeNumber(String ceNumber) {
        this.ceNumber = ceNumber;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getMarketName() {
        return marketName;
    }

    public void setMarketName(String marketName) {
        this.marketName = marketName;
    }

    public BigDecimal getAmountBalance() {
        return amountBalance;
    }

    public void setAmountBalance(BigDecimal amountBalance) {
        this.amountBalance = amountBalance;
    }

    public String getStoreNo() {
        return storeNo;
    }

    public void setStoreNo(String storeNo) {
        this.storeNo = storeNo;
    }


}
