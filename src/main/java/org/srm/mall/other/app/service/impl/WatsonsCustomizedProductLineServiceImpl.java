package org.srm.mall.other.app.service.impl;

import com.alibaba.fastjson.JSONObject;
import io.choerodon.core.exception.CommonException;
import org.apache.commons.collections4.CollectionUtils;
import org.hzero.core.base.BaseAppService;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.srm.mall.common.constant.ScecConstants;
import org.srm.mall.common.feign.dto.product.SkuCustomDTO;
import org.srm.mall.common.feign.dto.product.SpuCustomAttrGroup;
import org.srm.mall.common.utils.TransactionalComponent;
import org.srm.mall.infra.constant.WatsonsConstants;
import org.srm.mall.other.api.dto.CustomizedProductCheckDTO;
import org.srm.mall.other.api.dto.CustomizedProductDTO;
import org.srm.mall.other.api.dto.WatsonsCustomizedProductDTO;
import org.srm.mall.other.app.service.AllocationInfoService;
import org.srm.mall.other.app.service.WatsonsCustomizedProductLineService;
import org.srm.mall.other.app.service.WatsonsShoppingCartService;
import org.srm.mall.other.domain.entity.CustomizedProductLine;
import org.srm.mall.other.domain.entity.CustomizedProductValue;
import org.srm.mall.other.domain.entity.ShoppingCart;
import org.srm.mall.other.domain.entity.WatsonsShoppingCart;
import org.srm.mall.other.domain.repository.CustomizedProductLineRepository;
import org.srm.mall.other.domain.repository.CustomizedProductValueRepository;
import org.srm.mall.product.domain.repository.ProductWorkbenchRepository;
import org.srm.web.annotation.Tenant;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 *  屈臣氏定制品行表
 *
 * @author jianhao.zhang01@hand-china.com 2021/04/21 10:59 上午
 */
@Service("WatsonsWatsonsCustomizedProductLineService")
@Tenant(WatsonsConstants.TENANT_NUMBER)
public class WatsonsCustomizedProductLineServiceImpl extends CustomizedProductLineServiceImpl implements WatsonsCustomizedProductLineService {
    @Autowired
    private CustomizedProductLineRepository customizedProductLineRepository;

    @Autowired
    private CustomizedProductValueRepository customizedProductValueRepository;

    @Autowired
    private ProductWorkbenchRepository productWorkbenchRepository;

    @Autowired
    private TransactionalComponent transactionalComponent;

    @Autowired
    private AllocationInfoService allocationInfoService;

    private Logger logger = LoggerFactory.getLogger(WatsonsCustomizedProductLineServiceImpl.class);


