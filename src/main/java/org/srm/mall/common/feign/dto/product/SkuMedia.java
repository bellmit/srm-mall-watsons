package org.srm.mall.common.feign.dto.product;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.choerodon.mybatis.domain.AuditDomain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hzero.core.base.BaseConstants;
import org.hzero.starter.keyencrypt.core.Encrypt;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * sku多媒体
 *
 * @author yuhao.guo@hand-china.com 2020-12-15 21:06:24
 */
@ApiModel("sku多媒体")
@VersionAudit
@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "smpc_sku_media")
public class SkuMedia extends AuditDomain {

    public static final String FIELD_MEDIA_ID = "mediaId";
    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_SPU_ID = "spuId";
    public static final String FIELD_SKU_ID = "skuId";
    public static final String FIELD_MEDIA_PATH = "mediaPath";
    public static final String FIELD_MEDIA_TYPE = "mediaType";
    public static final String FIELD_PRIMARY_FLAG = "primaryFlag";
    public static final String FIELD_ORDER_SEQ = "orderSeq";

    //
    // 业务方法(按public protected private顺序排列)
    // ------------------------------------------------------------------------------

	public SkuMedia() {}

	public SkuMedia(Long tenantId, Long spuId, String mediaPath, Integer mediaType) {
		this.tenantId = tenantId;
		this.spuId = spuId;
		this.skuId = -1L;
		this.mediaPath = mediaPath;
		this.mediaType = mediaType;
		this.orderSeq = 1L;
		this.primaryFlag = BaseConstants.Flag.YES;
	}

	public SkuMedia importInit(Long tenantId, Long skuId, Long spuId, String imagePath) {
		this.tenantId = tenantId;
		this.spuId = spuId;
		this.skuId = skuId;
		this.mediaPath = imagePath;
		this.primaryFlag = BaseConstants.Flag.YES;
		return this;
	}
    //
    // 数据库字段
    // ------------------------------------------------------------------------------


    @ApiModelProperty("主键")
    @Id
    @GeneratedValue
	@Encrypt
    private Long mediaId;
    @ApiModelProperty(value = "租户ID  hpfm_tenant.tenant_id",required = true)
    @NotNull
    private Long tenantId;
    @ApiModelProperty(value = "SPU ID  smpc_spu.spu_id",required = true)
    @NotNull
	@Encrypt
    private Long spuId;
    @ApiModelProperty(value = "SKU ID  smpc_sku.sku_id，为-1时，即为spu图片/视频",required = true)
    @NotNull
	@Encrypt
    private Long skuId;
    @ApiModelProperty(value = "媒体url，内容存在oss中",required = true)
    @NotBlank
    private String mediaPath;
    @ApiModelProperty(value = "媒体类型  图片-0/视频-1/URL-2",required = true)
    @NotNull
    private Integer mediaType;
    @ApiModelProperty(value = "是否主图 0：非主图 1：主图",required = true)
    @NotNull
    private Integer primaryFlag;
    @ApiModelProperty(value = "排序号,排序号最小的作为主图，从1开始",required = true)
    @NotNull
    private Long orderSeq;

	//
    // 非数据库字段
    // ------------------------------------------------------------------------------

    //
    // getter/setter
    // ------------------------------------------------------------------------------

    /**
     * @return 
     */
	public Long getMediaId() {
		return mediaId;
	}

	public void setMediaId(Long mediaId) {
		this.mediaId = mediaId;
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
     * @return 媒体url，内容存在oss中
     */
	public String getMediaPath() {
		return mediaPath;
	}

	public void setMediaPath(String mediaPath) {
		this.mediaPath = mediaPath;
	}
    /**
     * @return 媒体类型  图片-0/视频-1/URL-2
     */
	public Integer getMediaType() {
		return mediaType;
	}

	public void setMediaType(Integer mediaType) {
		this.mediaType = mediaType;
	}
    /**
     * @return 是否主图 0：非主图 1：主图
     */
	public Integer getPrimaryFlag() {
		return primaryFlag;
	}

	public void setPrimaryFlag(Integer primaryFlag) {
		this.primaryFlag = primaryFlag;
	}
    /**
     * @return 排序号,排序号最小的作为主图，从1开始
     */
	public Long getOrderSeq() {
		return orderSeq;
	}

	public void setOrderSeq(Long orderSeq) {
		this.orderSeq = orderSeq;
	}

}
