package org.srm.mall.other.api.dto;

import org.apache.commons.collections4.CollectionUtils;
import org.hzero.starter.keyencrypt.core.Encrypt;
import org.srm.mall.other.domain.entity.AllocationInfo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class WatsonsCustomizedProductDTO extends CustomizedProductDTO{

    public static final String ALLOCATION_INFO = "ALLOCATION_INFO";

    @Encrypt
    private List<Long> relationIdList;

    private String relationType;

    @Encrypt
    private Long companyId;

    private BigDecimal latestPrice;

    @Encrypt
    private Long productId;

    private List<AllocationInfo> allocationInfoList;

    public WatsonsCustomizedProductDTO() {
        this.relationIdList = new ArrayList<>();
    }

    //拿到relationidlist  和relationType
    public WatsonsCustomizedProductDTO(List<WatsonsShoppingCartDTO> watsonsShoppingCartDTOS) {
        relationIdList = new ArrayList<>();
        //获取查询参数
            //获取预算idList
            for (WatsonsShoppingCartDTO watsonsShoppingCartDTO : watsonsShoppingCartDTOS) {
                if (CollectionUtils.isEmpty(watsonsShoppingCartDTO.getAllocationInfoList())) {
                    continue;
                }
                relationIdList.addAll(watsonsShoppingCartDTO.getAllocationInfoList().stream().map(AllocationInfo::getAllocationId).collect(Collectors.toList()));
            }
            relationType = ALLOCATION_INFO;
    }

    public WatsonsCustomizedProductDTO createCustomizedProductParam() {
        if (!CollectionUtils.isEmpty(allocationInfoList)) {
            //获取预算idList
            relationIdList.addAll(allocationInfoList.stream().map(AllocationInfo::getAllocationId).collect(Collectors.toList()));
            relationType = ALLOCATION_INFO;
        }
        return this;
    }

    public BigDecimal getLatestPrice() {
        return latestPrice;
    }

    public void setLatestPrice(BigDecimal latestPrice) {
        this.latestPrice = latestPrice;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }


    public List<Long> getRelationIdList() {
        return relationIdList;
    }

    public WatsonsCustomizedProductDTO setRelationIdList(List<Long> relationIdList) {
        this.relationIdList = relationIdList;
        return this;
    }

    public String getRelationType() {
        return relationType;
    }

    public WatsonsCustomizedProductDTO setRelationType(String relationType) {
        this.relationType = relationType;
        return this;
    }

    public List<AllocationInfo> getAllocationInfoList() {
        return allocationInfoList;
    }

    public void setAllocationInfoList(List<AllocationInfo> allocationInfoList) {
        this.allocationInfoList = allocationInfoList;
    }
}
