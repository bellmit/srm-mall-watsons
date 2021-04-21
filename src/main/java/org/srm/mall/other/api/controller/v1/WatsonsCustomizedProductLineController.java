package org.srm.mall.other.api.controller.v1;

import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.ApiOperation;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.hzero.starter.keyencrypt.core.Encrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.srm.mall.common.feign.dto.product.SpuCustomAttrGroup;
import org.srm.mall.infra.constant.WatsonsConstants;
import org.srm.mall.other.api.dto.CustomizedProductDTO;
import org.srm.mall.other.api.dto.WatsonsCustomizedProductDTO;
import org.srm.mall.other.app.service.CustomizedProductLineService;
import org.srm.mall.other.app.service.WatsonsCustomizedProductLineService;
import org.srm.mall.other.domain.entity.CustomizedProductLine;
import org.srm.mall.other.domain.entity.ShoppingCart;
import org.srm.web.annotation.Tenant;

import java.util.List;

/**
 * 定制品行表 管理 API
 *
 * @author yuewen.wei@hand-china.com 2021-03-26 10:28:29
 */
@RestController("watsonsCustomizedProductLineController.v1")
@RequestMapping("/v1/{organizationId}/customized-product-lines")
@Tenant(WatsonsConstants.TENANT_NUMBER)
public class WatsonsCustomizedProductLineController extends BaseController {

    private final WatsonsCustomizedProductLineService watsonsCustomizedProductLineService;

    @Autowired
    public WatsonsCustomizedProductLineController(WatsonsCustomizedProductLineService watsonsCustomizedProductLineService) {
        this.watsonsCustomizedProductLineService = watsonsCustomizedProductLineService;
    }

    @ApiOperation(value = "定制品行表单个商品-列表(校验-更新-查询)")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/list")
    public ResponseEntity<List<CustomizedProductLine>> list(@PathVariable("organizationId") Long organizationId, @RequestBody WatsonsCustomizedProductDTO watsonsCustomizedProductDTO) {
        List<CustomizedProductLine> list = watsonsCustomizedProductLineService.selectSingleProductCustomizedProduct(organizationId, watsonsCustomizedProductDTO);
        return Results.success(list);
    }

    @ApiOperation(value = "创建或修改定制品行表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping
    public ResponseEntity<List<CustomizedProductLine>> createOrUpdate(@PathVariable("organizationId") Long organizationId, @RequestBody List<CustomizedProductLine> customizedProductLineList) {
        watsonsCustomizedProductLineService.createOrUpdateForWatsons(organizationId, customizedProductLineList);
        return Results.success(customizedProductLineList);
    }
}
