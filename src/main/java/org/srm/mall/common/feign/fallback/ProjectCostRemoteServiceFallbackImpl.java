package org.srm.mall.common.feign.fallback;

import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.srm.mall.common.feign.ProjectCostRemoteService;
import org.srm.mall.other.domain.entity.ProjectCost;

/**
 * 费用项目查询远程服务超时
 *
 * @author jianhao.zhang01@hand-china.com 2021/01/07 18:30
 */
public class ProjectCostRemoteServiceFallbackImpl implements ProjectCostRemoteService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProjectCostRemoteServiceFallbackImpl.class);

    @Override
    public ResponseEntity<String> list(Long tenantId, ProjectCost projectCost, PageRequest pageRequest) {
        LOGGER.error("query cost project error :{}", projectCost);
        return null;
    }
}
