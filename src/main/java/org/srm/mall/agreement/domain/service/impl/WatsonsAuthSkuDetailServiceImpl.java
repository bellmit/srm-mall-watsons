package org.srm.mall.agreement.domain.service.impl;


import org.apache.servicecomb.pack.omega.context.annotations.SagaStart;
import org.hzero.core.base.BaseConstants;
import org.hzero.core.util.ResponseUtils;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.srm.boot.saga.utils.SagaClient;
import org.srm.mall.agreement.api.dto.AuthRangeDTO;
import org.srm.mall.agreement.api.dto.WatsonsAuthDimIntValueDTO;
import org.srm.mall.agreement.api.dto.WatsonsAuthDimStringValueDTO;
import org.srm.mall.agreement.domain.entity.*;
import org.srm.mall.agreement.domain.repository.AgreementLineRepository;
import org.srm.mall.agreement.domain.repository.AgreementRepository;
import org.srm.mall.agreement.domain.repository.PriceLibScopeLnRepository;
import org.srm.mall.agreement.domain.repository.WastonsAgreementDetailRepository;
import org.srm.mall.common.constant.AuthConstants;
import org.srm.mall.common.constant.ScecConstants;
import org.srm.mall.common.enums.WatsonsAuthDimensionEnum;
import org.srm.mall.common.feign.SagmRemoteService;
import org.srm.mall.infra.constant.WatsonsConstants;
import org.srm.web.annotation.Tenant;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 权限商品明细应用服务默认实现
 *
 * @author fu.ji@hand-china.com 2020-10-03 16:01:18
 */
@Service
@Tenant(WatsonsConstants.TENANT_NUMBER)
public class WatsonsAuthSkuDetailServiceImpl extends AuthSkuDetailServiceImpl {
    private static final Logger logger = LoggerFactory.getLogger(WatsonsAuthSkuDetailServiceImpl.class);
    @Autowired
    private AgreementRepository agreementRepository;
    @Autowired
    private AgreementLineRepository agreementLineRepository;
    @Autowired
    private SagmRemoteService sagmRemoteService;
    @Autowired
    private WastonsAgreementDetailRepository wastonsAgreementDetailRepository;
    @Autowired
    private PriceLibScopeLnRepository priceLibScopeLnRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    @SagaStart
    public void agreementAuthSkuDetail(Long tenantId, List<AgreementDetail> agreementDetails) {
        if (ObjectUtils.isEmpty(agreementDetails)) {
            return;
        }
        //  二开 非价格库的走标准的逻辑
        String ids = agreementDetails.stream().map(agreementDetailTemp -> String.valueOf(agreementDetailTemp.getAgreementLineId())).collect(Collectors.joining(BaseConstants.Symbol.COMMA));
        List<AgreementLine> agreementLines = agreementLineRepository.selectByIds(ids);
        Map<Long, AgreementLine> agreementLineMap = agreementLines.stream().collect(Collectors.toMap(AgreementLine::getAgreementLineId, k -> k, (v1, v2) -> v1));
        List<AgreementDetail> agreementDetailList = agreementDetails.stream().filter(agreementDetail -> !ScecConstants.AgreementFrom.PRICE.equals(agreementLineMap.get(agreementDetail.getAgreementLineId()).getSourceFrom())).collect(Collectors.toList());
        super.agreementAuthSkuDetail(tenantId, agreementDetailList);
        //  价格库的走自定义权限集
        List<AgreementDetail> priceAgreementDetailList = agreementDetails.stream().filter(agreementDetail -> ScecConstants.AgreementFrom.PRICE.equals(agreementLineMap.get(agreementDetail.getAgreementLineId()).getSourceFrom())).collect(Collectors.toList());
        handPriceAuthSkuDetail(tenantId, priceAgreementDetailList, agreementLineMap);
    }

