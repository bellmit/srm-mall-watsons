package org.srm.mall.other.api.dto;

import io.choerodon.core.exception.CommonException;
import io.swagger.annotations.ApiModelProperty;
import org.hzero.core.base.BaseConstants;
import org.hzero.core.util.ResponseUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.srm.mall.common.feign.SmdmRemoteService;
import org.srm.mall.context.entity.ItemCategory;
import org.srm.mall.other.api.dto.ShoppingCartDTO;
import org.srm.mall.other.app.service.impl.ShoppingCartServiceImpl;
import org.srm.mall.other.domain.entity.AllocationInfo;
import org.srm.mall.other.domain.entity.WatsonsShoppingCart;
import org.srm.mall.platform.domain.entity.PurReqMergeRule;

import java.util.List;

public class WatsonsShoppingCartDTO extends ShoppingCartDTO {

    @Autowired
    private SmdmRemoteService smdmRemoteService;

    private static final Logger logger = LoggerFactory.getLogger(ShoppingCartServiceImpl.class);

    private List<AllocationInfo> allocationInfoList;

    public List<AllocationInfo> getCostAllocationInfoList() {
        return allocationInfoList;
    }

    public void setCostAllocationInfoList(List<AllocationInfo> allocationInfoList) {
        this.allocationInfoList = allocationInfoList;
    }

    @ApiModelProperty(value = "CE号")
    private String ceNum;

    @ApiModelProperty(value = "联系电话")
    private String mobile;

    /**
     * 返回分组规则  供应商 品类  费用分配的门店
     *
     * @return 分组规则
     */
    public String watsonsGroupKey(Long tenantId, WatsonsShoppingCartDTO watsonsShoppingCartDTO, PurReqMergeRule purReqMergeRule) {
        StringBuffer key = new StringBuffer();

        ResponseEntity<String> responseOne = smdmRemoteService.selectCategoryByItemId(tenantId, watsonsShoppingCartDTO.getItemId(), BaseConstants.Flag.YES);
        if (ResponseUtils.isFailed(responseOne)) {
            logger.error("selectCategoryByItemId:{}", responseOne);
            throw new CommonException("查询商品二级品类失败！");
        }
        logger.info("selectCategoryByItemId:{}", responseOne);
        ItemCategory itemCategoryResultOne  = ResponseUtils.getResponse(responseOne, ItemCategory.class);
        Long parentCategoryId = itemCategoryResultOne.getParentCategoryId();

        ResponseEntity<String> responseTwo = smdmRemoteService.selectCategoryByItemId(tenantId, parentCategoryId, BaseConstants.Flag.YES);
        if (ResponseUtils.isFailed(responseTwo)) {
            logger.error("selectCategoryByItemId:{}", responseTwo);
            throw new CommonException("根据二级品类查询商品一级品类失败！");
        }
        logger.info("selectCategoryByItemId:{}", responseTwo);
        ItemCategory itemCategoryResultTwo  = ResponseUtils.getResponse(responseTwo, ItemCategory.class);

        if (BaseConstants.Flag.YES.equals(purReqMergeRule.getSupplierFlag())) {
            key.append(watsonsShoppingCartDTO.getSupplierCompanyId()).append("-");
        }
        if (BaseConstants.Flag.YES.equals(purReqMergeRule.getAddressFlag())) {
            key.append(allocationInfoList.get(0).getAddressId()).append("-");
        }
        if (BaseConstants.Flag.YES.equals(purReqMergeRule.getCategory())){
            key.append(itemCategoryResultTwo.getCategoryId()).append("-");
        }

        watsonsShoppingCartDTO.setItemCategoryId(itemCategoryResultTwo.getCategoryId());
        return key.toString();
    }

}
