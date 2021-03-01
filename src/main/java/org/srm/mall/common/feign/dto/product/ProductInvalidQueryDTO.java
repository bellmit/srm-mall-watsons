package org.srm.mall.common.feign.dto.product;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.util.ObjectUtils;
import org.srm.mall.common.feign.dto.agreement.Agreement;


import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 协议商品到期提醒DTO
 *
 * @author fu.ji@hand-china.com 2021-1-18 21:24:05
 */
public class ProductInvalidQueryDTO {
    public static final String FIELD_COMPANY = "company";
    public static final String FIELD_SUPPLIER_COMPANY = "supplier";
    public static final String FIELD_ROLE_IDS = "roleIds";
    private Long company;
    private Long supplier;
    private Long tenantId;
    private Integer sendFlag;
    private Integer days;
    private List<String> roleIds;
    private Date dateTo;
    private List<Agreement> agreements;

    public Long getCompany() {
        return company;
    }

    public ProductInvalidQueryDTO setCompany(Long company) {
        this.company = company;
        return this;
    }

    public Long getSupplier() {
        return supplier;
    }

    public ProductInvalidQueryDTO setSupplier(Long supplier) {
        this.supplier = supplier;
        return this;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public ProductInvalidQueryDTO setTenantId(Long tenantId) {
        this.tenantId = tenantId;
        return this;
    }

    public Integer getSendFlag() {
        return sendFlag;
    }

    public ProductInvalidQueryDTO setSendFlag(Integer sendFlag) {
        this.sendFlag = sendFlag;
        return this;
    }

    public Date getDateTo() {
        return dateTo;
    }

    public ProductInvalidQueryDTO setDateTo(Date dateTo) {
        this.dateTo = dateTo;
        return this;
    }

    public Integer getDays() {
        return days;
    }

    public ProductInvalidQueryDTO setDays(Integer days) {
        this.days = days;
        if (!ObjectUtils.isEmpty(this.days)) {
            this.setDateTo(DateUtils.truncate(DateUtils.addDays(new Date(), this.days), Calendar.DATE));
        }
        return this;
    }

    public List<String> getRoleIds() {
        return roleIds;
    }

    public ProductInvalidQueryDTO setRoleIds(List<String> roleIds) {
        this.roleIds = roleIds;
        return this;
    }

    public List<Agreement> getAgreements() {
        return agreements;
    }

    public ProductInvalidQueryDTO setAgreements(List<Agreement> agreements) {
        this.agreements = agreements;
        return this;
    }
}