    private void handPriceAuthSkuDetail(Long tenantId, List<AgreementDetail> priceAgreementDetailList, Map<Long, AgreementLine> agreementLineMap) {
        if (ObjectUtils.isEmpty(priceAgreementDetailList)) {
            return;
        }

        //    查询已存在权限集的数据
      // 维度配置
      List<WatsonsAuthorityList> watsonsAuthorityLists=wastonsAgreementDetailRepository.queryExistsAuth(priceAgreementDetailList,WatsonsAuthDimensionEnum.values());
        Map<Long, WatsonsAuthorityList> watsonsAuthorityListMap = watsonsAuthorityLists.stream().collect(Collectors.toMap(WatsonsAuthorityList::getAgreementDetailId, k -> k, (v1, v2) -> v1));
        List<AuthSkuDetail> authSkuDetails=new ArrayList<>(watsonsAuthorityLists.size());
        Iterator<AgreementDetail> iterator = priceAgreementDetailList.iterator();
        while (iterator.hasNext()) {
            AgreementDetail agreementDetail = iterator.next();
            if (!ObjectUtils.isEmpty(watsonsAuthorityListMap)&&watsonsAuthorityListMap.containsKey(agreementDetail.getAgreementDetailId())) {
                authSkuDetails.add(new AuthSkuDetail(agreementDetail, watsonsAuthorityListMap.get(agreementDetail.getAgreementDetailId())));
                iterator.remove();
            }
        }

        //  不存在的进行权限集创建
        if (!ObjectUtils.isEmpty(priceAgreementDetailList)) {
            // 查询权限
            // 查询维度
            // 查询维度明细
            List<WatsonsPriceLibScope> watsonsPriceLibScopes = wastonsAgreementDetailRepository.queryPriceScope(priceAgreementDetailList);
            List<Long> scopeIds = watsonsPriceLibScopes.stream().filter(watsonsPriceLibScope -> !ObjectUtils.isEmpty(watsonsPriceLibScope.getScopeId())).map(WatsonsPriceLibScope::getScopeId).collect(Collectors.toList());
            List<PriceLibScopeLn> priceLibScopeLns = priceLibScopeLnRepository.selectByCondition(Condition.builder(PriceLibScopeLn.class).andWhere(Sqls.custom().andIn(PriceLibScopeLn.FIELD_SCOPE_ID, scopeIds)).build());
            if (!ObjectUtils.isEmpty(priceLibScopeLns)) {
                Map<Long, List<PriceLibScopeLn>> priceLibScopeLnMap = priceLibScopeLns.stream().collect(Collectors.groupingBy(PriceLibScopeLn::getScopeId));
                for (WatsonsPriceLibScope watsonsPriceLibScope : watsonsPriceLibScopes) {
                    if (!ObjectUtils.isEmpty(watsonsPriceLibScope.getScopeId())) {
                        watsonsPriceLibScope.setPriceLibScopeLns(priceLibScopeLnMap.get(watsonsPriceLibScope.getScopeId()));
                    }
                }
            }
            Map<Long, List<WatsonsPriceLibScope>> watsonsPriceLibScopeMap = watsonsPriceLibScopes.stream().collect(Collectors.groupingBy(WatsonsPriceLibScope::getAgreementDetailId));
            // 按权限维度分组
            Map<String, List<AgreementDetail>> priceAgreementDetailMap = priceAgreementDetailList.stream().collect(Collectors.groupingBy(agreementDetail -> groupByKey(agreementDetail,watsonsPriceLibScopeMap)));
            for (Map.Entry<String, List<AgreementDetail>> entry : priceAgreementDetailMap.entrySet()) {
                // 创建新的权限集
                List<AgreementDetail> agreementDetails = entry.getValue();
                AuthorityList authorityList = priceCreateAuthority(agreementDetails, watsonsPriceLibScopeMap,agreementLineMap);
                for (AgreementDetail agreementDetail : agreementDetails) {
                    authSkuDetails.add(new AuthSkuDetail(agreementDetail,authorityList));
                }
            }
        }
        //需要进行创建权限集的数据
        //    添加进入权限集中
        String sagaKey = SagaClient.getSagaKey();
        ResponseUtils.getResponse(sagmRemoteService.sagaBatchSave(tenantId, sagaKey, authSkuDetails), AuthorityList.class);

    }

