package org.srm.mall.common.enums;


import org.srm.mall.infra.constant.WatsonsConstants;

import java.util.HashMap;
import java.util.Map;

/**
 * @author fu.ji@hanc-china.com 2021-1-27 17:19:11
 */
public enum WatsonsAuthDimensionEnum {
    /**
     * 屈臣式/区域
     */
    WATSONS_AREA("WATSONS_AREA", WatsonsConstants.ValueType.STRING, "watsonRegion"),
    /**
     * 市场
     */
    MARKET("MARKET", WatsonsConstants.ValueType.INT, "watsonMarket"),
    /**
     * 部门
     */
    WATSONS_AUTH_DEP("WATSONS_AUTH_DEP", WatsonsConstants.ValueType.INT, "watsonUnit"),
    /**
     * 写字楼
     */
    WATSONS_WAREHOUSESTORE("WATSONS_WAREHOUSESTORE", WatsonsConstants.ValueType.INT, "wareHouseStore");
    private String dimensionCode;
    private String valueType;
    private String priceScope;

    WatsonsAuthDimensionEnum(String dimensionCode, String valueType, String priceScope) {
        this.dimensionCode = dimensionCode;
        this.valueType = valueType;
        this.priceScope = priceScope;
    }

    public String getDimensionCode() {
        return dimensionCode;
    }

    public WatsonsAuthDimensionEnum setDimensionCode(String dimensionCode) {
        this.dimensionCode = dimensionCode;
        return this;
    }

    public String getValueType() {
        return valueType;
    }

    public WatsonsAuthDimensionEnum setValueType(String valueType) {
        this.valueType = valueType;
        return this;
    }

    public String getPriceScope() {
        return priceScope;
    }

    public WatsonsAuthDimensionEnum setPriceScope(String priceScope) {
        this.priceScope = priceScope;
        return this;
    }

    public static WatsonsAuthDimensionEnum valueOfPriceScope(String priceScope) {
        Map<String, WatsonsAuthDimensionEnum> watsonsAuthDimensionEnumMap = getWatsonsAuthDimensionEnumMap();
        return watsonsAuthDimensionEnumMap.get(priceScope);
    }

    public static Map<String,WatsonsAuthDimensionEnum> getWatsonsAuthDimensionEnumMap() {
        Map<String, WatsonsAuthDimensionEnum> map = new HashMap<>();
        for (WatsonsAuthDimensionEnum watsonsAuthDimensionEnum : WatsonsAuthDimensionEnum.values()) {
            map.put(watsonsAuthDimensionEnum.getPriceScope(), watsonsAuthDimensionEnum);
        }
        return map;
    }
}
