package org.srm.mall.other.api.dto;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.validation.constraints.NotBlank;
import io.choerodon.mybatis.domain.AuditDomain;
import java.math.BigDecimal;
import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 店铺对应仓库关联关系
 *
 * @author xiao.xu03@hand-china.com 2020-12-29 20:26:51
 */
@ApiModel("店铺对应仓库关联关系")
public class WatsonStoreInventoryRelationDTO {


	@ApiModelProperty("")
	@Id
	@GeneratedValue
	private Long id;
	@ApiModelProperty(value = "店铺号",required = true)
	@NotBlank
	private String storeId;
	@ApiModelProperty(value = "店铺中文名")
	private String storeNameCn;
	@ApiModelProperty(value = "店铺英文名")
	private String storeNameEn;
	@ApiModelProperty(value = "关联仓库编号",required = true)
	@NotBlank
	private String inventoryCode;
	@ApiModelProperty(value = "关联仓库名称")
	private String inventoryName;
	@ApiModelProperty(value = "经度",required = true)
	@NotNull
	private BigDecimal latitude;
	@ApiModelProperty(value = "纬度",required = true)
	@NotNull
	private BigDecimal longitude;
	@ApiModelProperty(value = "是否启用")
	private String enabledFlag;

	//
	// 非数据库字段
	// ------------------------------------------------------------------------------

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
	 * @return 店铺号
	 */
	public String getStoreId() {
		return storeId;
	}

	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}
	/**
	 * @return 店铺中文名
	 */
	public String getStoreNameCn() {
		return storeNameCn;
	}

	public void setStoreNameCn(String storeNameCn) {
		this.storeNameCn = storeNameCn;
	}
	/**
	 * @return 店铺英文名
	 */
	public String getStoreNameEn() {
		return storeNameEn;
	}

	public void setStoreNameEn(String storeNameEn) {
		this.storeNameEn = storeNameEn;
	}
	/**
	 * @return 关联仓库编号
	 */
	public String getInventoryCode() {
		return inventoryCode;
	}

	public void setInventoryCode(String inventoryCode) {
		this.inventoryCode = inventoryCode;
	}
	/**
	 * @return 关联仓库名称
	 */
	public String getInventoryName() {
		return inventoryName;
	}

	public void setInventoryName(String inventoryName) {
		this.inventoryName = inventoryName;
	}
	/**
	 * @return 经度
	 */
	public BigDecimal getLatitude() {
		return latitude;
	}

	public void setLatitude(BigDecimal latitude) {
		this.latitude = latitude;
	}
	/**
	 * @return 纬度
	 */
	public BigDecimal getLongitude() {
		return longitude;
	}

	public void setLongitude(BigDecimal longitude) {
		this.longitude = longitude;
	}
	/**
	 * @return 是否启用
	 */
	public String getEnabledFlag() {
		return enabledFlag;
	}

	public void setEnabledFlag(String enabledFlag) {
		this.enabledFlag = enabledFlag;
	}

}
