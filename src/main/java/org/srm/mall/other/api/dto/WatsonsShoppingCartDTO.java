package org.srm.mall.other.api.dto;

import io.choerodon.core.exception.CommonException;
import io.swagger.annotations.ApiModelProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.srm.mall.common.feign.SmdmRemoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.srm.mall.common.feign.SmdmRemoteService;
import org.srm.mall.context.entity.ItemCategory;
import org.srm.mall.other.api.dto.ShoppingCartDTO;
import org.srm.mall.other.app.service.impl.ShoppingCartServiceImpl;
import org.srm.mall.other.domain.entity.AllocationInfo;


import java.util.List;
import javax.persistence.Transient;

public class WatsonsShoppingCartDTO extends ShoppingCartDTO {

    private static final Logger logger = LoggerFactory.getLogger(ShoppingCartServiceImpl.class);

    public static final String ALLOCATION_INFO_LIST  = "body.allocationInfoList";

    private List<AllocationInfo> allocationInfoList;


    @ApiModelProperty(value = "联系电话")
    private String mobile;

    @ApiModelProperty(value = "采购品类名称")
    private String itemCategoryName;

    @ApiModelProperty(value = "用于合单操作的key")
    @Transient
    private String key;

    @ApiModelProperty(value = "CMS合同号")
    private String cmsNumber;


    @ApiModelProperty(value = "地址区域和详细地址校验标志位")
    @Transient
    private Integer addressCheckSuccess;

    @ApiModelProperty(value = "地址区域和详细地址校验错误信息")
    @Transient
    private String addressCheckErrorMessage;

    public Integer getAddressCheckSuccess() {
        return addressCheckSuccess;
    }

    public void setAddressCheckSuccess(Integer addressCheckSuccess) {
        this.addressCheckSuccess = addressCheckSuccess;
    }

    public String getAddressCheckErrorMessage() {
        return addressCheckErrorMessage;
    }

    public void setAddressCheckErrorMessage(String addressCheckErrorMessage) {
        this.addressCheckErrorMessage = addressCheckErrorMessage;
    }


    public List<AllocationInfo> getAllocationInfoList() {
        return allocationInfoList;
    }

    public void setAllocationInfoList(List<AllocationInfo> allocationInfoList) {
        this.allocationInfoList = allocationInfoList;
    }

    public String getCmsNumber() {
        return cmsNumber;
    }

    public void setCmsNumber(String cmsNumber) {
        this.cmsNumber = cmsNumber;
    }

    @Override
    public String getMobile() {
        return mobile;
    }

    @Override
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getItemCategoryName() {
        return itemCategoryName;
    }

    public void setItemCategoryName(String itemCategoryName) {
        this.itemCategoryName = itemCategoryName;
    }


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }


}
