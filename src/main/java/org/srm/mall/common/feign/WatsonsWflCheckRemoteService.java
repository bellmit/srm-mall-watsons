package org.srm.mall.common.feign;


import org.hzero.starter.keyencrypt.core.Encrypt;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.srm.mall.common.feign.dto.wflCheck.WatsonsWflCheckDTO;
import org.srm.mall.common.feign.dto.wflCheck.WatsonsWflCheckResultVO;
import org.srm.mall.common.feign.fallback.WatsonsCeInfoRemoteServiceFallbackImpl;
import org.srm.mall.common.feign.fallback.WatsonsWflCheckRemoteServiceFallbackImpl;
import org.srm.mall.other.api.dto.CheckCeInfoDTO;

import java.util.List;

/**
 * 费用项目查询远程服务
 *
 * @author jianhao.zhang01@hand-china.com 2021/01/07 17:38
 */
@FeignClient(value = "srm-purchase-cooperation", fallbackFactory = WatsonsWflCheckRemoteServiceFallbackImpl.class, path = "/v1")
public interface WatsonsWflCheckRemoteService {

    @PostMapping("/{organizationId}/watsons-approver-rule/wfl-start-check")
    ResponseEntity<String> wflStartCheck(@PathVariable Long organizationId, @Encrypt @RequestBody List<WatsonsWflCheckDTO> watsonsWflCheckDTOList);

}
