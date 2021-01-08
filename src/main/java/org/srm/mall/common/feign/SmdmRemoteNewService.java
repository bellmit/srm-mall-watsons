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
 * SMDM远程服务调用
 *
 * @author minzhen.you@hand-china.com 2019年3月21日下午2:06:24
 */
@FeignClient(value = "srm-mdm", fallbackFactory = SmdmRemoteNewServiceFallbackImpl.class, path = "/v1")
public interface SmdmRemoteNewService {


    @RequestMapping(method = RequestMethod.GET, path = "/{organizationId}/item-categories/sec-categories")
    ResponseEntity<String> selectSecondaryByThirdItemCategory(@PathVariable Long organizationId, @RequestParam("queryCategoryId") String queryCategoryId);

}
