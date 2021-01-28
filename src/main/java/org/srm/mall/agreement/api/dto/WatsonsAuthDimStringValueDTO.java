package org.srm.mall.agreement.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.hzero.core.base.BaseConstants;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

/**
 * 权限维度值字符串
 *
 * @author fu.ji@hand-china.com 2021-01-25 10:52:31
 */
@ApiModel("权限维度值字符串")
public class WatsonsAuthDimStringValueDTO {

    public static final String FIELD_DIMENSION_VALUE_ID = "dimensionValueId";
    public static final String FIELD_AUTH_DIMENSION_REF_ID = "authDimensionRefId";
    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_DATA_STRING = "dataString";

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
    @ApiModelProperty(value = "字符串维度值")
    private String dataString;

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
     * @return 字符串维度值
     */
    public String getDataString() {
        return dataString;
    }

    public void setDataString(String dataString) {
        this.dataString = dataString;
    }

    public String getDimensionCode() {
        return dimensionCode;
    }

    public WatsonsAuthDimStringValueDTO setDimensionCode(String dimensionCode) {
        this.dimensionCode = dimensionCode;
        return this;
    }

    public String getDimensionType() {
        return dimensionType;
    }

    public WatsonsAuthDimStringValueDTO setDimensionType(String dimensionType) {
        this.dimensionType = dimensionType;
        return this;
    }

    public String getLovCode() {
        return lovCode;
    }

    public WatsonsAuthDimStringValueDTO setLovCode(String lovCode) {
        this.lovCode = lovCode;
        return this;
    }

    public String getComponentType() {
        return componentType;
    }

    public WatsonsAuthDimStringValueDTO setComponentType(String componentType) {
        this.componentType = componentType;
        return this;
    }

    public LovValueDTO getLovValueDTO() {
        return lovValueDTO;
    }

    public WatsonsAuthDimStringValueDTO setLovValueDTO(LovValueDTO lovValueDTO) {
        this.lovValueDTO = lovValueDTO;
        return this;
    }
}
