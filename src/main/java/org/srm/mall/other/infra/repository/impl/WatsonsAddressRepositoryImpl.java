package org.srm.mall.other.infra.repository.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.srm.mall.other.api.dto.WatsonsAddressDTO;
import org.srm.mall.other.domain.repository.WatsonsAddressRepository;
import org.srm.mall.other.infra.mapper.WatsonsAddressMapper;

import java.util.Date;
import java.util.List;

@Component
public class WatsonsAddressRepositoryImpl implements WatsonsAddressRepository {

    @Autowired
    private WatsonsAddressMapper watsonsAddressMapper;

    @Override
    public List<WatsonsAddressDTO> selectUpdateAddressInfo(Long tenantId, Date updatedDate) {
        return watsonsAddressMapper.selectUpdateAddressInfo(tenantId, updatedDate);
    }

    @Override
    public Long selectTenantId(String tenantNum) {
        return watsonsAddressMapper.selectTenantId(tenantNum);
    }

    @Override
    public List<WatsonsAddressDTO> selectRegionMapping(List<Integer> adCodeList) {
        return watsonsAddressMapper.selectRegionMapping(adCodeList);
    }

    @Override
    public List<WatsonsAddressDTO> selectInvorgAddress(List<Long> organizationIdList, Long tenantId) {
        return watsonsAddressMapper.selectInvorgAddress(organizationIdList, tenantId);
    }
}
