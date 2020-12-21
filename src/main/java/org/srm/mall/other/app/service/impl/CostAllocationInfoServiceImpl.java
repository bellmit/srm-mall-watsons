package org.srm.mall.other.app.service.impl;

import org.hzero.core.base.BaseAppService;

import org.hzero.mybatis.helper.SecurityTokenHelper;
import org.srm.mall.other.app.service.CostAllocationInfoService;
import org.srm.mall.other.domain.entity.CostAllocationInfo;
import org.srm.mall.other.domain.repository.CostAllocationInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

/**
 * 屈臣氏费用分配表应用服务默认实现
 *
 * @author yuewen.wei@hand-china.com 2020-12-21 15:35:27
 */
@Service
public class CostAllocationInfoServiceImpl extends BaseAppService implements CostAllocationInfoService {

    private final CostAllocationInfoRepository costAllocationInfoRepository;

    @Autowired
    public CostAllocationInfoServiceImpl(CostAllocationInfoRepository costAllocationInfoRepository) {
        this.costAllocationInfoRepository = costAllocationInfoRepository;
    }

    @Override
    public Page<CostAllocationInfo> list(Long tenantId, CostAllocationInfo costAllocationInfo, PageRequest pageRequest) {
        return costAllocationInfoRepository.pageAndSort(pageRequest, costAllocationInfo);
    }

    @Override
    public CostAllocationInfo detail(Long tenantId, Long allocationId) {
        return costAllocationInfoRepository.selectByPrimaryKey(allocationId);
    }

    @Override
    public CostAllocationInfo create(Long tenantId, CostAllocationInfo costAllocationInfo) {
        validObject(costAllocationInfo);
        costAllocationInfoRepository.insertSelective(costAllocationInfo);
        return costAllocationInfo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CostAllocationInfo update(Long tenantId, CostAllocationInfo costAllocationInfo) {
        SecurityTokenHelper.validToken(costAllocationInfo);
        costAllocationInfoRepository.updateByPrimaryKeySelective(costAllocationInfo);
        return costAllocationInfo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void remove(CostAllocationInfo costAllocationInfo) {
        SecurityTokenHelper.validToken(costAllocationInfo);
        costAllocationInfoRepository.deleteByPrimaryKey(costAllocationInfo);
    }
}
