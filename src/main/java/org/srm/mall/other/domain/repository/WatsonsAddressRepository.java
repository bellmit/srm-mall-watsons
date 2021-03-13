package org.srm.mall.other.domain.repository;

import org.srm.mall.other.api.dto.WatsonsAddressDTO;

import java.util.Date;
import java.util.List;

public interface WatsonsAddressRepository {

    List<WatsonsAddressDTO> selectUpdateAddressInfo(Long tenantId, Date updatedDate);

    Long selectTenantId(String tenantNum);

    List<WatsonsAddressDTO> selectRegionMapping(List<Integer> adCodeList);

    List<WatsonsAddressDTO> selectInvorgAddress(List<Long> organizationIdList, Long tenantId);
}
