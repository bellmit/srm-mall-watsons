package org.srm.mall.common.feign;


import io.choerodon.mybatis.pagehelper.annotation.SortDefault;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.mybatis.pagehelper.domain.Sort;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.srm.mall.common.feign.fallback.WatsonsProjectCostRemoteServiceFallbackImpl;

import org.srm.mall.other.domain.entity.ProjectCost;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 费用项目查询远程服务
 *
 * @author jianhao.zhang01@hand-china.com 2021/01/07 17:38
 */

@FeignClient(value = "srm-saas-cux", fallbackFactory = WatsonsProjectCostRemoteServiceFallbackImpl.class, path = "/v1")
public interface WatsonsProjectCostRemoteService{

    @PostMapping("/{organizationId}/watson/project-costs/list")
    ResponseEntity<String> list(@PathVariable("organizationId") Long tenantId, @RequestBody ProjectCost projectCost, @RequestParam("pageRequest") PageRequest pageRequest);

}
