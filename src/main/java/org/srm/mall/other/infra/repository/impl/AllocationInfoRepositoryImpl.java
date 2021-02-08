package org.srm.mall.other.infra.repository.impl;

import io.choerodon.core.domain.Page;
import io.choerodon.core.exception.CommonException;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.srm.mall.other.api.dto.OrganizationInfoDTO;
import org.srm.mall.other.api.dto.WatsonsRegionDTO;
import org.srm.mall.other.api.dto.WhLovResultDTO;
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

    private Logger logger = LoggerFactory.getLogger(AllocationInfoRepositoryImpl.class);

    @Override
    public Page<OrganizationInfoDTO> selectAllocationShopOrganization(OrganizationInfoDTO organizationInfoDTO, PageRequest pageRequest) {
        List<OrganizationInfoDTO> organizationInfoDTOS = allocationInfoMapper.selectAllocationShopOrganization(organizationInfoDTO);
        for (OrganizationInfoDTO infoDTO : organizationInfoDTOS) {
            checkAddressByInvOrganization(infoDTO);
        }
        return PageHelper.doPageAndSort(pageRequest, () -> organizationInfoDTOS);
    }

    @Override
    public Void checkAddressByInvOrganization(OrganizationInfoDTO infoDTO) {
        Integer count = allocationInfoMapper.checkAddressByInvOrganization(infoDTO);
        if(count == 0){
            logger.error(infoDTO.getOrganizationCode()+ infoDTO.getOrganizationName()+"的相关地址信息不存在，请手工补充收货地址!");
            throw new CommonException(infoDTO.getOrganizationCode()+ infoDTO.getOrganizationName()+"的相关地址信息不存在，请手工补充收货地址!");
        }
        return null;
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
    @Override
    public WhLovResultDTO selectInvNameByInvCode(String inventoryCode, Long organizationId) {
        return allocationInfoMapper.selectInvNameByInvCode(inventoryCode,organizationId);
    }


}
