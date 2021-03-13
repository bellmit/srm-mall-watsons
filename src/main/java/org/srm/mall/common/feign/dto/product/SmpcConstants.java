package org.srm.mall.common.feign.dto.product;

/**
 * 商品中心常量
 *
 * @author min.wang01@hand-china.com 2020-12-07 14:16:36
 */
public interface SmpcConstants {
    /**
     * 服务缩写
     */
    String SERVICE_NAME = "SMPC";

    /**
     * 默认0租户
     */
    Long DEFAULT_TENANT = 0L;

    /**
     * 公共桶名称
     */
    String PUBLIC_BUCKET_NAME = "public-bucket";

    /**
     * 商品sku图片上传目录
     */
    String SKU_IMAGE_UPLOAD_PATH = "product/sku/pic";


    /**
     * 属性映射
     */
    interface AttributeMapping {
        /**
         * 计量单位
         */
        String MAPPING_TYPE_UOM = "UOM";


        /**
         * 税率
         */
        String MAPPING_TYPE_TAX = "TAX";

        /**
         * 币种
         */
        String MAPPING_TYPE_CURRENCY = "CURRENCY";


    }

    interface ErrorCode {

        /**
         * 品牌logo必须不大于1M！
         */
        String ERROR_LOGO_SIZE = "smpc.error.logo_size_too_large";
        /**
         * 品牌logo格式错误！格式限制为：jpg、jpeg、tif、tiff、png
         */
        String ERROR_LOGO_FORMAT = "smpc.error.logo_format_error";
        /**
         * 品牌logo宽高超出限制！限制为：160*90 px
         */
        String ERROR_LOGO_AREA = "smpc.error.logo_out_of_limit";
        /**
         * 图片解析失败
         */
        String ERROR_ANALYSIS_PICTURES = "smpc.error.image_analysis_error";
        /**
         * 编码重复
         */
        String ERROR_CODE_REPEAT = "smpc.error.code_repeat_error";
        /**
         * 名称重复
         */
        String ERROR_NAME_REPEAT = "smpc.error.name_repeat_error";
        /**
         * 基础属性不能删除
         */
        String ERROR_CANNOT_DELETE_BASE_ATTR = "smpc.error.cannot_delete_base_attr_error";
        /**
         * 已关联商品，不能删除
         */
        String ERROR_SKU_RELATE_ATTR = "smpc.error.related_to_sku_error";

        /**
         * 目录层级错误
         */
        String ERROR_CATALOG_LEVEL = "smpc.error.catalog.level";

        /**
         * 数据异常：目录编码已存在
         */
        String ERROR_CATALOG_CODE_EXITED = "smpc.error.catalog_code.exited";

        /**
         * 数据为空
         */
        String ERROR_EMPTY_DATA = "smpc.error.empty.data";

        /**
         * 库存不足，扣减失败
         */
        String ERROR_STOCK_LESS_THAN_DEDUCTION_QUANTITY = "smpc.error.stock_less_than_dedcution_quantity";
        /**
         * 该映射关系已存在
         */
        String ERROR_THE_MAPPING_RELATIONSHIP_ALREADY_EXISTS = "smpc.error.the.mapping.relationship.already.exists";
        /**
         * 标签不存在
         */
        String ERROR_LABEL_NOT_EXISTS = "smpc.error.label_not_exists";
        /**
         * 属性名错误
         */
        String ERROR_ATTR_TYPE_NAME_ERROR = "smpc.error.attr.type.name.error";
        /**
         * 分类不存在或不是三级分类
         */
        String SKU_CATEGORY_ERROR = "smpc.error.sku_category_error";
        /**
         * 包装清单长度不能超过800
         */
        String PACKING_LIST_LENGTH_ERROR = "smpc.error.packing_list_length_error";
        /**
         * 特殊售后说明长度不能超过800
         */
        String AFTER_SALE_LENGTH_ERROR = "smpc.error.special_after_sale_length_error";
        /**
         * 商品必填项不能为空
         */
        String REQUIRED_IS_NULL_ERROR = "smpc.error.required_is_null_error";

        /**
         * 该分类名称不存在
         */
        String ERROR_NO_CATEGORY_NAME_EXIST = "smpc.error.no.category.name.exist";

        /**
         * 该目录不存在
         */
        String ERROR_NO_CATALOGUE_EXIST = "smpc.error.no.catalogue.exist";

        /**
         * 为非三级目录
         */
        String ERROR_NOT_THREE_CATALOGUE = "smpc.error.not.three.catalogue";

        /**
         * 数据转换失败
         */
        String ERROR_DATA_CONVERT = "smpc.error.data.convert";

