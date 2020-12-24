package org.srm.mall.other.api.dto;

import io.swagger.annotations.ApiModelProperty;
import org.srm.mall.product.api.dto.ItemCategoryDTO;

/**
 * 用于feign调用接收返回参数的实体类扩展
 *
 * @author jianhao.zhang01@hand-china.com 2020/12/24 21:33
 */
public class WatsonsItemCategoryDTO extends ItemCategoryDTO {

    public String getParentCategoryName() {
        return parentCategoryName;
    }

    public void setParentCategoryName(String parentCategoryName) {
        this.parentCategoryName = parentCategoryName;
    }

    @ApiModelProperty("父级品类名称")
    private String parentCategoryName;

}
