package org.srm.mall.infra.constant;

/**
 * @author fu.ji@hand-china.com 2020-12-21 14:34:22
 */
public interface WatsonsConstants {
    /**
     * 租户编码
     */
    String TENANT_NUMBER = "SRM-WATSONS";

    public final class ErrorCode{
        private ErrorCode(){
        }

        /**
         * 门店地址与商城地址无关联关系
         */
        public static final String INV_ORGANIZATION_ADDRESS_ERROR = "inv.organization.address.error";

        /**
         * 门店地址所在区域商品不可售或无货
         */
        public static final String INV_ORGANIZATION_REGION_OOS = "inv.organization.region.oos";
    }

    final class PriceExpandField {
        private PriceExpandField() {

        }
        /**
         * CMS合同号
         */
        public static final String CONTRACT_NUM = "contractNum";

        /**
         * 最大订货量
         */
        public static final String LAST_PURCHASE_QUANTITY = "lastPurchaseQuantity";
    }
    public final class ValueType {
        private ValueType() {
        }

        /**
         * 字符串
         */
        public static final String STRING = "String";

        /**
         * 字符串
         */
        public static final String INT = "Int";

        /**
         * 其他 预定义的维度使用
         */
        public static final String OTHER = "OTHER";
    }
}
