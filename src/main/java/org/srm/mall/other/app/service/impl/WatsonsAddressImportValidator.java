package org.srm.mall.other.app.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.choerodon.core.oauth.DetailsHelper;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.imported.app.service.ValidatorHandler;
import org.hzero.boot.imported.infra.validator.annotation.ImportValidator;
import org.hzero.boot.imported.infra.validator.annotation.ImportValidators;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.srm.mall.other.api.dto.WatsonsImportAddressDTO;
import org.srm.mall.platform.domain.repository.CompanyRepository;
import org.srm.mall.platform.domain.repository.InvOrganizationRepository;
import org.srm.mall.platform.domain.vo.InvOrganizationVO;
import org.srm.mall.region.api.dto.RegionLevelPathDTO;
import org.srm.mall.region.app.service.MallRegionService;
import org.srm.mall.region.domain.entity.MallRegion;
import org.srm.mall.region.domain.entity.Region;
import org.srm.mall.region.domain.repository.MallRegionRepository;

import java.math.BigDecimal;
import java.util.List;

/**
 * 屈臣氏地址导入校验
 */
@ImportValidators({
        @ImportValidator(templateCode = "SCUX.WATSONS.ADDRESS_IMPORT")
})
public class WatsonsAddressImportValidator extends ValidatorHandler {
    public static final Logger logger = LoggerFactory.getLogger(WatsonsAddressImportValidator.class);

    @Autowired
    private InvOrganizationRepository invOrganizationRepository;
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MallRegionRepository regionRepository;
    @Autowired
    private MallRegionService mallRegionService;

    @Override
    public boolean validate(String data) {
        WatsonsImportAddressDTO addressDTO = null;
        try {
            addressDTO = objectMapper.readValue(data, WatsonsImportAddressDTO.class);
            assert addressDTO != null;
        } catch (Exception e) {
            logger.error("Watsons address import error: {}, data is {}", e.getMessage(), data);
            getContext().addErrorMsg("数据格式异常");
        }

        Long tenantId = DetailsHelper.getUserDetails().getOrganizationId();
        // 校验公司
        Long companyId = companyRepository.getCompanyIdByCompanyNum(tenantId, addressDTO.getCompanyNum());
        if (ObjectUtils.isEmpty(companyId)) {
            getContext().addErrorMsg("公司不存在");
            return false;
        }

        // 校验库存组织
        Integer orgCount = invOrganizationRepository.selectCountByCondition(Condition.builder(InvOrganizationVO.class).andWhere(Sqls.custom().andEqualTo(InvOrganizationVO.FIELD_ORGANIZATION_CODE, addressDTO.getInvOrganizationCode()).andEqualTo(InvOrganizationVO.FIELD_TENANT_ID, tenantId).andNotEqualTo(InvOrganizationVO
                .FIELD_SOURCE_CODE, "SYSTEM")).build());
        if (orgCount.equals(0)) {
            getContext().addErrorMsg("库存组织不存在");
            return false;
        }

        // 校验商城地区信息
        List<MallRegion> regions = regionRepository.selectByCondition(Condition.builder(MallRegion.class).andWhere(Sqls.custom().andEqualTo(Region.FIELD_REGION_CODE,addressDTO.getFourthRegionCode())).build());
        if (ObjectUtils.isEmpty(regions)){
            getContext().addErrorMsg("商城末级地区不存在");
            return false;
        }
        RegionLevelPathDTO regionLevelPathDTO = mallRegionService.selectRegionLevelPath(regions.get(0).getRegionId());
        MallRegion province = mallRegionService.selectByRegionCode(regionLevelPathDTO.getProvinceId());
        String watsonsRegion = addressDTO.getFullAddress().replace(addressDTO.getAddress(), "");
        if (!watsonsRegion.contains(province.getRegionName())){
            getContext().addErrorMsg("屈臣氏地区信息["+watsonsRegion+"]与商城省份信息["+province.getRegionName()+"]不匹配");
            return false;
        }
        return true;
    }
}
