package org.srm.mall.agreement.domain.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.choerodon.mybatis.domain.AuditDomain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hzero.boot.platform.lov.annotation.LovValue;
import org.hzero.starter.keyencrypt.core.Encrypt;
import org.springframework.util.ObjectUtils;
import org.srm.mall.agreement.api.dto.AuthRangeDTO;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 协议权限集
 *
 * @author fu.ji@hand-china.com 2020-10-03 16:01:18
 */
public class WatsonsAuthorityList extends AuthorityList {
    private Long agreementDetailId;

    public Long getAgreementDetailId() {
        return agreementDetailId;
    }

    public WatsonsAuthorityList setAgreementDetailId(Long agreementDetailId) {
        this.agreementDetailId = agreementDetailId;
        return this;
    }
}
