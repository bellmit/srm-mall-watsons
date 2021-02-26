package org.srm.mall.common.feign.dto.product;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.hzero.core.base.BaseConstants;
import org.srm.boot.platform.configcenter.ConfigCenterHelper;

import java.io.File;
import java.util.Objects;

/**
 * SAMG服务-销售协议常量
 *
 * @author fu.ji@hand-china.com 2019年2月21日下午3:22:16
 */
public class PurConstants {

    /**
     * 服务缩写
     */
    public static final String SERVICE_NAME = "SAGM";
    /**
     * 变更执行条数
     */
    public static final int CHANGE_EXECUTE_SIZE = 10000;

    public final class AgreementStatus {
        private AgreementStatus() {
        }

        /**
         * 新建
         */
        public static final String NEW = "NEW";

        /**
         * 已提交,待审核
         */
        public static final String SUBMITTED = "SUBMITTED";

        /**
         * 审核通过
         */
        public static final String APPROVED = "APPROVED";

        /**
         * 审核拒绝
         */
        public static final String REJECT = "REJECT";

        /**
         * 已失效
         */
        public static final String DISABLED = "DISABLED";

        /**
         * 已终止
         */
        public static final String TERMINATED = "TERMINATED";

        /**
         * 已发布
         */
        public static final String PUBLISHED = "PUBLISHED";

        /**
         * 待审批
         */
        public static final String WAITING = "WAITING";

        /**
         * 无需审批
         */
        public static final String PASS = "PASS";

        /**
         * 升级
         */
        public static final String UPGRADE = "UPGRADE";

    }

    /**
     * 商品发送事件编码
     */
    public static final String EVENT_CODE = "MALL_PRODUCT";


    /**
     * 商品默认图片路径
     */
    public static final String DEFAULT_IMAGE = System.getProperty("user.dir") + File.separator + "target" +
            File.separator + "classes" + File.separator + "image" + File.separator + "defaultImage.png";

    /**
     * 商品默认图片路径
     */
    public static final String EXCEL_PATH = System.getProperty("user.dir") + File.separator + "target" +
            File.separator + "classes" + File.separator + "excel" + File.separator + "order.xls";

    /**
     * 常量数字
     */
    public final class ConstantNumber {
        private ConstantNumber() {
        }

        public static final int INT_MINUS_1 = -1;
        public static final int INT_0 = 0;
        public static final int INT_1 = 1;
        public static final int INT_2 = 2;
        public static final int INT_3 = 3;
        public static final int INT_4 = 4;
        public static final int INT_9 = 9;
        public static final int INT_10 = 10;
        public static final int INT_15 = 15;
        public static final int INT_20 = 20;
        public static final int HUNDRED = 100;
        public static final long LONG_200 = 200L;
        public static final String STRING_0 = "0";
        public static final String STRING_MINUS_1 = "-1";
        public static final String STRING_1 = "1";
        public static final String STRING_2 = "2";
        public static final String STRING_3 = "3";
        public static final String STRING_00 = "00";
        public static final String STRING_02 = "02";
        public static final String STRING_05 = "05";
        public static final String STRING_07 = "07";
        public static final String STRING_08 = "08";
        public static final String STRING_99 = "99";
        public static final String STRING_200 = "200";
        public static final String STRING_0000 = "0000";
        public static final long LONG_MINUS_1 = -1L;
        public static final long LONG_0 = 0L;
        public static final long LONG_1 = 1L;
        public static final long LONG_2 = 2L;
        public static final long LONG_3 = 3L;
        public static final long LONG_99999 = 99999L;
        public static final byte BYTE_0 = 0;
        public static final byte BYTE_1 = 1;
        public static final byte BYTE_2 = 2;
        public static final byte BYTE_3 = 3;
        public static final byte BYTE_4 = 4;
        public static final byte BYTE_5 = 5;
        public static final byte BYTE_6 = 6;
        public static final byte BYTE_7 = 7;
        public static final byte BYTE_8 = 8;
        public static final byte BYTE_9 = 9;
    }

    /**
     * 价格服务 查询价格类型常量
     */
    public final class SagmSourceCode {
        private SagmSourceCode() {

        }

        /**
         * 订单中心
         */
        public static final String ORDER = "order";
        /**
         * 购物车
         */
        public static final String SHOPPING_CART = "shopping-cart";
        /**
         * 搜索
         */
        public static final String ES_SEARCH = "es-search";
        /**
         * 商祥
         */
        public static final String SKU_DETAILS = "sku-details";

        /**
         * 购物篮
         */
        public static final String MARKET_BASKET = "market-basket";
        /**
         * 比价单
         */
        public static final String PRODUCT_COMPARE = "product-compare";
        /**
         * 同款商品
         */
        public static final String PRODUCT_SIMILAR = "product-similar";

    }

    /**
     * 类目属性相关常量数字
     */

    public final class CategoryConstantNumber {
        private CategoryConstantNumber() {
        }

        public static final String HANDLERUP = "1";
        public static final String HANDLERDOWN = "2";
        public static final String HANDLERTOP = "3";
        public static final String HANDLERBOTTOM = "4";
        public static final String FIRSTLEV = "1";
        public static final String SECONDLEV = "2";
        public static final String THIRDLEV = "3";
    }

    /**
     * 目录类型
     */
    public final class CatalogType {
        private CatalogType() {
        }

        public static final String PLATFORM = "PLATFORM";
        public static final String COMPANY = "COMPANY";
        public static final String TENANT = "TENANT";
    }

    /**
     * 商品来源类型
     */
    public final class SourceType {
        public static final String JD = "JD";

        private SourceType() {
        }

        public static final String EC = "EC";
        public static final String CATALOGUE = "CATA";
        public static final String PUNCHOUT = "PUNCHOUT";
        public static final String NJD = "NJD";
        public static final String MISUMI = "MISUMI";
        public static final String YSW = "YSW";

        /**
         * 用于销量修改(目录化)
         */
        public static final String SALES_CATALOGUE = "CATALOGUE";

        /**
         * 用于销量修改(电商)
         */
        public static final String E_COMMERCE = "E-COMMERCE";

    }

    /**
     * Banner附件上传桶名
     */
    public final class BannerBucketName {
        private BannerBucketName() {
        }

        public static final String PUBLIC = "public";
        public static final String SCEC_BANNER = "scec-banner";
    }

    /**
     * Banner操作动作,SCEC.BANNER_OPERATION（NEW新建|EDIT编辑|PUT_ON上架|PULL_OFF下架）
     */
    public final class BannerOperationCode {
        private BannerOperationCode() {
        }

        public static final String NEW = "NEW";
        public static final String EDIT = "EDIT";
        public static final String PUT_ON = "PUT_ON";
        public static final String PULL_OFF = "PULL_OFF";
        public static final String ASSIGN = "ASSIGN";
        public static final String DEASSIGN = "DEASSIGN";
        public static final String ENABLE = "ENABLE";
        public static final String DISABLE = "DISABLE";
    }

    /**
     * 自定义栏状态操作动作,SCEC.BANNER_OPERATION（NEW新建|EDIT编辑|PUT_ON上架|PULL_OFF下架）
     */
    public final class CustomBarOperationCode {
        private CustomBarOperationCode() {
        }

        public static final String NEW = "NEW";
        public static final String EDIT = "EDIT";
        public static final String PUT_ON = "PUT_ON";
        public static final String PULL_OFF = "PULL_OFF";
        public static final String ASSIGN = "ASSIGN";
        public static final String DEASSIGN = "DEASSIGN";
    }

    /**
     * 购物篮启用信息
     */
    public final class ChangeFlagType {
        private ChangeFlagType() {
        }

        public static final String DATE_TIME_OUT = "购物篮已失效，不能启用";
        public static final String PRODUCT_IS_NULL = "商品为空，不能启用";


    }

    /**
     * 请求返回状态码
     */
    public final class ResultCode {
        private ResultCode() {
        }

        /**
         * 操作成功
         */
        public static final String OPERATION_SUCCESS = "0000";
        public static final String SUCCESS_CODE = "00";
        public static final String SUCCESS_MSG = "成功";
        public static final String SUCCESS_OK = "OK";
        public static final boolean SUCCESS = true;

    }


    /**
     * 价格库商品匹配
     */
    public final class PriceLibMatch {
        private PriceLibMatch() {
        }

        /**
         * 有阶梯价格
         */
        public static final int HAVE_LADDER_PRICE = 1;

        /**
         * 默认有效期至
         */
        public static final String DEFAULT_DATE = "2099-12-31";

        /**
         * 有效
         */
        public final static String VALID = "VALID";
        /**
         * 无效
         */
        public final static String IN_VALID = "IN_VALID";
        /**
         * 已过期
         */
        public final static String EXPIRE = "EXPIRE";

    }

    /**
     * 价格适用范围头表
     */
    public final class DimensionCode {
        /**
         * 公司维
         */
        public static final String COMPANY = "companyId";

        /**
         * 业务实体
         */
        public static final String OU = "ouId";

        /**
         * 库存组织维度
         */
        public static final String INV_ORGANIZATION = "invOrganizationId";
    }


    /**
     * 账号管理配置
     */
    public final class EcClientValue {
        private EcClientValue() {
        }

        /**
         * 运费code
         */
        public static final String FREIGHT_TYPE = "FREIGHT_TYPE";

        /**
         * 运费单列
         */
        public static final String FREIGHT_LIST = "FREIGHT_LIST";

    }


    /**
     * 地址等级
     */
    public final class RegionLevelType {
        private RegionLevelType() {
        }

        /**
         * 省
         */
        public static final String PROVINCE = "provinceId";
        /**
         * 市
         */
        public static final String CITY = "cityId";
        /**
         * 县
         */
        public static final String COUNTY = "countyId";
        /**
         * 乡镇
         */
        public static final String TOWN = "townId";

    }

    /**
     * 页面个性化
     */
    public final class PagePersonalization {
        private PagePersonalization() {
        }

        /**
         * 需求创建menu code，用于预采购申请获取个性化字段
         */
        public static final String PR_CREATION_MENU_CODE = "hzero.srm.requirement.prm.pr-creation";

        /**
         * 需求创建采购申请头menu code
         */
        public static final String PR_HEADER_CODE = "SPRM.PURCHASE_REQUISITION_CREATION.DETAIL_HEADER";

        /**
         * 需求创建采购申请行-目录化与SRM来源menu code
         */
        public static final String PR_LINE_CATA_CODE = "SPRM.PURCHASE_REQUISITION_CREATION.DETAIL_LINE";

        /**
         * 需求创建采购申请行-电商menu code
         */
        public static final String PR_LINE_EC_CODE = "SPRM.PURCHASE_REQUISITION_CREATION.DETAIL.LINE_ECOMMERCE";

        /**
         * 扩展字段
         */
        public static final String CUST_TYPE = "EXT";

        /**
         * 是否必输，1 必输
         */
        public static final int FIELD_REQUIRED = 1;

        /**
         * lov类型
         */
        public static final String CUST_LOV = "LOV";

        /**
         * 查询lov配置需要的参数
         */
        public static final String VIEW_CODE = "viewCode";

        /**
         * 采购员字段
         */
        public static final String PURCHASE_AGENT_FIELD_CODE = "purchaseAgentId";

        /**
         * 采购员字段名称
         */
        public static final String PURCHASE_AGENT_FIELD_NAME = "采购员";

        /**
         * 采购员值集
         */
        public static final String PURCHASE_AGENT_LOV_CODE = "SPUC.PURCHASE_AGENT";

        public static final String PURCHASE_PARAM_TENANT = "tenantId";

        public static final String PURCHASE_PARAM_PURCHASE_ORG = "purchaseOrgId";

        public static final String PURCHASE_PARAM_TYPE_CONTEXT = "context";

    }


    /**
     * 业务异常
     */
    public final class ErrorCode {
        public static final String AFTER_SALE_IS_EXIST = "after.sale.is.exist";
        public static final String COMPANY_MUST_NOT_NULL = "company.must.not.null";
        public static final String COMPANY_NOT_EXIT = "company.not.exit";
        public static final String INV_ORGANIZATION_NOT_EXIT = "inv.organization.not.exit";
        public static final String ACCOUNT_ACTIVE_ERROR = "account.active.error";
        /**
         * 快照异常
         */
        public static final String SNAPSHOT_ERROR = "snapshot.error";

        private ErrorCode() {
        }

        /**
         * 商品价格不一致
         */
        public static final String ERROR_INCONSISTENT_PRODUCT_PRICE = "error.inconsistent.product.price";

        /**
         * 商品总价计算错误
         */
        public static final String ERROR_PRODUCT_TOTAL_PRICE_CALCULATED_INCORRECTLY = "error.product.total.price.calculated.incorrectly";
        /**
         * 电商账号不存在
         */
        public static final String EC_CLINET_NOT_EXISTS = "ec.client.not.found.error";
        /**
         * 没有权限购买
         */
        public static final String NO_PERMISSION_TO_BUY = "product.buying.permission.error";

        /**
         * 建立合作伙伴关系失败
         */
        public static final String BUILD_PARTNERSHIPS_FAILED = "build.partnerships.failed";

        /**
         * 数据异常：该电商分类已存在映射
         */
        public static final String ERROR_CATEGORY_MAP_ALREADY_EXIST = "error.category.map.already.exist";

        /**
         * 数据异常：该SKU已存在映射
         */
        public static final String ERROR_SKU_MAP_ALREADY_EXIST = "error.sku.map.already.exist";

        /**
         * 该物料和目录映射关系已存在
         */
        public static final String ERROR_ITEM_MAP_ALREADY_EXIST = "error.item.map.already.exist";

        /**
         * 数据异常：商品日期存在重叠
         */
        public static final String ERROR_PRODUCT_DATE_OF_OVERLAP = "error.product.date.of.overlap";

        /**
         * 数据异常：协议提交状态错误
         */
        public static final String ERROR_AGREEMENT_STATUS = "error.agreement.status";


        /**
         * 数据为空
         */
        public static final String ERROR_EMPTY_DATA = "error.empty.data";

        /**
         * 数据异常：已存在默认地址
         */
        public static final String ERROR_ADDRESS_DEFAULT = "error.default_address";


        /**
         * 数据异常：默认地址不存在
         */
        public static final String ERROR_ADDRESS_DEFAULT_NOT_EXISTS = "error.default_address_not_exists";

        /**
         * 地址为禁用状态不可改为默认地址
         */
        public static final String DISABLE_ADDRESS_CANNOT_DEAFULT = "disabled.address.cannot.deafult";

        /**
         * 地址没有维护到最下级
         */
        public static final String ADDRESS_NOT_LOWEST = "address.not.lowest";

        /**
         * 数据异常：目录编码已存在
         */
        public static final String ERROR_CATALOG_CODE_EXITED = "error.catalog_code.exited";
        /**
         * 目录上级未启用
         */
        public static final String PARENT_CATALOG_NOT_ENABLE = "parent.catalog.not.enable";
        /**
         * 租户配置里面是是禁用状态公司无法启用
         */
        public static final String TENANT_CATALOG_DISABLE = "tenant.catalog.disable";

        public static final String QUERY_LOCAL_CACHE_REGION_ERROR = "query.local.cache.region.error";

