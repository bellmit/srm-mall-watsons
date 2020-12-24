package org.srm.mall.other.api.controller.v1;

import com.netflix.discovery.converters.Auto;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.hzero.core.util.Results;
import org.hzero.starter.keyencrypt.core.Encrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.srm.mall.config.SwaggerApiConfig;
import org.srm.mall.infra.constant.WatsonsConstants;
import org.srm.mall.order.api.dto.PreRequestOrderDTO;
import org.srm.mall.other.api.dto.ShoppingCartDTO;
import org.srm.mall.other.api.dto.WatsonsShoppingCartDTO;
import org.srm.mall.other.app.service.WatsonsShoppingCartService;
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
    @PostMapping("/pre-req")
    public ResponseEntity<List<PreRequestOrderDTO>> preRequestOrderView(@PathVariable("organizationId") Long organizationId, @RequestBody @Encrypt List<WatsonsShoppingCartDTO> watsonsShoppingCartDTOList) {
        return Results.success(watsonsShoppingCartService.watsonsPreRequestOrderView(organizationId, watsonsShoppingCartDTOList));
    }



}
