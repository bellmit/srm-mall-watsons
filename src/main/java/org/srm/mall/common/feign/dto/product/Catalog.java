package org.srm.mall.common.feign.dto.product;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.MultiLanguage;
import io.choerodon.mybatis.annotation.MultiLanguageField;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.choerodon.mybatis.domain.AuditDomain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hzero.core.base.BaseConstants;
import org.hzero.starter.keyencrypt.core.Encrypt;

import java.util.List;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 目录
 *
 * @author min.wang01@hand-china.com 2020-12-08 15:43:32
 */
@ApiModel("目录")
@VersionAudit
@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@MultiLanguage
@Table(name = "smpc_catalog")
public class Catalog extends AuditDomain {

    public static final String FIELD_CATALOG_ID = "catalogId";
    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_CATALOG_CODE = "catalogCode";
    public static final String FIELD_CATALOG_NAME = "catalogName";
    public static final String FIELD_LEVEL = "level";
    public static final String FIELD_LEVEL_PATH = "levelPath";
    public static final String FIELD_ORDER_SEQ = "orderSeq";
    public static final String FIELD_PARENT_CATALOG_ID = "parentCatalogId";
    public static final String FIELD_SOURCE_FROM = "sourceFrom";
    public static final String FIELD_CATEGORY_ID = "categoryId";
    public static final String FIELD_ICON_URL = "iconUrl";
    public static final String FIELD_ENABLED_FLAG = "enabledFlag";

    //
    // 业务方法(按public protected private顺序排列)
    // ------------------------------------------------------------------------------

    public Catalog() {}

    public Catalog(SpuPublishDTO spuPublishDTO) {
        this.tenantId = spuPublishDTO.getTenantId();
        this.catalogId = spuPublishDTO.getCatalogId();
        this.catalogCode = spuPublishDTO.getCatalogCode();
        this.enabledFlag = BaseConstants.Flag.YES;
    }

    //
    // 数据库字段
    // ------------------------------------------------------------------------------


    @ApiModelProperty("")
    @Id
    @GeneratedValue
    @Encrypt
    private Long catalogId;
    @ApiModelProperty(value = "租户ID  hpfm_tenant.tenant_id", required = true)
    @NotNull
    private Long tenantId;
    @ApiModelProperty(value = "目录编码", required = true)
    @NotBlank
    private String catalogCode;
    @ApiModelProperty(value = "目录名称", required = true)
    @NotBlank
    @MultiLanguageField
    private String catalogName;
    @ApiModelProperty(value = "目录层级", required = true)
    @NotNull
    private Integer level;
    @ApiModelProperty(value = "目录路径，编码路径，使用 | 分隔")
    private String levelPath;
    @ApiModelProperty(value = "排序号", required = true)
    @NotNull
    private Long orderSeq;
    @ApiModelProperty(value = "上级目录ID，顶级目录为-1  smpc_catalog.catalog_id", required = true)
    @NotNull
    @Encrypt(ignoreValue = {"-1"})
    private Long parentCatalogId;
    @ApiModelProperty(value = "目录来源 smpc.CATALOG_SOURCE_FROM  手工-MANUAL/分类引用-CATEGROY", required = true)
    @NotBlank
    private String sourceFrom;
    @ApiModelProperty(value = "分类ID smpc_category.category_id", required = true)
    @NotNull
    @Encrypt
    private Long categoryId;
    @ApiModelProperty(value = "图标url 主要用于移动端三级目录")
    private String iconUrl;
    @ApiModelProperty(value = "启用标识 1启用 0禁用", required = true)
    @NotNull
    private Integer enabledFlag;


    //
    // 非数据库字段
    // ------------------------------------------------------------------------------
    /**
     * 临时字段，用来存放目录查询时冗余的层级目录ID及层级
     */
    @Transient
    private Long secondCatalogId;
    @Transient
    private Long secondLevel;
    @Transient
    private Long rootCatalogId;
    @Transient
    private Long rootLevel;
    @ApiModelProperty(value = "父目录编码")
    @Transient
    private String parentCatalogCode;
    @Transient
    private List<Catalog> subCatalogs;
    @Transient
    private Long skuId;
    //
    // getter/setter