        /**
         * 商品编码为空
         */
        public static final String PRODUCT_NUM_NULL_ERROR = "product.num.null.error";

        /**
         * 商品未提交
         */
        public static final String PRODUCT_NOT_SUBMITTED = "product.not.submitted";

        /**
         * 商品未审批
         */
        public static final String PRODUCT_NOT_APPROVED = "product.not.approved";

        /**
         * 商品未上架
         */
        public static final String PRODUCT_NOT_SHELF = "product.not.shelf";

        /**
         * 商品已上架
         */
        public static final String PRODUCT_ALREADY_SHELF = "product.already.shelf";

        /**
         * 商品未锁定
         */
        public static final String PRODUCT_NOT_LOCK = "product.not.lock";

        /**
         * 商品已锁定
         */
        public static final String PRODUCT_ALREADY_LOCK = "product.already.lock";

        /**
         * 商品目录已禁用
         */
        public static final String PRODUCT_CATALOG_DISABLED = "product.catalog.disabled";

        /**
         * 商品目录不存在
         */
        public static final String PRODUCT_CATALOG_NOT_EXISTS = "product.catalog.not.exists";

        /**
         * 电商账户未找到
         */
        public static final String ECCLIENT_NOT_FOUND_ERROR = "query.ecclient.not.found.error";

        /**
         * 获取JD Token失败
         */
        public static final String INVOKE_JD_OAUTH_ERROR = "invoke.jd.oauth.error";

        /**
         * 未找到京东公司账户关联关系
         */
        public static final String EC_COMPANY_ASSIGN_NOT_FOUND_ERROR = "ec.company.assign.not.found.error";

        /**
         * 电商商品采购数量大于最大采购量
         */
        public static final String EC_PRODUCT_MAX_PURCHASE_QUANTITY_ERROR = "ec.product.max.purchase.quantity.error";

        /**
         * 目录化商品不满足最小采购数量
         */
        public static final String CATA_PRODUCT_MINIMUM_PURCHASE_QUANTITY_ERROR =
                "cata.product.minimum.purchase.quantity.error";

        /**
         * 账户余额不足
         */
        public static final String ACCOUNT_BALANCE_NOT_ENOUGH_ERROR = "account.balance.not.enough.error";

        public static final String PRE_REQUEST_ORDER_FAILED = "create.pre.request.order.failed";

        public static final String PRE_REQUEST_EC_ORDER_FAILED = "create.pre.request.ec.order.failed";

        public static final String PRODUCT_IS_NULL_ERROR = "product.is.null.error";

        /**
         * 商品不存在
         */
        public static final String PRODUCT_NOT_EXISTS = "small.product.not.exists";

        /**
         * 电商商品不存在
         */
        public static final String EC_PRODUCT_NOT_EXISTS = "ec.product.not.exists";

        /**
         * 默认收单地址不存在
         */
        public static final String DEFAULT_INVOICE_ADDRESS_ERROR = "default.invoice.address.error";

        /**
         * 供应商获取失败
         */
        public static final String SUPPLIER_QUERY_ERROR = "supplier.query.error";

        /**
         * 创建采购申请失败
         */
        public static final String CREATE_PURCHASE_REQUISITION_ERROR = "create.purchase.requisition.error";

        /**
         * 资源请求为空
         */
        public static final String REPOSITORY_RETURN_NULL = "repository.return.null";

        /**
         * 请求页不在1-100之间
         */
        public static final String PAGE_NUM_CROSSING = "Request.page.number.not.in.1-100";

        /**
         * 商品不存在
         */
        public static final String INFORMATION_STATISTICS_FAILED = "product.Information.statistics.failed";

        /**
         * 开票机构ID为空错误
         */
        public static final String INVOICE_ORG_NULL_ERROR = "invoice.org.null.error";

        /**
         * 获取电子发票信息错误
         */
        public static final String GET_ELEC_INVOICE_ERROR = "get.elec.invoice.error";
        /**
         * 获取自订单头Id错误
         */
        public static final String QUERY_EC_PO_SUB_HEADER_ID_ERROR = "query.ec.po.sub.header.id.error";
        /**
         * 发票抬头为空错误
         */
        public static final String INVOICE_HEADER_NULL_ERROR = "invoice.header.null.error";
        /**
         * 电商子订单号不能为空
         */
        public static final String EC_ORDER_NUM_NULL_ERROR = "ec.order.num.null.error";

        /**
         * 有效日期不存在
         */
        public static final String DURATION_OF_RESPONSE_NOT_EXISTS = "duration.of.response.not.exists";

        /**
         * 有效日期以失效
         */
        public static final String DURATION_OF_RESPONSE_IS_INVALID = "duration.of.response.is.invalid";

        /**
         * 类型转换错误
         */
        public static final String TYPE_CONVERSION_ERROR = "Type.conversion.error";

        /**
         * Feign调用失败
         */
        public static final String FEIGN_INVOKE_ERROR = "feign.invoke.error";

        /**
         * 库存查询失败
         */
        public static final String FEIGN_QUERY_STOCK_ERROR = "feign.query.stock.error";

        /**
         * 是否可售查询失败
         */
        public static final String FEIGN_QUERY_SALE_CHECK_ERROR = "feign.query.sale.check.error";

        /**
         * Feign调用失败
         */
        public static final String ELASTIC_SEARCH_REQUEST_ERROR = "elastic.search.request.error";

        /**
         * 请修改后再提交
         */
        public static final String PLEASE_MODIFY_AND_SUBMIT_AGAIN = "please.modify.and.submit.again";

        /**
         * 商品不可用不能提交
         */
        public static final String PRODUCT_ENABLE_FLAG_IS_FALSE = "product.enable.flag.is.false";

        /**
         * 开始时间不能晚于截止时间
         */
        public static final String START_DATE_CANNOT_LATER_THEN_END_DATE = "start.date.cannot.later.then.end.date";

        /**
         * 开始时间或者截止时间为空
         */
        public static final String START_DATE_OR_END_DATE_IS_NULL = "start.date.or.end.date.is.null";

        /**
         * 只能在截止日期前进行上架
         */
        public static final String ONLY_UPSHELF_BEFORE_END_DATE = "only.upshelf.before.end.date";

        /**
         * 调用电商订单取消接口错误
         */
        public static final String INVOKE_EC_ORDER_CANCEL_ERROR = "invoke.ec.order.cancel.error";

        /**
         * 取消采购申请失败
         */
        public static final String PURCHASE_REQUEST_CANCELLATION_ERROR = "purchase.request.cancellation.error";

        /**
         * LOV查询失败
         */
        public static final String LOV_QUERY_ERROR = "lov.query.error";

        /**
         * LOV不存在
         */
        public static final String LOV_NOT_EXIST = "lov.not.exist";

        /**
         * 值集tag未维护
         */
        public static final String LOV_TAG_NOT_EXSIT = "lov.tag.not.exist";

        /**
         * 值集值不存在
         */
        public static final String LOV_VALUE_NOT_EXIST = "lov.value.not.exist";

        /**
         * 获取京东订单信息失败
         */
        public static final String QUERY_JD_ORDER_ERROR = "query.jd.order.error";

        /**
         * 获取订单信息失败
         */
        public static final String QUERY_ORDER_ERROR = "query.order.error";

        /**
         * 电商订单不存在
         */
        public static final String EC_PURCHASE_ORDER_NOT_EXIST = "ec.purchase.order.not.exist";

        /**
         * 订单不存在
         */
        public static final String ORDER_NOT_EXIST = "order.not.exist";

        /**
         * 电商订单商品已全部发运,无法继续发运
         */
        public static final String EC_PURCHASE_ORDER_DESPATCH_COMPELETED = "ec.purchase.despatch.compeleted";
        /**
         * 电商订单非待收货、确认收货状态不可妥投
         */
        public static final String EC_PURCHASE_ORDER_NOT_ALLOWED = "ec.purchase.order.not.allowed";

        /**
         * 电商子订单不存在
         */
        public static final String EC_SUB_PURCHASE_ORDER_NOT_EXIST = "ec.sub.purchase.order.not.exist";

        /**
         * 租户未维护所需税率
         */
        public static final String CURRENT_TENANT_TAX_NOT_EXIST = "current.tenant.tax.not.exist";

        /**
         * 商品税率不存在
         */
        public static final String PRODUCT_TAX_NOT_EXIST = "product.tax.not.exist";


        /**
         * 商品币种不存在
         */
        public static final String PRODUCT_CURRENCY_CODE_NOT_EXIST = "product.currency.code.not.exist";


        /**
         * 商品单位不存在
         */
        public static final String PRODUCT_UOM_NAME_NOT_EXIST = "product.uom.name.not.exist";

        /**
         * 销售单位为空
         */
        public static final String PRODUCT_UOM_NAME_NULL = "error.product.uom.name.null";


        /**
         * 商品暂不可售
         */
        public static final String EC_PRODUCT_NOT_MARKETABLE = "商品暂时不可售";

        /**
         * 商品不满足最小采购量
         */
        public static final String EC_PRODUCT_MINIMUM_PURCHASE_ERROR = "small.ec.product.minimum.purchase.error";

        /**
         * Banner 上架数量上限
         */
        public static final String BANNER_SHELF_CEILING = "banner.shelf.ceiling";

        /**
         * 单商品Banner商品数量限制
         */
        public static final String SINGLE_BANNER_PRODUCT_LIMIT = "single.banner.product.limit";

        /**
         * 多商品Banner商品数量限制
         */
        public static final String MULTI_BANNER_PRODUCT_LIMIT = "multi.banner.product.limit";

        /**
         * 纯图片Banner商品数量限制
         */
        public static final String IMAGE_BANNER_PRODUCT_LIMIT = "image.banner.product.limit";

        /**
         * 库存组织被禁用
         */
        public static final String INV_ORGANIZATION_ENABLED_FLAG_ERROR = "inv_organization.enabledflag.error";
        /**
         * 采购组织已被禁用
         */
        public static final String PURCHASE_ORGANIZATION_ENABLED_FLAG_ERROR = "purchase.organization.enabledFlag.error";
        /**
         * 地址已被禁用
         */
        public static final String ADDRESS_ENABLED_FLAG = "address.enabled.flag";
        /**
         * 采购组织已被禁用
         */
        public static final String PURCHASE_ORGANIZATION_FINDED_FLAG_ERROR = "purchase.organization.findedFlag.error";

        /**
         * 商品价格获取失败
         */
        public static final String EC_PRODUCT_PRICE_ERROR = "商品价格获取失败";

        /**
         * 本地缓存命中失败
         */
        public static final String LOCAL_CACHE_ERROR = "local.cache.error";

        /**
         * 电商订单取消检验失败
         */
        public static final String EC_ORDER_CANCEL_VALIDATE_ERROR = "ec.order.cancel.error";

        /**
         * 电商订单提交检验失败
         */
        public static final String EC_ORDER_SUBMIT_VALIDATE_ERROR = "ec.order.submit.validate.error";

        /**
         * 电商订单确认检验失败
         */
        public static final String EC_ORDER_CONFIRM_VALIDATE_ERROR = "ec.order.confirm.validate.error";

        /**
         * 业务实体被禁用
         */
        public static final String OPERATION_UNIT_BE_DISABLED = "operation_unit.be.disabled";

        /**
         * SRM组织信息子公司被禁用
         */
        public static final String SRM_COMPANY_BE_DISABLED = "srm.company.be.disabled";

        /**
         * 商品已下架
         */
        public static final String PRODUCT_REMOVED_FROM_SHELVES = "product.removed.from.shelves";

        /**
         * 商品已下架或在默认收货地址不可售
         */
        public static final String PRODUCT_CANNOT_SELL = "product.cannot.sell";

        /**
         * 该区域暂不可售
         */
        public static final String THE_REGION_NOT_FOR_SALE = "region.not.for.sale";
        /**
         * 商品不存在或已下架
         */
        public static final String PRODUCT_DOES_NOT_EXIST_OR_HAS_BEEN_REMOVED_FROM_SHELVES = "small.product.does.not.exist.or.has.been.removed.from.shelves";

        /**
         * 商品已失效
         */
        public static final String PRODUCT_HAVE_EXPIRED = "product.have.expired";

        /**
         * 商品已锁定
         */
        public static final String PRODUCT_LOCKED = "product.locked";

        /**
         * 收货/发票地址信息错误
         */
        public static final String ADDRESS_INFO_ERROR = "address.info.error";

        /**
         * 获取物料相关信息错误
         */
        public static final String GET_ITEM_INFO_ERROR = "get.item.info.error";

        /**
         * 订单服务创建送货单失败
         */
        public static final String CREATE_ASN_ERROR = "create.asn.error";

        /**
         * 查询商城订单错误
         */
        public static final String QUERY_MALL_PO_ERROR = "query.mall.po.error";

        /**
         * 京东售后单取消失败
         */
        public static final String JD_AFTER_SALE_CANCEL_FAILED = "jd.after.sale.cancel.failed";

        /**
         * 京东售后单确认失败
         */
        public static final String JD_AFTER_SALE_CONFIRM_FAILED = "jd.after.sale.confirm.failed";

        /**
         * srm订单不存在
         */
        public static final String EC_PO_NUM_NOT_EXIST = "ec_po_num.not.exist";

        /**
         * 缺少商品ID
         */
        public static final String NO_PRODUCT_ID = "no.product.id";

        /**
         * 阶梯等级区间段值错误 上一行号数量至的值必须等于下一行号数量从的值
         */
        public static final String ERROR_LADDER_INTERVAL_VALUE_NOT_EQUALS = "ladder.interval.value.not.equals";

        /**
         * 阶梯等级数量从需小于数量至
         */
        public static final String ERROR_LADDER_FROM_MORE_THAN_TO = "error.ladder.from.more.than.to";
        /**
         * 数据转换异常
         */
        public static final String ERROR_DATA_TRANS = "error.data.trans";
        /**
         * 获取租户ID失败
         */
        public static final String ERROR_GET_TENANT_IDS = "error.get.tenant.ids";

        /**
         * 获取公司ID失败
         */
        public static final String ERROR_GET_COMPANY_IDS = "error.get.company.ids";

        /**
         * 目录层级错误
         */
        public static final String ERROR_CATALOG_LEVEL = "error.catalog.level";

        /**
         * 获取上级目录错误
         */
        public static final String ERROR_GET_PARENT_CATALOG = "error.get.parent.catalog";

        /**
         * 非直属上级目录
         */
        public static final String ERROR_DIRECTLY_PARENT_CATALOG = "error.directly.parent.catalog";


        /**
         * 该电商不存在
         */
        public static final String ERROR_NO_EC_EXIST = "error.no.ec.exist";

        /**
         * 品牌不存在
         */
        public static final String ERROR_BRAND_NO_EXIST = "error.brand.no.exist";
        /**
         * 该电商分类名称不存在
         */
        public static final String ERROR_NO_EC_CATEGORY_NAME_EXIST = "error.no.ec.category.name.exist";
        /**
         * 该分类名称不存在
         */
        public static final String ERROR_NO_CATEGORY_NAME_EXIST = "error.no.category.name.exist";

