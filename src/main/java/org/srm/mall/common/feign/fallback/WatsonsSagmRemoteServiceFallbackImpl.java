package org.srm.mall.common.feign.fallback;

import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.srm.mall.common.feign.WatsonsSagmRemoteService;
import org.srm.mall.common.feign.WatsonsWareHouseRemoteService;

/**
 * cms超时
 *
 * @author jianhao.zhang01@hand-china.com 2021/01/21 17:14
 */
@Component
public class WatsonsSagmRemoteServiceFallbackImpl implements FallbackFactory<WatsonsSagmRemoteService> {
    private static final Logger logger = LoggerFactory.getLogger(WatsonsSagmRemoteServiceFallbackImpl.class);


    @Override
    public WatsonsSagmRemoteService create(Throwable cause) {
        return new WatsonsSagmRemoteService(){
            @Override
            public ResponseEntity<String> queryAgreementLineById(Long tenantId, Long agreementLineId) {
                logger.error("query cms info timeout agreementLineId is "+ agreementLineId);
                return null;
            }
        };
    }
}
