package org.srm.mall.common.feign.dto.product;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.choerodon.mybatis.domain.AuditDomain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hzero.starter.keyencrypt.core.Encrypt;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * sku售后
 *
 * @author yuhao.guo@hand-china.com 2020-12-17 15:17:51
 */
@ApiModel("sku售后")
@VersionAudit
@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "smpc_after_sale")
public class AfterSale extends AuditDomain {

    public static final String FIELD_SKU_SALE_ID = "skuSaleId";
    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_SPU_ID = "spuId";
    public static final String FIELD_SKU_ID = "skuId";
    public static final String FIELD_RETURN_DURATION = "returnDuration";
    public static final String FIELD_CHANGE_DURATION = "changeDuration";
    public static final String FIELD_QUALITY_DURATION = "qualityDuration";
    public static final String FIELD_INSTRUCTION = "instruction";
    public static final String FIELD_CHANGE_SPECIAL = "changeSpecial";
    public static final String FIELD_AFTER_SALE_SPECIAL = "afterSaleSpecial";
    public static final String FIELD_RETURN_SPECIAL = "returnSpecial";

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
    private Long skuSaleId;
    @ApiModelProperty(value = "租户ID  hpfm_tenant.tenant_id",required = true)
    @NotNull
    private Long tenantId;
    @ApiModelProperty(value = "SPU ID  smpc_spu.spu_id",required = true)
    @NotNull
	@Encrypt
    private Long spuId;
    @ApiModelProperty(value = "SKU ID  smpc_sku.sku_id",required = true)
    @NotNull
	@Encrypt
    private Long skuId;
   @ApiModelProperty(value = "退货有效天数")    
    private Long returnDuration;
   @ApiModelProperty(value = "换货有效天数")    
    private Long changeDuration;
   @ApiModelProperty(value = "质保有效月数")    
    private Long qualityDuration;
   @ApiModelProperty(value = "特殊商品说明")    
    private String instruction;
    @ApiModelProperty(value = "换货特殊商品",required = true)
    @NotNull
    private Integer changeSpecial;
    @ApiModelProperty(value = "特殊售后 0:否  1:是",required = true)
    @NotNull
    private Integer afterSaleSpecial;
    @ApiModelProperty(value = "退货特殊商品",required = true)
    @NotNull
    private Integer returnSpecial;

	//
    // 非数据库字段
    // ------------------------------------------------------------------------------

    //
    // getter/setter
    // ------------------------------------------------------------------------------

    /**
     * @return 
     */
	public Long getSkuSaleId() {
		return skuSaleId;
	}

	public void setSkuSaleId(Long skuSaleId) {
		this.skuSaleId = skuSaleId;
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
     * @return 退货有效天数
     */
	public Long getReturnDuration() {
		return returnDuration;
	}

	public void setReturnDuration(Long returnDuration) {
		this.returnDuration = returnDuration;
	}
    /**
     * @return 换货有效天数
     */
	public Long getChangeDuration() {
		return changeDuration;
	}

	public void setChangeDuration(Long changeDuration) {
		this.changeDuration = changeDuration;
	}
    /**
     * @return 质保有效月数
     */
	public Long getQualityDuration() {
		return qualityDuration;
	}

	public void setQualityDuration(Long qualityDuration) {
		this.qualityDuration = qualityDuration;
	}
    /**
     * @return 特殊商品说明
     */
	public String getInstruction() {
		return instruction;
	}

	public void setInstruction(String instruction) {
		this.instruction = instruction;
	}
    /**
     * @return 换货特殊商品
     */
	public Integer getChangeSpecial() {
		return changeSpecial;
	}

	public void setChangeSpecial(Integer changeSpecial) {
		this.changeSpecial = changeSpecial;
	}
    /**
     * @return 特殊售后 0:否  1:是
     */
	public Integer getAfterSaleSpecial() {
		return afterSaleSpecial;
	}

	public void setAfterSaleSpecial(Integer afterSaleSpecial) {
		this.afterSaleSpecial = afterSaleSpecial;
	}
    /**
     * @return 退货特殊商品
     */
	public Integer getReturnSpecial() {
		return returnSpecial;
	}

	public void setReturnSpecial(Integer returnSpecial) {
		this.returnSpecial = returnSpecial;
	}

}
