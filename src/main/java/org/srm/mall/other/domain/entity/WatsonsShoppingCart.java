package org.srm.mall.other.domain.entity;

import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.util.List;
import javax.persistence.Transient;

public class WatsonsShoppingCart extends ShoppingCart {


    private List<AllocationInfo> allocationInfoList;


    public List<AllocationInfo> getAllocationInfoList() {
        return allocationInfoList;
    }

    public void setAllocationInfoList(List<AllocationInfo> allocationInfoList) {
        this.allocationInfoList = allocationInfoList;
    }

}
