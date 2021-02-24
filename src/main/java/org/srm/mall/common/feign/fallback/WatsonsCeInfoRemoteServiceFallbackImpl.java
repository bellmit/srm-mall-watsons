package org.srm.mall.common.feign.fallback;

import feign.hystrix.FallbackFactory;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.srm.mall.common.feign.WatsonsCeInfoRemoteService;
import org.srm.mall.common.feign.WatsonsProjectCostRemoteService;
import org.srm.mall.other.api.dto.CheckCeInfoDTO;
import org.srm.mall.other.domain.entity.ProjectCost;

import java.math.BigDecimal;

/**
 * CE信息远程服务超时
 *
 * @author jianhao.zhang01@hand-china.com 2021/01/18 23:08
 */
@Component
public class WatsonsCeInfoRemoteServiceFallbackImpl implements FallbackFactory<WatsonsCeInfoRemoteService> {

    private static final Logger LOGGER = LoggerFactory.getLogger(WatsonsCeInfoRemoteServiceFallbackImpl.class);

    @Override
    public WatsonsCeInfoRemoteService create(Throwable cause) {
        return new WatsonsCeInfoRemoteService() {

            @Override
            public ResponseEntity<String> queryCeInfo(Long tenantId, String storeNo, Integer size, Integer page, String ceNumber, String description, String projectName) {
                LOGGER.error("query CE info error :{}", storeNo,ceNumber,projectName,description);
                return null;
            }

            @Override
            public ResponseEntity<String> checkCeInfo(Long tenantId, CheckCeInfoDTO checkCeInfoDTO) {
                LOGGER.error("check CE info error :{}", checkCeInfoDTO);
                return null;
            }
        };
    }
}
