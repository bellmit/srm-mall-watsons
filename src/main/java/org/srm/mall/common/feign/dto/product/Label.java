package org.srm.mall.common.feign.dto.product;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.MultiLanguage;
import io.choerodon.mybatis.annotation.MultiLanguageField;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.choerodon.mybatis.domain.AuditDomain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hzero.boot.platform.lov.annotation.LovValue;
import org.hzero.starter.keyencrypt.core.Encrypt;

import java.util.List;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 标签
 *
 * @author yuhao.guo@hand-china.com 2020-12-07 17:25:39
 */
@ApiModel("标签")
@VersionAudit
@ModifyAudit
@MultiLanguage
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "smpc_label")
public class Label extends AuditDomain {

    public static final String FIELD_LABEL_ID = "labelId";
    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_LABEL_CODE = "labelCode";
    public static final String FIELD_LABEL_NAME = "labelName";
    public static final String FIELD_LABEL_COLOR_CODE = "labelColorCode";
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
    private Long labelId;
    @ApiModelProperty(value = "租户ID  hpfm_tenant.tenant_id",required = true)
    @NotNull
    private Long tenantId;
    @ApiModelProperty(value = "标签编码",required = true)
    @NotBlank
    private String labelCode;
	@MultiLanguageField
   @ApiModelProperty(value = "标签名称")    
    private String labelName;
    @ApiModelProperty(value = "标签颜色",required = true)
    @NotBlank
    private String labelColorCode;
    @ApiModelProperty(value = "启用标识",required = true)
    @NotNull
    private Integer enabledFlag;

	//
    // 非数据库字段
    // ------------------------------------------------------------------------------
	@Transient
	@LovValue(meaningField = "customFlagMeaning", lovCode = "SMAL.CUSTOM_FLAG")
	private Integer customFlag;
	@Transient
	private String customFlagMeaning;
	@Transient
	private List<String> labelCodeList;
	@Transient
	private String langCode;
	@Transient
	private Long orderSeq;
	@Transient
	private Long productId;
    //
    // getter/setter
    // ------------------------------------------------------------------------------

    /**
     * @return 
     */
	public Long getLabelId() {
		return labelId;
	}

	public void setLabelId(Long labelId) {
		this.labelId = labelId;
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
     * @return 标签编码
     */
	public String getLabelCode() {
		return labelCode;
	}

	public void setLabelCode(String labelCode) {
		this.labelCode = labelCode;
	}
    /**
     * @return 标签名称
     */
	public String getLabelName() {
		return labelName;
	}

	public void setLabelName(String labelName) {
		this.labelName = labelName;
	}
    /**
     * @return 标签颜色
     */
	public String getLabelColorCode() {
		return labelColorCode;
	}

	public void setLabelColorCode(String labelColorCode) {
		this.labelColorCode = labelColorCode;
	}
    /**
     * @return 启用标识
     */
	public Integer getEnabledFlag() {
		return enabledFlag;
	}

	public void setEnabledFlag(Integer enabledFlag) {
		this.enabledFlag = enabledFlag;
	}

	public Integer getCustomFlag() {
		return customFlag;
	}

	public void setCustomFlag(Integer customFlag) {
		this.customFlag = customFlag;
	}

	public String getCustomFlagMeaning() {
		return customFlagMeaning;
	}

	public void setCustomFlagMeaning(String customFlagMeaning) {
		this.customFlagMeaning = customFlagMeaning;
	}

	public List<String> getLabelCodeList() {
		return labelCodeList;
	}

	public void setLabelCodeList(List<String> labelCodeList) {
		this.labelCodeList = labelCodeList;
	}

	public String getLangCode() {
		return langCode;
	}

	public void setLangCode(String langCode) {
		this.langCode = langCode;
	}

	public Long getOrderSeq() {
		return orderSeq;
	}

	public void setOrderSeq(Long orderSeq) {
		this.orderSeq = orderSeq;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}
}
