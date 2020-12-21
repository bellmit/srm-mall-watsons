package org.srm.mall.other.api.controller.v1;

import org.hzero.core.util.Results;
import org.hzero.core.base.BaseController;
import org.srm.mall.other.app.service.CostAllocationInfoService;
import org.srm.mall.other.domain.entity.CostAllocationInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.annotation.SortDefault;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.mybatis.pagehelper.domain.Sort;
import io.choerodon.swagger.annotation.Permission;

import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 屈臣氏费用分配表 管理 API
 *
 * @author yuewen.wei@hand-china.com 2020-12-21 15:35:27
 */
@RestController("costAllocationInfoController.v1" )
@RequestMapping("/v1/{organizationId}/cost-allocation-infos" )
public class CostAllocationInfoController extends BaseController {

    private final CostAllocationInfoService costAllocationInfoService;
    @Autowired
    public CostAllocationInfoController(CostAllocationInfoService costAllocationInfoService) {
        this.costAllocationInfoService = costAllocationInfoService;
    }

    @ApiOperation(value = "屈臣氏费用分配表列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    public ResponseEntity<Page<CostAllocationInfo>> list(@PathVariable("organizationId") Long organizationId, CostAllocationInfo costAllocationInfo, @ApiIgnore @SortDefault(value = CostAllocationInfo.FIELD_ALLOCATION_ID,
            direction = Sort.Direction.DESC) PageRequest pageRequest) {
        Page<CostAllocationInfo> list = costAllocationInfoService.list(organizationId, costAllocationInfo, pageRequest);
        return Results.success(list);
    }

    @ApiOperation(value = "屈臣氏费用分配表明细")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/{allocationId}")
    public ResponseEntity<CostAllocationInfo> detail(@PathVariable("organizationId") Long organizationId, @PathVariable Long allocationId) {
        CostAllocationInfo costAllocationInfo =costAllocationInfoService.detail(organizationId, allocationId);
        return Results.success(costAllocationInfo);
    }

    @ApiOperation(value = "创建屈臣氏费用分配表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping
    public ResponseEntity<CostAllocationInfo> create(@PathVariable("organizationId") Long organizationId, @RequestBody CostAllocationInfo costAllocationInfo) {
            costAllocationInfoService.create(organizationId, costAllocationInfo);
        return Results.success(costAllocationInfo);
    }

    @ApiOperation(value = "修改屈臣氏费用分配表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PutMapping
    public ResponseEntity<CostAllocationInfo> update(@PathVariable("organizationId") Long organizationId, @RequestBody CostAllocationInfo costAllocationInfo) {
        costAllocationInfoService.update(organizationId, costAllocationInfo);
        return Results.success(costAllocationInfo);
    }

    @ApiOperation(value = "删除屈臣氏费用分配表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @DeleteMapping
    public ResponseEntity<?> remove(@RequestBody CostAllocationInfo costAllocationInfo) {
        costAllocationInfoService.remove(costAllocationInfo);
        return Results.success();
    }

}
