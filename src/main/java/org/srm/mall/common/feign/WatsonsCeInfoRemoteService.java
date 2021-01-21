package org.srm.mall.common.feign;


import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.srm.mall.common.feign.fallback.WatsonsCeInfoRemoteServiceFallbackImpl;
import org.srm.mall.common.feign.fallback.WatsonsProjectCostRemoteServiceFallbackImpl;
import org.srm.mall.other.domain.entity.ProjectCost;

import java.math.BigDecimal;

/**
 * 费用项目查询远程服务
 *
 * @author jianhao.zhang01@hand-china.com 2021/01/07 17:38
 */
@FeignClient(value = "srm-interface", fallbackFactory = WatsonsCeInfoRemoteServiceFallbackImpl.class, path = "/v1")
public interface WatsonsCeInfoRemoteService {

    @GetMapping("/{organizationId}/execute-exp/generic/scux-common1")
    ResponseEntity<String> queryCeInfo(@PathVariable("organizationId") Long tenantId, @RequestParam("storeNo")String storeNo, @RequestParam("size") Integer size, @RequestParam("page") Integer page);

    @PostMapping("/{organizationId}/execute-exp/generic/scux-common1")
    ResponseEntity<String> checkCeInfo(@PathVariable("organizationId") Long tenantId, @RequestParam("ceId") int ceId, @RequestParam("changeAmount") BigDecimal changeAmount);

}
