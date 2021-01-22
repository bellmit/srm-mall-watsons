package org.srm.mall.other.api.dto;

import org.srm.mall.product.api.dto.BaseDto;

/**
 * 地址信息返回DTO
 *
 * @author jianhao.zhang01@hand-china.com 2021/01/19 16:56
 */
public class WatsonsRegionDTO extends BaseDto {


    private Long regionId;

    private String regionCode;

    private String regionName;

    private String parentRegionCode;

    private int regionLevel;

    private String levelPath;

    private int enabledFlag;

    public Long getRegionId() {
        return regionId;
    }

    public void setRegionId(Long regionId) {
        this.regionId = regionId;
    }

    public String getRegionCode() {
        return regionCode;
    }

    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public String getParentRegionCode() {
        return parentRegionCode;
    }

    public void setParentRegionCode(String parentRegionCode) {
        this.parentRegionCode = parentRegionCode;
    }

    public int getRegionLevel() {
        return regionLevel;
    }

    public void setRegionLevel(int regionLevel) {
        this.regionLevel = regionLevel;
    }

    public String getLevelPath() {
        return levelPath;
    }

    public void setLevelPath(String levelPath) {
        this.levelPath = levelPath;
    }

    public int getEnabledFlag() {
        return enabledFlag;
    }

    public void setEnabledFlag(int enabledFlag) {
        this.enabledFlag = enabledFlag;
    }

}
