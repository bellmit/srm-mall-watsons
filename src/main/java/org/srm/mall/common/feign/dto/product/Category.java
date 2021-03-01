package org.srm.mall.common.feign.dto.product;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.MultiLanguage;
import io.choerodon.mybatis.annotation.MultiLanguageField;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.choerodon.mybatis.domain.AuditDomain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hzero.starter.keyencrypt.core.Encrypt;

import java.util.List;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 分类
 *
 * @author yuhao.guo@hand-china.com 2020-12-08 10:22:38
 */
@ApiModel("分类")
@VersionAudit
@ModifyAudit
@MultiLanguage
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "smpc_category")
public class Category extends AuditDomain {

    public static final String FIELD_CATEGORY_ID = "categoryId";
    public static final String FIELD_CATEGORY_CODE = "categoryCode";
    public static final String FIELD_CATEGORY_NAME = "categoryName";
    public static final String FIELD_ORDER_SEQ = "orderSeq";
    public static final String FIELD_PARENT_ID = "parentId";
    public static final String FIELD_LEVEL_PATH = "levelPath";
    public static final String FIELD_LEVEL = "level";
    public static final String FIELD_ENABLED_FLAG = "enabledFlag";

    //
    // 业务方法(按public protected private顺序排列)
    // ------------------------------------------------------------------------------

    //
    // 数据库字段
    // ------------------------------------------------------------------------------


    @ApiModelProperty("")
    @Id
    @GeneratedValue
    @Encrypt
    private Long categoryId;
    @ApiModelProperty(value = "分类编码", required = true)
    @NotBlank
    private String categoryCode;
    @ApiModelProperty(value = "分类名称", required = true)
    @NotBlank
    @MultiLanguageField
    private String categoryName;
    @ApiModelProperty(value = "排序", required = true)
    @NotNull
    private Long orderSeq;
    @ApiModelProperty(value = "父id", required = true)
    @NotNull
    @Encrypt
    private Long parentId;
    @ApiModelProperty(value = "编码层级")
    private String levelPath;
    @ApiModelProperty(value = "分类标记(1,2,3级)", required = true)
    @NotNull
    private Integer level;
    @ApiModelProperty(value = "是否有效 0无效 1有效", required = true)
    @NotNull
    private Integer enabledFlag;

    //
    // 非数据库字段
    // ------------------------------------------------------------------------------
    @Transient
    private String parentCategoryName;
    @Transient
    private Long skuId;

    /**
     * 下级类目
     */
    @Transient
    private List<Category> children;

    //
    // getter/setter

    // ------------------------------------------------------------------------------

    /**
     * @return
     */
    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    /**
     * @return 分类编码
     */
    public String getCategoryCode() {
        return categoryCode;
    }

    public Category setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
        return this;
    }

    /**
     * @return 分类名称
     */
    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    /**
     * @return 排序
     */
    public Long getOrderSeq() {
        return orderSeq;
    }

    public void setOrderSeq(Long orderSeq) {
        this.orderSeq = orderSeq;
    }

    /**
     * @return 父id
     */
    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    /**
     * @return 编码层级
     */
    public String getLevelPath() {
        return levelPath;
    }

    public void setLevelPath(String levelPath) {
        this.levelPath = levelPath;
    }

    /**
     * @return 分类标记(1, 2, 3级)
     */
    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    /**
     * @return 是否有效 0无效 1有效
     */
    public Integer getEnabledFlag() {
        return enabledFlag;
    }

    public void setEnabledFlag(Integer enabledFlag) {
        this.enabledFlag = enabledFlag;
    }

    public String getParentCategoryName() {
        return parentCategoryName;
    }

    public void setParentCategoryName(String parentCategoryName) {
        this.parentCategoryName = parentCategoryName;
    }

    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    public List<Category> getChildren() {
        return children;
    }

    public void setChildren(List<Category> children) {
        this.children = children;
    }
}