        /**
         * 物料不存在
         */
        String ERROR_CAN_NOT_FIND_ITEM_INFO = "smpc.error.item.not.exists";
        /**
         * 物料品类不存在
         */
        String ERROR_CAN_NOT_FIND_ITEM_CATEGORY_INFO = "smpc.error.item.category.not.exists";

        /**
         * 商品不存在
         */
        String ERROR_NO_SKU_EXIST = "smpc.error.sku.not.exists";
        /*
         * 供应商不存在
         */
        String ERROR_NO_SUPPLIER_COMPANY = "smpc.error.supplier.company.not.exists";

        /**
         * 库存组织不存在
         */
        String ERROR_NO_ORGANIZATION = "smpc.error.organization.not.exists";

        /**
         * 库存添加量必须为正数
         */
        String ERROR_REPLENISHMENT_STOCK_MUST_POSITIVE = "smpc.error.replenishment.stock.must.positive";
        /**
         * 商品审核类目为空
         */
        String SKU_AUDIT_CATEGORY_IS_NULL = "smpc.error.audit.category.is.null";

        /**
         * 商品审核数据不存在
         */
        String SKU_AUDIT_IS_NULL = "smpc.error.audit.is.null";

        /**
         * 已存在此供应商数据
         */
        String SKU_AUDIT_COMPANY_EXIST = "smpc.error.supplier.exists";

        /**
         * 存在非三级分类
         */
        String SKU_AUDIT_CATAGORY_EXIST_THIRD_LEVEL = "smpc.error.category.exist.third.level";

        /**
         * 总库存不能为负数
         */
        String ERROR_TOTAL_STOCK_NOT_NEGATIVE = "smpc.error.total.stock.not.negative";
        /**
         * 电商账号异常，请检查电商账号配置
         */
        String SUPPLIER_ACCOUNT_ERROR = "smpc.error.ec_account_error";
        /**
         * 商品组不存在
         */
        String SPU_NOT_EXISTS = "smpc.error.spu_not_exists";
        /**
         *
         */
        String SALE_PROPERTY_LESS_PUBISH = "发布过的spu，销售属性不能减少";
        /**
         *
         */
        String SALE_PROPERTY_VALUE_LESS_PUBISH = "发布过的spu，销售属性值不能减少";

        /**
         * 压缩文件超过最大限制
         */
        String ERROR_ZIP_FILE_OVERSIZE = "smpc.error.zip.file.oversize";

        /**
         * 压缩文件上传失败
         */
        String ERROR_UPLOAD_ZIP_FILE_FAILED = "smpc.error.upload.zip.file.failed";

        /**
         * 压缩文件后缀错误
         */
        String ERROR_ZIP_FILE_EXTENSION = "smpc.error.zip.file.extension";

        /**
         * 解压文件错误
         */
        String ERROR_UNZIP_FILE = "smpc.error.unzip.file";

        /**
         * 商品图片超过最大限制
         */
        String ERROR_SKU_IMAGE_OVERSIZE = "smpc.error.sku.image.oversize";

        /**
         * 商品图片格式不对
         */
        String ERROR_SKU_IMAGE_TYPE = "smpc.error.sku.image.type";

        /**
         * 单次上传图片数量超过限制
         */
        String ERROR_SKU_IMAGE_TOO_MANY = "smpc.error.sku.image.too.many";

        /**
         * 数据校验未通过，或已导入过
         */
        String ERROR_DATA_VALID_FAILED = "smpc.error.data.valid.failed";

        /**
         * 预警阈值不能为负数
         */
        String ERROR_WARNING_STOCK_NOT_NEGATIVE = "smpc.error.warning.stock.not.negative";
        /**
         * 商品图片命名错误，请检查
         */
        String ERROR_SKU_IMAGE_NAME_ERROR = "smpc.error.sku.image.name.error";

        /**
         * 商品未维护库存
         */
        String ERROR_SKU_NOT_EXIST_STOCK = "smpc.error.sku.not.exist.stock";

        /**
         * 创建目录失败
         */
        String ERROR_CREATE_DIRECTORY = "smpc.error.create.directory";

        /**
         * 已上架的商品需要下架后才能导入
         */
        String ERROR_SHELF_SKU_NOT_ALLOW_IMPORT = "smpc.error.shelf.sku.not.allow.import";
    }

    /**
     * 品牌常量类
     */
    interface Brand {

        /**
         * logo大小
         */
        long LOGO_MAX_SIZE = 1024 * 1024L;
        /**
         * logo宽度
         */
        int LOGO_MAX_WIDTH = 160;
        /**
         * logo高度
         */
        int LOGO_MAX_HEIGHT = 90;
        /**
         * 品牌logo上传目录
         */
        String PRODUCT_BRAND_LOGO = "product/brand/logo";

    }

