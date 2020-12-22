package org.srm.mall.agreement.infra.mapper;

import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.srm.mall.agreement.api.dto.UnitDTO;
import org.srm.mall.agreement.api.dto.UnitRefDTO;
import org.srm.mall.agreement.domain.entity.WatsonsUnitRef;

import java.util.LinkedList;
import java.util.List;

/**
 * 采买组织映射Mapper
 *
 * @author fu.ji@hand-china.com 2020-08-20 15:36:17
 */
public interface WatsonsUnitRefMapper extends BaseMapper<WatsonsUnitRef> {
    /**
     * 查询组织信息
     * 1 用户权限分配的组织
     * 2 用户关联员工、员工的所属部门
     *
     * @param unitRefDTO 查询条件
     * @return 组织信息
     */
    List<UnitRefDTO> queryUnitByUser(UnitRefDTO unitRefDTO);
}