    private String groupByKey(AgreementDetail agreementDetail, Map<Long, List<WatsonsPriceLibScope>> watsonsPriceLibScopeMap) {
        StringBuilder stringBuilder=new StringBuilder();
        List<WatsonsPriceLibScope> watsonsPriceLibScopes = watsonsPriceLibScopeMap.get(agreementDetail.getAgreementDetailId());
        if (watsonsPriceLibScopes.size() == 1 && ObjectUtils.isEmpty(watsonsPriceLibScopes.get(0).getDimensionCode())) {
            return "";
        }
        Map<String, WatsonsAuthDimensionEnum> watsonsAuthDimensionEnumMap = WatsonsAuthDimensionEnum.getWatsonsAuthDimensionEnumMap();
        //进行排序
        watsonsPriceLibScopes.sort(Comparator.comparing(WatsonsPriceLibScope::getDimensionCode));
        // 进行字符串拼接
        for (WatsonsPriceLibScope watsonsPriceLibScope : watsonsPriceLibScopes) {
            // 只处理需要比较的维度
            WatsonsAuthDimensionEnum watsonsAuthDimensionEnum = watsonsAuthDimensionEnumMap.get(watsonsPriceLibScope.getDimensionCode());
            if (ObjectUtils.isEmpty(watsonsAuthDimensionEnum)) {
                continue;
            }
            stringBuilder.append(BaseConstants.Symbol.DOLLAR);
            stringBuilder.append(watsonsPriceLibScope.getDimensionCode());
            List<PriceLibScopeLn> priceLibScopeLns = watsonsPriceLibScope.getPriceLibScopeLns();
            if (!ObjectUtils.isEmpty(priceLibScopeLns)) {
                stringBuilder.append(priceLibScopeLns.stream().map(priceLibScopeLn->!ObjectUtils.isEmpty(priceLibScopeLn.getDataId())?String.valueOf(priceLibScopeLn.getDataId()):priceLibScopeLn.getDataCode()).collect(Collectors.toSet()).toString());
            }
            stringBuilder.append(BaseConstants.Symbol.WELL);
        }
        return stringBuilder.toString();
    }


