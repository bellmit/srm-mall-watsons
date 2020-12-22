package org.srm.mall.other.domain.entity;

import java.util.List;

public class WatsonsShoppingCart extends ShoppingCart {

    private List<AllocationInfo> allocationInfoList;

    public List<AllocationInfo> getCostAllocationInfoList() {
        return allocationInfoList;
    }

    public void setCostAllocationInfoList(List<AllocationInfo> allocationInfoList) {
        this.allocationInfoList = allocationInfoList;
    }

}
