package org.srm.mall.other.api.dto;

import org.srm.mall.other.api.dto.ShoppingCartDTO;
import org.srm.mall.other.domain.entity.AllocationInfo;

import java.util.List;

public class WatsonsShoppingCartDTO extends ShoppingCartDTO {

    private List<AllocationInfo> allocationInfoList;

    public List<AllocationInfo> getAllocationInfoList() {
        return allocationInfoList;
    }

    public void setAllocationInfoList(List<AllocationInfo> allocationInfoList) {
        this.allocationInfoList = allocationInfoList;
    }
}
