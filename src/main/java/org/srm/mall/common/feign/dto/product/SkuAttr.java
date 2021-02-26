package org.srm.mall.common.feign.dto.product;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.choerodon.mybatis.domain.AuditDomain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hzero.starter.keyencrypt.core.Encrypt;

import java.util.Objects;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

/**
 * sku规格参数表
 *
 * @author yuhao.guo@hand-china.com 2020-12-15 21:06:24
 */
@ApiModel("sku规格参数表")
@VersionAudit
@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "smpc_sku_attr")
public class SkuAttr extends AuditDomain {

    public static final String FIELD_SKU_ATTR_ID = "skuAttrId";
    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_SPU_ID = "spuId";
    public static final String FIELD_SKU_ID = "skuId";
    public static final String FIELD_CATEGORY_ID = "categoryId";
    public static final String FIELD_ATTR_ID = "attrId";
    public static final String FIELD_ATTR_VALUE_ID = "attrValueId";
    public static final String FIELD_ATTR_TYPE = "attrType";
    public static final String FIELD_DESCRIPTION = "description";

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		SkuAttr skuAttr = (SkuAttr) o;
		return tenantId.equals(skuAttr.tenantId) &&
				spuId.equals(skuAttr.spuId) &&
				Objects.equals(skuId, skuAttr.skuId) &&
				categoryId.equals(skuAttr.categoryId) &&
				attrId.equals(skuAttr.attrId) &&
				Objects.equals(attrValueId, skuAttr.attrValueId) &&
				Objects.equals(attrType, skuAttr.attrType) &&
				Objects.equals(description, skuAttr.description);
	}

	@Override
	public int hashCode() {
		return Objects.hash(tenantId, spuId, skuId, categoryId, attrId, attrValueId, attrType, description);
	}

	//
    // 数据库字段
    // ------------------------------------------------------------------------------


    @ApiModelProperty("")
    @Id
    @GeneratedValue
	@Encrypt
    private Long skuAttrId;
    @ApiModelProperty(value = "租户ID  hpfm_tenant.tenant_id",required = true)
    @NotNull
    private Long tenantId;
    @ApiModelProperty(value = "SPU ID  smpc_spu.spu_id",required = true)
    @NotNull
	@Encrypt
    private Long spuId;
   @ApiModelProperty(value = "SKU ID  smpc_sku.sku_id")
   	@Encrypt
    private Long skuId;
    @ApiModelProperty(value = "分类ID  smpc_category.category_id",required = true)
    @NotNull
	@Encrypt
    private Long categoryId;
    @ApiModelProperty(value = "属性ID  smpc_attribute.attribute_id",required = true)
    @NotNull
	@Encrypt
    private Long attrId;
   @ApiModelProperty(value = "属性值ID  smpc_attribute_value.attr_value_id")
   	@Encrypt
    private Long attrValueId;
    @ApiModelProperty(value = "属性类型 1.销售属性；2：基本属性 3：其他（规格参数）（只是用于商品发布页面展示）",required = true)
    @NotNull
    private Integer attrType;
   @ApiModelProperty(value = "描述")    
    private String description;

	//
    // 非数据库字段
    // ------------------------------------------------------------------------------

	@Transient
	private String attributeCode;
	@Transient
	private String attributeName;
	@Transient
	private String attrValueCode;
	@Transient
	private String attrValueName;
    //
    // getter/setter
    // ------------------------------------------------------------------------------

    /**
     * @return 
     */
	public Long getSkuAttrId() {
		return skuAttrId;
	}

	public void setSkuAttrId(Long skuAttrId) {
		this.skuAttrId = skuAttrId;
	}
    /**
     * @return 租户ID  hpfm_tenant.tenant_id
     */
	public Long getTenantId() {
		return tenantId;
	}

	public void setTenantId(Long tenantId) {
		this.tenantId = tenantId;
	}
    /**
     * @return SPU ID  smpc_spu.spu_id
     */
	public Long getSpuId() {
		return spuId;
	}

	public void setSpuId(Long spuId) {
		this.spuId = spuId;
	}
    /**
     * @return SKU ID  smpc_sku.sku_id
     */
	public Long getSkuId() {
		return skuId;
	}

	public void setSkuId(Long skuId) {
		this.skuId = skuId;
	}
    /**
     * @return 分类ID  smpc_category.category_id
     */
	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}
    /**
     * @return 属性ID  smpc_attribute.attribute_id
     */
	public Long getAttrId() {
		return attrId;
	}

	public void setAttrId(Long attrId) {
		this.attrId = attrId;
	}
    /**
     * @return 属性值ID  smpc_attribute_value.attr_value_id
     */
	public Long getAttrValueId() {
		return attrValueId;
	}

	public void setAttrValueId(Long attrValueId) {
		this.attrValueId = attrValueId;
	}
	/**
     * @return 属性类型 1.销售属性；2：基本属性 3：其他（规格参数）（只是用于商品发布页面展示）
     */
	public Integer getAttrType() {
		return attrType;
	}

	public void setAttrType(Integer attrType) {
		this.attrType = attrType;
	}
    /**
     * @return 描述
     */
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getAttributeCode() {
		return attributeCode;
	}

	public void setAttributeCode(String attributeCode) {
		this.attributeCode = attributeCode;
	}

	public String getAttributeName() {
		return attributeName;
	}

	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}

	public String getAttrValueCode() {
		return attrValueCode;
	}

	public void setAttrValueCode(String attrValueCode) {
		this.attrValueCode = attrValueCode;
	}

	public String getAttrValueName() {
		return attrValueName;
	}

	public void setAttrValueName(String attrValueName) {
		this.attrValueName = attrValueName;
	}
}