    public Long getSecondCatalogId() {
        return secondCatalogId;
    }

    public void setSecondCatalogId(Long secondCatalogId) {
        this.secondCatalogId = secondCatalogId;
    }

    public Long getSecondLevel() {
        return secondLevel;
    }

    public void setSecondLevel(Long secondLevel) {
        this.secondLevel = secondLevel;
    }

    public Long getRootCatalogId() {
        return rootCatalogId;
    }

    public void setRootCatalogId(Long rootCatalogId) {
        this.rootCatalogId = rootCatalogId;
    }

    public Long getRootLevel() {
        return rootLevel;
    }

    public void setRootLevel(Long rootLevel) {
        this.rootLevel = rootLevel;
    }

    public String getParentCatalogCode() {
        return parentCatalogCode;
    }

    public void setParentCatalogCode(String parentCatalogCode) {
        this.parentCatalogCode = parentCatalogCode;
    }

    public List<Catalog> getSubCatalogs() {
        return subCatalogs;
    }

    public void setSubCatalogs(List<Catalog> subCatalogs) {
        this.subCatalogs = subCatalogs;
    }

    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    // ------------------------------------------------------------------------------

    /**
     * @return
     */
    public Long getCatalogId() {
        return catalogId;
    }

    public Catalog setCatalogId(Long catalogId) {
        this.catalogId = catalogId;
        return this;
    }

    /**
     * @return 租户ID  hpfm_tenant.tenant_id
     */
    public Long getTenantId() {
        return tenantId;
    }

    public Catalog setTenantId(Long tenantId) {
        this.tenantId = tenantId;
        return this;
    }

    /**
     * @return 目录编码
     */
    public String getCatalogCode() {
        return catalogCode;
    }

    public Catalog setCatalogCode(String catalogCode) {
        this.catalogCode = catalogCode;
        return this;
    }

    /**
     * @return 目录名称
     */
    public String getCatalogName() {
        return catalogName;
    }

    public Catalog setCatalogName(String catalogName) {
        this.catalogName = catalogName;
        return this;
    }

    /**
     * @return 目录层级
     */
    public Integer getLevel() {
        return level;
    }

    public Catalog setLevel(Integer level) {
        this.level = level;
        return this;
    }

    /**
     * @return 目录路径，编码路径，使用 | 分隔
     */
    public String getLevelPath() {
        return levelPath;
    }

    public void setLevelPath(String levelPath) {
        this.levelPath = levelPath;
    }

    /**
     * @return 排序号
     */
    public Long getOrderSeq() {
        return orderSeq;
    }

    public void setOrderSeq(Long orderSeq) {
        this.orderSeq = orderSeq;
    }

    /**
     * @return 上级目录ID，顶级目录为-1  smpc_catalog.catalog_id
     */
    public Long getParentCatalogId() {
        return parentCatalogId;
    }

    public Catalog
    setParentCatalogId(Long parentCatalogId) {
        this.parentCatalogId = parentCatalogId;
        return this;
    }

    /**
     * @return 目录来源 smpc.CATALOG_SOURCE_FROM  手工-MANUAL/分类引用-CATEGROY
     */
    public String getSourceFrom() {
        return sourceFrom;
    }

    public Catalog setSourceFrom(String sourceFrom) {
        this.sourceFrom = sourceFrom;
        return this;
    }

    /**
     * @return 分类ID smpc_category.category_id
     */
    public Long getCategoryId() {
        return categoryId;
    }

    public Catalog setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
        return this;
    }

    /**
     * @return 图标url 主要用于移动端三级目录
     */
    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    /**
     * @return 启用标识
     */
    public Integer getEnabledFlag() {
        return enabledFlag;
    }

    public Catalog setEnabledFlag(Integer enabledFlag) {
        this.enabledFlag = enabledFlag;
        return this;
    }

    /**
     * 目录是否启用
     *
     * @return boolean
     */
    public boolean isEnabled() {
        return SmpcConstants.Catalog.CATALOG_ENABLED.equals(this.enabledFlag);
    }
}