        /**
         * 该目录不存在
         */
        public static final String ERROR_NO_CATALOGUE_EXIST = "error.no.catalogue.exist";
        /**
         * 为非三级目录
         */
        public static final String ERROR_NOT_THREE_CATALOGUE = "error.not.three.catalogue";
        /**
         * 未分配
         */
        public static final String ERROR_NOT_ALLOCATE = "error.not.allocate";
        /**
         * 获取租户id失败
         */
        public static final String ERROR_GET_TENANT_ID = "error.get.tenant.id";
        /**
         * 不在当前租户组织架构下
         */
        public static final String ERROR_NOT_IN_TENANT_ORG = "error.not.in.tenant.org";
        /**
         * 不在当前公司下
         */
        public static final String ERROR_NOT_IN_THIS_COMPANY = "error.not.in.this.company";
        /**
         * 不在当前库存组织中
         */
        public static final String ERROR_NOT_IN_THIS_INV_ORG = "error.not.in.this.inv.org";
        /**
         * 物料和品类只能选填其中一个
         */
        public static final String ERROR_ONLY_ONE_IN_ITEM_OR_CATEGORY = "error.only.one.in.item.or.category";
        /**
         * 物料和品类必须选填一个
         */
        public static final String ERROR_MUST_ONE_IN_ITEM_OR_CATEGORY = "error.must.one.in.item.or.category";
        /**
         * 映射物料数据来源系统必填
         */
        public static final String ERROR_MAP_ITEM_SOURCE_CODE_REQUIRED = "error.map.item.source.code.required";
        /**
         * 不存在
         */
        public static final String ERROR_NOT_EXIST = "error.not.exist";
        /**
         * 找不出对应品类信息
         */
        public static final String ERROR_CAN_NOT_FIND_CATEGORY_INFO = "error.can.not.find.category.info";
        /**
         * 该映射关系已存在
         */
        public static final String ERROR_THE_MAPPING_RELATIONSHIP_ALREADY_EXISTS = "error.the.mapping.relationship.already.exists";
        /**
         * 找不出对应物料信息
         */
        public static final String ERROR_CAN_NOT_FIND_ITEM_INFO = "error.can.not.find.item.info";
        /**
         * 未找到对应分类
         */
        public static final String ERROR_CAN_NOT_FIND_CATEGORY = "error.can.not.find.category";

        /**
         *
         */
        public static final String ERROR_REFRESH_SHOPPINGCAR = "error.refresh.shoppingcar";

        /**
         * 确认收货
         */
        public static final String EC_ORDER_CONFIRM_ERROR = "非收货订单不可收货";

        /**
         * 订单已超过评价期限
         */
        public static final String ERROR_CAN_NOT_ASSESSMENT = "error.can.not.assessment";

        /**
         * 最小采购量不能小于最小阶梯数量
         */
        public static final String MIN_QUANTITY_MUST_MORE_THAN_MIN_LADDERPRICE = "min.quantity.must.more.than.min.ladderprice";

        /**
         * 预约取货时间不能为空
         */
        public static final String RESERVE_TIME_NOT_EXIST = "error.reserve.time.not.exist";

        /**
         * 商品审核类目为空
         */
        public static final String PRODUCT_AUDIT_CATEGORY_IS_NULL = "商品审核规则分类不能为空";

        /**
         * 商品审核数据不存在
         */
        public static final String PRODUCT_AUDIT_IS_NULL = "商品审核数据不存在";

        /**
         * 个人选买目录化商品不可购
         */
        public static final String CATA_PRODUCT_NOT_PUR = "个人选买目录化商品不可购";

        /**
         * 没有基础数据不可配置
         */
        public static final String ES_DATA_NOT_ERROR = "es.data.can.not.find";

        /**
         * 集团排序号不合法
         */
        public static final String GROUP_BANNER_ORDER_SEQ_ILLEGAL = "group.banner.order.seq.illegal";

        /**
         * 公司排序号不合法
         */
        public static final String COMPANY_BANNER_ORDER_SEQ_ILLEGAL = "company.banner.order.seq.illegal";

        /**
         * 排序号重复
         */
        public static final String ORDER_SEQ_REPEAT = "order.seq.repeat";


        /**
         * 集团banner上架且启用数量已满
         */
        public static final String GROUP_BANNER_FULL_ERROR = "group.banner.full.error";

        /**
         * 商品匹配协议状态异常
         */
        public static final String PRODUCT_MATCH_STATUS_ERROR = "product_match_status.error";

        /**
         * 商品匹配协议明细状态异常
         */
        public static final String PRODUCT_MATCH_DETAILS_STATUS_ERROR = "product_match_details_status.error";

        /**
         * 取消SRM订单失败
         */
        public static final String PURCHASE_ORDER_REQUEST_CANCEL_ERROR = "purchase.request.cancel.error";

        /**
         * 未找到该订单号物流信息
         */
        public static final String NOT_FOUND_LOGISTICS_INFO = "not.found.logistics.info";

        /**
         * sku不存在
         */
        public static final String SKU_NOT_FOUND_ERROR = "sku.not.found.error";

        /**
         * 无该目录权限
         */
        public static final String CATALOG_PERMISSION_NOT_PASS = "catalog.permission.not.pass";

        /**
         * 创建采购申请单失败
         */
        public static final String CREATE_PURCHASE_REQUEST_ERROR = "create.purchase.request.error";

        /**
         * 品牌导入错误
         */
        public static final String CREATE_BRAND_ERROR = "create.brand.error";

        /**
         * 品牌导入错误 品牌code不能为空
         */
        public static final String BRAND_CODE_NOT_NULL = "brand.code.not.null";

        /**
         * 品牌导入错误中文名称不能为空
         */
        public static final String BRAND_NAME_NOT_NULL = "brand.name.not.null";

        /**
         * 该平台分类已存在
         */
        public static final String ERROR_PLATFORM_CATEGORY_EXISTS = "该平台分类已存在";

        /**
         * 平台分类层级错误
         */
        public static final String ERROR_PALTFORM_CATEGORY_LEVEL = "平台分类层级错误";

        /**
         * 平台分类层级不可为空
         */
        public static final String ERROR_PLATFORM_CATEGORY_LEV_CAN_NOT_EMPTY = "platform.category.lev.cannot.empty";

        /**
         * 平台分类名称不可为空
         */
        public static final String ERROR_PLATFORM_CATEGORY_NAME_CAN_NOT_EMPTY = "platform.category.name.cannot.empty";

        /**
         * 物料不存在或商品未映射
         */
        public static final String ITEM_DOES_NOT_EXIST_OR_IS_NOT_MAPPED = "small.item.does.not.exist.or.is.not.mapped";
        /**
         * 当前物料映射商品不唯一
         */
        public static final String CURRENT_ITEM_MAP_IS_NOT_UNIQUE = "small.current.item.map.is.not.unique";

        /**
         * 商品名称不一致
         */
        public static final String NAME_OF_THE_PRODUCT_IS_INCONSISTENT = "small.name.of.the.product.is.inconsistent";

        /**
         * 数量不能为空
         */
        public static final String QUANTITY_CANNOT_BE_EMPTY = "small.quantity.cannot.be.empty";

        /**
         * 无该目录权限
         */
        public static final String THIS_DIRECTORY_HAS_NO_PERMISSIONS = "small.this.directory.has.no.permissions";

        /**
         * 目录被禁用
         */
        public static final String CATALOG_ENABLED_FLAG_ERROR = "catalog.enabled_flag_error";

        /**
         * 2级 或 3级 分类下存在父级分类为空，请检查数据
         */
        public static final String FATHER_CATEGORY_IS_NULL = "father.category.is.null";

        /**
         * 分类编码为空，请检查数据
         */
        public static final String CATEGORY_CODE_IS_NULL = "category.code.is.null";

        /**
         * 调用电商接口返回异常
         */
        public static final String EC_EXCEPTION = "ec.exception";

        /**
         * 调用电商接口返回异常
         */
        public static final String EC_PO_EXCEPTION = "ec.po.exception";

        /**
         * 供应商不是合法的合作伙伴
         */
        public static final String ERROR_SUPPLIER_ILLEGITIMATE_PARTNER = "error.supplier.illegitimate.partner";

        /**
         * 协议类型错误
         */
        public static final String ERROR_AGREEMENT_TYPE_ERROR = "error.agreement.type.error";

        /**
         * 日期不合法
         */
        public static final String ERROR_ILLEGAL_DATE = "error.illegal.date";

        /**
         * 分类编码不存在
         */
        public static final String ERROR_CATEGORY_CODE_NOT_EXIST = "error.category.code.not.exist";

        /**
         * 商品编码不存在
         */
        public static final String ERROR_PRODUCT_CODE_NOT_EXIST = "error.product.code.not.exist";

        /**
         * 黑名单商品编码不存在
         */
        public static final String ERROR_BLACKLIST_PRODUCT_CODE_NOT_EXIST = "error.blacklist.product.code.not.exist";

        /**
         * 折扣率和协议价为空
         */
        public static final String ERROR_DISCOUNT_RATE_AND_AGREEMENT_PRICE_NULL = "error.discount.rate.and.agreement.price.null";

        /**
         * 折扣率不为空
         */
        public static final String ERROR_DISCOUNT_RATE_SHOULD_BE_EMPTY = "error.discount.rate.should.be.empty";

        /**
         * 供货周期不为空
         */
        public static final String ERROR_DELIVERY_CYCLE_SHOULD_BE_EMPTY = "error.delivery.cycle.should.be.empty";

        /**
         * 最小购买量不为空
         */
        public static final String ERROR_LIMIT_QUANTITY_SHOULD_BE_EMPTY = "error.limit.quantity.should.be.empty";

        /**
         * 协议价不为空
         */
        public static final String ERROR_AGREEMENT_PRICE_SHOULD_BE_EMPTY = "error.agreement.price.should.be.empty";

        /**
         * 折扣率不合法
         */
        public static final String ERROR_ILLEGAL_DISCOUNT_RATE = "error.illegal.discount.rate";


        /**
         * 协议分类编码维护商品，折扣率必输
         */
        public static final String ERROR_ILLEGAL_DISCOUNT_NOT_NULL = "error.illegal.discount.not.null";

        /**
         * 供货周期不合法
         */
        public static final String ERROR_ILLEGAL_DELIVERY_CYCLE = "error.illegal.delivery.cycle";

        /**
         * 最小购买量不合法
         */
        public static final String ERROR_ILLEGAL_LIMIT_QUANTITY = "error.illegal.limit.quantity";

        /**
         * 协议价合法
         */
        public static final String ERROR_ILLEGAL_AGREEMENT_PRICE = "error.illegal.agreement.price";

        /**
         * 分类编码、商品编码和黑名单只写其中一列
         */
        public static final String ERROR_CATEGORY_CODE_PRODUCT_CODE_BLACKLIST_PRODUCT_CODE_ONLY_ONE = "error.category.code.product.code.blacklist.product.code.only.one";

        /**
         * 未生成比价单
         */
        public static final String COMPARE_NOT_EXISTS = "compare.not.exists";

        /**
         * 超出当前目录下最大购买价格限制
         */
        public static final String CATALOG_OVER_LIMIT_PRICE = "small.catalog.over.limit.price";

        /**
         * 没有权限
         */
        public static final String NO_PERMISSION = "no.permission";

        /**
         * 公司开票信息没有维护
         */
        public static final String COMPANY_INVOICE_INFORMATION_NOT_MAINTAINED = "company.invoice.information.not.maintained";

        /**
         * 物料已映射目录
         */
        public static final String ITEM_REF_CATALOG_EXISTS = "item.ref.catalog.exists";

        /**
         * 地址不存在
         */
        public static final String ADDRESS_NOT_EXISTS = "address.not.exists";

        /**
         * 个人收货地址已被禁用
         */
        public static final String PERSION_ADDRESS_DISABLED = "person.address.has.disabled";

        /**
         * 公司信息异常
         */
        public static final String ERROR_COMPANY_INFO = "error.company.information";

        /**
         * 税率等信息异常
         */
        public static final String ERROR_TAX_INFO = "error.tax.and.uom.information";


        /**
         * 协议不可编辑
         */
        public static final String ERROR_AGREEMENT_UNEDITABLE = "error.agreement.uneditable";
        /**
         * 协议状态不合法
         */
        public static final String ERROR_AGREEMENT_STATUS_NOT_VALID = "error.agreement.status.not.valid";
        /**
         * 协议行不能为空
         */
        public static final String ERROR_AGREEMENT_LINE_CANNOT_BE_EMPTY = "error.agreement.line.cannot.be.empty";

        /**
         * 协议行已过期
         */
        public static final String ERROR_AGREEMENT_LINE_HAS_EXPIRED = "error.agreement.line.has.expired";
        /**
         * 协议行有效期不合法
         */
        public static final String ERROR_AGREEMENT_LINE_INVALID_VALIDITY_PERIOD = "error.agreement.line.invalid.validity.period";
        /**
         * 协议阶梯价不能为空
         */
        public static final String ERROR_AGREEMENT_LADDER_PRICE_CANNOT_BE_EMPTY = "error.agreement.ladder.price.cannot.be.empty";
        /**
         * 协议分配公司不能为空
         */
        public static final String ERROR_AGREEMENT_ASSIGN_CANNOT_BE_EMPTY = "error.agreement.assign.cannot.be.empty";

        /**
         * 协议分配公司不能为空
         */
        public static final String ERROR_AGREEMENT_UNIT_CANNOT_BE_EMPTY = "error.agreement.unit.cannot.be.empty";
        /**
         * 协议区域不能为空
         */
        public static final String ERROR_AGREEMENT_REGION_CANNOT_BE_EMPTY = "error.agreement.region.cannot.be.empty";
        /**
         * 在[{0}]存在价格更低的物料
         */
        public static final String ERROR_ITEM_PRICE_CHECK = "error.item.price.check";

        // 协议导入

        /**
         * 协议不存在
         */
        public static final String AGREEMENT_NOT_EMPTY = "error.agreement.not.empty";

        /**
         * 价格库来源数据不存在
         */
        public static final String PRICE_LIB_NOT_EMPTY = "error.price.lib.not.empty";

        /**
         * 协议公司和价格库公司不同
         */
        public static final String PRICE_AND_AGREEMENT_COMPANY_DIFFERENT = "error.price.and.agreement.company.different";

        /**
         * 协议供应商和价格库供应商不同
         */
        public static final String PRICE_AND_AGREEMENT_SUPPLIER_COMPANY_DIFFERENT = "error.price.and.agreement.supplier.company.different";

        /**
         * 价格库税率为空
         */
        public static final String PRICE_LIB_TAX_IS_EMPTY = "error.price.lib.tax.is.empty";

        /**
         * 价格库物料为空
         */
        public static final String PRICE_LIB_ITEM_IS_EMPTY = "error.price.lib.item.is.empty";

        /**
         * 价格库单位为空
         */
        public static final String PRICE_LIB_UOM_IS_EMPTY = "error.price.lib.uom.is.empty";

        /**
         * 价格库含税单价为空
         */
        public static final String PRICE_LIB_TAX_PRICE_IS_EMPTY = "error.price.lib.tax.price.is.empty";

        /**
         * 价格库阶梯价为空
         */
        public static final String PRICE_LIB_LADDER_PRICE_IS_EMPTY = "error.price.lib.ladder.price.is.empty";

