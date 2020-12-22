package org.srm.mall.other.api.controller.v1;

import org.hzero.core.util.Results;
import org.hzero.core.base.BaseController;
import org.hzero.starter.keyencrypt.core.Encrypt;
import org.srm.mall.infra.constant.WatsonsConstants;
import org.srm.mall.other.app.service.AllocationInfoService;
import org.srm.mall.other.domain.entity.AllocationInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;

import io.swagger.annotations.ApiOperation;
import org.srm.mall.other.domain.entity.WatsonsShoppingCart;
import org.srm.web.annotation.Tenant;

import java.util.List;

/**
 * 屈臣氏费用分配表 管理 API
 *
 * @author yuewen.wei@hand-china.com 2020-12-21 15:35:27
 */
@RestController("costAllocationInfoController.v1")
@RequestMapping("/v1/{organizationId}/cost-allocation-infos")
@Tenant(WatsonsConstants.TENANT_NUMBER)
public class CostAllocationInfoController extends BaseController {

    @Autowired
    private AllocationInfoService allocationInfoService;

    @ApiOperation(value = "屈臣氏费用分配表列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/{cartId}")
    public ResponseEntity<List<AllocationInfo>> list(@PathVariable("organizationId") Long organizationId, @Encrypt @PathVariable("cartId") Long cartId) {
        List<AllocationInfo> list = allocationInfoService.list(organizationId, cartId);
        return Results.success(list);
    }

    @ApiOperation(value = "创建费用分配")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping
    public ResponseEntity<WatsonsShoppingCart> create(@PathVariable("organizationId") Long organizationId, @RequestBody @Encrypt WatsonsShoppingCart watsonsShoppingCart) {
        allocationInfoService.create(organizationId, watsonsShoppingCart);
        return Results.success(watsonsShoppingCart);
    }

    @ApiOperation(value = "更新购物车数量")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/update-quantity")
    public ResponseEntity<WatsonsShoppingCart> updateShoppingCart(@PathVariable("organizationId") Long organizationId, @RequestBody @Encrypt WatsonsShoppingCart watsonsShoppingCart) {
        allocationInfoService.create(organizationId, watsonsShoppingCart);
        return Results.success(watsonsShoppingCart);
    }

    @ApiOperation(value = "删除屈臣氏费用分配表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @DeleteMapping
    public ResponseEntity<WatsonsShoppingCart> remove(@RequestBody @Encrypt WatsonsShoppingCart watsonsShoppingCart) {
        return Results.success(allocationInfoService.remove(watsonsShoppingCart));
    }

}
