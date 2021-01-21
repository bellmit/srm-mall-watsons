package org.srm.mall.other.api.dto;

import lombok.Data;

@Data
public class WatsonsAddressDTO {

    /**
     * 纬度
     */
    private String latitude;

    /**
     * 经度
     */
    private String longitude;

    /**
     * 库存组织id
     */
    private Long invOrganizationId;

    /**
     * 百度返回数据
     */
    private WatsonsAddressResultDTO addressResult;

    /**
     * 更新是否成功
     */
    private Boolean success;

    /**
     * 失败信息
     */
    private String resultMsg;

    /**
     * 商城对应的地区id
     */
    private Long mallRegionId;

    private String mallLevelPath;

    /**
     * 商城对应的地区等级
     */
    private Integer mallRegionLevel;

    /**
     * 百度地区编码
     */
    private Integer adCode;

    /**
     * 屈臣氏同步过来的详细地址
     */
    private String address;

    private Long companyId;

    private String addressRegion;

}
