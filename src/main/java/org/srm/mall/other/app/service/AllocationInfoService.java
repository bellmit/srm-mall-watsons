package org.srm.mall.other.app.service;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.srm.mall.other.api.dto.AllocationInfoDTO;
import org.srm.mall.other.api.dto.CeLovResultDTO;
import org.srm.mall.other.api.dto.OrganizationInfoDTO;
import org.srm.mall.other.api.dto.WatsonsShoppingCartDTO;
import org.srm.mall.other.domain.entity.AllocationInfo;
import org.srm.mall.other.domain.entity.ProjectCost;
import org.srm.mall.other.domain.entity.WatsonsShoppingCart;

import java.util.List;

/**
 * 屈臣氏费用分配表应用服务
 *
 * @author yuewen.wei@hand-china.com 2020-12-21 15:35:27
 */
public interface AllocationInfoService {

    /**
     * 屈臣氏费用分配表查询参数
     *
     * @param tenantId 租户ID
     * @return 屈臣氏费用分配表列表
     */
    List<AllocationInfo> list(Long tenantId, Long cartId);

    /**
     * 创建屈臣氏费用分配表
     *
     * @param tenantId           租户ID
     * @param watsonsShoppingCart  屈臣氏费用分配表
     * @return 屈臣氏费用分配表
     */
    List<AllocationInfo> create(Long tenantId, WatsonsShoppingCart watsonsShoppingCart);

    /**
     * 更新购物车商品数量
     * @param watsonsShoppingCart
     * @return
     */
    WatsonsShoppingCart updateShoppingCart(WatsonsShoppingCart watsonsShoppingCart);

    /**
     * 删除屈臣氏费用分配表
     */
    WatsonsShoppingCart remove(WatsonsShoppingCart watsonsShoppingCart);

    /**
     * 批量插入费用分配
     * @param organizationId
     * @param allocationInfoDTO
     * @return
     */
    AllocationInfoDTO batchCreate(Long organizationId, AllocationInfoDTO allocationInfoDTO);

    /**
     * 查询商品行对应的费用项目和费用项目子分类
     * @param organizationId
     * @param itemCategoryId
     * @return
     */
    List<ProjectCost> selectAllocationProjectLov(Long organizationId, Long itemCategoryId, Long itemId, Integer size, Integer page);

    /**
     * 查询店铺对应的CE信息
     * @param organizationId
     * @param storeNo
     * @return
     */
    List<CeLovResultDTO> selectCeInfoLov(Long organizationId, String storeNo, Integer size, Integer page);
}
