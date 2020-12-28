package org.srm.mall.other.app.service;

import org.srm.mall.order.api.dto.PreRequestOrderDTO;
import org.srm.mall.other.api.dto.ShoppingCartDTO;
import org.srm.mall.other.api.dto.WatsonsPreRequestOrderDTO;
import org.srm.mall.other.api.dto.WatsonsShoppingCartDTO;

import java.util.List;

/**
 *  屈臣氏二开预采购申请service
 *
 * @author jianhao.zhang01@hand-china.com 2020/12/23 17:31
 */
public interface WatsonsShoppingCartService {

    /**
     * 预采购申请预览
     *
     * @param watsonsShoppingCartDTOList  购物车商品
     * @return PreRequestOrderDTO   预采购申请返回DTO
     */
    List<WatsonsPreRequestOrderDTO> watsonsPreRequestOrderView(Long tenantId, List<WatsonsShoppingCartDTO> watsonsShoppingCartDTOList);

}