    @Transactional(rollbackFor = Exception.class)
    public AuthorityList priceCreateAuthority(List<AgreementDetail> authSkuDetails, Map<Long, List<WatsonsPriceLibScope>> watsonsPriceLibScopeMap, Map<Long, AgreementLine> agreementLineMap) {
        AgreementDetail agreementDetail = authSkuDetails.get(0);
        List<WatsonsPriceLibScope> watsonsPriceLibScopes = watsonsPriceLibScopeMap.get(agreementDetail.getAgreementDetailId());
        AgreementLine agreementLine = agreementLineMap.get(agreementDetail.getAgreementLineId());
        Agreement agreement = agreementRepository.selectByPrimaryKey(agreementLine.getAgreementId());
        AuthorityList authorityList = new AuthorityList();
        authorityList.setAgreementType(AuthConstants.AuthAgreementType.PUR_AGREEMENT);
        authorityList.setAgreementHeaderId(agreementLine.getAgreementId());
        authorityList.setAllSkuEnable(BaseConstants.Flag.NO);
        authorityList.setStatusCode(AuthConstants.AuthorityListStatusCode.PUBLISHED);
        authorityList.setTenantId(agreementDetail.getTenantId());
        authorityList.setAgreementHeaderNum(agreement.getAgreementNumber());
        authorityList.setControlWayCode(AuthConstants.AuthControlWay.CONTAIN);
        authorityList.setAllUserEnable(ObjectUtils.isEmpty(watsonsPriceLibScopes)? BaseConstants.Flag.YES : BaseConstants.Flag.NO);
        authorityList.setAutomaticallyFlag(BaseConstants.Flag.YES);
        authorityList.setRemark("Automatically created");
        List<AuthRangeDTO> authRangeDTOS = new ArrayList<>(WatsonsAuthDimensionEnum.values().length);
        if (!ObjectUtils.isEmpty(watsonsPriceLibScopes)) {
            for (WatsonsPriceLibScope watsonsPriceLibScope : watsonsPriceLibScopes) {
                WatsonsAuthDimensionEnum watsonsAuthDimensionEnum = WatsonsAuthDimensionEnum.valueOfPriceScope(watsonsPriceLibScope.getDimensionCode());
                if (ObjectUtils.isEmpty(watsonsAuthDimensionEnum)) {
                    continue;
                }
                if (Objects.equals(watsonsAuthDimensionEnum.getValueType(), WatsonsConstants.ValueType.INT)) {
                    AuthRangeDTO<List<WatsonsAuthDimIntValueDTO>> authRangeDTO = new AuthRangeDTO<>();
                    authRangeDTO.setType(AuthConstants.AuthRangeType.USER);
                    authRangeDTO.setAuthDimension(watsonsAuthDimensionEnum.getDimensionCode());
                    List<PriceLibScopeLn> priceLibScopeLns = watsonsPriceLibScope.getPriceLibScopeLns();
                    if (!ObjectUtils.isEmpty(priceLibScopeLns)) {
                        List<WatsonsAuthDimIntValueDTO> watsonsAuthDimIntValueDTOS = priceLibScopeLns.stream().map((priceLibScopeLn) -> {
                            WatsonsAuthDimIntValueDTO watsonsAuthDimIntValueDTO = new WatsonsAuthDimIntValueDTO();
                            watsonsAuthDimIntValueDTO.setDataInt(priceLibScopeLn.getDataId());
                            return watsonsAuthDimIntValueDTO;
                        }).collect(Collectors.toList());
                        authRangeDTO.setData(watsonsAuthDimIntValueDTOS);
                    }
                    authRangeDTOS.add(authRangeDTO);
                }
                if (Objects.equals(watsonsAuthDimensionEnum.getValueType(), WatsonsConstants.ValueType.STRING)) {
                    AuthRangeDTO<List<WatsonsAuthDimStringValueDTO>> authRangeDTO = new AuthRangeDTO<>();
                    authRangeDTO.setType(AuthConstants.AuthRangeType.USER);
                    authRangeDTO.setAuthDimension(watsonsAuthDimensionEnum.getDimensionCode());
                    List<PriceLibScopeLn> priceLibScopeLns = watsonsPriceLibScope.getPriceLibScopeLns();
                    if (!ObjectUtils.isEmpty(priceLibScopeLns)) {
                        List<WatsonsAuthDimStringValueDTO> watsonsAuthDimStringValueDTOS = priceLibScopeLns.stream().map((priceLibScopeLn) -> {
                            WatsonsAuthDimStringValueDTO watsonsAuthDimIntValueDTO = new WatsonsAuthDimStringValueDTO();
                            watsonsAuthDimIntValueDTO.setDataString(!ObjectUtils.isEmpty(priceLibScopeLn.getDataId())?String.valueOf(priceLibScopeLn.getDataId()):priceLibScopeLn.getDataCode());
                            return watsonsAuthDimIntValueDTO;
                        }).collect(Collectors.toList());
                        authRangeDTO.setData(watsonsAuthDimStringValueDTOS);
                    }
                    authRangeDTOS.add(authRangeDTO);
                }
            }
        }
        authorityList.setAuthRangeDTOS(authRangeDTOS);
        String sagaKey = SagaClient.getSagaKey();
        return ResponseUtils.getResponse(sagmRemoteService.sagaCreate(authorityList.getTenantId(), sagaKey, authorityList), AuthorityList.class);
    }
}
