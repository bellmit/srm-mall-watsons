package org.srm.mall.common.feign.dto.wflCheck;

import java.util.List;

/**
 * @author yande.wang@hand-china.com 2021/3/12 13:47
 * @description
 */
public class WatsonsWflCheckDTO {

    private Long categoryId;
    private List<String> storeIdList;   // 库存组织编码

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public List<String> getStoreIdList() {
        return storeIdList;
    }

    public void setStoreIdList(List<String> storeIdList) {
        this.storeIdList = storeIdList;
    }
}
