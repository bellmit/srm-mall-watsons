package org.srm.mall.other.infra.repository.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.srm.mall.other.domain.repository.WatsonsItemRepository;
import org.srm.mall.other.infra.mapper.WatsonsItemMapper;
import org.srm.mall.product.api.dto.QueryItemCodeDTO;

import java.util.List;

@Component
public class WatsonsItemRepositoryImpl implements WatsonsItemRepository {

    @Autowired
    private WatsonsItemMapper watsonsItemMapper;

    @Override
    public QueryItemCodeDTO selectItemByCode(Long tenantId,Long itemCategoryId){
        return watsonsItemMapper.selectItemByCode(tenantId,itemCategoryId);
    }
}
