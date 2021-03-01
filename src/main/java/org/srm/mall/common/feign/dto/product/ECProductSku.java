package org.srm.mall.common.feign.dto.product;

import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.MultiLanguage;
import io.choerodon.mybatis.annotation.MultiLanguageField;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.choerodon.mybatis.domain.AuditDomain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hzero.starter.keyencrypt.core.Encrypt;

import java.math.BigDecimal;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 店铺商品SKU
 *
 * @author nie.liang@hand-china.com 2019-12-03 16:51:32
 */
@ApiModel("店铺商品SKU")
@VersionAudit
@ModifyAudit
@MultiLanguage
@Table(name = "scec_product_sku")
public class ECProductSku extends AuditDomain {

    public static final String FIELD_ID = "id";
    public static final String FIELD_CODE = "code";
    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_SKU_NAME = "skuName";
    public static final String FIELD_SALE_STATUS = "saleStatus";
    public static final String FIELD_SPU_ID = "spuId";
    public static final String FIELD_COMPANY_ID = "companyId";
    public static final String FIELD_SOURCE_FROM = "sourceFrom";
    public static final String FIELD_SELL_PRICE = "sellPrice";
    public static final String FIELD_PRICE = "price";
    public static final String FIELD_NUM = "num";
    public static final String FIELD_MARKET_PRICE = "marketPrice";
    public static final String FIELD_PACKING_LIST = "packingList";
    public static final String FIELD_SKU_TYPE = "skuType";
    public static final String FIELD_LOCK_STATUS = "lockStatus";
    public static final String FIELD_THIRD_SKU_ID = "thirdSkuId";
    public static final String FIELD_YN = "yn";
    public static final String FIELD_ATTACHMENT_UUID_AS = "attachmentUuidAs";
    public static final String FIELD_CID = "cid";
    public static final String FIELD_SHELF_STATUS = "shelfStatus";
    public static final String FIELD_SCORE = "score";
    public static final String FIELD_COMMENT_NUMBER = "commentNumber";
    //
    // 业务方法(按public protected private顺序排列)
    // ------------------------------------------------------------------------------

    //
    // 数据库字段
    // ------------------------------------------------------------------------------


    @ApiModelProperty("pk")
    @Id
    @GeneratedValue
    private Long id;
    @ApiModelProperty(value = "")
    private String code;
    @ApiModelProperty(value = "平台id", required = true)
    @NotNull
    private Long tenantId;
    @ApiModelProperty(value = "sku名称", required = true)
    @NotBlank
    @MultiLanguageField
    private String skuName;
    @ApiModelProperty(value = "")
    private Integer saleStatus;
    @ApiModelProperty(value = "商品id", required = true)
    @NotNull
    @Encrypt
    private Long spuId;
    @ApiModelProperty(value = "供应商简码或id（线上的话则与tenant_id一致）", required = true)
    @NotNull
    @Encrypt
    private Long companyId;
    @ApiModelProperty(value = "目录化：CATA；电商：EC", required = true)
    @NotBlank
    private String sourceFrom;
    @ApiModelProperty(value = "")
    private BigDecimal sellPrice;
    @ApiModelProperty(value = "基础价格（元）", required = true)
    @NotNull
    private BigDecimal price;
    @ApiModelProperty(value = "", required = true)
    @NotNull
    private Long num;
    @ApiModelProperty(value = "")
    private BigDecimal marketPrice;
    @ApiModelProperty(value = "")
    private String packingList;
    @ApiModelProperty(value = "sku 类型 1:主sku,0:非主sku")
    private Integer skuType;
    @ApiModelProperty(value = "sku锁定状态：0：锁定 1：解锁")
    private Integer lockStatus;
    @ApiModelProperty(value = "电商sku_id")
    private String thirdSkuId;
    @ApiModelProperty(value = "是否删除 0删除 1有效", required = true)
    @NotNull
    private Integer yn;
    @ApiModelProperty(value = "")
    private String attachmentUuidAs;
    @ApiModelProperty(value = "三级分类ID", required = true)
    @NotNull
    @Encrypt
    private Long cid;
    @ApiModelProperty(value = "上架状态")
    private Long shelfStatus;
    @ApiModelProperty(value = "采购方租户")
    private Long purchaseTenantId;
    @ApiModelProperty(value = "评分")
    private BigDecimal score;
    @ApiModelProperty("评论人数")
    private Long commentNumber;

