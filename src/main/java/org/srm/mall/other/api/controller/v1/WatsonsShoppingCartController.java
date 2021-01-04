package org.srm.mall.other.api.controller.v1;

import com.netflix.discovery.converters.Auto;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.CustomPageRequest;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.core.base.BaseConstants;
import org.hzero.core.util.Results;
import org.hzero.starter.keyencrypt.core.Encrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.srm.mall.common.annotation.ParamLog;
import org.srm.mall.config.SwaggerApiConfig;
import org.srm.mall.infra.constant.WatsonsConstants;
import org.srm.mall.order.api.dto.PreRequestOrderDTO;
import org.srm.mall.other.api.dto.ShoppingCartDTO;
import org.srm.mall.other.api.dto.WatsonsPreRequestOrderDTO;
import org.srm.mall.other.api.dto.WatsonsShoppingCartDTO;
import org.srm.mall.other.app.service.ShoppingCartService;
import org.srm.mall.other.app.service.WatsonsShoppingCartService;
import org.srm.mall.other.domain.entity.AllocationInfo;
import org.srm.mall.other.domain.entity.ShoppingCart;
import org.srm.web.annotation.Tenant;

import java.util.List;

/**
 * 屈臣氏二开预采购申请controller
 *
 * @author jianhao.zhang01@hand-china.com 2020/12/23 17:24
 */

@Api(tags = SwaggerApiConfig.SHOPPING_CART)
@RestController("watsonsShoppingCartController.v1")
@RequestMapping("/v1/{organizationId}/shopping-carts")
@Tenant(WatsonsConstants.TENANT_NUMBER)
public class WatsonsShoppingCartController {

    @Autowired
    private WatsonsShoppingCartService watsonsShoppingCartService;


    @ApiOperation(value = "预采购申请预览")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @ProcessLovValue(targetField = {BaseConstants.FIELD_BODY, WatsonsPreRequestOrderDTO.WATSONS_SHOPPINGCART_DTO_LIST})
    @PostMapping("/pre-req")
    public ResponseEntity<List<WatsonsPreRequestOrderDTO>> preRequestOrderView(@PathVariable("organizationId") Long organizationId, @RequestBody @Encrypt List<WatsonsShoppingCartDTO> watsonsShoppingCartDTOList) {
        List<WatsonsPreRequestOrderDTO> watsonsPreRequestOrderDTOList = watsonsShoppingCartService.watsonsPreRequestOrderView(organizationId, watsonsShoppingCartDTOList);
        return Results.success(watsonsPreRequestOrderDTOList);
    }

    @ApiOperation(value = "进入购物车")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    @CustomPageRequest
    @ProcessLovValue(targetField = {BaseConstants.FIELD_BODY, WatsonsShoppingCartDTO.ALLOCATION_INFO_LIST})
    @ParamLog
    public ResponseEntity<List<ShoppingCartDTO>> shppingCartEnter(@PathVariable("organizationId") Long organizationId, @Encrypt ShoppingCart shoppingCart) {
        shoppingCart.setTenantId(organizationId);
        List<ShoppingCartDTO> shoppingCartDTOS = watsonsShoppingCartService.shppingCartEnter(organizationId, shoppingCart);
        return Results.success(shoppingCartDTOS);
    }

}
