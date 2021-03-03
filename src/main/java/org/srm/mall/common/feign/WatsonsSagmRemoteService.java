package org.srm.mall.common.feign;

import org.hzero.starter.keyencrypt.core.Encrypt;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.srm.mall.common.feign.dto.agreemnet.AgreementLine;
import org.srm.mall.common.feign.fallback.WatsonsSagmRemoteServiceFallbackImpl;
import org.srm.mall.common.feign.fallback.WatsonsWareHouseRemoteServiceFallbackImpl;

/**
 * cms查询远程服务
 *
 * @author jianhao.zhang01@hand-china.com 2021/01/07 17:38
 */
@FeignClient(value = "small-agreement", fallbackFactory = WatsonsSagmRemoteServiceFallbackImpl.class, path = "/v1")
public interface WatsonsSagmRemoteService {

    @GetMapping("/{organizationId}/agreement/agreement-line/{agreementLineId}")
    ResponseEntity<String> queryAgreementLineById(@PathVariable("organizationId")Long tenantId, @PathVariable("agreementLineId")@Encrypt Long agreementLineId);

}
