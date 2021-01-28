package org.srm.mall.other.domain.repository;

import org.srm.mall.product.api.dto.QueryItemCodeDTO;


public interface WatsonsItemRepository {

    /**
     * 查询一级物料品类信息
     * @param tenantId
     * @param itemCategoryId
     * @return
     */
    QueryItemCodeDTO selectItemByCode(Long tenantId,Long itemCategoryId);
}