    @Override
    public List<CustomizedProductLine> selectCustomizedProductList(Long tenantId, WatsonsCustomizedProductDTO watsonsCustomizedProductDTO) {
        if (CollectionUtils.isEmpty(watsonsCustomizedProductDTO.getRelationIdList())){
            return new ArrayList<>();
        }
        List<CustomizedProductLine> customizedProductLineList = customizedProductLineRepository.selectByCondition(Condition.builder(CustomizedProductLine.class).andWhere(Sqls.custom()
                .andEqualTo(CustomizedProductLine.FIELD_TENANT_ID, tenantId).andEqualTo(CustomizedProductLine.FIELD_RELATION_TYPE, watsonsCustomizedProductDTO.getRelationType())
                .andIn(CustomizedProductLine.FIELD_RELATION_ID, watsonsCustomizedProductDTO.getRelationIdList())).build());
        if (CollectionUtils.isEmpty(customizedProductLineList)){
            return new ArrayList<>();
        }
        //查询对应的属性行
        List<CustomizedProductValue> customizedProductValueList = customizedProductValueRepository.selectByCondition(Condition.builder(CustomizedProductValue.class).andWhere(Sqls.custom()
                .andIn(CustomizedProductValue.FIELD_CP_LINE_ID, customizedProductLineList.stream().map(CustomizedProductLine::getCpLineId).collect(Collectors.toList()))).build());
        Map<Long, List<CustomizedProductValue>> customizedProductValueMap = customizedProductValueList.stream().collect(Collectors.groupingBy(CustomizedProductValue::getCpLineId));
        for (CustomizedProductLine customizedProductLine : customizedProductLineList){
            customizedProductLine.setCustomizedProductValueList(customizedProductValueMap.get(customizedProductLine.getCpLineId()));
        }
        logger.info("the customizedProductLineList is {}", JSONObject.toJSON(customizedProductLineList));
        return customizedProductLineList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<CustomizedProductLine> selectSingleProductCustomizedProduct(Long tenantId, WatsonsCustomizedProductDTO watsonsCustomizedProductDTO) {
        List<CustomizedProductLine> customizedProductLineList = selectCustomizedProductList(tenantId, watsonsCustomizedProductDTO.createCustomizedProductParam());
        List<SpuCustomAttrGroup> spuCustomAttrGroups = productWorkbenchRepository.selectSingleSkuCustomAttrNoException(tenantId, watsonsCustomizedProductDTO.getProductId());
        for (CustomizedProductLine customizedProductLine : customizedProductLineList){
            //检查该定制品行和商品中心返回的定制品属性是否有出入
            CustomizedProductCheckDTO customizedProductCheck = customizedProductLine.check(spuCustomAttrGroups);
            transactionalComponent.requiredNew(() -> updateCustomizedProductInfo(customizedProductCheck));
            if (customizedProductCheck.getSuccess() == 0) {
                throw new CommonException(ScecConstants.ProductCustomized.ERROR_PRODUCT_CUSTOMIZED_CHANGE);
            }
            if (customizedProductCheck.getSuccess() == 1){
                //设置为定制品的最新金额
                customizedProductLine.setLatestPrice(watsonsCustomizedProductDTO.getLatestPrice());
                //定制品行计算价格
                allocationInfoService.calculateForCpLine(customizedProductLine);
            }
        }
        return customizedProductLineList;
    }

    @Override
    @Transactional
    public List<CustomizedProductLine> createOrUpdateForWatsons(Long tenantId, List<CustomizedProductLine> customizedProductLineList) {
        if (CollectionUtils.isEmpty(customizedProductLineList)){
            return customizedProductLineList;
        }
        List<SpuCustomAttrGroup> spuCustomAttrGroups = productWorkbenchRepository.selectSingleSkuCustomAttrNoException(tenantId, customizedProductLineList.get(0).getSkuId());
        for (CustomizedProductLine customizedProductLine : customizedProductLineList){
            //校验 定制品属性是否变化
            CustomizedProductCheckDTO customizedProductCheck = customizedProductLine.check(spuCustomAttrGroups);
            transactionalComponent.requiredNew(() -> updateCustomizedProductInfo(customizedProductCheck));
            if (customizedProductCheck.getSuccess() == 0){
                throw new CommonException(ScecConstants.ProductCustomized.ERROR_PRODUCT_CUSTOMIZED_CHANGE);
            }
            if (customizedProductLine.getCpLineId() == null) {
                //新增
                processAssignmentValue(tenantId,customizedProductLine);
                logger.info("the customizedProductLine need to be inserted is {}",JSONObject.toJSON(customizedProductLine));
                customizedProductLineRepository.insertSelective(customizedProductLine);
            } else {
                //更新
                processAssignmentValue(tenantId,customizedProductLine);
                logger.info("the customizedProductLine need to be updated is {}",JSONObject.toJSON(customizedProductLine));
                customizedProductLineRepository.updateByPrimaryKeySelective(customizedProductLine);
            }

            for (CustomizedProductValue customizedProductValue : customizedProductLine.getCustomizedProductValueList()){
                customizedProductValue.setCpLineId(customizedProductLine.getCpLineId());
                customizedProductValue.setTenantId(tenantId);
                customizedProductValue.check();
                if (customizedProductValue.getCpValueId() == null){
                    logger.info("the customizedProductValue need to be inserted is {}",JSONObject.toJSON(customizedProductValue));
                    customizedProductValueRepository.insertSelective(customizedProductValue);
                } else {
                    logger.info("the customizedProductValue need to be updated is {}",JSONObject.toJSON(customizedProductValue));
                    customizedProductValueRepository.updateByPrimaryKeySelective(customizedProductValue);
                }
            }
        }
        return customizedProductLineList;
    }

    private void processAssignmentValue(Long tenantId, CustomizedProductLine customizedProductLine) {
        if (ObjectUtils.isEmpty(customizedProductLine.getAllocationId()) || ObjectUtils.isEmpty(customizedProductLine.getCpQuantity()) || ObjectUtils.isEmpty(customizedProductLine.getLatestPrice())
                || ObjectUtils.isEmpty(customizedProductLine.getSkuId()) || ObjectUtils.isEmpty(customizedProductLine.getShipperFlag()) || ObjectUtils.isEmpty(customizedProductLine.getCustomizedProductValueList())) {
            throw new CommonException(ScecConstants.ProductCustomized.DATA_CHECK_ERROR);
        }
        customizedProductLine.setTenantId(tenantId);
        customizedProductLine.setRelationId(customizedProductLine.getAllocationId());
        customizedProductLine.setRelationType(WatsonsConstants.ALLOCATION_INFO);
        allocationInfoService.calculateForCpLine(customizedProductLine);
    }

}
