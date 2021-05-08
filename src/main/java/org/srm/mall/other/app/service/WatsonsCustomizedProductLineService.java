package org.srm.mall.other.app.service;

import org.srm.mall.common.feign.dto.product.SpuCustomAttrGroup;
import org.srm.mall.other.api.dto.CustomizedProductCheckDTO;
import org.srm.mall.other.api.dto.CustomizedProductDTO;
import org.srm.mall.other.api.dto.WatsonsCustomizedProductDTO;
import org.srm.mall.other.domain.entity.CustomizedProductLine;
import org.srm.mall.other.domain.entity.ShoppingCart;
import org.srm.mall.other.domain.entity.WatsonsShoppingCart;

import java.util.List;

/**
 * 定制品行表应用服务
 *
 * @author yuewen.wei@hand-china.com 2021-03-26 10:28:29
 */
public interface WatsonsCustomizedProductLineService{


    /**
     * 查询定制品列表
     * @return
     */
    List<CustomizedProductLine> selectCustomizedProductList(Long tenantId, WatsonsCustomizedProductDTO watsonsCustomizedProductDTO);


    /**
     * 查询单个商品的定制品列表
     * @return
     */
    List<CustomizedProductLine> selectSingleProductCustomizedProduct(Long tenantId, WatsonsCustomizedProductDTO watsonsCustomizedProductDTO);


    /**
     * 创建定制品行表
     *
     * @param tenantId 租户ID
     * @param customizedProductLineList 定制品行表
     * @return 定制品行表
     */
    List<CustomizedProductLine> createOrUpdateForWatsons(Long tenantId, List<CustomizedProductLine> customizedProductLineList);


}
