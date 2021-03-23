package org.srm.mall.other.domain.repository;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.hzero.mybatis.base.BaseRepository;
import org.srm.mall.other.api.dto.OrganizationInfoDTO;
import org.srm.mall.other.api.dto.WatsonsRegionDTO;
import org.srm.mall.other.api.dto.WatsonsShoppingCartDTO;
import org.srm.mall.other.api.dto.WhLovResultDTO;
import org.srm.mall.other.domain.entity.AllocationInfo;
import org.srm.mall.other.domain.entity.WatsonsShoppingCart;
import org.srm.mall.region.api.dto.AddressDTO;
import org.srm.mall.region.domain.entity.Address;

import java.util.List;

/**
 * 屈臣氏费用分配表资源库
 *
 * @author yuewen.wei@hand-china.com 2020-12-21 15:35:27
 */
public interface AllocationInfoRepository extends BaseRepository<AllocationInfo> {

    //查询分配门店的库存组织
    Page<OrganizationInfoDTO> selectAllocationShopOrganization(OrganizationInfoDTO organizationInfoDTO, PageRequest pageRequest);

    Integer selectHasProjectSubcategoryId(Long projectCostId, Long tenantId);

    //供货组织表根据组织code查组织id
    AddressDTO selectIdByCode(Long organizationId, String watsonsOrganizationCode);

    WatsonsRegionDTO selectRegionInfoByRegionId(Long regionId);

    WatsonsRegionDTO selectRegionInfoByRegionCode(String regionCode);

    WhLovResultDTO selectInvNameByInvCode(String inventoryCode, Long organizationId);

    WhLovResultDTO selectInvInfoByInvId(Long watsonsOrganizationId, Long organizationId);

    //暂时弃用
    Void checkAddressByInvOrganization(OrganizationInfoDTO infoDTO);

    String checkDeliveryType(String itemCode, String sourceCode, Long tenantId);
}