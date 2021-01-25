package org.srm.mall.common.feign.fallback;

import feign.hystrix.FallbackFactory;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.srm.mall.common.feign.WatsonsProjectCostRemoteService;
import org.srm.mall.other.domain.entity.ProjectCost;

/**
 * 费用项目查询远程服务超时
 *
 * @author jianhao.zhang01@hand-china.com 2021/01/07 18:30
 */
@Component
public class WatsonsProjectCostRemoteServiceFallbackImpl implements FallbackFactory<WatsonsProjectCostRemoteService> {

    private static final Logger LOGGER = LoggerFactory.getLogger(WatsonsProjectCostRemoteServiceFallbackImpl.class);


    @Override
    public WatsonsProjectCostRemoteService create(Throwable cause) {

        return new WatsonsProjectCostRemoteService() {
            @Override
            public ResponseEntity<String> list(Long tenantId, ProjectCost projectCost, PageRequest pageRequest) {
                    LOGGER.error("query cost project error :{}", projectCost);
                    return null;
            }
        };
    }
}