        /**
         * 价格库不含税单价为空
         */
        public static final String PRICE_LIB_UNIT_PRICE_IS_EMPTY = "error.price.lib.unit.price.is.empty";

        /**
         * 价格库币种为空
         */
        public static final String PRICE_LIB_CURRENCY_PRICE_IS_EMPTY = "error.price.lib.currency.price.is.empty";

        /**
         * 物料不存在
         */
        public static final String ITEM_DOES_NOT_EXIST = "error.item.does.not.exist";

        /**
         * 物料名称为空
         */
        public static final String ITEM_NAME_IS_EMPTY = "error.item.name.is.empty";

        /**
         * 含税单价为空
         */
        public static final String TAX_PRICE_IS_EMPTY = "error.tax.price.is.empty";

        /**
         * 未税单价为空
         */
        public static final String UNIT_PRICE_IS_EMPTY = "error.unit.price.is.empty";

        /**
         * 单位不存在
         */
        public static final String UOM_DOES_NOT_EXIST = "error.uom.does.not.exist";

        /**
         * 税率不存在
         */
        public static final String TAX_DOES_NOT_EXIST = "error.tax.does.not.exist";

        /**
         * 币种不存在
         */
        public static final String CURRENCY_DOES_NOT_EXIST = "error.currency.does.not.exist";

        /**
         * 目录不存在
         */
        public static final String CATALOG_DOES_NOT_EXIST = "error.catalog.does.not.exist";

        /**
         * 物料品类不存在
         */
        public static final String ITEM_CATEGORY_DOES_NOT_EXIST = "error.item.category.does.not.exist";

        /**
         * 协议数量为空
         */
        public static final String AGREEMENT_QUANTITY_IS_EMPTY = "error.agreement.quantity.is.empty";

        /**
         * 起订量为空
         */
        public static final String AGREEMENT_ORDER_QUANTITY_IS_EMPTY = "error.agreement.order.quantity.is.empty";

        /**
         * 采购量为空
         */
        public static final String AGREEMENT_PURCHASE_QUANTITY_IS_EMPTY = "error.agreement.purchase.quantity.is.empty";

        /**
         * 采购额为空
         */
        public static final String AGREEMENT_PURCHASE_AMOUNT_IS_EMPTY = "error.agreement.purchase.amount.is.empty";

        /**
         * 送货区域为空
         */
        public static final String AGREEMENT_REGION_IS_EMPTY = "error.agreement.region.is.empty";

        /**
         * 部分区域不存在
         */
        public static final String AGREEMENT_REGION_DOES_NOT_EXIST = "error.agreement.region.does.not.exist";

        /**
         * 运费不存在
         */
        public static final String POSTAGE_DOES_NOT_EXIST = "error.postage.does.not.exist";

        /**
         * 可采买公司为空
         */
        public static final String AGREEMENT_ASSIGN_IS_EMPTY = "error.agreement.assign.is.empty";

        /**
         * 部分公司不存在
         */
        public static final String AGREEMENT_ASSIGN_DOES_NOT_EXIST = "error.agreement.assign.does.not.exist";

        /**
         * 部分公司不存在
         */
        public static final String AGREEMENT_UNIT_DOES_NOT_EXIST = "error.agreement.unit.does.not.exist";

        /**
         * 商品为空
         */
        public static final String PRODUCT_DOES_NOT_EXIST = "error.product.does.not.exist";

        /**
         * 商品分类不存在
         */
        public static final String PRODUCT_CATEGORY_DOES_NOT_EXIST = "error.product.category.does.not.exist";

        /**
         * 商品介绍模板为空
         */
        public static final String PRODUCT_TEMPLATE_IS_EMPTY = "error.product.template.is.empty";

        /**
         * 请先进行预算校验
         */
        public static final String BUDGET_OCCUPANCY = "error.budget.occupancy";

        /**
         * 预算校验失败
         */
        public static final String BUDGET_OCCUPANCY_FAILED = "budget.occupany.failed";

        /**
         * 请配置币种映射
         */
        public static final String CURRENCY_CONFIGURE_MAPPING_ERROR = "currency.configure.mapping.error";

        /**
         * 请选择要校验的商品
         */
        public static final String CHOOSE_AT_LEAST_ONE = "choose.at.least.one";

        /**
         * 物流单号不存在
         */
        public static final String EC_LOGISTICS_NUMBER_NOT_EXIST = "ec.logistics_number.not.exist";

        /**
         * 组织为空
         */
        public static final String ERROR_UNIT_IS_EMPTY = "error.unit.is.empty";
        /**
         * 库存组织为空
         */
        public static final String ERROR_INV_ORGANIZATION_IS_EMPTY = "error.inv.organization.is.empty";
        /**
         * 采购组织为空
         */
        public static final String ERROR_PUR_ORGANIZATION_IS_EMPTY = "error.pur.organization.is.empty";
        /**
         * 公司为空
         */
        public static final String ERROR_COMPANY_IS_EMPTY = "error.company.is.empty";

        /**
         * 属性名错误
         */
        public static final String ERROR_ATTR_TYPE_NAME_ERROR = "error.attr.type.name.error";

        /**
         * 申请数量不能大于可售后数量
         */
        public static final String APPLY_QUANTITY_ERROR = "apply.quantity.error";

        /**
         * 商品不支持退换货
         */
        public static final String PRODUCT_SUPPORT_ERROR = "product.support.error";

        /**
         * 商品不支持退货
         */
        public static final String PRODUCT_SUPPORT_RETURN_ERROR = "product.support.return.error";

        /**
         * 商品不支持换货
         */
        public static final String PRODUCT_SUPPORT_CHANGE_ERROR = "product.support.change.error";

        /**
         * 超过支持退货时间
         */
        public static final String EXCEDED_SUPPORT_RETURN_TIME = "exced.support.return.time";

        /**
         * 超过支持换货时间
         */
        public static final String EXCEDED_SUPPORT_CHANGE_TIME = "exced.support.change.time";

        /**
         * 默认收货地址未配置
         */
        public static final String DEFAULT_RECEIVER_ADDRESS_NOT_CONFIGURED = "默认收货地址未配置";

        /**
         * 默认分类不存在报错
         */
        public static final String ERROR_DEFAULT_PRODUCT_CATEGORY_CODE_NOT_EMPTY = "error.default.product.category.code.not.empty";


        /**
         * 订单未妥投
         */
        public static final String ORDER_HAS_NOT_YET_BEEN_PLACED = "order.has.not.yet.been.placed";

        /**
         * 请选择默认值
         */
        public static final String CHOOSE_DEFAULT_VALUE = "choose.default.value";

        /**
         * 存在线上支付方式，不允许选择线下支付类型
         */
        public static final String OFFLINE_PAYMENT_TYPE_NOT_ALLOWED = "offline.payment.type.not.allowed";

        /**
         * 当前为线上支付类型，不允许选择线下支付方式
         */
        public static final String PAYMENT_TYPE_IS_OFFLINE = "payment.type.is.offline";

        /**
         * 价格不能为空
         */
        public static final String ERROR_PRODUCT_PRICE_NOT_EMPTY = "smal.error.product.price.not.empty";

        /**
         * 未匹配到对应的商品单位属性
         */
        public static final String ERROR_CANNOT_MATCH_SKU_UOM = "smal.error.cannot_match_sku_uom";
        /**
         * 未匹配到对应的商品税率属性
         */
        public static final String ERROR_CANNOT_MATCH_SKU_TAX = "smal.error.cannot_match_sku_tax";
        /**
         * 未匹配到对应的商品币种属性
         */
        public static final String ERROR_CANNOT_MATCH_SKU_CURRENCY = "smal.error.cannot_match_sku_currency";
        /**
         * 未匹配到对应的srm单位属性
         */
        public static final String ERROR_CANNOT_MATCH_SRM_UOM = "smal.error.cannot_match_srm_uom";
        /**
         * 未匹配到对应的srm税率属性
         */
        public static final String ERROR_CANNOT_MATCH_SRM_TAX = "smal.error.cannot_match_srm_tax";
        /**
         * 未匹配到对应的srm币种属性
         */
        public static final String ERROR_CANNOT_MATCH_SRM_CURRENCY = "smal.error.cannot_match_srm_currency";

        /**
         * 未匹配到对应的srm币种属性
         */
        public static final String ERROR_SKU_AFTER_SALE = "smal.error.sku_after_sale_error";
        /**
         * 导入数据中有重复的价格库数据
         */
        public static final String ERROR_PRICE_LIB_MATCH_ID_IS_REPEAT = "smal.error.price.lib.match.id.is.repeat";

        /**
         * 请先进行评价
         */
        public static final String ERROR_ASSESS_FIRST = "error.assess.first";

        /**
         * 超过可追加评价时间
         */
        public static final String ERROR_EXCEEDED_ASSESS_TIME = "error.exceeded.assess.time";

        /**
         * 未查询到评价信息
         */
        public static final String ERROR_QUERY_EMPTY_ASSESSMENT = "error.query.empty.assessment";

        /**
         * 阶梯价从至数量格式不正确
         */
        public static final String ERROR_LADDER_MOUNT_FORMAT = "smal.error.ladder.mount.format";

        /**
         * 阶梯价数量和价格格式不正确
         */
        public static final String ERROR_LADDER_MOUNT_PRICE_FORMAT = "smal.error.ladder.mount.price.format";

        /**
         * 阶梯价单价格式不正确
         */
        public static final String ERROR_LADDER_PRICE_FORMAT = "smal.error.ladder.price.format";

        /**
         * 阶梯价含税单价格式不正确
         */
        public static final String ERROR_LADDER_TAX_PRICE_FORMAT = "smal.error.ladder.tax.price.format";

        /**
         * 暂不支持实时价
         */
        public static final String ERROR_PRICE_TYPE_ERROR = "smal.error.price_type_error";

        /**
         * 编码重复
         */
        public static final String ERROR_CODE_REPEAT = "smal.error.code_repeat_error";

        /**
         * 标签不存在
         */
        public static final String ERROR_LABEL_NOT_EXISTS = "smal.error.label_not_exists";

        /**
         * 名称重复
         */
        public static final String ERROR_NAME_REPEAT = "smal.error.name_repeat_error";


        /**
         * 事件发送失败
         */
        public static final String ERROR_SEND_EVENT_MESSAGE = "smal.error.send.event.message";


        /**
         * 主商品已下架
         */
        public static final String MAIN_PRODUCT_UN_SHELF = "smal.main.product.un.shelf";
        /**
         * 商品被锁定
         */
        public static final String MAIN_PRODUCT_IS_LOCKED = "smal.main.product.is.locked";

        /**
         * 该商品已被供应商下架，不允许上架
         */
        public static final String PRODUCT_UN_SHELF_BY_SUPPLIER = "smal.product.un.shelf.by.supplier";

        /**
         * 协议已失效
         */
        public static final String PRODUCT_AGREEMENT_HAS_EXPIRED = "smal.product.agreement.has.expired";

        /**
         * 协议未生效
         */
        public static final String PRODUCT_AGREEMENT_HAS_NOT_TAKEN_EFFECT = "smal.product.agreement.has.not.taken.effect";
        /**
         * 商品未映射目录或目录已禁用
         */
        public static final String PRODUCT_UNMAPPED_CATALOG = "smal.product.unmapped.catalog";

        /**
         * 商品在xx组织 xx 区域已上架
         */
        public static final String PRODUCT_SHELF_IN_ORGANIZATION_AND_REGION = "smal.product.shelf.in.organization.and.region";

        /**
         * 协议变更，生成版本V
         */
        public static final String AGREEMENT_UPGRADE_OPERATED_REMARK = "smal.agreement.upgrade.operated.remark";

        /**
         * 协议
         */
        public static final String AGREEMENT_DESC = "smal.agreement.desc";

        /**
         * 运费规则被上架中的协议引用中，不能禁用
         */
        public static final String POSTAGE_HAS_USED = "smal.postage.has.used";

        /**
         * 上架失败，请检查
         */
        public static final String LISTING_FAILED_PLEASE_CHECK = "smal.error.listing.failed.please.check";
        /**
         * 部分上架成功，请检查
         */
        public static final String PARTIALLY_LISTED_SUCCESSFULLY_PLEASE_CHECK = "smal.error.partially.listed.successfully.please.check";
        /**
         * 上架成功
         */
        public static final String SUCCESSFULLY_LISTED = "smal.error.successfully.listed";

        /**
         * 请维护参考价
         */
        public static final String ERROR_REFERENCE_PRICE_NOT_EMPTY = "smal.error.reference.price.not.empty";

        /**
         * 协议行不存在有效的组织
         */
        public static final String AGREEMENT_LINE_NOT_EXITS_UNIT = "smal.error.agreement.line.not.exits.unit";

        /**
         * 总库存不能为负数
         */
        public static final String ERROR_TOTAL_STOCK_NOT_NEGATIVE = "smal.error.total.stock.not.negative";


        /**
         * 商品采购数量大于最大购买量
         */
        public static final String PRODUCT_MAX_PURCHASE_QUANTITY_ERROR = "smal.product.max.purchase.quantity.error";

        /**
         * 公司编码不正确或当前账户无此公司权限
         */
        public static final String ERROR_COMPANY_INVALID = "smal.error.company.invalid";

        /**
         * 库存组织编码不正确或当前账号无此库存组织权限
         */
        public static final String ERROR_ORGANIZATION_INVALID = "smal.error.organization.invalid";

        /**
         * 地址区域不为4级或地址区域间没有用【 / 】 分割
         */
        public static final String ERROR_ADDRESS_INVALID = "smal.error.address.invalid";

        /**
         * 地址不存在
         */
        public static final String ERROR_ADDRESS_NOT_EXIST = "smal.error.address.not.exist";

        /**
         * 地址[]不存在
         */
        public static final String ERROR_ADDRESS_DOES_NOT_EXIST = "smal.error.address.does.not.exist";

        /**
         * 状态填写错误，请填写 Y或 N
         */
        public static final String ERROR_STATUS_INVALID = "smal.error.status.invalid";

        /**
         * 是否默认填写错误，请填写 Y或N
         */
        public static final String ERROR_DEFAULT_FLAG_INVALID = "smal.error.default.flag.invalid";

    }

    public final class Budget {
        private Budget() {
        }

        /**
         * 校验成功状态
         */
        public static final String OCCUPIED = "OCCUPIED";

        /**
         * 校验失败提示信息
         */
        public static final String ERROR_MESSAGE = "预算校验不通过";

        /**
         * 校验成功提示信息
         */
        public static final String SUCCESS_MESSAGE = "校验成功";

        public static final String NO_BUDGET_CONTROL_MESSAGE = "无预算控制，已通过";

        /**
         * 事件code
         */
        public static final String BUDGET_RELEASE_EVENT_CODE = "BUDGET_RELEASE";

        /**
         * 已占用
         */
        public static final int OCCUPANCY_SUCCESS = 1;

        /**
         * 未占用
         */
        public static final int UNOCCUPIED = 0;

        /**
         * 创建预采购申请成功
         */
        public static final int OCCUPANCY_CREATE_PURCHASE = 2;

