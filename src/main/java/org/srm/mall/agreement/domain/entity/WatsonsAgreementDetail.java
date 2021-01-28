package org.srm.mall.agreement.domain.entity;

import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.choerodon.mybatis.domain.AuditDomain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.mybatis.domian.SecurityToken;
import org.hzero.starter.keyencrypt.core.Encrypt;
import org.springframework.util.ObjectUtils;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.math.BigDecimal;

/**
 * 
 *
 * @author fu.ji@hand-china.com 2020-05-22 10:50:31
 */
public class WatsonsAgreementDetail extends AgreementDetail {

	@Override
	public Class<? extends SecurityToken> associateEntityClass() {
		return AgreementDetail.class;
	}
}
