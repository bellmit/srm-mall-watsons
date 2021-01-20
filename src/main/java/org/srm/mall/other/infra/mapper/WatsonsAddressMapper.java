package org.srm.mall.other.infra.mapper;

import org.apache.ibatis.annotations.Param;
import org.srm.mall.other.api.dto.WatsonsAddressDTO;

import java.util.Date;
import java.util.List;

public interface WatsonsAddressMapper {

    List<WatsonsAddressDTO> selectUpdateAddressInfo(@Param("tenantId") Long tenantId, @Param("updatedDate") Date updatedDate);

    Long selectTenantId(@Param("tenantNum") String tenantNum);

    List<WatsonsAddressDTO> selectRegionMapping(@Param("adCodeList") List<Integer> adCodeList);

    List<WatsonsAddressDTO> selectInvorgAddress(@Param("organizationIdList") List<Long> organizationIdList, @Param("tenantId") Long tenantId);
}