        /**
         * 无预算控制
         */
        public static final int NO_BUDGET_CONTROL = 3;

        /**
         * 商城预算类型
         */
        public static final String DOCUMENT_TYPE = "PRE_PR";


    }


    public final class Product {
        private Product() {
        }

        /**
         * 商品编码规则
         */
        public static final String CODE_RULE_PRODUCT_NUM = "SCEC.PRODUCT";

        /**
         * 目录化商品维护列表界面
         */
        public static final int PRODUCT_MAINTAIN_LIST_TAB = 1;

        /**
         * 目录化商品审批列表界面
         */
        public static final int PRODUCT_APPROVE_LIST_TAB = 2;

        /**
         * 目录化商品上架界面
         */
        public static final int PRODUCT_PUTAWAY_LIST_TAB = 3;

        /**
         * 目录化商品待上架
         */
        public static final int PRODUCT_STAY_PUTAWAY_LIST_TAB = 4;

        /**
         * 目录化商品查询列表界面
         */
        public static final int PRODUCT_LIST_TAB = 5;

        /**
         * 目录化商品分享列表界面
         */
        public static final int PRODUCT_LIST_SHARE = 6;
        /**
         * 商品来源
         */
        public static final String PRODUCT_SOURCE_FROM = "目录化";


        /**
         * 商品类型 1：线上
         */
        public static final int PRODUCT_TYPE_ONLINE = 1;

        /**
         * 商品类型 2：线下
         */
        public static final int PRODUCT_TYPE_OUTLINE = 2;


    }

    public final class ProductStatus {
        private ProductStatus() {
        }

        /**
         * 新建
         */
        public static final String NEW = "NEW";

        /**
         * 已提交
         */
        public static final String SUBMITTED = "SUBMITTED";

        /**
         * 编辑
         */
        public static final String EDIT = "EDIT";

        /**
         * 待提交
         */
        public static final String TOSUBMIT = "TOSUBMIT";

        /**
         * 审批通过
         */
        public static final String APPROVED = "APPROVED";

        /**
         * 审批拒绝
         */
        public static final String REJECT = "REJECT";

        /**
         * 手工创建
         */
        public static final String MANUAL = "MANUAL";

        /**
         * 寻源结果
         */
        public static final String SOURCING = "SOURCING";

        /**
         * 商品分享
         */
        public static final String SHARE = "SHARE";
        /**
         * 已上架
         */
        public static final String SHELF = "SHELF";

        /**
         * 供方上架
         */
        public static final String SUPPLIER_SHELF = "SHELF";

        /**
         * 已下架
         */
        public static final String UNSHELVE = "UNSHELVE";

        /**
         * 已锁定
         */
        public static final String LOCK = "LOCK";

        /**
         * 已解锁
         */
        public static final String UNLOCK = "UNLOCK";

        /**
         * 商品审核通过
         */
        public static final String AUDITPASS = "AUDITPASS";

        /**
         * 商品审核驳回
         */
        public static final String AUDITREJECT = "AUDITREJECT";

        /**
         * 已作废
         */
        public static final String INVALID = "INVALID";

        /**
         * 已失效
         */
        public static final String DISABLED = "DISABLED";

        /**
         * 供应商
         */
        public static final String SUPPLIER = "SUPPLIER";

        /**
         * 采购商
         */
        public static final String PURCHASE = "PURCHASER";

        /**
         * 自动失效
         */
        public static final String FAILURE = "商品过期自动失效";

        /**
         * 商品有货
         */
        public static final String GOODS_IN_STOCK = "有货";

        /**
         * 待审核
         */
        public static final String WAIT_AUDIT = "待审核";

        /**
         * 协议更改
         */
        public static final String AGREEMENT_CHANGE = "协议更改";


    }

    /**
     * 申请开发票
     */
    public final class InvoiceReq {
        private InvoiceReq() {
        }

        public static final String STATUS_ALL = "ALL";
        public static final String STATUS_PART = "PART";
        public static final String STATUS_SEND = "SEND";
        public static final String STATUS_SUBMITTED = "SUBMITTED";
        public static final String STATUS_SUBMIT_FAIL = "SUBMIT_FAIL";
        public static final String SUCCESS = "success";
        public static final String RESULT = "result";

        /**
         * 开票状态：新建
         */
        public static final String STATUS_NEW = "NEW";
        /**
         * 开票状态：通过
         */
        public static final String STATUS_PASS = "PASS";
        /**
         * 开票状态：驳回
         */
        public static final String STATUS_REJECT = "REJECT";
        /**
         * 开票状态：作废
         */
        public static final String STATUS_INVALID = "INVALID";

        /**
         * 开票成功code
         */
        public static final String SUCCESS_ALL_CODE = "0005";

        public static final String SUCCESS_PART_CODE = "0006";

        public static final String SUCCESS_RESULT_CODE = "0000";

        public static final String BILLED = "BILLED";

        public static final String UNBILLED = "UNBILLED";

        public static final String PART_BILLED = "PART_BILLED";

        public static final String CANCEL_BILLED = "CANCEL_BILLED";
        /**
         * 售后问题导致开票失败code
         */
        public static final String FAIL_AS_CODE = "3302";
        /**
         * 售后问题导致发票驳回
         */
        public static final String FAIL_AS = "FAIL_AS";
        /**
         * 资质问题导致发票驳回
         */
        public static final String FAIL_QU = "FAIL_QU";
    }


    /**
     * 电商平台代码
     */
    public final class ECommercePlatformCode {
        private ECommercePlatformCode() {
        }

        /**
         * 京东
         */
        public static final String PLATFORM_JD = "JD";
        /**
         * 震坤行
         */
        public static final String PLATFORM_ZKH = "ZKH";

        /**
         * 晨光
         */
        public static final String PLATFORM_CG = "CG";
    }

    /**
     * 采购申请并单规则代码
     */
    public final class PurReqMergeRuleCode {
        private PurReqMergeRuleCode() {
        }

        /**
         * 默认
         */
        public static final String DEFAULT = "DEFAULT";

        /**
         * 自定义
         */
        public static final String CUSTOMIZE = "CUSTOMIZE";
    }

    /**
     * 缓存代码
     */
    public final class CacheCode {

        private CacheCode() {

        }

        public static final String SERVICE_NAME = "smal";

        public static final String USER_CONFIG_INDEX = "user_config_index";

        public static final String MIX_DEPLOYMENT = "mix_deployment";

        public static final String PLATFORM_INDEX = "platform_index";

        /**
         * 支付信息
         */
        public static final String PAYMENT_INFO = "payment_info";

        public static final String CATEGORY_CODE = "category_code";

        public static final String CARALOG_ITEM = "catalog_item";

        public static final String ITEM_CODE = "item_code";

        public static final String BAN_SKU_ID = "ban_sku_id";


        public static final String CATALOG_ID = "catalog_id";

        /**
         * 商品推送锁
         */
        public static final String PRODUCT_LOCK = "product_lock";

        /**
         * 协议库存锁
         */
        public static final String AGREEMENT_STOCK = "agreement_stock";

        public static final String BAN_USER_CATALOG = "ban_user_catalog";

        /**
         * 商品推送中 spu
         */
        public static final String USER_CATALOG = "user_catalog";
        /**
         * 分配公司
         */
        public static final String EC_COMPANY_ASSIGN = "ec_company_assign";

        /**
         * 电商账户表
         */
        public static final String EC_CLIENT = "ec_client";

        /**
         * 分类映射缓存目录
         */
        public static final String CATEGORY_CATALOG_REF = "category_catalog_ref";

        /**
         * 子账户目录可见性
         */
        public static final String USER_CATALOG_INVISIBLE = "user_catalog_invisible";

        /**
         * 公司目录可见性
         */
        public static final String COMPANY_CATALOG_INVISIBLE = "company_catalog_invisible";

        /**
         * 子账户目录可见性
         */
        public static final String ROLE_CATALOG_INVISIBLE = "role_catalog_invisible";


        public static final String CACHE_DEFAULT_VALUE = "undefine";

        /**
         * 采购申请预览
         */
        public static final String PURCHASE_REQUISITION_PREVIEW = "purchase_requisition_preview";

        public static final String PUNCHOUT = "punchout";


        /**
         * 协议行号
         */
        public static final String AGREEMENT_LINE_NUM = "agreement_line_num";

    }

    /**
     * 地址类型
     */
    public final class AdressType {

        private AdressType() {

        }

        /**
         * 收单地址
         */
        public static final String INVOICE = "INVOICE";

        /**
         * 收货地址
         */
        public static final String RECEIVER = "RECEIVER";


    }


    /**
     * 电商订单/子订单状态
     */
    public final class EcPurchaseOrderStatus {
        private EcPurchaseOrderStatus() {
        }

        /**
         * 新建
         */
        public static final String NEW = "NEW";

        /**
         * 提交中
         */
        public static final String SUBMITTING = "SUBMITTING";

        /**
         * 已提交
         */
        public static final String SUBMITTED = "SUBMITTED";

        /**
         * 已取消
         */
        public static final String CANCELLED = "CANCELLED";

        /**
         * 已确认
         */
        public static final String CONFIRMED = "CONFIRMED";

        /**
         * 已妥投
         */
        public static final String DELIVERED = "DELIVERED";

        /**
         * 妥投拒绝
         */
        public static final String DELIVERED_REJECTED = "DELIVERED_REJECTED";

        /**
         * 确认中
         */
        public static final String CONFIRMING = "CONFIRMING";

    }

    public final class MallOrderCenterStatus {
        private MallOrderCenterStatus() {
        }

        /**
         * 商城订单状态 SMAL.ORDER_STATUS
         * 待处理
         */
        public static final String PENDING = "PENDING";

        /**
         * 商城订单状态 SMAL.ORDER_STATUS
         * 待发货
         */
        public static final String WAITING_SENT = "WAITING_SENT";

        /**
         * 商城订单状态 SMAL.ORDER_STATUS
         * 待供方确认
         */
        public static final String WAITING_CONFIRM = "WAITING_CONFIRM";

        /**
         * 商城订单状态 SMAL.ORDER_STATUS
         * 待审批
         */
        public static final String WAITING_APPROVE = "WAITING_APPROVE";

        /**
         * 商城订单状态 SMAL.ORDER_STATUS
         * 待支付
         */
        public static final String WAITING_PAY = "WAITING_PAY";

        /**
         * 商城订单状态 SMAL.ORDER_STATUS
         * 已支付
         */
        public static final String PAID = "PAID";

        /**
         * 商城订单状态 SMAL.ORDER_STATUS
         * 审批通过
         */
        public static final String APPROVED = "APPROVED";

        /**
         * 商城订单状态 SMAL.ORDER_STATUS
         * 审批拒绝
         */
        public static final String REJECT = "REJECT";

        /**
         * 商城订单状态 SMAL.ORDER_STATUS
         * 待收货
         */
        public static final String WAITING_RECEIVE = "WAITING_RECEIVE";

        /**
         * 商城订单状态 SMAL.ORDER_STATUS
         * 交易完成
         */
        public static final String FINISH = "FINISH";

        /**
         * 商城订单状态 SMAL.ORDER_STATUS
         * 交易关闭(拒收)
         */
        public static final String DELIVER_REJECT = "DELIVER_REJECT";

        /**
         * 商城订单状态 SMAL.ORDER_STATUS
         * 交易关闭(取消订单)
         */
        public static final String CANCEL = "CANCELLED";

        /**
         * 已确认
         */
        public static final String CONFIRMED = "CONFIRMED";

        /**
         * 新建
         */
        public static final String NEW = "NEW";

        /**
         * 提交中
         */
        public static final String SUBMITTING = "SUBMITTING";

        /**
         * 已提交
         */
        public static final String SUBMITTED = "SUBMITTED";

        /**
         * 已妥投
         */
        public static final String DELIVERED = "DELIVERED";

        /**
         * 妥投拒绝
         */
        public static final String DELIVERED_REJECTED = "DELIVERED_REJECTED";

        /**
         * 确认中
         */
        public static final String CONFIRMING = "CONFIRMING";


    }

    /**
     * 电商订单/子订单展示状态
     */
    public final class EcDidsPlayStatus {
        private EcDidsPlayStatus() {
        }

        /**
         * 处理中
         */
        public static final String PENDING = "PENDING";

        /**
         * 已取消
         */
        public static final String CANCEL = "CANCEL";

        /**
         * 待付款
         */
        public static final String WAITING_PAY = "WAITING_PAY";

        /**
         * 待发货
         */
        public static final String WAITING_SENT = "WAITING_SENT";

        /**
         * 待收货
         */
        public static final String WAITING_RECEIVE = "WAITING_RECEIVE";

        /**
         * 已确认收货
         */
        public static final String WAITING_FINISH = "WAITING_FINISH";

        /**
         * 完成
         */
        public static final String FINISH = "FINISH";

        /**
         * 拒收
         */
        public static final String DELIVER_REJECT = "DELIVER_REJECT";

    }


    /**
     * 电商财务相关状态
     */
    public final class EcFinanceStatus {
        private EcFinanceStatus() {
        }

        /**
         * 未开票
         */
        public static final String UNBILLED = "UNBILLED";

        /**
         * 已开票
         */
        public static final String BILLED = "BILLED";

        /**
         * 未对账
         */
        public static final String UNBLANCE = "UNBLANCE";

        /**
         * 已对账
         */
        public static final String BALANCED = "BALANCED";

        /**
         * 部分对账
         */
        public static final String PART_BALANCED = "PART_BALANCED";

        /**
         * 售后重对账
         */
        public static final String AFS_REBALANCE = "AFS_REBALANCE";
    }


    /**
     * 电商订单取消代码
     */
    public final class EcPruchaseOrderCancelCode {
        private EcPruchaseOrderCancelCode() {
        }

        /**
         * 新建取消
         */
        public static final String NEW_CANCELLED = "NEW_CANCELLED";

        /**
         * 采购申请取消
         */
        public static final String REQ_CANCELLED = "REQ_CANCELLED";

        /**
         * 订单取消
         */
        public static final String PO_CANCELLED = "PO_CANCELLED";

        /**
         * 电商自动取消
         */
        public static final String EC_AUTO_CANCELLED = "EC_AUTO_CANCELLED";

        /**
         * 电商线下取消
         */
        public static final String EC_OFFLINE_CANCELLED = "EC_OFFLINE_CANCELLED";
    }


    /**
     * 编码规则代码
     */
    public final class RuleCode {
        private RuleCode() {
        }

        /**
         * 商城订单编号
         */
        public static final String EC_PO_NUM = "SCEC.PURCHASE_ORDER";

        /**
         * 协议编码
         */
        public static final String SMAL_AGREEMENT_NUM = "SMAL.AGREEMENT_NUM";

        /**
         * 商城订单批次号
         */
        public static final String S2FUL_ORDER_BATCH_CODE = "S2FUL.ORDER_BATCH_CODE";
        /**
         * 协议名称编码规则
         */
        public static final String SMAL_AGREEMENT_NAME = "SMAL.AGREEMENT_NAME";


    }

    /**
     * 事件消费代码
     */
    public final class EventDataCode {
        private EventDataCode() {
        }

        /**
         * 已发布
         */
        public static final String PUBLISHED_ACTION = "PUBLISHED";

