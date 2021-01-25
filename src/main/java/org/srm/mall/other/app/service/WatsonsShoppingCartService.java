package org.srm.mall.other.app.service;

import org.srm.mall.order.api.dto.PreRequestOrderDTO;
import org.srm.mall.order.api.dto.PreRequestOrderResponseDTO;
import org.srm.mall.other.api.dto.ShoppingCartDTO;
import org.srm.mall.other.api.dto.WatsonsAddressDTO;
import org.srm.mall.other.api.dto.WatsonsPreRequestOrderDTO;
import org.srm.mall.other.api.dto.WatsonsShoppingCartDTO;
import org.srm.mall.other.domain.entity.ShoppingCart;
import org.srm.mall.region.api.dto.AddressDTO;
import org.srm.mall.region.domain.entity.Address;

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

    /**
     * 进入购物车
     *
     * @param shoppingCart
     * @return list
     */
    List<ShoppingCartDTO> shppingCartEnter(Long organizationId, ShoppingCart shoppingCart);


    /**
     * 创建预采购申请订单
     *
     * @param tenantId
     * @param preRequestOrderDTOList
     * @return
     */
    PreRequestOrderResponseDTO watsonsPreRequestOrder(Long tenantId, String customizeUnitCode, List<WatsonsPreRequestOrderDTO> preRequestOrderDTOList);

    /**
     * 根据送货方式仓转店或者直送自动带出地址区域和详细地址
     *
     * @param organizationId
     * @param watsonsOrganizationCode
     * @param watsonsOrganizationId
     * @return
     */
    List<WatsonsAddressDTO> checkAddress(Long organizationId, Long watsonsOrganizationId, String watsonsOrganizationCode);

    /**
     *  校验传来的购物车的详细地址和地址区域
     *
     * @param organizationId
     * @param watsonsShoppingCartDTOS
     * @return
     */
    String checkAddressValidate(Long organizationId, List<WatsonsShoppingCartDTO> watsonsShoppingCartDTOS);

}
