package org.srm.mall.other.api.controller.v1;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.annotation.SortDefault;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.mybatis.pagehelper.domain.Sort;
import org.hzero.core.util.Results;
import org.hzero.core.base.BaseController;
import org.hzero.starter.keyencrypt.core.Encrypt;
import org.srm.mall.infra.constant.WatsonsConstants;
import org.srm.mall.order.domain.entity.PoHeader;
import org.srm.mall.other.api.dto.*;
import org.srm.mall.other.api.dto.*;
import org.srm.mall.other.api.dto.AllocationInfoDTO;
import org.srm.mall.other.api.dto.OrganizationInfoDTO;
import org.srm.mall.other.api.dto.WatsonsShoppingCartDTO;
import org.srm.mall.other.app.service.AllocationInfoService;
import org.srm.mall.other.domain.entity.AllocationInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;

import io.swagger.annotations.ApiOperation;
import org.srm.mall.other.domain.entity.ProjectCost;
import org.srm.mall.other.domain.entity.WatsonsShoppingCart;
import org.srm.mall.other.domain.repository.AllocationInfoRepository;
import org.srm.web.annotation.Tenant;
import springfox.documentation.annotations.ApiIgnore;

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

    @Autowired
    private AllocationInfoRepository allocationInfoRepository;

    @ApiOperation(value = "屈臣氏费用分配表列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    public ResponseEntity<List<AllocationInfo>> list(@PathVariable("organizationId") Long organizationId, @Encrypt @RequestParam("cartId") Long cartId) {
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

    @ApiOperation(value = "批量创建费用分配")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/batch-create")
    public ResponseEntity<AllocationInfoDTO> batchCreate(@PathVariable("organizationId") Long organizationId, @RequestBody @Encrypt AllocationInfoDTO allocationInfoDTO) {
        return Results.success(allocationInfoService.batchCreate(organizationId, allocationInfoDTO));
    }

    @ApiOperation(value = "更新购物车数量")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/update-quantity")
    public ResponseEntity<WatsonsShoppingCart> updateShoppingCart(@PathVariable("organizationId") Long organizationId, @RequestBody @Encrypt WatsonsShoppingCart watsonsShoppingCart) {
        allocationInfoService.updateShoppingCart(watsonsShoppingCart);
        return Results.success(watsonsShoppingCart);
    }

    @ApiOperation(value = "删除屈臣氏费用分配表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @DeleteMapping
    public ResponseEntity<WatsonsShoppingCart> remove(@RequestBody @Encrypt WatsonsShoppingCart watsonsShoppingCart) {
        return Results.success(allocationInfoService.remove(watsonsShoppingCart));
    }

    @ApiOperation(value = "屈臣氏费用分配写字楼/店铺")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/cost-allocation-shop")
    public ResponseEntity<Page<OrganizationInfoDTO>> selectCostAllocationShop(@PathVariable("organizationId") Long organizationId, OrganizationInfoDTO organizationInfoDTO,
                                                                              @ApiIgnore @SortDefault(value = OrganizationInfoDTO.FIELD_ORGANIZATION_ID, direction = Sort.Direction.DESC) PageRequest pageRequest) {
        return Results.success(allocationInfoRepository.selectAllocationShopOrganization(organizationInfoDTO, pageRequest));
    }

    @ApiOperation(value = "屈臣氏费用项目值集接口")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/cost-allocation-project-lov")
    public ResponseEntity<List<ProjectCost>> selectAllocationProjectLov(@PathVariable("organizationId") Long organizationId, Long itemCategoryId, Long itemId, @RequestParam("size") Integer size, @RequestParam("page") Integer page) {
        return Results.success(allocationInfoService.selectAllocationProjectLov(organizationId, itemCategoryId,itemId,size,page));
    }

    @ApiOperation(value = "屈臣氏CE信息值集接口")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/cost-ce-lov")
    public ResponseEntity<Page<CeLovResultDTO>> selectCeInfoLov(@PathVariable("organizationId") Long organizationId, @RequestParam("storeNo")String storeNo, @RequestParam("size") Integer size, @RequestParam("page") Integer page) {
        return Results.success(allocationInfoService.selectCeInfoLov(organizationId, storeNo,size,page));
    }

    @ApiOperation(value = "屈臣氏仓转店收货仓值集")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/cost-warehouse-lov")
    public ResponseEntity<Page<WatsonStoreInventoryRelationDTO>> selectWhLov(@PathVariable("organizationId") Long organizationId, @RequestParam("storeId")String storeId,@RequestParam("size") Integer size, @RequestParam("page") Integer page) {
        //storeId传店铺code
        return Results.success(allocationInfoService.selectWhLov(organizationId,storeId,size,page));
    }
}