        /**
         * 确认/发布取消
         */
        public static final String CANCEL_ACTION = "CANCELED";
    }


    /**
     * 映射批导
     */
    public final class ItemRefImportErrorCode {
        private ItemRefImportErrorCode() {
        }

        /**
         * 不在当前租户组织架构下
         */
        public static final String NOT_BELONG_TO_CURRENT_TENANT_ORGANIZATION =
                "not.belong.to.current.tenant.organization";

        /**
         * 不在当前公司下
         */
        public static final String NOT_BELONG_TO_CURRENT_COMPANY = "not.belong.to.current.company";

        /**
         * 不在当前库存组织中
         */
        public static final String NOT_BELONG_TO_CURRENT_INVENTORY_ORGANIZATION =
                "not.belong.to.current.inventory.organization";

        /**
         * 目录不存在
         */
        public static final String CATALOGUE_INEXISTENCE = "catalogue.inexistence";

        /**
         * 找不出对应物料信息
         */
        public static final String ITEM_INFORMATION_CANNOT_BE_FOUND = "item.information.cannot.be.found";

        /**
         * 为非建立合作伙伴关系的电商
         */
        public static final String NON_ESTABLISHMENT_OF_PARTNERSHIP_E_COMMERCE =
                "non-establishment.of.partnership.e-commerce";

        /**
         * 未找到对应分类
         */
        public static final String CATEGORY_NOT_BE_FOUND = "category.not.be.found";

        /**
         * 该公司下没有维护这个三级目录
         */
        public static final String COMPANY_DOSE_NOT_MAINTAIN = "company.dose.not.maintain";

        /**
         * 目录级别错误
         */
        public static final String CATALOG_LEVEL_ERROR = "catalog.level.error";

        /**
         * 分类级别错误
         */
        public static final String CATEGORY_LEVEL_ERROR = "category.level.error";

    }

    /**
     * 目录映射批导
     */
    public final class CatalogMapImportErrorCode {
        private CatalogMapImportErrorCode() {
        }

        /**
         * 该电商不存在
         */
        public static final String E_COMMERCE_INEXISTENCE = "E-commerce.inexistence";

        /**
         * 该电商分类名称不存在
         */
        public static final String CATEGORY_INEXISTENCE = "category.inexistence";

        /**
         * 该目录不存在
         */
        public static final String CATALOG_INEXISTENCE = "catalog.inexistence";

        /**
         * 非三级目录
         */
        public static final String CATALOG_ISNOT_THIRD_LEVEL = "catalog.isnot.third.level";

    }

    public final class AfterSale {

        public static final String CODE_RULE_AFTER_SALE_NUM = "SCEC.AFTER_SALE";

        /**
         * 售后服务中
         */
        public static final String SERVICING = "SERVICING";

        /**
         * 售后服务完成
         */
        public static final String SERVICED = "SERVICED";

        /**
         * 售后服务取消
         */
        public static final String CANCELED = "CANCELED";

        /**
         * 退货完成
         */
        public static final String RETURNED = "RETURNED";
        /**
         * 换货完成
         */
        public static final String EXCHANGED = "EXCHANGED";
        /**
         * 维修完成
         */
        public static final String REPAIRED = "REPAIRED";
        /**
         * 协同售后服务中
         */
        public static final String SERVING = "SERVING";

    }

    public final class AfterSaleStatus {

        /**
         * 审核中
         */
        public static final String APPROVING = "APPROVING";

        /**
         * 审核中
         */
        public static final String EXTERNAL_APPROVING = "EXTERNAL_APPROVING";

        /**
         * 外部系统审批拒绝
         */
        public static final String EXTERNAL_APPROVING_REJECT = "EXTERNAL_REJECT";

        /**
         * 审核通过
         */
        public static final String PASS = "PASS";

        /**
         * 驳回
         */
        public static final String REJECT = "REJECT";

        /**
         * 已发货
         */
        public static final String DELIVERED = "DELIVERED";

        /**
         * 待发货
         */
        public static final String WAIT_SENT = "WAIT_SENT";

        /**
         * 待收货
         */
        public static final String WAIT_RECEIVE = "WAIT_RECEIVE";

        /**
         * 已收货
         */
        public static final String RECEIVED = "RECEIVED";

        /**
         * 处理中
         */
        public static final String PROCESSING = "PROCESSING";

        /**
         * 待用户确认
         */
        public static final String WAIT_CONFIRM = "WAIT_CONFIRM";

        /**
         * 用户已确认
         */
        public static final String CONFIRMED = "CONFIRMED";

        /**
         * 已完成
         */
        public static final String FINISHED = "FINISH";

        /**
         * 取消
         */
        public static final String CANCELED = "CANCELED";

        /**
         * 自营配送
         */
        public static final String SELF_DISTRIBUTION = "10";

        /**
         * 第三方配送
         */
        public static final String THIRD_PARTY = "20";

        /**
         * 审批环节
         */
        public static final String APPROVED_LINK = "APPROVED_LINK";

        /**
         * 待处理
         */
        public static final String WAIT_PROCESS = "WAIT_PROCESS";

        /**
         * 商品拒收
         */
        public static final String PRODUCT_REJECT = "PRODUCT_REJECT";

    }

    public final class AfsType {
        public static final int APPROVING = 10;

        /**
         * 退货
         */
        public static final String RETURN = "1";

        /**
         * 换货
         */
        public static final String CHANGE = "2";

        /**
         * 维修
         */
        public static final String REPAIR = "3";

        /**
         * 无需审批
         */
        public static final String NO_APPROVAL = "1";

        /**
         * 外部系统审批
         */
        public static final String EXTERNAL_APPROVAL = "2";

        /**
         * 功能审批
         */
        public static final String FUNCTION_APPROVAL = "3";

        /**
         * 工作流审批
         */
        public static final String WORKFLOW_APPROVAL = "4";

    }

    public final class AfterSaleStatusCode {

        /**
         * 申请环节码
         */
        public static final int APPROVING = 10;

        /**
         * 审核完成码
         */
        public static final int PASS = 20;

        /**
         * 已收货
         */
        public static final int RECEIVED = 30;

        /**
         * 处理中
         */
        public static final int PROCESSING = 40;

        /**
         * 待确认
         */
        public static final int WAIT_CONFIRM = 50;

        /**
         * 完成
         */
        public static final int FINISHED = 60;

        /**
         * 取消
         */
        public static final int CANCELED = 70;
    }

    /**
     * 订单中心售后提示信息
     */
    public final class MallOrderHintMessage {
        private MallOrderHintMessage() {
        }

        /**
         * 未妥投,不支持售后
         */
        public static final String AFS_UNSUPPROT_UNDELIVERED = "afs.unsupport.undelivered";
    }


    /**
     * 在线支付状态
     */
    public final class PaymentStatus {
        private PaymentStatus() {
        }

        /**
         * 在线支付描述
         */
        public static final String ONLINE_PAYMENT_DESC = "在线支付";

        /**
         * 未支付
         */
        public static final String NON_PAYMENT = "NON-PAYMENT";

        /**
         * 支付成功
         */
        public static final String SUCCESS = "SUCCESS";
    }

    /**
     * 选买类型
     */
    public final class PurchaseType {
        private PurchaseType() {
        }

        /**
         * 个人选买
         */
        public static final String PERSON = "PERSON";

        /**
         * 公司选买
         */
        public static final String COMPANY = "COMPANY";
    }

    public final class TimeType {
        private TimeType() {
        }

        /**
         * 三个月
         */
        public static final String THREE_MONTH = "threeMonth";

        /**
         * 一周
         */
        public static final String ONE_WEEK = "oneWeek";

        /**
         * 一个月
         */
        public static final String ONE_MONTH = "oneMonth";

        /**
         * 三个月前
         */
        public static final String THREE_MONTH_AGO = "threeMonthAgo";

        /**
         * 全部
         */
        public static final String ALL = "all";


    }

    public final class ProductPublishConstant {
        //销售属性
        public static final int SALE_PROPERTY = 1;
        //基本属性
        public static final int BASE_PROPERTY = 2;

        public static final int HASHMAP_SIZE = 20;

        //字符串描述
        public static final String SALE_PROPERTY_LESS_PUBISH = "发布过的spu，销售属性不能减少";
        public static final String SALE_PROPERTY_VALUE_LESS_PUBISH = "发布过的spu，销售属性值不能减少";
        public static final String TENEANT_ID_NULL = "租户id为空";
        public static final String CID_NULL = "类目id为空";
        public static final String PARAM_NULL = "参数为空";
        public static final String SKU_ATTR_UNIQUE = "不允许两个sku属性值完全相同";
        public static final String PICTURE_NOT_NULL = "每个sku的图片不能为空";
        public static final String PACKAGE_LENGTH_OVERFIT = "包装清单长度不能超过%d";
        public static final String NO_SPU = "没有指定的spu";
        public static final String NO_SKU = "没有指定的sku";
        public static final String PUBLISH_NOT_ALLOW_SAVE = "已经发布过的spu，禁止保存";
        public static final String CID_NOT_FINAL = "该类目不是终极类目";
        public static final String SKU_OPTION_NULL = "SKu的必填属性没有必填";
        public static final String PUBLISH_SKU_PROPERTY_LESS = "发布过的sku，销售属性不能减少";
        public static final String PUBLISH_SKU_PROPERTY_CHANGE = "发布过的sku销售属性值不能更改";
        public static final String SKU_PROPERTY_NOT_NULL = "sku的属性不允许attrValId和description字段同时为空";
        public static final String SKU_EXTEND_PROPERTY_NOT_NULL = "sku的自定义属性value不能为空";
        public static final String AFS_NULL = "售后信息为空";
        public static final String SPECIAL_AFS_INSTROCTION = "当勾选特殊售后说明书，说明不能为空";
        public static final String SPECIAL_AFS_INSTROCTION_LENGTH_OVERFIT = "特殊售后说明，长度不能超过%d";
        public static final String SPECIAL_AFS_INSTROCTION_NOTNULL = "非特殊售后，不允许设置说明";
        public static final String AFS_SPECIAL_PARM = "是否特殊售后说明必填";
        public static final String AFS_SPECIAL_ONLY = "特殊售后商品，不允许设置退换货说明";
    }

    public final class ProductAddUpdateConstant {
        //销售属性
        public static final int SALE_ATTRIBUTE = 1;
        //基本属性
        public static final int BASE_ATTRIBUTE = 2;

        public static final int HASHMAP_SIZE = 20;

        //字符串描述
        public static final String SUPPLIER_ACCOUNT_ERROR = "请联系客户检查电商账号配置";
    }

    public enum YnEnum {
        /**
         * 有效
         */
        VALID((byte) 1, "有效"),
        /**
         * 逻辑删除
         */
        DELETE((byte) 0, "逻辑删除");

        @Getter
        private final Byte code;
        @Getter
        private final String description;

        YnEnum(Byte code, String description) {
            this.code = code;
            this.description = description;
        }
    }


    public enum DeliveryEnum {
        /**
         * SRM消息已接收
         */
        RECEIVE_FLAG(1, "SRM消息已接收"),
        /**
         * SRM消息未接收
         */
        NOT_RECEIVE_FLAG(0, "SRM消息未接收"),

        /**
         * 妥投消息已接收
         */
        DELIVERY_FLAG(1, "妥投消息已接收"),
        /**
         * 妥投消息未接收
         */
        NOT_DELIVERY_FLAG(0, "妥投消息未接收");

        @Getter
        private final Integer code;
        @Getter
        private final String description;

        DeliveryEnum(Integer code, String description) {
            this.code = code;
            this.description = description;
        }
    }


    public enum productPublishEnum {
        UN_PUBLISH(0, "草稿状态"),
        PUBLISH(1, "发布状态"),
        SHELF_DEFAULT(2, "上下架状态初始值"),
        SHELF_OFF(0, "下架状态"),
        SHELF_ON(1, "上架状态"),
        LOCK_DEFAULT(2, "锁定解锁初始值"),
        LOCK_ON(0, "锁定状态"),
        LOCK_OFF(1, "解锁状态"),
        AUDIT_DEFAULT(3, "审核状态初始值"),
        AUDIT_REJECT(0, "审核驳回"),
        AUDIT_AGREE(1, "审核通过"),
        AUDIT_WAIT(2, "待审核"),
        WAS_AUDIT_PASS_NOT(0, "曾经审核通过，否"),
        WAS_AUDIT_PASS_YES(1, "曾经审核通过，是");
        private final int code;
        private final String value;

        productPublishEnum(int code, String value) {
            this.code = code;
            this.value = value;
        }

        public int getCode() {
            return code;
        }

        public String getValue() {
            return value;
        }
    }

    public enum ProductPubProcessEnum {
        //新建发布 新建草稿 草稿保存 草稿提交 发布提交
        NEW_PUBLISH(0, "直接发布"),
        NEW_TEMP(1, "直接保存"),
        AGAIN_TEMP(2, "再次保存"),
        TEMP_PUBISH(3, "草稿编辑后提交"),
        AGAIN_PUBLISH(4, "发布之后再提交");

        private final int code;
        private final String value;

        ProductPubProcessEnum(int code, String value) {
            this.code = code;
            this.value = value;
        }

        public int getCode() {
            return code;
        }

        public String getValue() {
            return value;
        }
    }


    /**
     * 协议价格类型
     */
    public final class PriceType {
        private PriceType() {
        }

        /**
         * 阶梯价
         */
        public static final String LADDER_PRICE = "LADDER_PRICE";
        /**
         * 固定价格
         */
        public static final String REGULAR_PRICE = "REGULAR_PRICE";
        /**
         * 实时价格
         */
        public static final String REAL_TIME_PRICE = "REAL_TIME_PRICE";
    }

    public final class MixDeploymentType {
        private MixDeploymentType() {
        }

        /**
         * 预占
         */
        public static final String SUBMIT = "MIX_DEPLOYMENT_SUBMIT";

        /**
         * 取消
         */
        public static final String CANCEL = "MIX_DEPLOYMENT_CANCEL";

        /**
         * 送货单
         */
        public static final String CREATE_ASN = "MIX_DEPLOYMENT_CREATE_ASN";

        /**
         * 妥投
         */
        public static final String DELIVERY = "MIX_DEPLOYMENT_DELIVERY";

        /**
         * 开票结果
         */
        public static final String INVOICE_RESULT = "MIX_DEPLOYMENT_INVOICE_RESULT";

        /**
         * 发票信息
         */
        public static final String INVOICE_INFO = "MIX_DEPLOYMENT_INVOICE_INFO";

        /**
         * 目录化预占
         */
        public static final String CATA_SUBMIT = "MIX_DEPLOYMENT_CATA_SUBMIT";

        /**
         * 电商订单售后信息
         */
        public static final String AFTER_SALE = "MIX_DEPLOYMENT_AFTER_SALE";

        /**
         * 电商订单售后审核信息
         */
        public static final String AFTER_SALE_AUDIT = "MIX_DEPLOYMENT_AFTER_SALE_AUDIT";

        /**
         * 电商订单售后结果信息
         */
        public static final String AFTER_SALE_RESULT = "MIX_DEPLOYMENT_AFTER_SALE_RESULT";

