package org.srm.mall.other.api.dto;

import io.choerodon.mybatis.domain.AuditDomain;

public class ItfBaseBO extends AuditDomain {
    private Long sitfPkId;
    private Long tenantId;
    private Integer errorFlag;
    private String errorMessage;
    private String errorType;
    private Boolean orderState;
    private String esPoHeaderId;



    private String failed;
    private String code;
    private String message;
    public ItfBaseBO() {
    }

    public ItfBaseBO(Integer errorFlag, String errorMessage, String errorType) {
        this.errorFlag = errorFlag;
        this.errorMessage = errorMessage;
        this.errorType = errorType;
    }

    public String getFailed() {
        return failed;
    }

    public void setFailed(String failed) {
        this.failed = failed;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getSitfPkId() {
        return this.sitfPkId;
    }

    public void setSitfPkId(Long sitfPkId) {
        this.sitfPkId = sitfPkId;
    }

    public Integer getErrorFlag() {
        return this.errorFlag;
    }

    public void setErrorFlag(Integer errorFlag) {
        this.errorFlag = errorFlag;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorType() {
        return this.errorType;
    }

    public void setErrorType(String errorType) {
        this.errorType = errorType;
    }

    public Long getTenantId() {
        return this.tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public Boolean getOrderState() {
        return this.orderState;
    }

    public void setOrderState(Boolean orderState) {
        this.orderState = orderState;
    }

    public String getEsPoHeaderId() {
        return this.esPoHeaderId;
    }

    public void setEsPoHeaderId(String esPoHeaderId) {
        this.esPoHeaderId = esPoHeaderId;
    }

    @Override
    public String toString() {
        return "ItfBaseBO{sitfPkId=" + this.sitfPkId + ", tenantId=" + this.tenantId + ", errorFlag=" + this.errorFlag + ", errorMessage='" + this.errorMessage + '\'' + ", errorType='" + this.errorType + '\'' + '}';
    }
}