    //
    // 非数据库字段
    // ------------------------------------------------------------------------------

    @Transient
    private String codeAndSkuName;

    @Transient
    private String thirdSpuId;

    @Transient
    private String spuName;
    //
    // getter/setter
    // ------------------------------------------------------------------------------


    public String getThirdSpuId() {
        return thirdSpuId;
    }

    public void setThirdSpuId(String thirdSpuId) {
        this.thirdSpuId = thirdSpuId;
    }

    public String getSpuName() {
        return spuName;
    }

    public void setSpuName(String spuName) {
        this.spuName = spuName;
    }

    /**
     * @return pk
     */
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return
     */
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    /**
     * @return 平台id
     */
    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    /**
     * @return sku名称
     */
    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    /**
     * @return
     */
    public Integer getSaleStatus() {
        return saleStatus;
    }

    public void setSaleStatus(Integer saleStatus) {
        this.saleStatus = saleStatus;
    }

    /**
     * @return 商品id
     */
    public Long getSpuId() {
        return spuId;
    }

    public void setSpuId(Long spuId) {
        this.spuId = spuId;
    }

    /**
     * @return 供应商简码或id（线上的话则与tenant_id一致）
     */
    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    /**
     * @return 目录化：CATA；电商：EC
     */
    public String getSourceFrom() {
        return sourceFrom;
    }

    public void setSourceFrom(String sourceFrom) {
        this.sourceFrom = sourceFrom;
    }

    /**
     * @return
     */
    public BigDecimal getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(BigDecimal sellPrice) {
        this.sellPrice = sellPrice;
    }

    /**
     * @return 基础价格（元）
     */
    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    /**
     * @return
     */
    public Long getNum() {
        return num;
    }

    public void setNum(Long num) {
        this.num = num;
    }

    /**
     * @return
     */
    public BigDecimal getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(BigDecimal marketPrice) {
        this.marketPrice = marketPrice;
    }

    /**
     * @return
     */
    public String getPackingList() {
        return packingList;
    }

    public void setPackingList(String packingList) {
        this.packingList = packingList;
    }

    /**
     * @return sku 类型 1:主sku,0:非主sku
     */
    public Integer getSkuType() {
        return skuType;
    }

    public void setSkuType(Integer skuType) {
        this.skuType = skuType;
    }

    /**
     * @return sku锁定状态：0：锁定 1：解锁
     */
    public Integer getLockStatus() {
        return lockStatus;
    }

    public void setLockStatus(Integer lockStatus) {
        this.lockStatus = lockStatus;
    }

    /**
     * @return 电商sku_id
     */
    public String getThirdSkuId() {
        return thirdSkuId;
    }

    public void setThirdSkuId(String thirdSkuId) {
        this.thirdSkuId = thirdSkuId;
    }

    /**
     * @return 是否删除 0删除 1有效
     */
    public Integer getYn() {
        return yn;
    }

    public void setYn(Integer yn) {
        this.yn = yn;
    }

    /**
     * @return
     */
    public String getAttachmentUuidAs() {
        return attachmentUuidAs;
    }

    public void setAttachmentUuidAs(String attachmentUuidAs) {
        this.attachmentUuidAs = attachmentUuidAs;
    }

    /**
     * @return 三级分类ID
     */
    public Long getCid() {
        return cid;
    }

    public void setCid(Long cid) {
        this.cid = cid;
    }

    /**
     * @return 上架状态
     */
    public Long getShelfStatus() {
        return shelfStatus;
    }

    public void setShelfStatus(Long shelfStatus) {
        this.shelfStatus = shelfStatus;
    }

    public Long getPurchaseTenantId() {
        return purchaseTenantId;
    }

    public void setPurchaseTenantId(Long purchaseTenantId) {
        this.purchaseTenantId = purchaseTenantId;
    }

    public BigDecimal getScore() {
        return score;
    }

    public void setScore(BigDecimal score) {
        this.score = score;
    }

    public Long getCommentNumber() {
        return commentNumber;
    }

    public void setCommentNumber(Long commentNumber) {
        this.commentNumber = commentNumber;
    }

    public String getCodeAndSkuName() {
        return codeAndSkuName;
    }

    public void setCodeAndSkuName(String codeAndSkuName) {
        this.codeAndSkuName = codeAndSkuName;
    }
}
