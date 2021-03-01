package org.srm.mall.common.feign.dto.product;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.choerodon.mybatis.domain.AuditDomain;
import io.swagger.annotations.ApiModelProperty;
import org.hzero.starter.keyencrypt.core.Encrypt;

import java.math.BigDecimal;
import javax.persistence.Transient;

/**
 * 目录价格限制
 *
 * @author yuhao.guo@hand-china.com 2021-02-23 20:52:06
 */
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class CatalogPriceLimit extends AuditDomain {

    public static final String FIELD_ID = "id";
    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_USER_ID = "userId";
    public static final String FIELD_CATALOG_ID = "catalogId";
    public static final String FIELD_CURRENCY_ID = "currencyId";
    public static final String FIELD_LIMIT_PRICE = "limitPrice";

    //
    // 业务方法(按public protected private顺序排列)
    // ------------------------------------------------------------------------------

    public CatalogPriceLimit() {
    }

    public CatalogPriceLimit(Long tenantId, Long userId, Long skuId, Long catalogId, BigDecimal price) {
        this.tenantId = tenantId;
        this.userId = userId;
        this.skuId = skuId;
        this.checkPrice = price;
        this.catalogId = catalogId;
    }

    //
    // 数据库字段
    // ------------------------------------------------------------------------------


    @ApiModelProperty("表ID")
    @Encrypt
    private Long id;
    @ApiModelProperty(value = "租户id", required = true)
    private Long tenantId;
    @ApiModelProperty(value = "用户id", required = true)
    @Encrypt
    private Long userId;
    @ApiModelProperty(value = "三级目录id", required = true)
    @Encrypt
    private Long catalogId;
    @ApiModelProperty(value = "币种id(关联smdm_currency表的currency_id)", required = true)
    @Encrypt
    private Long currencyId;
    @ApiModelProperty(value = "限制最高价格", required = true)
    private BigDecimal limitPrice;

    //
    // 非数据库字段
    // ------------------------------------------------------------------------------

    @Transient
    private String catalogCode;
    @Transient
    private String catalogName;
    @Transient
    private String currencyCode;
    @Transient
    private String currencyName;
    @Transient
    private BigDecimal checkPrice;
    @Transient
    @Encrypt
    private Long skuId;

    //
    // getter/setter
    // ------------------------------------------------------------------------------

    /**
     * @return 表ID
     */
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return 租户id
     */
    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    /**
     * @return 用户id
     */
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * @return 三级目录id
     */
    public Long getCatalogId() {
        return catalogId;
    }

    public void setCatalogId(Long catalogId) {
        this.catalogId = catalogId;
    }

    /**
     * @return 币种id(关联smdm_currency表的currency_id)
     */
    public Long getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(Long currencyId) {
        this.currencyId = currencyId;
    }

    /**
     * @return 限制最高价格
     */
    public BigDecimal getLimitPrice() {
        return limitPrice;
    }

    public void setLimitPrice(BigDecimal limitPrice) {
        this.limitPrice = limitPrice;
    }

    public String getCatalogCode() {
        return catalogCode;
    }

    public void setCatalogCode(String catalogCode) {
        this.catalogCode = catalogCode;
    }

    public String getCatalogName() {
        return catalogName;
    }

    public void setCatalogName(String catalogName) {
        this.catalogName = catalogName;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    public BigDecimal getCheckPrice() {
        return checkPrice;
    }

    public void setCheckPrice(BigDecimal checkPrice) {
        this.checkPrice = checkPrice;
    }

    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

}