        /**
         * 混合部署电商订单确认
         */
        public static final String CONFIRMED = "MIX_DEPLOYMENT_CONFIRMED";

        /**
         * 混合部署申请开票
         */
        public static final String INVOICE_REQ = "MIX_DEPLOYMENT_INVOICE_REQ";

        /**
         * 混合部署目录化订单回执
         */
        public static final String CATA_RECEIPT = "MIX_DEPLOYMENT_CATA_RECEIPT";

        /**
         * 取消
         */
        public static final String CATA_CANCEL = "MIX_DEPLOYMENT_CATA_CANCEL";

    }

    /**
     * 京东运费层级
     */
    public final class JDFreightLevel {
        /**
         * 低&免运费级别（超过或相等）
         */
        public static final int FREIGHT_FREE = 100;
        /**
         * 高&低运费级别（超过或相等）
         */
        public static final int FREIGHT_MIDDLE = 50;


        /**
         * 高运费价格（元）
         */
        public static final int FREIGHT_COST_HIGN = 8;
        /**
         * 低运费价格（元）
         */
        public static final int FREIGHT_COST_LOW = 6;
    }

    public final class MixDeploymentESType {
        private MixDeploymentESType() {
        }

        /**
         * 类型
         */
        public static final String RELATIONTYPE = "TENANT";

    }

    /**
     * 配置中心供应商屏蔽编码
     */
    public static final String ENABLE_HIDE_PURCHASE_SUPPLIER_INFO = "010909";

    /**
     * 协议商品审批配置中心编码
     */
    public static final String AGREEMENT_PRODUCT_APPROVE_CODE = "011027";

    public final class AgreementFrom {
        /**
         * 采购协议
         */
        public static final String PUR = "PUR";

        /**
         * 商城协议
         */
        public static final String MALL = "MALL";

        /**
         * 寻源结果
         */
        public static final String SOURCE = "SOURCE";

        /**
         * 价格库
         */
        public static final String PRICE = "PRICE";

        /**
         * 手工创建
         */
        public static final String MANUAL = "MANUAL";


    }

    /**
     * 商城协议物资类型
     */
    public final class MaterialType {

        /**
         * 普通物资
         */
        public static final String COMMEN_MATERIAL = "COMMEN_MATERIAL";

        /**
         * 自有商品/资产/服务
         */
        public static final String SELF_MATERIAL = "SELF_MATERIAL";

    }

    /**
     * 商城协议支付方式
     */
    public final class PaymentType {

        /**
         * 在线支付
         */
        public static final String ONLINE_PAYMENT = "ONLINE_PAYMENT";

        /**
         * 账期支付
         */
        public static final String PERIOD_PAYMENT = "PERIOD_PAYMENT";

        /**
         * 货到付款
         */
        public static final String DELIVERY_PAYMENT = "DELIVERY_PAYMENT";

        /**
         * 公司转账
         */
        public static final String COMPANY_PAYMENT = "COMPANY_PAYMENT";

        /**
         * 余额支付
         */
        public static final String BALANCE_PAYMENT = "BALANCE_PAYMENT";

        /**
         * 积分支付
         */
        public static final String POINT_PAYMENT = "POINT_PAYMENT";

        /**
         * 集中支付
         */
        public static final String CONCENTRATED_PAYMENT = "CONCENTRATED_PAYMENT";

    }

    /**
     * 商城协议商品映射方式
     */
    public final class SkuRefType {

        /**
         * 物料映射
         */
        public static final String REF_ITEM = "REF_ITEM";

        /**
         * 平台分类映射
         */
        public static final String REF_CATEGORY = "REF_CATEGORY";

    }

    public final class PoolStatus {
        /**
         * 未生效
         */
        public static final String INVALID = "INVALID";

        /**
         * 有效
         */
        public static final String VALID = "VALID";

        /**
         * 失效
         */
        public static final String FAILURE = "FAILURE";

    }

    /**
     * 协议类型
     */
    public final class AgreementType {
        /**
         * 未生效
         */
        public static final String STANDARD = "STANDARD";

        /**
         * 集团级
         */
        public static final int AGREEMENT_BELONG_TYPE_GROUP = -1;

        /**
         * 公司级
         */
        public static final int AGREEMENT_BELONG_TYPE_COMPANY = 1;

        /**
         * 销售协议（采销一体）
         */
        public static final String SALE = "SALE";
        /**
         * 采购协议（标准）
         */
        public static final String PURCHASE = "PURCHASE";

    }

    /**
     * 协议分配
     */
    public final class AgreementAssign {
        /**
         * 原始数据
         */
        public static final int ORIGINAL = 0;
        /**
         * 被分配数据
         */
        public static final int BE_ASSIGNED = 1;

        /**
         * 分配所有公司
         */
        public static final int ASSIGN_ALL_COMPANY = 1;

        /**
         * 分配部分公司
         */
        public static final int ASSING_PART_COMPANY = 0;


    }

    /**
     * 协议默认值
     */
    public final class Agreement {

        /**
         * 协议自动创建商品 默认分类编码
         */
        public static final String DEFAULT_PRODUCT_CATEGORY_CODE = "990000001101";

        public static final String ALL = "全";
        /**
         * 全 描述
         */
        public static final String ALL_DESC = "smal.agreement.line.all";

        /**
         * 所有组织
         */
        public static final String AGREEMENT_LINE_ALL_UNIT = "smal.agreement.line.all.unit";

        /**
         * 所有区域
         */
        public static final String AGREEMENT_LINE_ALL_REGION = "smal.agreement.line.all.region";
        /**
         * 自动创建协议默认时间
         */
        public static final String DEFAULT_DATE = "9999-12-31";

        /**
         * 默认批执行数量
         */
        public static final String BATCH_COUNT = "1";
        /**
         * 默认批执行值
         */
        public static final String BATCH_VALUE = "0";
        /**
         * 批执行字段名
         */
        public static final String BATCH_NAME = "batch";
        /**
         * 批执行字段值
         */
        public static final String BATCH_VALUE_NAME = "batchValue";
    }

    public final class ProductMatchStatus {
        /**
         * 新建
         */
        public static final String NEW = "NEW";

        /**
         * 提交
         */
        public static final String SUBMIT = "SUBMIT";

        /**
         * 已生效
         */
        public static final String EFFECTIVE = "EFFECTIVE";

        /**
         * 拒绝生效
         */
        public static final String REJECT_EFFECT = "REJECT_EFFECT";

        /**
         * 已失效
         */
        public static final String ALREADY_FAILURE = "ALREADY_FAILURE";

        /**
         * 待失效
         */
        public static final String FAILURE = "FAILURE";

        /**
         * 头-待审批
         */
        public static final String WAITING_APPROVAL = "WAITING_APPROVAL";

        /**
         * 头-部分生效
         */
        public static final String PART_EFFECTIVE = "PART_EFFECTIVE";

        /**
         * 头-待失效
         */
        public static final String WAITING_FAILURE = "WAITING_FAILURE";

        /**
         * 头-已生效
         */
        public static final String ALREADY_EFFECTIVE = "ALREADY_EFFECTIVE";

        /**
         * 头-拒绝生效
         */
        public static final String REJECT_FAILURE = "REJECT_FAILURE";

        /**
         * 头-未匹配
         */
        public static final String NOT_MATCH = "NOT_MATCH";

        /**
         * 头-已过期
         */
        public static final String OVERDUE = "OVERDUE";


    }


    /**
     * 常用标识
     */
    public final class Flags {

        /**
         * 已启用/已生效
         */
        public static final int ENABLE_FLAG = 1;

        /**
         * 禁用/失效
         */
        public static final int DISENABLE_FLAG = 0;

        /**
         * 删除
         */
        public static final int DELETE_VALUE = 2;

        /**
         * 集团公司
         */
        public static final long GROUP_COMPANY = -1L;

    }

    /**
     * 订单支付状态
     */
    public final class PaymentMessage {
        public static final String NON_PAYMENT = "NON-PAYMENT";
        public static final String SUCCESS = "支付成功";
        public static final String FAIL = "支付失败";
        public static final String MESSAGE_ABNORMAL = "支付信息异常";
        public static final String PROCESS_MESSAGE_ABNORMAL = "处理支付结果异常";
    }

    /**
     * 获取库存状态编号
     */
    public final class ProductStock {
        /**
         * 有货 现货-下单立即发货
         */
        public static final int STOCK_NUM_1 = 1;
        /**
         * 有货 在途-正在内部配货，预计2~6天到达本仓库
         */
        public static final int STOCK_NUM_2 = 2;
        /**
         * 有货 可配货-下单后从有货仓库配货
         */
        public static final int STOCK_NUM_3 = 3;
        /**
         * 预订
         */
        public static final int STOCK_NUM_4 = 4;
        /**
         * 无货
         */
        public static final int STOCK_NUM_5 = 5;
        /**
         * 无货开预定
         */
        public static final int STOCK_NUM_6 = 6;
    }

    /**
     * 商品属性
     */
    public final class ProductAttr {
        private ProductAttr() {
        }

        /**
         * 商品条形码
         */
        public static final String PRODUCT_BAR_CODE = "商品条形码";
    }

    /**
     * 配置中心编码
     */
    public final class SettingCenterCode {
        private SettingCenterCode() {
        }

        /**
         * 最小采买金额配置中心编码
         */
        public static final String MIN_PURCHASE_AMOUNT_CODE = "011025";

        /**
         * 是否弹出个人中心配置弹框
         */
        public static final String PERSONAL_CENTER_CONFIG = "011026";
    }

    public final class AgreementTypeCode {
        private AgreementTypeCode() {
        }

        /**
         * 普通协议
         */
        public static final String NORMAL = "NORMAL";

        /**
         * 框架协议
         */
        public static final String FRAME = "FRAME";
    }

    /**
     * 送货单行状态
     */
    public final class AsnLineStatus {
        private AsnLineStatus() {
        }

        /**
         * 接收完成
         */
        public static final String RECEIVE_COMPLETED = "RECEIVE_COMPLETED";

        /**
         * 部分接收
         */
        public static final String RECEIVE_PARTIAL = "RECEIVE_PARTIAL";

        /**
         * 未接受
         */
        public static final String RECEIVE_NONE = "RECEIVE_NONE";
    }

    /**
     * 商品类型
     */
    public final class ProductSkuType {
        private ProductSkuType() {
        }

        /**
         * 普通商品
         */
        public static final int ORDINARY_PRODUCT = 0;
        /**
         * 附件商品
         */
        public static final int ANNEX_PRODUCT = 1;
        /**
         * 赠品商品
         */
        public static final int GIFT_PRODUCT = 2;
        /**
         * 服务商品
         */
        public static final int SERVICE_PRODUCT = 3;
    }

    /**
     * 属性、属性值
     */
    public final class Attr {

        /**
         * 导入
         */
        public static final byte IMPORT = 0;

        /**
         * 平台正常新增
         */
        public static final byte NORMAL_CREATE = 1;

        /**
         * 有效
         */
        public static final byte IS_YN = 1;

        /**
         * 无效
         */
        public static final byte IS_NOT_YN = 0;

        /**
         * 导入
         */
        public static final boolean SOURCE_TYPE_IMPORT = false;

        /**
         * 平台新增
         */
        public static final boolean SOURCE_TYPE_CREATE = true;

        /**
         * 基本属性
         */
        public static final byte BASE_TYPE = 1;

        /**
         * 非基本属性
         */
        public static final byte OTHER_TYPE = 0;

        /**
         * 未删除
         */
        public static final boolean IS_NOT_DEL = true;

        /**
         * 删除
         */
        public static final boolean IS_DEL = false;

    }

    /**
     * ecClient电商账户表缓存key
     *
     * @param keys
     * @return
     */
    public static String ecClientKey(String... keys) {
        String prefix = CacheCode.SERVICE_NAME + ":" + CacheCode.EC_CLIENT + ":";
        if (Objects.isNull(keys) || keys.length < 1) {
            return null;
        }
        String key = "";
        for (String k : keys) {
            if (StringUtils.isEmpty(key)) {
                key = k;
            } else {
                key += "_" + k;
            }
        }
        return prefix + key;
    }

    /**
     * 消息类型
     */
    public final class MessageType {

        /**
         * 邮件
         */
        public static final String EMAIL = "EMAIL";
        /**
         * 短信
         */
        public static final String SMS = "SMS";
        /**
         * 站内消息
         */
        public static final String WEB = "WEB";
    }

    /**
     * 协议发起者类型
     */
    public final class PurOwnerType {
        private PurOwnerType() {
        }

        /**
         * 采购方发起
         */
        public static final String PURCHASE = "PURCHASE";
        /**
         * 供应商发起
         */
        public static final String SUPPLIER = "SUPPLIER";


    }

    public final class Pattern {
        private Pattern() {
        }

        public static final String INT_REGEX = "[0-9]*";
        /**
         * 阶梯价数字校验正则 1-100;100-200.01;200.01-;
         */
        public static final String LADDER_MOUNT_PATTEN = "^([\\d]+[\\.]?[\\d]*[-]([\\d]+[\\.]?[\\d]*)?[;]?)+$";

        /**
         * 阶梯价价格校验正则 134.1432;141234.1324
         */
        public static final String PRICE_PATTEN = "^([\\d]+[\\.]?[\\d]*[;]?)+$";

    }

    /**
     * Excel工具类错误码
     */
    public static class ExcelUtilErrorCode {
        /**
         * 设置单元格出错
         */
        public static final String ERROR_SET_CELL_VALUE = "error.set_cell_value";
        /**
         * 设置Sheet页数据出错
         */
        public static final String ERROR_SET_SHEET_BODY = "error.set_sheet_body";

        private ExcelUtilErrorCode() {
        }

        /**
         * 入参实体错误，实体未添加【@ExcelExportSheet】注解
         */
        public static final String ENTITY_DID_NOT_ADD_EXCELSHEET_ANNOTATION = "error.entity_did_not_add_ExcelSheet_annotation";

        /**
         * 必须有子数据
         */
        public static final String ERROR_MUST_HAVE_SUBDATA = "error.must_have_subdata";
        /**
         * 入参实体错误，实体未添加【@ExcelExportColumn】注解
         */
        public static final String ENTITY_DID_NOT_ADD_EXCELCOLUMN_ANNOTATION = "error.entity_did_not_add_ExcelColumn_annotation";


    }

    /**
     * 多语言
     */
    public final class Language {
        private Language() {
        }

        /**
         * 英文
         */
        public static final String EN = "en_US";
        /**
         * 中文
         */
        public static final String ZH = "zh_CN";

        /**
         * 日文
         */
        public static final String JP = "jp_JP";

    }

    /**
     * sku图片来源
     */
    public final class SkuImageType {

        /**
         * 来源url地址
         */
        public static final String URL = "URL";

        /**
         * 来源file文件
         */
        public static final String FILE = "FILE";
    }

    /**
     * 运费类型
     */
    public final class FreightType {

        private FreightType() {
        }

        /**
         * 运费单列
         */
        public static final String FREIGHT_LIST = "FREIGHT_LIST";

        /**
         * 运费分摊
         */
        public static final String FREIGHT_SHARE = "FREIGHT_SHARE";

