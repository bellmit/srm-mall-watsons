package org.srm.mall.other.infra.mapper;


import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.srm.mall.context.entity.ItemCategory;
import org.srm.mall.product.api.dto.QueryItemCodeDTO;

import java.util.List;

/**
 * 物料品类信息
 */
public interface WatsonsItemMapper extends BaseMapper<ItemCategory> {

    /**
     * 查询一级物料品类
     * @param tenantId
     * @param itemCategoryId
     * @return
     */
    QueryItemCodeDTO selectItemByCode(@Param("tenantId") Long tenantId,@Param("itemCategoryId") Long itemCategoryId);
}
