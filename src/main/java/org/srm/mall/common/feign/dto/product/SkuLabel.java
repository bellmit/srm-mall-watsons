package org.srm.mall.common.feign.dto.product;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.choerodon.mybatis.domain.AuditDomain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hzero.starter.keyencrypt.core.Encrypt;
import org.srm.boot.common.BaseConstants;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

/**
 * 商品标签
 *
 * @author yuhao.guo@hand-china.com 2020-12-07 17:25:39
 */
@ApiModel("商品标签")
@VersionAudit
@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "smpc_sku_label")
public class SkuLabel extends AuditDomain {

    public static final String FIELD_ID = "id";
    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_SKU_ID = "skuId";
    public static final String FIELD_LABEL_ID = "labelId";
    public static final String FIELD_ORDER_SEQ = "orderSeq";
	public static final String FIELD_ENABLED_FLAG = "enabledFlag";

    //
    // 业务方法(按public protected private顺序排列)
    // ------------------------------------------------------------------------------
	public SkuLabel() {}

	public SkuLabel(Long tenantId, Long productId, Label label) {
		this.tenantId = tenantId;
		this.skuId = productId;
		this.labelId = label.getLabelId();
		this.orderSeq = label.getOrderSeq();
		this.enabledFlag = BaseConstants.Flag.YES;
	}
    //
    // 数据库字段
    // ------------------------------------------------------------------------------


    @ApiModelProperty("")
    @Id
    @GeneratedValue
    private Long id;
    @ApiModelProperty(value = "租户ID  hpfm_tenant.tenant_id",required = true)
    @NotNull
    private Long tenantId;
    @ApiModelProperty(value = "SKU ID  smpc_sku.sku_id",required = true)
    @NotNull
	@Encrypt
    private Long skuId;
    @ApiModelProperty(value = "标签ID smpc_label.label_id",required = true)
    @NotNull
	@Encrypt
    private Long labelId;
    @ApiModelProperty(value = "排序号",required = true)
    @NotNull
    private Long orderSeq;
	@ApiModelProperty(value = "启用禁用",required = true)
	@NotNull
	private Integer enabledFlag;

	//
    // 非数据库字段
    // ------------------------------------------------------------------------------
	@Transient
	private String labelName;
	@Transient
	private String labelCode;
	@Transient
	private String labelColorCode;
    //
    // getter/setter
    // ------------------------------------------------------------------------------

    /**
     * @return 
     */
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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
     * @return SKU ID  smpc_sku.sku_id
     */
	public Long getSkuId() {
		return skuId;
	}

	public void setSkuId(Long skuId) {
		this.skuId = skuId;
	}
    /**
     * @return 标签ID smpc_label.label_id
     */
	public Long getLabelId() {
		return labelId;
	}

	public void setLabelId(Long labelId) {
		this.labelId = labelId;
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
	 * @return 启用禁用
	 */
	public Integer getEnabledFlag() {
		return enabledFlag;
	}

	public void setEnabledFlag(Integer enabledFlag) {
		this.enabledFlag = enabledFlag;
	}

	public String getLabelName() {
		return labelName;
	}

	public void setLabelName(String labelName) {
		this.labelName = labelName;
	}

	public String getLabelCode() {
		return labelCode;
	}

	public void setLabelCode(String labelCode) {
		this.labelCode = labelCode;
	}

	public String getLabelColorCode() {
		return labelColorCode;
	}

	public void setLabelColorCode(String labelColorCode) {
		this.labelColorCode = labelColorCode;
	}
}