        /**
         * 无运费
         */
        public static final String NO_FREIGHT = "NO_FREIGHT";

        /**
         * 免费
         */
        public static final String FREE = "FREE";
    }


    /**
     * 图片导入
     */
    public final class ImageImport {
        private ImageImport() {
        }

        /**
         * 新建
         */
        public static final String NEW = "NEW";

        /**
         * 校验成功
         */
        public static final String VALID_SUCCESS = "VALID_SUCCESS";

        /**
         * 校验失败
         */
        public static final String VALID_FAILED = "VALID_FAILED";

        /**
         * 导入成功
         */
        public static final String IMPORT_SUCCESS = "IMPORT_SUCCESS";

        /**
         * 导入失败
         */
        public static final String IMPORT_FAILED = "IMPORT_FAILED";

        /**
         * 解压文件出错
         */
        public static final String ERROR_IN_UNZIP = "error.in.unzip";

        /**
         * 找不到zip文件
         */
        public static final String CAN_NOT_FOUND_FILE = "can.not.found.file";

        /**
         * 上传文件失败
         */
        public static final String UPLOAD_IMAGE_FAILED = "upload.image.failed";

        /**
         * 该批次下无数据
         */
        public static final String NO_DATA_IN_THIS_BATCH = "no.data.in.this.batch";

        /**
         * 有数据未校验成功，不允许导入
         */
        public static final String CAN_NOT_IMPORT = "can.not.import";

        /**
         * zip文件超过512MB
         */
        public static final String ZIP_IS_OVERSIZE = "zip.is.oversize";
    }

    public final class PriceLibraryImport {
        private PriceLibraryImport() {
        }

        public static final String FAILURE_STATUS = "FAILURE";
    }


    /**
     * 消息发送配置编码&消息模板参数
     */
    public final class MallMessage {
        private MallMessage() {
        }

        /**
         * 商城协议发布消息通知
         */
        public static final String AGREEMENT_PUBLISH = "SMAL.AGREEMENT_PUBLISH.STATION";
        /**
         * 商城协议商品待审批通知
         */
        public static final String AGREEMENT_PRODUCT_PUBLISH = "SMAL.AGREEMENT_PRODUCT_PUBLISH";

        /**
         * 商城协议到期提醒
         */
        public static final String AGREEMENT_EXPIRES = "SMAL.AGREEMENT_EXPIRES";

        /**
         * 商品失效消息通知采购方
         */
        public static final String INVALID_SKU = "SMAL.INVALID_SKU";

        /**
         * 商城协议商品审批拒绝通知
         */
        public static final String AGREEMENT_PRODUCT_REJECT = "SMAL.AGREEMENT_PRODUCT_REJECT";

        /**
         * 消息模板配置参数：采购方公司名称
         */
        public static final String COMPANY_NAME = "COMPANY_NAME";
        /**
         * 消息模板配置参数：协议编码
         */
        public static final String AGREEMENT_NUMBER = "AGREEMENT_NUMBER";
        /**
         * 消息模板配置参数：协议编码链接
         */
        public static final String AGREEMENT_NUMBER_URL = "AGREEMENT_NUMBER_URL";
        /**
         * 消息模板配置参数：协议名称
         */
        public static final String AGREEMENT_NAME = "AGREEMENT_NAME";
        /**
         * 消息模板配置参数：待审批协议商品数量
         */
        public static final String AGREEMENT_QUANTITY = "AGREEMENT_QUANTITY";
        /**
         * 消息模板配置参数：当前时间
         */
        public static final String CURRENT_TIME = "CURRENT_TIME";
        /**
         * 消息模板配置参数：站内消息跳转链接
         */
        public static final String AGREEMENT_URL = "AGREEMENT_URL";
        /**
         * 消息模板配置参数：短信模板中的协议编码+名称
         */
        public static final String AGREEMENT = "AGREEMENT";

        /**
         * 消息发送参数：租户id
         */
        public static final String ORGANIZATION_ID = "organizationId";
        /**
         * 消息发送参数：公司id
         */
        public static final String COMPANY_ID = "companyId";
        /**
         * 消息发送参数：供应商租户id
         */
        public static final String SUPPLIER_TENANT_ID = "supplierTenantId";
        /**
         * 消息发送参数：供应商公司id
         */
        public static final String SUPPLIER_COMPANY_ID = "supplierCompanyId";

        /**
         * 下单时间
         */
        public static final String ORDER_TIME = "ORDER_TIME";

        /**
         * po单号
         */
        public static final String PO_NO = "PO_NO";

        /**
         * 含税金额
         */
        public static final String ORDER_AMOUNT = "ORDER_AMOUNT";

        /**
         * 下单人姓名
         */
        public static final String ORDER_PERSON_NAME = "ORDER_PERSON_NAME";

        /**
         * 下单人手机号
         */
        public static final String ORDER_PERSON_PHONE = "ORDER_PERSON_PHONE";

        /**
         * 下单人邮箱
         */
        public static final String ORDER_PERSON_EMIAL = "ORDER_PERSON_EMIAL";

        /**
         * 订单行信息
         */
        public static final String PO_LINES = "PO_LINES";

        /**
         * ponchout下单消息
         */
        public static final String PUNCHOUT_ORDER = "SMAL.PUNCHOUT_ORDER";

        /**
         * 到期协议编号
         */
        public static final String AGREEMENT_NUM_LIST = "AGREEMENT_NUM_LIST";

        /**
         * 用户id
         */
        public static final String USER_ID = "userId";

        /**
         * 供应商公司名称
         */
        public static final String SUPPLIER_COMPANY_NAME = "supplierCompanyName";

        /**
         * 商品编码
         */
        public static final String SKU_CODE = "skuCode";

        /**
         * 跳转链接
         */
        public static final String SKU_CODE_URL = "skuCodeUrl";

        /**
         * 时间
         */
        public static final String DATE = "date";

        /**
         * ponchout下单消息亚速旺
         */
        public static final String PUNCHOUT_YSW_ORDER = "SMAL.PUNCHOUT_YSW_ORDER";

        /**
         * 协议行到期提前提醒
         */
        public static final String SAGM_AGREE_PRODUCT_EXPIRATION = "SAGM.AGREE_PRODUCT_EXPIRATION";

    }


    /**
     * 送货单地址规则定义相关编码
     */
    public final class AsnCode {
        private AsnCode() {
        }

        public static final String LOGISTICS_NUM = "LOGISTICS_NUM";
        public static final String STREAM_CODE = "STREAM_CODE";

    }


    /**
     * 业务规则定义相关编码
     */
    public final class ConfigCenterCode {

        private ConfigCenterCode() {
        }


        /**
         * 协议价格库视图来源
         */
        public static final String PRICE_LIB_VIEW_CODE = "SITE.SMAL.AGREEMENT.PRICE_LIB_VIEW_CODE";

        /**
         * 商城协议审批方式-业务规则编码
         */
        public static final String APPROVAL_METHOD = "SITE.SMAL.AGREEMENT.APPROVAL_METHOD";
        /**
         * 商城协议发布方式-业务规则编码
         */
        public static final String RELEASE_METHOD = "SITE.SMAL.AGREEMENT.RELEASE_METHOD";
        /**
         * 商品审批业务编码
         */
        public static final String PRODUCT_APPROVAL_METHOD = "SITE.SMAL.PRODUCT.PRODUCT_APPROVAL";
        /**
         * 业务规则-是否自动创建商品
         */
        public static final String AGREEMENT_AUTO_CREATE_PRODUCT = "SITE.SMAL.AGREEMENT.AUTO_CREATE_PRODUCT";

        /**
         * 商品审批业务编码
         */
        public static final String PRODUCT_EDIT_MOD = "SITE.SMAL.PRODUCT.SUPPLIER_EDIT_MOD";

        /**
         * 审批方式-功能性审批
         */
        public static final String APPROVAL_FUNCTIONAL = "FUNCTIONAL";
        /**
         * 审批方式-自审批/无需审批
         */
        public static final String APPROVAL_SELF = "SELF";

        /**
         * 发布方式-手工发布
         */
        public static final String RELEASE_MANUAL = "MANUAL";
        /**
         * 发布方式-自动发布
         */
        public static final String RELEASE_AUTO = "AUTO";

        /**
         * 商城协议商品审批方式-业务规则编码
         */
        public static final String PRE_PRODUCT_REDIRECT = "SITE.SMAL.PRE_PURCHASE.REDIRECT_TO_WORKSPACE";

        /**
         * 价格库发布后是否自动创建协议配置-业务规则编码
         */
        public static final String AUTO_CREATE_AGREEMENT = "SITE.SMAL.AGREEMENT.AUTO_CREATE_AGREEMENT";

        /**
         * 是否启用订单中心配置
         */
        public static final String ENABLE_ORDER_CENTER_FLAG = "SITE.SMAL.ENABLE_ORDER_CENTER_FLAG";

        /**
         * 是否启目录化商品库存校验
         */
        public static final String ENABLE_SKU_STOCK_CHECK = "SITE.SMAL.ENABLE_SKU_STOCK_CHECK";

        /**
         * 售后审批方式
         */
        public static final String AFTER_SALE_APPROVAL_TYPE = "SITE.SMAL.MAINSITE.AFTER_SALE_APPROVE";

        /**
         * 主站组织展示配置
         */
        public static final String SITE_SMAL_MAINSITE_ORGANIZATION_NAME_DISPLAY = "SITE.SMAL.MAINSITE.ORGANIZATION_NAME_DISPLAY";

        /**
         * 是否启用混合部署
         */
        public static final String ENABLE_MIX_DEPLOY_FLAG = "SITE.SMAL.ENABLE_MIX_DEPLOY_FLAG";

        /**
         * 选择送货单号的规则
         */
        public static final String SITE_SMAL_ECORDER_DELIVERY_NOTICE_CREATE_ASN_NUM = "SITE.SMAL.ECORDER_DELIVERY_NOTICE_CREATE_ASN_NUM";

        /**
         * 协议商品到期业务规则配置
         */
        public static final String SITE_SMAL_AGREEMENT_PRODUCT_EXPIRATION = "SITE.SMAL.AGREEMENT.PRODUCT_EXPIRATION";

        /**
         * 协议商品到期业务规则配置-角色
         */
        public static final String SITE_SMAL_AGREEMENT_PRODUCT_EXPIRATION_ROLE = "SITE.SMAL.AGREEMENT.PRODUCT_EXPIRATION_ROLE";

    }

    public final class AgreementImport {
        private AgreementImport() {
        }

        public static final String ALL_COMPANY = "所有公司";
        public static final String ALL_UNIT = "所有组织";
        public static final String ALL_REGION = "所有区域";
        public static final String CREATE_SKU_YES = "是";
        public static final String IS_LADDER_YES = "是";
        public static final String MAX_COUNT = "9999999999";
        public static final String DEFAULT_POSTAGE = "包邮";

    }

    /**
     * 销售协议商品临时表状态
     */
    public final class SaleSkuTempStatus {
        private SaleSkuTempStatus() {
        }

        /**
         * 新增/更新
         */
        public static final String UPSERT = "UPSERT";
        /**
         * 删除
         */
        public static final String DELETE = "DELETE";
    }


    /**
     * 是否启用订单中心
     *
     * @param tenantId
     * @return
     */
    public static Boolean enableOrderCenterFlag(Long tenantId) {
        try {
            Integer omsFlag = Integer.parseInt(ConfigCenterHelper.select(tenantId, ConfigCenterCode.ENABLE_ORDER_CENTER_FLAG)
                    .invokeWithCustomizeUnit(null));
            if (Objects.nonNull(omsFlag) && omsFlag.equals(BaseConstants.Flag.YES)) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    /**
     * 权限来源
     */
    public static final class AuthAgreementType {
        private AuthAgreementType() {
        }

        /**
         * 手工新建
         */
        public static final String MANUAL = "MANUAL";

        /**
         * 销售协议
         */
        public static final String SALE_AGREEMENT = "SALE_AGREEMENT";

        /**
         * 采购协议
         */
        public static final String PUR_AGREEMENT = "PUR_AGREEMENT";
    }

    /**
     * 主站组织展示值
     */
    public final class UnitAliasConfig {
        private UnitAliasConfig() {

        }

        /**
         * 编码-名称
         */
        public static final String CODE_NAME = "CODE_NAME";
        /**
         * 编码-名称-公司名
         */
        public static final String CODE_NAME_COMPANY = "CODE_NAME_COMPANY";
        /**
         * 别名
         */
        public static final String ALIAS_NAME = "ALIAS_NAME";
    }

    /**
     * 配置中心配置类型
     */
    public final class PageConfigType {
        private PageConfigType() {
        }

        /**
         * 底部信息栏
         */
        public static final String BOTTOM_INFO = "BOTTOM_INFO";

        /**
         * 二维码
         */
        public static final String QR_CODE = "QR_CODE";

        /**
         * 快速链接
         */
        public static final String QUICK_LINK = "QUICK_LINK";
    }

    public final class ProductCatalogSourceFrom {
        private ProductCatalogSourceFrom() {

        }

        public static final String AGREEMENT = "AGREEMENT";
    }

    /*
     * 商品来源类型
     */
    public final class ProductSourceFromType {
        public ProductSourceFromType() {
        }

        /*
         * 同步
         */
        public static final String SYNCHRONIZE = "SYNCHRONIZE";

        /**
         * 手工创建
         */
        public static final String MANUAL = "MANUAL";

        /**
         * 基于协议创建
         */
        public static final String AGREEMENT = "AGREEMENT";
    }

    public final class EventCode {
        private EventCode() {

        }

        /**
         * 自动创建协议事件发送
         */
        public static final String AUTO_CREATE_AGREEMENT = "smal_auto_create_agreement";

        /**
         * oms订单数据一致性
         */
        public static final String SMAL_OMS_ORDER = "smal_order_confirm";

    }

    /**
     * SAGM.PRICE_SECRET_TYPE 协议行价格保密类型
     */
    public final class PriceSecretType {
        public PriceSecretType() {

        }

        /**
         * 隐藏价格
         */
        public static final String HIDE = "HIDE";

        /**
         * 显示参考价
         */
        public static final String REFERENCE = "REFERENCE";

        /**
         * 不保密
         */
        public static final String NONE = "NONE";
    }

    /**
     * Banner 状态
     */
    public final class ShelfStatus {
        private ShelfStatus() {
        }

        /**
         * 已上架
         */
        public static final int SHELF = 1;

        /**
         * 自动下架/已下架
         */
        public static final int UNSHELF = 0;

        /**
         * 待上架
         */
        public static final int WAITING_SHELF = 4;

        /**
         * 已失效
         */
        public static final int EXPIRED = -1;

        /**
         * 手动下架
         */
        public static final int MANUAL_UNSHELF = 2;

        /**
         * 上架失败
         */
        public static final int SHELF_FAILED = 3;

        /**
         * 已解锁
         */
        public static final int UNLOCK = 1;

        /**
         * 已锁定
         */
        public static final int LOCK = 0;

        /**
         * 逻辑删除
         */
        public static final int LOGIC_DELETE_FLAG = 1;

        /**
         * 默认逻辑删除标识
         */
        public static final int DEFAULT_LOGIC_DELETE_FLAG = 0;


    }

}
