package org.srm.mall.common.feign;


import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.srm.mall.common.feign.fallback.WatsonsCeInfoRemoteServiceFallbackImpl;
import org.srm.mall.common.feign.fallback.WatsonsProjectCostRemoteServiceFallbackImpl;
import org.srm.mall.other.domain.entity.ProjectCost;

/**
 * 费用项目查询远程服务
 *
 * @author jianhao.zhang01@hand-china.com 2021/01/07 17:38
 */

@FeignClient(value = "srm-interface", fallbackFactory = WatsonsCeInfoRemoteServiceFallbackImpl.class, path = "/v1")
public interface WatsonsCeInfoRemoteService {

    @PostMapping("/{organizationId}/execute-exp/generic/scux-common1")
    ResponseEntity<String> queryCeInfo(@PathVariable("organizationId") Long tenantId,@RequestParam("storeNo")String storeNo, @RequestParam("size") Integer size, @RequestParam("page") Integer page);

}
