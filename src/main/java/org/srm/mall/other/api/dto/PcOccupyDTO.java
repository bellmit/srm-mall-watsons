package org.srm.mall.other.api.dto;

import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.choerodon.mybatis.domain.AuditDomain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 采购协议金额占用记录表
 *
 * @author heng.zhang04@hand-china
 * @date 2021-01-25 21:27:40
 */
@VersionAudit
@ModifyAudit
public class PcOccupyDTO {


    //
    // 数据库字段
    // ------------------------------------------------------------------------------


    @ApiModelProperty("表ID，主键，供其他表做外键" )
    @Id
    @GeneratedValue
    private Long occupyId;

    @ApiModelProperty(value = "租户ID", required = true)
    @NotNull
    private Long tenantId;

    @ApiModelProperty(value = "来源单据ID" )
    private Long sourceId;

    @ApiModelProperty(value = "来源单据类型" )
    private String sourceType;

    @ApiModelProperty(value = "合同ID", required = true)
    @NotNull
    private Long pcHeaderId;

    @ApiModelProperty(value = "占用金额", required = true)
    @NotNull
    private BigDecimal occupyAmount;

    @ApiModelProperty(value = "协议编号")
    private String pcNum;
    @ApiModelProperty(value = "版本号")
    private Long  version;
    @ApiModelProperty(value = "已执行金额")
    private BigDecimal executedAmount;
    @ApiModelProperty(value = "待执行金额")
    private BigDecimal toExecuteAmount;

    /**
     * 操作类型（占用、取消、更新、扣减-用于后续财务模块使用）
     * 值集：OCCUPY/CANCEL/UPDATE/DEDUCT
     */
    @ApiModelProperty(value = "操作类型")
    private String operationType;


    //
    // 非数据库字段
    // ------------------------------------------------------------------------------

    //
    // getter/setter
    // ------------------------------------------------------------------------------


    public String getPcNum() {
        return pcNum;
    }

    public void setPcNum(String pcNum) {
        this.pcNum = pcNum;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public BigDecimal getExecutedAmount() {
        return executedAmount;
    }

    public void setExecutedAmount(BigDecimal executedAmount) {
        this.executedAmount = executedAmount;
    }

    public BigDecimal getToExecuteAmount() {
        return toExecuteAmount;
    }

    public void setToExecuteAmount(BigDecimal toExecuteAmount) {
        this.toExecuteAmount = toExecuteAmount;
    }

    /**
     * @return 表ID，主键，供其他表做外键
     */
    public Long getOccupyId() {
        return occupyId;
    }

    public void setOccupyId(Long occupyId) {
        this.occupyId = occupyId;
    }

    /**
     * @return 租户ID
     */
    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    /**
     * @return 来源单据ID
     */
    public Long getSourceId() {
        return sourceId;
    }

    public void setSourceId(Long sourceId) {
        this.sourceId = sourceId;
    }

    /**
     * @return 来源单据类型
     */
    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }
    /**
     * @return 合同ID
     */
    public Long getPcHeaderId() {
        return pcHeaderId;
    }

    public void setPcHeaderId(Long pcHeaderId) {
        this.pcHeaderId = pcHeaderId;
    }

    /**
     * @return 占用金额
     */
    public BigDecimal getOccupyAmount() {
        return occupyAmount;
    }

    public void setOccupyAmount(BigDecimal occupyAmount) {
        this.occupyAmount = occupyAmount;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

}