    /**
     * 文件桶
     */
    interface Bucket {
        /**
         * 公共桶
         */
        String PUBLIC_BUCKET_NAME = "public-bucket";
        /**
         * 私有桶
         */
        String PRIVATE_BUCKET_NAME = "private-bucket";
    }

    /**
     * 埋点topic编码
     */
    interface BuryPoint {
        String BURY_POINT_CODE = "bury-point-code";
    }

    /**
     * 编码规则code
     */
    interface CodeRule {

        /**
         * 品牌编码规则
         */
        String CODE_RULE_BRAND_CODE = "SMPC.BRAND";
        /**
         * 属性编码规则
         */
        String CODE_RULE_ATTRIBUTE_CODE = "SMPC.ATTRIBUTE";
        /**
         * 分类编码规则
         */
        String CODE_RULE_CATEGORY_CODE = "SMPC.CATEGORY";
        /**
         * sku/spu编码规则
         */
        String CODE_RULE_SKU_CODE = "SMPC.SKU";
    }

    /**
     * 映射类型
     * SMPC.MAPPING_TYPE
     */
    interface MappingType {

        /**
         * 商品分类
         */
        String CATEGORY = "CATEGORY";
        /**
         * 目录
         */
        String CATALOG = "CATALOG";
        /**
         * 物料
         */
        String ITEM = "ITEM";
        /**
         * 物料品类
         */
        String ITEM_CATEGORY = "ITEM_CATEGORY";
    }

    /**
     * 目录
     */
    interface Catalog {

        /**
         * 目录启用
         */
        Integer CATALOG_ENABLED = 1;

        /**
         * 目录禁用
         */
        Integer CATALOG_DISABLED = 0;

        /**
         * 默认上级目录ID
         */
        Long DEFAULT_PARENT_CATALOG_ID = -1L;

        /**
         * 商品来源-手工
         */
        String SOURCE_FROM_MANUAL = "MANUAL";

        /**
         * 商品来源-分类
         */
        String SOURCE_FROM_CATEGORY = "CATEGORY";

    }

    /**
     * 业务规则配置
     */
    interface ConfigCenter {
        /**
         * 是否启目录化商品库存校验
         */
        String ENABLE_SKU_STOCK_CHECK = "SITE.SMPC.ENABLE_SKU_STOCK_CHECK";
    }

    /**
     * 导入模板编码
     */
    interface ImportCode {

        /**
         * 品牌导入编码
         */
        String BRAND_IMPORT_CODE = "SMPC.BRAND_IMPORT";
        /**
         * 属性导入
         */
        String ATTRIBUTE_IMPORT_CODE = "SMPC.ATTRIBUTE_IMPORT";
        /**
         * 属性导入
         */
        String ATTRIBUTE_VALUE_IMPORT_CODE = "SMPC.ATTRIBUTE_VALUE_IMPORT";
        /**
         * 目录物料映射
         */
        String CATALOG_ITEM_REF_IMPORT_CODE = "SMPC.CATALOG_ITEM_REF_IMPORT";

    }

    /**
     * 商品创建方式
     */
    interface SourceFromType {

        /**
         * 手工创建
         */
        String MANUAL = "MANUAL";
        /**
         * 基于协议创建
         */
        String AGREEMENT = "AGREEMENT";
        /**
         * 电商同步
         */
        String SYNCHRONIZE = "SYNCHRONIZE";
        /**
         * 电商同步
         */
        String SUPPLIER = "SUPPLIER";

    }


    /**
     * 启用标识
     */
    interface EnabledFlag {
        /**
         * 启用
         */
        Integer ENABLED_FLAG = 1;

        /**
         * 禁用
         */
        Integer DISABLED_FLAG = 0;
    }

    /**
     * sku状态
     */
    interface SkuStatus {

        /**
         * 新建
         */
        Integer NEW = 0;
        /**
         * 待审核
         */
        Integer WAITING = 1;
        /**
         * 审核拒绝
         */
        Integer REJECT = 2;
        /**
         * 生效
         */
        Integer VALID = 3;
        /**
         * 失效
         */
        Integer INVALID = 4;
        /**
         * 锁定
         */
        Integer LOCKED = 5;

    }

    /**
     * 业务规则定义编码
     */
    interface CnfCode {

        /**
         * 供应商可编辑商品模块
         */
        String PRODUCT_EDIT_MOD = "SITE.SMAL.PRODUCT.SUPPLIER_EDIT_MOD";

        /**
         * 商品审批业务编码
         */
        String PRODUCT_APPROVAL_METHOD = "SITE.SMAL.PRODUCT.PRODUCT_APPROVAL";

    }

    /**
     * 商品来源
     */
    interface SkuSourceFrom {
        /**
         * 电商
         */
        String SOURCE_FROM_EC = "EC";
        /**
         * 目录化
         */
        String SOURCE_FROM_CATA = "CATA";
    }

