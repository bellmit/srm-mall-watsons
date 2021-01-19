package org.srm.mall.other.api.dto;

import org.srm.mall.region.api.dto.AddressDTO;
import org.srm.mall.region.domain.entity.Address;

/**
 * 屈臣氏二开地址返回
 *
 * @author jianhao.zhang01@hand-china.com 2021/01/19 16:31
 */
public class WatsonsAddressDTO extends AddressDTO {

    private String addressRegion;

    public String getAddressRegion() {
        return addressRegion;
    }

    public void setAddressRegion(String addressRegion) {
        this.addressRegion = addressRegion;
    }

}
