package org.srm.mall.other.app.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.choerodon.core.oauth.DetailsHelper;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.imported.app.service.BatchImportHandler;
import org.hzero.boot.imported.infra.validator.annotation.ImportService;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.srm.mall.common.constant.ScecConstants;
import org.srm.mall.other.api.dto.WatsonsImportAddressDTO;
import org.srm.mall.platform.domain.repository.CompanyRepository;
import org.srm.mall.platform.domain.repository.InvOrganizationRepository;
import org.srm.mall.platform.domain.vo.InvOrganizationVO;
import org.srm.mall.region.app.service.AddressService;
import org.srm.mall.region.app.service.MallRegionService;
import org.srm.mall.region.domain.entity.Address;
import org.srm.mall.region.domain.entity.MallRegion;
import org.srm.mall.region.domain.entity.Region;
import org.srm.mall.region.domain.repository.AddressRepository;
import org.srm.mall.region.domain.repository.MallRegionRepository;

import javax.jws.Oneway;
import java.util.List;

/**
 * 屈臣氏地址导入
 */
@ImportService(templateCode = "SCUX.WATSONS.ADDRESS_IMPORT")
public class WatsonsAddressImportImpl extends BatchImportHandler {
    public static final Logger logger = LoggerFactory.getLogger(WatsonsAddressImportImpl.class);
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
    @Autowired
    private AddressService addressService;
    @Autowired
    private AddressRepository addressRepository;

    @Override
    public Boolean doImport(List<String> data) {
        for (String addressString : data) {
            WatsonsImportAddressDTO addressDTO = null;
            try {
                addressDTO = objectMapper.readValue(addressString, WatsonsImportAddressDTO.class);
                assert addressDTO != null;
            } catch (Exception e) {
                logger.error("Watsons address import error: {}, data is {}", e.getMessage(), data);
            }

            Long tenantId = DetailsHelper.getUserDetails().getOrganizationId();
            DetailsHelper.getUserDetails().setUserId(-1L);
            // 获取公司
            Long companyId = companyRepository.getCompanyIdByCompanyNum(tenantId, addressDTO.getCompanyNum());

            // 获取商城地区信息
            List<MallRegion> regions = regionRepository.selectByCondition(Condition.builder(MallRegion.class).andWhere(Sqls.custom().andEqualTo(Region.FIELD_REGION_CODE,addressDTO.getFourthRegionCode())).build());
            String parentRegionName = mallRegionService.getParentNames(regions.get(0).getRegionId(), StringUtils.EMPTY);

            // 校验库存组织
            List<InvOrganizationVO> organizationVOS = invOrganizationRepository.selectByCondition(Condition.builder(InvOrganizationVO.class).andWhere(Sqls.custom().andEqualTo(InvOrganizationVO.FIELD_ORGANIZATION_CODE, addressDTO.getInvOrganizationCode()).andEqualTo(InvOrganizationVO.FIELD_TENANT_ID, tenantId).andNotEqualTo(InvOrganizationVO
                    .FIELD_SOURCE_CODE, "SYSTEM")).build());

            List<Address> dbAddress = addressRepository.selectByCondition(Condition.builder(Address.class).andWhere(Sqls.custom().andEqualTo(Address.FIELD_INV_ORGANIZATION_ID,organizationVOS.get(0).getOrganizationId())).build());

            Address address = new Address();
            if (!ObjectUtils.isEmpty(dbAddress)){
                address.setAddressId(dbAddress.get(0).getAddressId());
                address.setObjectVersionNumber(dbAddress.get(0).getObjectVersionNumber());
            }
            address.setTenantId(tenantId);
            address.setCompanyId(companyId);
            address.setAddressType(ScecConstants.AdressType.RECEIVER);
            address.setRegionId(regions.get(0).getRegionId());
            address.setContactName("屈臣氏虚拟人员");
            address.setAddress(addressDTO.getAddress());
            address.setMobile("131xxxx8888");
            address.setFullAddress(parentRegionName+addressDTO.getAddress());
            address.setBelongType(1);
            address.setEnabledFlag(1);
            address.setInvOrganizationId(organizationVOS.get(0).getOrganizationId());

            addressService.insertAndUpdateAddress(address);
        }
        return true;
    }
}
