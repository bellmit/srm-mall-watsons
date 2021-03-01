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
import javax.validation.constraints.NotNull;

/**
 * sku自定义属性值
 *
 * @author yuhao.guo@hand-china.com 2020-12-15 21:06:24
 */
@ApiModel("sku自定义属性值")
@VersionAudit
@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "smpc_sku_attr_extend")
public class SkuAttrExtend extends AuditDomain {

    public static final String FIELD_ATTR_EXTEND_ID = "attrExtendId";
    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_SPU_ID = "spuId";
    public static final String FIELD_SKU_ID = "skuId";
    public static final String FIELD_CATEGORY_ID = "categoryId";
    public static final String FIELD_ATTR_TYPE = "attrType";
    public static final String FIELD_ATTR_NAME = "attrName";
    public static final String FIELD_ATTR_VALUE = "attrValue";

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		SkuAttrExtend that = (SkuAttrExtend) o;
		return tenantId.equals(that.tenantId) &&
				spuId.equals(that.spuId) &&
				Objects.equals(skuId, that.skuId) &&
				categoryId.equals(that.categoryId) &&
				Objects.equals(attrType, that.attrType) &&
				Objects.equals(attrName, that.attrName) &&
				Objects.equals(attrValue, that.attrValue);
	}

	@Override
	public int hashCode() {
		return Objects.hash(tenantId, spuId, skuId, categoryId, attrType, attrName, attrValue);
	}

	//
    // 数据库字段
    // ------------------------------------------------------------------------------


    @ApiModelProperty("")
    @Id
    @GeneratedValue
	@Encrypt
	private Long attrExtendId;
    @ApiModelProperty(value = "租户ID hpfm_tenant.tenant_id",required = true)
    @NotNull
    private Long tenantId;
    @ApiModelProperty(value = "SPU ID smpc_spu.spu_id",required = true)
    @NotNull
	@Encrypt
    private Long spuId;
   @ApiModelProperty(value = "SKU ID smpc_sku.sku_id")
   	@Encrypt
    private Long skuId;
    @ApiModelProperty(value = "分类ID  smpc_category.category_id",required = true)
    @NotNull
	@Encrypt
    private Long categoryId;
    @ApiModelProperty(value = "属性类型 1.销售属性；2：基本属性 3：其他（规格参数）（只是用于商品发布页面展示）",required = true)
    @NotNull
    private Integer attrType;
   @ApiModelProperty(value = "属性名")    
    private String attrName;
   @ApiModelProperty(value = "属性值")    
    private String attrValue;

	//
    // 非数据库字段
    // ------------------------------------------------------------------------------

    //
    // getter/setter
    // ------------------------------------------------------------------------------

    /**
     * @return 
     */
	public Long getAttrExtendId() {
		return attrExtendId;
	}

	public void setAttrExtendId(Long attrExtendId) {
		this.attrExtendId = attrExtendId;
	}
    /**
     * @return 租户ID hpfm_tenant.tenant_id
     */
	public Long getTenantId() {
		return tenantId;
	}

	public void setTenantId(Long tenantId) {
		this.tenantId = tenantId;
	}
    /**
     * @return SPU ID smpc_spu.spu_id
     */
	public Long getSpuId() {
		return spuId;
	}

	public void setSpuId(Long spuId) {
		this.spuId = spuId;
	}
    /**
     * @return SKU ID smpc_sku.sku_id
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
     * @return 属性类型 1.销售属性；2：基本属性 3：其他（规格参数）（只是用于商品发布页面展示）
     */
	public Integer getAttrType() {
		return attrType;
	}

	public void setAttrType(Integer attrType) {
		this.attrType = attrType;
	}
    /**
     * @return 属性名
     */
	public String getAttrName() {
		return attrName;
	}

	public void setAttrName(String attrName) {
		this.attrName = attrName;
	}
    /**
     * @return 属性值
     */
	public String getAttrValue() {
		return attrValue;
	}

	public void setAttrValue(String attrValue) {
		this.attrValue = attrValue;
	}

}
