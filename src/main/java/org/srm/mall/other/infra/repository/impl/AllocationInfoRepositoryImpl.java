package org.srm.mall.other.infra.repository.impl;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.srm.mall.other.api.dto.OrganizationInfoDTO;
import org.srm.mall.other.api.dto.WatsonsRegionDTO;
import org.srm.mall.other.domain.entity.AllocationInfo;
import org.srm.mall.other.domain.entity.WatsonsShoppingCart;
import org.srm.mall.other.domain.repository.AllocationInfoRepository;
import org.springframework.stereotype.Component;
import org.srm.mall.other.infra.mapper.AllocationInfoMapper;
import org.srm.mall.region.api.dto.AddressDTO;
import org.srm.mall.region.domain.entity.Address;

import java.util.List;

/**
 * 屈臣氏费用分配表 资源库实现
 *
 * @author yuewen.wei@hand-china.com 2020-12-21 15:35:27
 */
@Component
public class AllocationInfoRepositoryImpl extends BaseRepositoryImpl<AllocationInfo> implements AllocationInfoRepository {

    @Autowired
    private AllocationInfoMapper allocationInfoMapper;

    @Override
    public Page<OrganizationInfoDTO> selectAllocationShopOrganization(OrganizationInfoDTO organizationInfoDTO, PageRequest pageRequest) {
        return PageHelper.doPageAndSort(pageRequest, () -> allocationInfoMapper.selectAllocationShopOrganization(organizationInfoDTO));
    }

    @Override
    public Integer selectHasProjectSubcategoryId(Long projectCostId, Long tenantId) {
        return allocationInfoMapper.selectHasProjectSubcategoryId(projectCostId,tenantId);
    }

    @Override
    public AddressDTO selectIdByCode(Long organizationId, String watsonsOrganizationCode) {
        return allocationInfoMapper.selectIdByCode(organizationId,watsonsOrganizationCode);
    }

    @Override
    public WatsonsRegionDTO selectRegionInfoByRegionId(Long regionId) {
        return allocationInfoMapper.selectRegionInfoByRegionId(regionId);
    }

    @Override
    public WatsonsRegionDTO selectRegionInfoByRegionCode(String regionCode) {
        return allocationInfoMapper.selectRegionInfoByRegionCode(regionCode);

    }


}
