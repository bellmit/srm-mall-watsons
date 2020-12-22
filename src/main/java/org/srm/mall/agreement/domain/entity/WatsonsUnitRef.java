package org.srm.mall.agreement.domain.entity;

import org.hzero.mybatis.domian.SecurityToken;

/**
 * 采买组织映射
 *
 * @author fu.ji@hand-china.com 2020-08-20 15:36:17
 */
public class WatsonsUnitRef extends UnitRef {

    @Override
    public Class<? extends SecurityToken> associateEntityClass() {
        return UnitRef.class;
    }
}
