package org.srm.mall.agreement.infra.repository.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.srm.mall.agreement.api.dto.UnitRefDTO;
import org.srm.mall.agreement.infra.mapper.WatsonsUnitRefMapper;
import org.srm.mall.infra.constant.WatsonsConstants;
import org.srm.web.annotation.Tenant;

import java.util.List;

/**
 * 采买组织映射 资源库实现
 *
 * @author fu.ji@hand-china.com 2020-08-20 15:36:17
 */
@Component
@Tenant(WatsonsConstants.TENANT_NUMBER)
public class WatsonsUnitRefRepositoryImpl extends UnitRefRepositoryImpl {
    @Autowired
    private WatsonsUnitRefMapper watsonsUnitRefMapper;
    @Override
    public List<UnitRefDTO> queryUnitByUser(UnitRefDTO unitRefDTO) {
        return watsonsUnitRefMapper.queryUnitByUser(unitRefDTO);
    }
}