    /**
     * 协议价格类型
     */
    interface PriceType {
        /**
         * 阶梯价
         */
        String LADDER_PRICE = "LADDER_PRICE";
        /**
         * 固定价格
         */
        String REGULAR_PRICE = "REGULAR_PRICE";
        /**
         * 实时价格
         */
        String REAL_TIME_PRICE = "REAL_TIME_PRICE";
    }

    /**
     * Banner 状态
     */
    interface ShelfStatus {
        /**
         * 已上架
         */
        int SHELF = 1;

        /**
         * 待上架/已下架
         */
        int UNSHELF = 4;

        /**
         * 已失效
         */
        int EXPIRED = -1;

        /**
         * 手动下架
         */
        int MANUAL_UNSHELF = 2;

        /**
         * 上架失败
         */
        int SHELF_FAILED = 3;

        /**
         * 已解锁
         */
        int UNLOCK = 1;

        /**
         * 已锁定
         */
        int LOCK = 0;

        /**
         * 逻辑删除
         */
        int LOGIC_DELETE_FLAG = 1;

        /**
         * 默认逻辑删除标识
         */
        int DEFAULT_LOGIC_DELETE_FLAG = 0;
    }

    /**
     * 协议默认值
     */
    interface Agreement {

        /**
         * 协议自动创建商品 默认分类编码
         */
        String DEFAULT_PRODUCT_CATEGORY_CODE = "990000001101";

        String ALL = "全";
        /**
         * 全 描述
         */
        String ALL_DESC = "smpc.agreement.line.all";

        /**
         * 所有组织
         */
        String AGREEMENT_LINE_ALL_UNIT = "smpc.agreement.line.all.unit";

        /**
         * 所有区域
         */
        String AGREEMENT_LINE_ALL_REGION = "smpc.agreement.line.all.region";
        /**
         * 自动创建协议默认时间
         */
        String DEFAULT_DATE = "9999-12-31";
    }

    interface CacheCode {

        /**
         * 服务名
         */
        String SERVICE_NAME = "smal";
        /**
         * 商品推送锁
         */
        String PRODUCT_LOCK = "product_lock";
        /**
         * 电商账户缓存 key
         */
        String EC_CLIENT = "ec_client";
    }

    /**
     * 层级，适用于目录、分类等层级数据
     */
    interface Level {
        /**
         * 一级
         */
        int ROOT_LEVEL = 1;
        /**
         * 二级目录
         */
        int SECOND_LEVEL = 2;
        /**
         * 三级目录
         */
        int THIRD_LEVEL = 3;
    }

    /**
     * 商品图片导入状态
     */
    interface ImageImportStatus {
        /**
         * 新建
         */
        String NEW = "NEW";
        /**
         * 校验失败
         */
        String CHECK_ERROR = "CHECK_ERROR";
        /**
         * 校验成功
         */
        String CHECK_SUCCESS = "CHECK_SUCCESS";
        /**
         * 导入成功
         */
        String IMPORT_SUCCESS = "IMPORT_SUCCESS";
        /**
         * 导入失败
         */
        String IMPORT_ERROR = "IMPORT_ERROR";
    }

    /**
     * SKU 媒体类型
     */
    interface SkuMediaType {
        /**
         * 图片
         */
        Integer IMAGE = 0;
        /**
         * 视频
         */
        Integer VIDEO = 1;
        /**
         * url
         */
        Integer URL = 2;
    }

    /**
     * 消息发送配置编码&消息模板参数
     */
    interface ProductMessage {
        /**
         * 消息发送参数：租户id
         */
        String ORGANIZATION_ID = "organizationId";
        /**
         * 消息模板配置参数：当前时间
         */
        String CURRENT_TIME = "CURRENT_TIME";
        /**
         * 商城库存预警阈值提醒
         */
        String WARNING_STOCK_REMIND = "SMPC.WARNING_STOCK_REMIND";

        /**
         * 商城库存预警阈值提醒-sku编码
         */
        String SKU_CODES = "skuCodes";
    }

    /**
     * 库存记录操作编码
     */
    interface SkuStockOperationCode {
        /**
         * 扣减
         */
        String DEDUCTION = "DEDUCTION";
        /**
         * 调整
         */
        String ADJUST = "ADJUST";
    }

    /**
     * 库存记录操作类型
     */
    interface SkuStockOperationType {
        /**
         * 维护
         */
        String MAINTAIN = "MAINTAIN";
        /**
         * 下单
         */
        String ORDER = "ORDER";
    }

    /**
     * 分布式事务相关常量
     */
    interface DistributeTransactionMethodKey {

        /**
         * 库存扣减
         */
        String STOCK_DEDUCTION = "SMPC-SKUSTOCKSERVICEIMPL-STOCKDEDUCTION";
    }
}
