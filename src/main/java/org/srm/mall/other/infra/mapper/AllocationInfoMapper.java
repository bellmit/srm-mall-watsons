package org.srm.mall.other.infra.mapper;

import org.srm.mall.other.api.dto.OrganizationInfoDTO;
import org.srm.mall.other.domain.entity.AllocationInfo;
import io.choerodon.mybatis.common.BaseMapper;

import java.util.List;

/**
 * 屈臣氏费用分配表Mapper
 *
 * @author yuewen.wei@hand-china.com 2020-12-21 15:35:27
 */
public interface AllocationInfoMapper extends BaseMapper<AllocationInfo> {

    List<OrganizationInfoDTO> selectAllocationShopOrganization(OrganizationInfoDTO organizationInfoDTO);

}
