package org.srm.mall.other.api.dto;

import org.srm.mall.product.api.dto.BaseDto;

/**
 * 仓转店收货仓值集返回值
 *
 * @author jianhao.zhang01@hand-china.com 2021/01/21 17:02
 */
public class WhLovResultDTO extends BaseDto {

    private String inventoryCode;

    private String inventoryName;

    public String getInventoryCode() {
        return inventoryCode;
    }

    public void setInventoryCode(String inventoryCode) {
        this.inventoryCode = inventoryCode;
    }

    public String getInventoryName() {
        return inventoryName;
    }

    public void setInventoryName(String inventoryName) {
        this.inventoryName = inventoryName;
    }

}
