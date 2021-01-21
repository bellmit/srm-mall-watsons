package org.srm.mall.common.feign;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.srm.mall.common.feign.fallback.WatsonsWareHouseRemoteServiceFallbackImpl;

import java.math.BigDecimal;

/**
 * 仓转店查询远程服务
 *
 * @author jianhao.zhang01@hand-china.com 2021/01/07 17:38
 */
@FeignClient(value = "srm-saas-cux", fallbackFactory = WatsonsWareHouseRemoteServiceFallbackImpl.class, path = "/v1")
public interface WatsonsWareHouseRemoteService {

    @GetMapping("/{organizationId}/watson/store-inventory/relations/list")
    ResponseEntity<String> queryWhInfo(@PathVariable("organizationId") Long tenantId, @RequestParam("storeId")String storeId);

}
