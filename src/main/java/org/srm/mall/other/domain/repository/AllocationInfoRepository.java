package org.srm.mall.other.domain.repository;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.hzero.mybatis.base.BaseRepository;
import org.srm.mall.other.api.dto.OrganizationInfoDTO;
import org.srm.mall.other.domain.entity.AllocationInfo;

import java.util.List;

/**
 * 屈臣氏费用分配表资源库
 *
 * @author yuewen.wei@hand-china.com 2020-12-21 15:35:27
 */
public interface AllocationInfoRepository extends BaseRepository<AllocationInfo> {

    //查询分配门店的库存组织
    Page<OrganizationInfoDTO> selectAllocationShopOrganization(OrganizationInfoDTO organizationInfoDTO, PageRequest pageRequest);

}
