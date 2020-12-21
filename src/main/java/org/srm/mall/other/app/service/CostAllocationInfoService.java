package org.srm.mall.other.app.service;

import org.srm.mall.other.domain.entity.CostAllocationInfo;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
/**
 * 屈臣氏费用分配表应用服务
 *
 * @author yuewen.wei@hand-china.com 2020-12-21 15:35:27
 */
public interface CostAllocationInfoService {

    /**
    * 屈臣氏费用分配表查询参数
    *
    * @param tenantId 租户ID
    * @param costAllocationInfo 屈臣氏费用分配表
    * @param pageRequest 分页
    * @return 屈臣氏费用分配表列表
    */
    Page<CostAllocationInfo> list(Long tenantId, CostAllocationInfo costAllocationInfo, PageRequest pageRequest);

    /**
     * 屈臣氏费用分配表详情
     *
     * @param tenantId 租户ID
     * @param allocationId 主键
     * @return 屈臣氏费用分配表列表
     */
    CostAllocationInfo detail(Long tenantId, Long allocationId);

    /**
     * 创建屈臣氏费用分配表
     *
     * @param tenantId 租户ID
     * @param costAllocationInfo 屈臣氏费用分配表
     * @return 屈臣氏费用分配表
     */
    CostAllocationInfo create(Long tenantId, CostAllocationInfo costAllocationInfo);

    /**
     * 更新屈臣氏费用分配表
     *
     * @param tenantId 租户ID
     * @param costAllocationInfo 屈臣氏费用分配表
     * @return 屈臣氏费用分配表
     */
    CostAllocationInfo update(Long tenantId, CostAllocationInfo costAllocationInfo);

    /**
     * 删除屈臣氏费用分配表
     *
     * @param costAllocationInfo 屈臣氏费用分配表
     */
    void remove(CostAllocationInfo costAllocationInfo);
}
