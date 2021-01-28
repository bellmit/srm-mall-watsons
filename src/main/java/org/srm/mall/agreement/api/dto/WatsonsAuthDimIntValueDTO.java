package org.srm.mall.agreement.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.hzero.core.base.BaseConstants;
import org.hzero.starter.keyencrypt.core.Encrypt;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

/**
 * 权限维度值整形
 *
 * @author fu.ji@hand-china.com 2021-01-25 10:52:31
 */
@ApiModel("权限维度值整形")
public class WatsonsAuthDimIntValueDTO {

    public static final String FIELD_DIMENSION_VALUE_ID = "dimensionValueId";
    public static final String FIELD_AUTH_DIMENSION_REF_ID = "authDimensionRefId";
    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_DATA_INT = "dataInt";

//
// 业务方法(按public protected private顺序排列)
// ------------------------------------------------------------------------------

//
// 数据库字段
// ------------------------------------------------------------------------------


    @ApiModelProperty("表ID，主键，供其他表做外键")
    @Id
    @GeneratedValue
    private Long dimensionValueId;
    @ApiModelProperty(value = "权限维度关联id", required = true)
    @NotNull
    private Long authDimensionRefId;
    @ApiModelProperty(value = "租户id", required = true)
    @NotNull
    private Long tenantId;
    @ApiModelProperty(value = "整形维度值")
    @Encrypt
    private Long dataInt;

    //
// 非数据库字段
// ------------------------------------------------------------------------------
    @ApiModelProperty(value = "维度编码", required = true)
    @Transient
    private String dimensionCode;
    @ApiModelProperty(value = "维度类型SAGM.AUTH_RANGE_TYPE", required = true)
    @Transient
    private String dimensionType;
    @ApiModelProperty(value = "维度值集编码", required = true)
    @Transient
    private String lovCode;
    @ApiModelProperty(value = "组件类型", required = true)
    @Transient
    private String componentType;
    @Transient
    private LovValueDTO lovValueDTO;
//
// getter/setter
// ------------------------------------------------------------------------------

    public String groupByKey() {
        return new StringBuilder().append(this.dimensionType).append(BaseConstants.Symbol.MIDDLE_LINE).append(this.dimensionCode).toString();
    }

    /**
     * @return 表ID，主键，供其他表做外键
     */
    public Long getDimensionValueId() {
        return dimensionValueId;
    }

    public void setDimensionValueId(Long dimensionValueId) {
        this.dimensionValueId = dimensionValueId;
    }

    /**
     * @return 权限维度关联id
     */
    public Long getAuthDimensionRefId() {
        return authDimensionRefId;
    }

    public void setAuthDimensionRefId(Long authDimensionRefId) {
        this.authDimensionRefId = authDimensionRefId;
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
     * @return 整形维度值
     */
    public Long getDataInt() {
        return dataInt;
    }

    public void setDataInt(Long dataInt) {
        this.dataInt = dataInt;
    }

    public String getDimensionCode() {
        return dimensionCode;
    }

    public WatsonsAuthDimIntValueDTO setDimensionCode(String dimensionCode) {
        this.dimensionCode = dimensionCode;
        return this;
    }

    public String getDimensionType() {
        return dimensionType;
    }

    public WatsonsAuthDimIntValueDTO setDimensionType(String dimensionType) {
        this.dimensionType = dimensionType;
        return this;
    }

    public String getLovCode() {
        return lovCode;
    }

    public WatsonsAuthDimIntValueDTO setLovCode(String lovCode) {
        this.lovCode = lovCode;
        return this;
    }

    public String getComponentType() {
        return componentType;
    }

    public WatsonsAuthDimIntValueDTO setComponentType(String componentType) {
        this.componentType = componentType;
        return this;
    }

    public LovValueDTO getLovValueDTO() {
        return lovValueDTO;
    }

    public WatsonsAuthDimIntValueDTO setLovValueDTO(LovValueDTO lovValueDTO) {
        this.lovValueDTO = lovValueDTO;
        return this;
    }
}
