package org.srm.mall.common.feign;

import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.hzero.core.util.Results;
import org.hzero.starter.keyencrypt.core.Encrypt;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.srm.mall.common.feign.fallback.SmdmRemoteNewServiceFallbackImpl;
import org.srm.mall.common.feign.fallback.SmdmRemoteServiceFallbackImpl;
import org.srm.mall.context.dto.ItemListDTO;
import org.srm.mall.other.api.dto.WatsonsItemCategorySearchDTO;
import org.srm.mall.product.api.dto.ItemCategoryDTO;
import org.srm.mall.product.api.dto.ItemCategorySearchDTO;

import java.util.List;

/**
 *smdm服务
 *
 * @author jianhao.zhang01@hand-china.com 2021-01-12 14:07
 */

@FeignClient(value = "srm-mdm", fallbackFactory = SmdmRemoteNewServiceFallbackImpl.class, path = "/v1")
public interface SmdmRemoteNewService {


    @RequestMapping(method = RequestMethod.GET, path = "/{organizationId}/item-categories/sec-categories")
    ResponseEntity<String> selectSecondaryByThirdItemCategory(@PathVariable Long organizationId, @RequestParam("queryCategoryId") String queryCategoryId);



    @RequestMapping(method = RequestMethod.GET, path = "/{organizationId}/item-categories/queryById")
    ResponseEntity<String> queryById(@PathVariable Long organizationId, @RequestParam("queryCategoryId") String queryCategoryId);


    @RequestMapping(method = RequestMethod.GET, path = "/{organizationId}/item-categories/queryLevelPathByItemId")
    ResponseEntity<String> queryLevelPathByItemId(@PathVariable Long organizationId, @RequestParam("itemId") Long itemId);

    /**
     * 根据一级品类id查询编码、名称
     * @param organizationId
     * @param categoryId
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, path = "/{organizationId}/item-categories/{categoryId}")
    ResponseEntity<String> queryItemById(@PathVariable Long organizationId, @Encrypt @PathVariable Long categoryId);
}
