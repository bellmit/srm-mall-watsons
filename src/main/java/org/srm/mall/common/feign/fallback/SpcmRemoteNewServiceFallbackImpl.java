package org.srm.mall.common.feign.fallback;

import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;
import org.srm.mall.common.feign.SmdmRemoteNewService;
import org.srm.mall.common.feign.SpcmRemoteNewService;
import org.srm.mall.other.api.dto.PcOccupyDTO;

import java.util.List;

/**
 * description
 *
 * @author jianhao.zhang01@hand-china.com 2021/01/27 21:59
 */
@Component
public class SpcmRemoteNewServiceFallbackImpl implements FallbackFactory<SpcmRemoteNewService> {

    private static final Logger logger = LoggerFactory.getLogger(SpcmRemoteNewServiceFallbackImpl.class);

    @Override
    public SpcmRemoteNewService create(Throwable throwable) {
        return new SpcmRemoteNewService() {

            @Override
            public ResponseEntity<String> occupy(String sagaKey, Long organizationId,  List<PcOccupyDTO> pcOccupyDTOS) {
                logger.error("occupy CMS price error! param pcOccupyDTOS :{}",pcOccupyDTOS);
                return null;
            }
        };
    }
}

