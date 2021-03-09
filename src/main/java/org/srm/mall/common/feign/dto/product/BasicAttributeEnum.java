package org.srm.mall.common.feign.dto.product;

/**
 * 基本属性枚举
 */
public enum BasicAttributeEnum {
    BRAND_CODE("000000000001","品牌"),
    MODEL_CODE("000000000002","型号"),
    BAR_CODE("000000000003","商品条形码"),
    ORIGIN_CODE("000000000004","产地"),
    SALE_UNIT_CODE("000000000005","销售单位"),
    WEIGHT_CODE("000000000006","重量"),
    PACKAGING_CODE("000000000007","包装尺寸"),
    TAX_CODE("000000000008","税率"),
    CURRENCY_CODE("000000000009","币种");

    private String code;
    private String attrName;

    BasicAttributeEnum(String code, String attrName) {
        this.code = code;
        this.attrName = attrName;
    }

    public static boolean isBasicAttrByCode(String code) {
        for (BasicAttributeEnum attrEnum : BasicAttributeEnum.values()) {
            if (code.equals(attrEnum.getCode())) {
                return true;
            }
        }
        return false;
    }

    public static boolean isBasicAttrByName(String attrName) {
        for (BasicAttributeEnum attrEnum : BasicAttributeEnum.values()) {
            if (attrName.equals(attrEnum.getAttrName())) {
                return true;
            }
        }
        return false;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getAttrName() {
        return attrName;
    }

    public void setAttrName(String attrName) {
        this.attrName = attrName;
    }
}
