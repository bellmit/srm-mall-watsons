package org.srm.mall.common.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.srm.mall.common.feign.fallback.SmdmRemoteNewServiceFallbackImpl;
import org.srm.mall.common.feign.fallback.SpcmRemoteNewServiceFallbackImpl;
import org.srm.mall.other.api.dto.PcOccupyDTO;

import java.util.List;

/**
 *spcm服务
 *
 * @author jianhao.zhang01@hand-china.com 2021-01-12 14:07
 */

@FeignClient(value = "srm-purchase-contract-manage", fallbackFactory = SpcmRemoteNewServiceFallbackImpl.class, path = "/v1")
public interface SpcmRemoteNewService {

    /*
        cms占用
     */

    @RequestMapping(method = RequestMethod.POST, path = "/{organizationId}/pc-occupy")
    ResponseEntity<String> occupy(@RequestParam("sagaKey") String sagaKey, @PathVariable Long organizationId, @RequestBody List<PcOccupyDTO> pcOccupyDTOS);
}
