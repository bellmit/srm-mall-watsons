package org.srm.mall.infra.constant;

/**
 * @author fu.ji@hand-china.com 2020-12-21 14:34:22
 */
public interface WatsonsConstants {
    /**
     * 租户编码
     */
    String TENANT_NUMBER = "SRM-WATSONS";

    public static final String ALLOCATION_INFO = "ALLOCATION_INFO";

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

        /**
         *
         */
        public static final String ERROR_PRODUCT_STOCK_UNDERSTOCK = "product.stock.deduction.error";
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


    public static class operationTypeCode{

        /**
         * 占用
         */
        public static final String SPCM_OCCUPY = "OCCUPY";

        /**
         * 取消
         */
        public static final String SPCM_CANCEL = "CANCEL";
        /**
         * 更新
         */
        public static final String SPCM_UPDATE = "UPDATE";
        /**
         * 扣减-用于后续财务模块使用
         */
        public static final String SPCM_DEDUCT = "DEDUCT";
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

    public static class smalSourceType{

        /**
         * 商城预占
         */
        public static final String SMAL_PRE = "SMAL_PRE";
    }

}
