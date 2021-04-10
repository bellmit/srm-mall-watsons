package org.srm.mall.common.feign.fallback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.srm.mall.common.feign.WatsonsWflCheckRemoteService;
import org.srm.mall.common.feign.dto.wflCheck.WatsonsWflCheckDTO;
import org.srm.mall.common.feign.dto.wflCheck.WatsonsWflCheckResultVO;

import java.util.List;

/**
 * description
 *
 * @author ericzhang 2021/03/19 3:50 下午
 */
@Component
public class WatsonsWflCheckRemoteServiceFallbackImpl implements WatsonsWflCheckRemoteService {

    private Logger logger = LoggerFactory.getLogger(WatsonsWflCheckRemoteServiceFallbackImpl.class);
    @Override
    public ResponseEntity<String> wflStartCheck(Long organizationId, List<WatsonsWflCheckDTO> watsonsWflCheckDTOList) {
        logger.error("check wfl before paste order error {} ", watsonsWflCheckDTOList);
        return null;
    }
}
