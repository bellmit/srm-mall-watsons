package org.srm.mall.common.feign.fallback;

import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.srm.mall.common.feign.WatsonsWareHouseRemoteService;

/**
 * 仓转店超时
 *
 * @author jianhao.zhang01@hand-china.com 2021/01/21 17:14
 */
@Component
public class WatsonsWareHouseRemoteServiceFallbackImpl implements FallbackFactory<WatsonsWareHouseRemoteService> {

    private static final Logger LOGGER = LoggerFactory.getLogger(WatsonsWareHouseRemoteServiceFallbackImpl.class);


    @Override
    public WatsonsWareHouseRemoteService create(Throwable cause) {
        return new WatsonsWareHouseRemoteService(){

            @Override
            public ResponseEntity<String> queryWhInfo(Long tenantId, String storeId) {
                LOGGER.error("query warehouse info error :{}", storeId);
                return null;
            }
        };
    }
}
