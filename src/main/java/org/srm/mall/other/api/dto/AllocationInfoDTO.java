package org.srm.mall.other.api.dto;

import org.srm.mall.other.domain.entity.AllocationInfo;
import org.srm.mall.other.domain.entity.WatsonsShoppingCart;

import java.util.List;

public class AllocationInfoDTO {

    private List<AllocationInfo> allocationInfoList;

    private List<WatsonsShoppingCart> watsonsShoppingCartList;

    public List<AllocationInfo> getAllocationInfoList() {
        return allocationInfoList;
    }

    public void setAllocationInfoList(List<AllocationInfo> allocationInfoList) {
        this.allocationInfoList = allocationInfoList;
    }

    public List<WatsonsShoppingCart> getWatsonsShoppingCartList() {
        return watsonsShoppingCartList;
    }

    public void setWatsonsShoppingCartList(List<WatsonsShoppingCart> watsonsShoppingCartList) {
        this.watsonsShoppingCartList = watsonsShoppingCartList;
    }
}
