package org.srm.mall.other.app.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import io.choerodon.core.domain.Page;
import io.choerodon.core.domain.PageInfo;
import io.choerodon.core.exception.CommonException;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.apache.commons.collections4.CollectionUtils;
import org.hzero.core.base.BaseAppService;
import org.hzero.core.base.BaseConstants;
import org.hzero.core.util.ResponseUtils;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.srm.mall.common.constant.ScecConstants;
import org.srm.mall.common.feign.*;
import org.srm.mall.common.feign.*;
import org.srm.mall.common.feign.WatsonsProjectCostRemoteService;
import org.srm.mall.common.feign.SagmRemoteService;
import org.srm.mall.common.feign.SmdmRemoteNewService;
import org.srm.mall.common.feign.*;
import org.srm.mall.common.feign.dto.product.SpuCustomAttrGroup;
import org.srm.mall.infra.constant.WatsonsConstants;
import org.srm.mall.other.api.dto.*;
import org.srm.mall.other.api.dto.WhLovResultDTO;
import org.srm.mall.other.app.service.WatsonsShoppingCartService;
import org.srm.mall.other.domain.entity.*;
import org.srm.mall.other.api.dto.CeLovResultDTO;
import org.srm.mall.other.api.dto.WhLovResultDTO;
import org.srm.mall.other.api.dto.WatsonsShoppingCartDTO;
import org.srm.mall.other.app.service.AllocationInfoService;
import org.srm.mall.other.app.service.ShoppingCartService;
import org.srm.mall.other.domain.repository.AllocationInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.srm.mall.other.domain.repository.CustomizedProductLineRepository;
import org.srm.mall.other.domain.repository.CustomizedProductValueRepository;
import org.srm.mall.product.api.dto.ItemCategoryDTO;
import org.srm.mall.product.api.dto.PriceParamDTO;
import org.srm.mall.product.api.dto.PriceResultDTO;
import org.srm.mall.product.api.dto.ProductSaleCheckDTO;
import org.srm.mall.product.domain.repository.ProductWorkbenchRepository;
import org.srm.mall.region.domain.entity.Address;
import org.srm.mall.region.domain.repository.AddressRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.validation.constraints.NotBlank;

/**
 * 屈臣氏费用分配表应用服务默认实现
 *
 * @author yuewen.wei@hand-china.com 2020-12-21 15:35:27
 */
@Service
public class AllocationInfoServiceImpl extends BaseAppService implements AllocationInfoService {


    private static final Logger logger = LoggerFactory.getLogger(AllocationInfoServiceImpl.class);


    @Autowired
    private AllocationInfoRepository allocationInfoRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private SagmRemoteService sagmRemoteService;

    @Autowired
    @Qualifier("watsonsShoppingCartService")
    private ShoppingCartService shoppingCartService;

    @Autowired
    private WatsonsProjectCostRemoteService watsonsProjectCostRemoteService;

    @Autowired
    private SmdmRemoteNewService smdmRemoteNewService;

    @Autowired
    private WatsonsWareHouseRemoteService watsonsWareHouseRemoteService;

    @Autowired
    private WatsonsCeInfoRemoteService watsonsCeInfoRemoteService;

    @Autowired
    private ProductWorkbenchRepository productWorkbenchRepository;

    @Autowired
    private CustomizedProductLineRepository customizedProductLineRepository;

    @Autowired
    private CustomizedProductValueRepository customizedProductValueRepository;

    @Autowired
    @Lazy
    private WatsonsShoppingCartService watsonsShoppingCartService;



    @Override
    public List<AllocationInfo> list(Long tenantId, Long cartId) {
        List<AllocationInfo> allocationInfoList = allocationInfoRepository.select(AllocationInfo.FIELD_CART_ID, cartId);
        if (!CollectionUtils.isEmpty(allocationInfoList)) {
            for (AllocationInfo allocationInfo : allocationInfoList) {
                allocationInfo.setTotalAmount(allocationInfo.getPrice().multiply(new BigDecimal(allocationInfo.getQuantity())));
            }
        }
        return allocationInfoList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<AllocationInfo> create(Long tenantId, WatsonsShoppingCart watsonsShoppingCart) {
        List<AllocationInfo> allocationInfoList = watsonsShoppingCart.getAllocationInfoList();
        if (CollectionUtils.isEmpty(allocationInfoList)) {
            logger.error("未进行费用分配！:{}", allocationInfoList);
            throw new CommonException("未进行费用分配!");
        }
        for (AllocationInfo allocationInfo : allocationInfoList) {
            //查询对应的地址
//            handleReceiverAddress(allocationInfo, tenantId);

            //校验对应的地址商品是否可售,等待价格服务提供接口
            saleAndStockCheck(tenantId, allocationInfo, watsonsShoppingCart);
            if (allocationInfo.getAllocationId() == null) {
                //保存费用分配的时候插入关联id
                allocationInfoRepository.insert(allocationInfo);
                if (!ObjectUtils.isEmpty(allocationInfo.getCustomizedProductLine())){
                    allocationInfo.getCustomizedProductLine().setRelationId(allocationInfo.getAllocationId());
                }
            } else {
                AllocationInfo allocationInfoParam = allocationInfoRepository.selectByPrimaryKey(allocationInfo.getAllocationId());
                allocationInfo.setObjectVersionNumber(allocationInfoParam.getObjectVersionNumber());
                allocationInfoRepository.updateByPrimaryKey(allocationInfo);
            }
        }

        //合并相同数据
        watsonsShoppingCart.setAllocationInfoList(mergeSameAllocationInfo(tenantId,watsonsShoppingCart));
        return allocationInfoList;
    }

    /**
     * 商品可售行与库存检查
     *
     * @param tenantId
     * @param allocationInfo
     * @param watsonsShoppingCart
     */
    private void saleAndStockCheck(Long tenantId, AllocationInfo allocationInfo, WatsonsShoppingCart watsonsShoppingCart) {
        String provinceId = null;
        String cityId = null;
        String countyId = null;
        // 校验可售
        PriceParamDTO priceParamDTO = new PriceParamDTO(tenantId, addressRepository.querySecondRegionId(allocationInfo.getAddressId()), null, watsonsShoppingCart.getProductId());
        priceParamDTO.setAddressId(allocationInfo.getAddressId());
        priceParamDTO.getSkuParamDTOS().get(0).setQuantity(BigDecimal.ONE);
        priceParamDTO.setUnitLevelPath(watsonsShoppingCart.getLevelPath());
        WatsonsRegionDTO watsonsRegionDTO = allocationInfoRepository.selectRegionInfoByRegionId(allocationInfo.getLastRegionId());
        if(ObjectUtils.isEmpty(watsonsRegionDTO.getLevelPath())){
            throw new CommonException(allocationInfo.getCostShopName()+"的地址的区域信息不存在,无法校验库存和可售信息!");
        }
        String levelPath = watsonsRegionDTO.getLevelPath();
        String[] splitRes = levelPath.split("\\.");
        if(splitRes.length < 3){
            throw new CommonException(allocationInfo.getCostShopName()+"选择的地址区域不满足省市区三级或以上,无法校验库存和可售信息!");
        }
        WatsonsRegionDTO province = allocationInfoRepository.selectRegionInfoByRegionCode(splitRes[0]);
        WatsonsRegionDTO city = allocationInfoRepository.selectRegionInfoByRegionCode(splitRes[1]);
        WatsonsRegionDTO region = allocationInfoRepository.selectRegionInfoByRegionCode(splitRes[2]);
        provinceId = province.getRegionId().toString();
        cityId = city.getRegionId().toString();
        countyId = region.getRegionId().toString();
        priceParamDTO.setRegionId(Long.valueOf(cityId));

        ProductSaleCheckDTO productSaleCheckDTO = new ProductSaleCheckDTO();
        productSaleCheckDTO.setProvinceId(provinceId);
        productSaleCheckDTO.setCityId(cityId);
        productSaleCheckDTO.setCountyId(countyId);
        priceParamDTO.setEcProductCheckDto(productSaleCheckDTO);

        ResponseEntity<String> result = sagmRemoteService.selectPrice(tenantId, ScecConstants.SagmSourceCode.SHOPPING_CART, priceParamDTO);
        List<PriceResultDTO> priceResultDTOS = ResponseUtils.getResponse(result, new TypeReference<List<PriceResultDTO>>() {
        });
        if (BaseConstants.Flag.NO.equals(priceResultDTOS.get(0).getSaleEnable())) {
            throw new CommonException(WatsonsConstants.ErrorCode.INV_ORGANIZATION_REGION_OOS, allocationInfo.getCostShopName());
        }
        //TODO 校验库存

    }

    private List<AllocationInfo> mergeSameAllocationInfo(Long tenantId, WatsonsShoppingCart watsonsShoppingCart) {
        List<AllocationInfo> result = new ArrayList<>();
        List<AllocationInfo> allocationInfoList = allocationInfoRepository.select(AllocationInfo.FIELD_CART_ID, watsonsShoppingCart.getCartId());
        //按费用承担店铺id  费用承担部门id 仓转店收获仓id分组  该行商品对应的费用分配
        //按费用承担店铺id  费用承担部门id 仓转店收获仓id  这三个维度作为相同数据
        //关联定制品列表，若定制品的属性值也一样，才能合并   按属性map以后相同的定制品信息放在一起merge  删除多余的表中的费用分配数据 并重新插入
        List<Long> detailIdList = new ArrayList<>();
        if (watsonsShoppingCart.getCustomFlag() != null && watsonsShoppingCart.getCustomFlag() == 1){
            List<SpuCustomAttrGroup> spuCustomAttrGroups = productWorkbenchRepository.selectSingleSkuCustomAttrNoException(tenantId, watsonsShoppingCart.getProductId());
            if (!CollectionUtils.isEmpty(spuCustomAttrGroups)){
                //此处取出前端传来的定制品数据，由于可能存在 费用分配和定制品数据都是新增的情况，此时费用分配先插入并进行合并判断，若调用查询费用分配对应定制品数据的接口由于定制品还未插入，是找不到的
                //因此此处直接取shoppingCart中传来的预算对应定制品数据，不去查表
                List<AllocationInfo> AllocationInfoWithCpList = watsonsShoppingCart.getAllocationInfoList();
                Map<Long, AllocationInfo> allocationInfoMap = AllocationInfoWithCpList.stream().collect(Collectors.toMap(AllocationInfo::getAllocationId, Function.identity(), (k1, k2)->k1));
                for (AllocationInfo allocationInfo : allocationInfoList){
                    allocationInfo.setCustomizedProductLine(allocationInfoMap.get(allocationInfo.getAllocationId()) == null ? null : allocationInfoMap.get(allocationInfo.getAllocationId()).getCustomizedProductLine());
                }
            }
            for (SpuCustomAttrGroup spuCustomAttrGroup : spuCustomAttrGroups){
                detailIdList.addAll(spuCustomAttrGroup.getDetailIdList());
            }
        }

        //按店铺 仓转店 定制品detailid对应的cpValue来分组费用分配
        //若定制品的属性值也一样，才能合并该费用分配
        //如果所有的定制品属性和原有的分组都一样 则把该定制品行数据进行合并  删除原有的value 和 line
        Map<String, List<AllocationInfo>> allocationMap = allocationInfoList.stream().collect(Collectors.groupingBy(allocationInfo -> allocationInfo.groupKey(detailIdList)));
        for (Map.Entry<String, List<AllocationInfo>> entry : allocationMap.entrySet()) {
            List<AllocationInfo> tempInfoList = entry.getValue();
            if (tempInfoList.size() > 1) {
                AllocationInfo allocationInfo = tempInfoList.get(0);
                CustomizedProductLine customizedProductLine = allocationInfo.getCustomizedProductLine();
                allocationInfo.setDeliveryTypeMeaning(watsonsShoppingCart.getAllocationInfoList().get(0).getDeliveryTypeMeaning());
                for (int i = 1; i < tempInfoList.size(); i++) {
                    //每个相同组的  费用分配组的第一个费用分配的数量设置为自己和该相同组的其他剩余费用分配的数量  即合并
                    allocationInfo.setQuantity(allocationInfo.getQuantity() + tempInfoList.get(i).getQuantity());
                    allocationInfoRepository.deleteByPrimaryKey(tempInfoList.get(i));
                    //定制品的合并
                    if (!ObjectUtils.isEmpty(customizedProductLine)){
                        //所有定制品行数据 都刷成所有全部的数量和金额
                        customizedProductLine.setCpAmount(customizedProductLine.getCpAmount().add(tempInfoList.get(i).getCustomizedProductLine().getCpAmount()));
                        customizedProductLine.setCpQuantity(customizedProductLine.getCpQuantity().add(tempInfoList.get(i).getCustomizedProductLine().getCpQuantity()));
                        customizedProductLineRepository.deleteByPrimaryKey(tempInfoList.get(i).getCustomizedProductLine());
                        if (!CollectionUtils.isEmpty(tempInfoList.get(i).getCustomizedProductLine().getCustomizedProductValueList())){
                            customizedProductValueRepository.batchDeleteByPrimaryKey(tempInfoList.get(i).getCustomizedProductLine().getCustomizedProductValueList());
                        }
                    }
                }
                allocationInfoRepository.updateByPrimaryKey(allocationInfo);
                if (!ObjectUtils.isEmpty(customizedProductLine)){
                    customizedProductLineRepository.updateOptional(customizedProductLine, CustomizedProductLine.FIELD_CP_QUANTITY, CustomizedProductLine.FIELD_CP_AMOUNT);
                }
                result.add(allocationInfo);
            } else {
                tempInfoList.get(0).setDeliveryTypeMeaning(watsonsShoppingCart.getAllocationInfoList().get(0).getDeliveryTypeMeaning());
                result.addAll(tempInfoList);
            }
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public WatsonsShoppingCart updateShoppingCart(WatsonsShoppingCart watsonsShoppingCart) {
        List<AllocationInfo> allocationInfoList = allocationInfoRepository.select(BudgetInfo.FIELD_CART_ID, watsonsShoppingCart.getCartId());
        BigDecimal quantity = BigDecimal.ZERO;
        //定制品更新购物车商品金额
        watsonsShoppingCartService.calculateCustomizedProductForShoppingCartWhenAllocationUpdate(watsonsShoppingCart);
        if (!CollectionUtils.isEmpty(allocationInfoList)) {
            for (AllocationInfo allocationInfo : allocationInfoList) {
                quantity = quantity.add(new BigDecimal(allocationInfo.getQuantity()));
                allocationInfo.setDeliveryTypeMeaning(watsonsShoppingCart.getAllocationInfoList().get(0).getDeliveryTypeMeaning());
            }
            watsonsShoppingCart.setQuantity(quantity);
            shoppingCartService.create(watsonsShoppingCart);
        }
        watsonsShoppingCart.setAllocationInfoList(allocationInfoList);
        return watsonsShoppingCart;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public WatsonsShoppingCart remove(WatsonsShoppingCart watsonsShoppingCart) {
        if (!CollectionUtils.isEmpty(watsonsShoppingCart.getAllocationInfoList())) {
            allocationInfoRepository.batchDeleteByPrimaryKey(watsonsShoppingCart.getAllocationInfoList());
            updateShoppingCart(watsonsShoppingCart);
        }
        return watsonsShoppingCart;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AllocationInfoDTO batchCreate(Long organizationId, AllocationInfoDTO allocationInfoDTO) {
        //传来的是多个商品，每个商品会带自己的所要购买的全部数量    传来的多个费用分配， 多个商品用同一套费用分配   每个商品都要和所有的费用分配进行计算与分配
        //多个商品看成一个 进行多次费用分配  每个商品都按百分比进行拆分   数量按百分比分配
        //1. 计算每个商品数量
        if (CollectionUtils.isEmpty(allocationInfoDTO.getAllocationInfoList()) || CollectionUtils.isEmpty(allocationInfoDTO.getWatsonsShoppingCartList())) {
            throw new CommonException("参数不能为空");
        }
        //百分比校验是否等于100%
        BigDecimal sumPercent = BigDecimal.ZERO;
        for (AllocationInfo allocationInfo : allocationInfoDTO.getAllocationInfoList()) {
            sumPercent = sumPercent.add(allocationInfo.getPercent());
        }
        if (sumPercent.compareTo(new BigDecimal(100)) != 0) {
            throw new CommonException("百分比不满足100%");
        }
        for (WatsonsShoppingCart watsonsShoppingCart : allocationInfoDTO.getWatsonsShoppingCartList()) {
            //清空该商品原来的分配
            AllocationInfo condition = new AllocationInfo();
            condition.setCartId(watsonsShoppingCart.getCartId());
            allocationInfoRepository.delete(condition);
            List<AllocationInfo> resultList = new ArrayList<>();
            //在该商品下    遍历所有费用分配信息
            for (AllocationInfo allocationInfo : allocationInfoDTO.getAllocationInfoList()) {
                //每个商品，在该费用分配下的数量
                BigDecimal quantity = watsonsShoppingCart.getQuantity().multiply(allocationInfo.getPercent()).divide(new BigDecimal(100), 5, BigDecimal.ROUND_HALF_UP);
                //判断数量是否有小数，若有小数则抛异常
                if (new BigDecimal(quantity.intValue()).compareTo(quantity) != 0) {
                    throw new CommonException("商品：" + watsonsShoppingCart.getProductName() + "的百分比:" + allocationInfo.getPercent() + "数量计算为小数");
                }
                //数量校验的没问题 该费用分配可行
                AllocationInfo result = new AllocationInfo();
                BeanUtils.copyProperties(allocationInfo, result);
                result.setQuantity(quantity.longValue());
                result.setPrice(watsonsShoppingCart.getLatestPrice());
                result.setCartId(watsonsShoppingCart.getCartId());
                resultList.add(result);
            }
            //此时该商品已经和所有的费用分配信息生成了一个费用分配行放入了resultList
            //2. 插入费用分配行
            watsonsShoppingCart.setAllocationInfoList(resultList);
            create(organizationId, watsonsShoppingCart);
        }
        return allocationInfoDTO;
    }


    /**
     * 只传itemid  不传categoryid
     * 只传categoryid 不传itemid
     * 传了itemid  也传了物料id
     *
     * @param organizationId
     * @param itemCategoryId 物料品类id
     * @param itemId         物料id
     * @param size           分页size
     * @param page           分页page
     * @return List<ProjectCost>
     * @author jianhao.zhang01@hand-china.com 2021-01-12 12:50
     */
    @Override
    public List<ProjectCost> selectAllocationProjectLov(Long organizationId, Long itemCategoryId, Long itemId, Integer size, Integer page) {
        //品类和物料都为空，直接报错
        if (ObjectUtils.isEmpty(itemCategoryId) && ObjectUtils.isEmpty(itemId)) {
            throw new CommonException("该商品物料品类和物料都为空,无法查询费用项目!");
        }

        ProjectCost projectCost = new ProjectCost();
        projectCost.setTenantId(organizationId);
        PageRequest pageRequest = new PageRequest();
        pageRequest.setSize(size);
        pageRequest.setPage(page);

//        根据三级品类id查二级品类
//        ResponseEntity<String> itemCategoryDTORes = smdmRemoteNewService.selectSecondaryByThirdItemCategory(organizationId, String.valueOf(itemCategoryId));
//        if (ResponseUtils.isFailed(itemCategoryDTORes)) {
//            logger.error("select secondaryItemCategoryId By thirdItemCategoryId failed:{}", itemCategoryDTORes);
//            throw new CommonException("根据三级物料品类查询二级物料品类失败!");
//        }
//        logger.info("selectSecondaryByThirdItemCategory:{}", itemCategoryDTORes);
//        ItemCategoryDTO itemCategoryResultOne  = ResponseUtils.getResponse(itemCategoryDTORes, new TypeReference<ItemCategoryDTO>() {});
//        Long secondaryCategoryId = itemCategoryResultOne.getParentCategoryId();


        //只传categoryid 不传itemid
        if (!ObjectUtils.isEmpty(itemCategoryId) && ObjectUtils.isEmpty(itemId)) {
            ResponseEntity<String> itemCategoryInfoRes = smdmRemoteNewService.queryById(organizationId, String.valueOf(itemCategoryId));
            if (ResponseUtils.isFailed(itemCategoryInfoRes)) {
                logger.error("query itemCategory info By itemCategoryId error! param itemCategoryId: {}", itemCategoryId);
                throw new CommonException("根据物料品类id查询物料品类信息失败!");
            }
            logger.info("query itemCategory info By itemCategoryId success! param itemCategoryId: {}", itemCategoryId);
            ItemCategoryDTO itemCategoryResultOne = ResponseUtils.getResponse(itemCategoryInfoRes, new TypeReference<ItemCategoryDTO>() {
            });
            if(ObjectUtils.isEmpty(itemCategoryResultOne) || ObjectUtils.isEmpty(itemCategoryResultOne.getLevelPath())){
                throw new CommonException("根据物料品类id查询物料品类信息为空!");
            }
            String levelPath = itemCategoryResultOne.getLevelPath();
            return getProjectCosts(organizationId, projectCost, pageRequest, itemCategoryResultOne, levelPath, "当前物料品类已经是一级品类,无法查询费用项目!");
        }

        //只传itemid  不传categoryid
        if (ObjectUtils.isEmpty(itemCategoryId) && !ObjectUtils.isEmpty(itemId)) {
            ResponseEntity<String> levelPathByItemId = smdmRemoteNewService.queryLevelPathByItemId(organizationId, itemId);
            if (ResponseUtils.isFailed(levelPathByItemId)) {
                logger.error("query LevelPath By ItemId error! param itemId: {}", itemId);
                throw new CommonException("根据物料id查询物料品类信息失败!");
            }
            logger.info("query LevelPath By ItemId success, param itemId: {}", itemId);
            ItemCategoryDTO itemCategoryResultOne = ResponseUtils.getResponse(levelPathByItemId, new TypeReference<ItemCategoryDTO>() {
            });
            if(ObjectUtils.isEmpty(itemCategoryResultOne) || ObjectUtils.isEmpty(itemCategoryResultOne.getLevelPath())){
                throw new CommonException("根据物料id查询物料品类信息为空!");
            }
            String levelPath = itemCategoryResultOne.getLevelPath();
            return getProjectCosts(organizationId, projectCost, pageRequest, itemCategoryResultOne, levelPath, "当前物料品类已经是一级品类, 无法查询费用项目!");
        }

        //传了itemId也传了categoryId
        if (!ObjectUtils.isEmpty(itemCategoryId) && !ObjectUtils.isEmpty(itemId)) {
            ResponseEntity<String> itemCategoryInfoRes = smdmRemoteNewService.queryById(organizationId, String.valueOf(itemCategoryId));
            if (ResponseUtils.isFailed(itemCategoryInfoRes)) {
                logger.error("query itemCategory info By itemCategoryId error! param itemCategoryId: {}", itemCategoryId);
                throw new CommonException("根据物料品类id查询物料品类信息失败!");
            }
            logger.info("query itemCategory info By itemCategoryId success! param itemCategoryId: {}", itemCategoryId);
            ItemCategoryDTO itemCategoryResultOne = ResponseUtils.getResponse(itemCategoryInfoRes, new TypeReference<ItemCategoryDTO>() {
            });
            if(ObjectUtils.isEmpty(itemCategoryResultOne) || ObjectUtils.isEmpty(itemCategoryResultOne.getLevelPath())){
                throw new CommonException("根据物料品类id查询物料品类信息为空!");
            }
            String levelPath = itemCategoryResultOne.getLevelPath();

            if (!StringUtils.isEmpty(levelPath)) {
                String[] splitRes = levelPath.split("\\|");
                if (splitRes.length > 3) {
                    throw new CommonException("该商品有三级以上的品类映射,请重新选择商品!");
                }
                if (splitRes.length == 3) {
                    //该itemCategoryId就是三级品类id  直接设置二级品类id进行查询
                    projectCost.setSecondaryCategoryId(itemCategoryResultOne.getParentCategoryId());
                }
                if (splitRes.length == 2) {
                    //该itemCategoryId就是二级品类id  直接设置二级品类id进行查询
                    projectCost.setSecondaryCategoryId(itemCategoryResultOne.getCategoryId());
                }
                if (splitRes.length == 1) {
                    //该itemCategoryId就是一级品类id 调用物料查询
                    //throw new CommonException("当前物料品类已经是一级品类, 无法查询费用项目!");
                    //itemCategoryId不行的话用itemId去找
                    ResponseEntity<String> levelPathByItemId = smdmRemoteNewService.queryLevelPathByItemId(organizationId, itemId);
                    if (ResponseUtils.isFailed(levelPathByItemId)) {
                        logger.error("query LevelPath By ItemId error! param itemId: {}", itemId);
                        throw new CommonException("根据物料id查询物料品类信息失败!");
                    }
                    logger.info("query LevelPath By ItemId success, param itemId: {}", itemId);
                    ItemCategoryDTO itemCategoryResultTwo = ResponseUtils.getResponse(levelPathByItemId, new TypeReference<ItemCategoryDTO>() {
                    });
                    if(ObjectUtils.isEmpty(itemCategoryResultTwo)  || ObjectUtils.isEmpty(itemCategoryResultTwo.getLevelPath())){
                        throw new CommonException("根据物料id查询物料品类信息为空!");
                    }
                    String levelPathNew = itemCategoryResultTwo.getLevelPath();
                    if (!StringUtils.isEmpty(levelPathNew)) {
                        String[] splitResNew = levelPathNew.split("\\|");
                        if (splitResNew.length > 3) {
                            throw new CommonException("该商品有三级以上的品类映射,请重新选择商品!");
                        }
                        if (splitResNew.length == 3) {
                            //该itemCategoryId就是三级品类id  直接设置二级品类id进行查询
                            projectCost.setSecondaryCategoryId(itemCategoryResultTwo.getParentCategoryId());
                        }
                        if (splitResNew.length == 2) {
                            //该itemCategoryId就是二级品类id  直接设置二级品类id进行查询
                            projectCost.setSecondaryCategoryId(itemCategoryResultTwo.getCategoryId());
                        }
                        if (splitResNew.length == 1) {
                            //该itemCategoryId就是一级品类id
                            throw new CommonException("当前物料品类已经是一级品类, 无法查询费用项目!");
                        }
                        if (splitResNew.length == 0) {
                            logger.error("当前商品没有映射多级映射路径levelPath");
                            throw new CommonException("根据品类id查询品类信息失败!");
                        }

                        //调用协同接口查费用项目
                        ResponseEntity<String> projectCostRes = watsonsProjectCostRemoteService.list(organizationId, projectCost, pageRequest);
                        if (ResponseUtils.isFailed(projectCostRes)) {
                            logger.error("select cost allocation project failed :{}", projectCost);
                            throw new CommonException("根据二级物料品类查询费用项目失败! 请查看参数的二级品类id值!");
                        }
                        logger.info("select cost allocation project :{}", projectCost);
                        Page<ProjectCost> costProjectRes = ResponseUtils.getResponse(projectCostRes, new TypeReference<Page<ProjectCost>>() {
                        });
                        List<ProjectCost> content = costProjectRes.getContent();
                        //检查是否有子分类
                        for (ProjectCost cost : content) {
                            Long projectCostId = cost.getProjectCostId();
                            Integer subcategoryNum = allocationInfoRepository.selectHasProjectSubcategoryId(projectCostId, cost.getTenantId());
                            if (subcategoryNum > 0) {
                                cost.setHasProjectCostSubcategory(true);
                            } else {
                                cost.setHasProjectCostSubcategory(false);
                            }
                        }
                        return content;
                    } else {
                        logger.error("当前商品没有映射多级映射路径levelPath");
                        throw new CommonException("当前商品没有映射多级映射路径!");
                    }
                }
                if (splitRes.length == 0) {
                    logger.error("当前商品没有映射多级映射路径levelPath");
                    throw new CommonException("根据品类id查询品类信息失败!");
                }

                //调用协同接口查费用项目
                ResponseEntity<String> projectCostRes = watsonsProjectCostRemoteService.list(organizationId, projectCost, pageRequest);
                if (ResponseUtils.isFailed(projectCostRes)) {
                    logger.error("select cost allocation project failed :{}", projectCost);
                    throw new CommonException("根据二级物料品类查询费用项目失败! 请查看参数的二级品类id值!");
                }
                logger.info("select cost allocation project :{}", projectCost);
                Page<ProjectCost> costProjectRes = ResponseUtils.getResponse(projectCostRes, new TypeReference<Page<ProjectCost>>() {
                });
                List<ProjectCost> content = costProjectRes.getContent();

                //检查是否有子分类
                for (ProjectCost cost : content) {
                    Long projectCostId = cost.getProjectCostId();
                    Integer subcategoryNum = allocationInfoRepository.selectHasProjectSubcategoryId(projectCostId, cost.getTenantId());
                    if (subcategoryNum > 0) {
                        cost.setHasProjectCostSubcategory(true);
                    } else {
                        cost.setHasProjectCostSubcategory(false);
                    }
                }
                return content;
            } else {
                logger.error("当前商品没有映射多级映射路径levelPath");
                throw new CommonException("当前商品没有映射多级映射路径!");
            }
        }
        return null;
    }

    @Override
    public Page<CeLovResultDTO> selectCeInfoLov(Long organizationId, String storeNo, Integer size, Integer page, String ceNumber, String description, String projectName) {
        ResponseEntity<String> ceInfo = watsonsCeInfoRemoteService.queryCeInfo(organizationId, storeNo, size, page+1,ceNumber,description,projectName);
        if (ResponseUtils.isFailed(ceInfo)) {
            logger.error("select CE info failed! 店铺号为"+ storeNo +"ce号为:"+ceNumber+"项目描述为:"+description+"项目名称为:"+projectName);
            throw new CommonException("根据店铺id查询ce编号信息失败! 店铺号为:" + storeNo);
        }
        logger.info("select CE info success :{}", storeNo);
        CeLovResult ceLovResult = ResponseUtils.getResponse(ceInfo, new TypeReference<CeLovResult>() {});
        for (CeLovResultDTO ceLovResultDTO : ceLovResult.getList()) {
            ceLovResultDTO.setCeView(ceLovResultDTO.getCeNumber()+"("+ceLovResultDTO.getItemName()+")");
        }
        return new Page<>(ceLovResult.getList(),new PageInfo(page,size),ceLovResult.getTotal());
    }

    @Override
    public Page<WatsonStoreInventoryRelationDTO> selectWhLov(Long organizationId, String storeId, Integer size, Integer page) {
        ResponseEntity<String> whInfo = watsonsWareHouseRemoteService.queryWhInfo(organizationId, storeId);
        if (ResponseUtils.isFailed(whInfo)) {
            logger.error("select warehouse info failed :{}", storeId);
            throw new CommonException("根据店铺code查询仓转店信息失败! 店铺号为:" + storeId);
        }
        logger.info("select warehouse info success :{}", storeId);
        Page<WatsonStoreInventoryRelationDTO> response = ResponseUtils.getResponse(whInfo, new TypeReference<Page<WatsonStoreInventoryRelationDTO>>() {
        });
        if(CollectionUtils.isEmpty(response.getContent())){
            throw new CommonException("根据店铺code查询仓转店信息为空! 店铺号为:" + storeId);
        }
        WhLovResultDTO res = allocationInfoRepository.selectInvNameByInvCode(response.getContent().get(0).getInventoryCode(),organizationId);
//        //检查该库存组织是否有对应地址
//        OrganizationInfoDTO infoDTO = new OrganizationInfoDTO();
//        infoDTO.setOrganizationId(Long.valueOf(res.getInventoryId()));
//        //organizationId为租户id
//        infoDTO.setTenantId(organizationId);
//        allocationInfoRepository.checkAddressByInvOrganization(infoDTO);
        response.getContent().get(0).setInventoryName(res.getInventoryName());
        return new Page<>(response.getContent(),new PageInfo(page,size),response.getTotalElements());
    }

    private List<ProjectCost> getProjectCosts(Long organizationId, ProjectCost projectCost, PageRequest pageRequest, ItemCategoryDTO itemCategoryResultOne, String levelPath, String s) {
        if (!StringUtils.isEmpty(levelPath)) {
            String[] splitRes = levelPath.split("\\|");
            if (splitRes.length > 3) {
                throw new CommonException("该商品有三级以上的品类映射,请重新选择商品!");
            }
            if (splitRes.length == 3) {
                //该itemCategoryId就是三级品类id  直接设置二级品类id进行查询
                projectCost.setSecondaryCategoryId(itemCategoryResultOne.getParentCategoryId());
            }
            if (splitRes.length == 2) {
                //该itemCategoryId就是二级品类id  直接设置二级品类id进行查询
                projectCost.setSecondaryCategoryId(itemCategoryResultOne.getCategoryId());
            }
            if (splitRes.length == 1) {
                //该itemCategoryId就是一级品类id
                throw new CommonException(s);
            }
            if (splitRes.length == 0) {
                logger.error("当前商品没有映射多级映射路径levelPath");
                throw new CommonException("根据品类id查询品类信息失败!");
            }

            //调用协同接口查费用项目
            ResponseEntity<String> projectCostRes = watsonsProjectCostRemoteService.list(organizationId, projectCost, pageRequest);
            if (ResponseUtils.isFailed(projectCostRes)) {
                logger.error("select cost allocation project failed :{}", projectCost);
                throw new CommonException("根据二级物料品类查询费用项目失败! 请查看参数的二级品类id值!");
            }
            logger.info("select cost allocation project :{}", projectCost);
            Page<ProjectCost> costProjectRes = ResponseUtils.getResponse(projectCostRes, new TypeReference<Page<ProjectCost>>() {
            });
            List<ProjectCost> content = costProjectRes.getContent();

            //检查是否有子分类
            for (ProjectCost cost : content) {
                Long projectCostId = cost.getProjectCostId();
                Integer subcategoryNum = allocationInfoRepository.selectHasProjectSubcategoryId(projectCostId, cost.getTenantId());
                if (subcategoryNum > 0) {
                    cost.setHasProjectCostSubcategory(true);
                } else {
                    cost.setHasProjectCostSubcategory(false);
                }
            }
            return content;
        } else {
            logger.error("当前商品没有映射多级映射路径levelPath");
            throw new CommonException("当前商品没有映射多级映射路径!");
        }
    }


    /**
     *计算定制品行的最新价格 如果没有计价属性则取商品的价格作为最新单价
     * @param customizedProductLine
     * @author ericzhang 2021-04-26 10:48 下午
     * @return
     */
    public void calculateForCpLine(CustomizedProductLine customizedProductLine) {
        //没有开启计价属性：按照标准计算金额
        //计价属性不开启的计算
        if (customizedProductLine.getShipperFlag() == null || customizedProductLine.getShipperFlag() != 1) {
            //没有开启计价属性，定制品的数量都为空
            customizedProductLine.setLineCqNum(null);
            customizedProductLine.setLineTotalCqNum(null);
            //该定制品行的金额
            customizedProductLine.setCpAmount(customizedProductLine.getLatestPrice().multiply(customizedProductLine.getCpQuantity()));
        } else {
            //开启了计价属性，根据定制品属性计算
            // 属性值为空则返回空
            if (CollectionUtils.isEmpty(customizedProductLine.getCustomizedProductValueList())) {
                //如果属性值为空，默认采用单价乘以数量
                customizedProductLine.setCpAmount(customizedProductLine.getLatestPrice().multiply(customizedProductLine.getCpQuantity()));
                customizedProductLine.setLineTotalCqNum(null);
                customizedProductLine.setLineCqNum(null);
            } else {
                customizedProductLine.setLineCqNum(null);
                for (CustomizedProductValue customizedProductValue : customizedProductLine.getCustomizedProductValueList()) {
                    if (customizedProductValue.getPricingFlag() == null || customizedProductValue.getPricingFlag() != 1) {
                        continue;
                    }
                    //如果值为空，不进行计算
                    if (ObjectUtils.isEmpty(customizedProductValue.getCpValue())) {
                        break;
                    }
                    //for 循环算出所有cpvalue 和 系数的乘积   即 长乘 宽 乘系数
                    BigDecimal num = customizedProductValue.getUnitCoefficient().multiply(new BigDecimal(ObjectUtils.isEmpty(customizedProductValue.getCpValue()) ? ScecConstants.ConstantNumber.STRING_1 : customizedProductValue.getCpValue()));
                    customizedProductLine.setLineCqNum(num.multiply(customizedProductLine.getLineCqNum()==null?BigDecimal.ONE:customizedProductLine.getLineCqNum()));
                }
                //面积 * 数量 = 共多少数量
                customizedProductLine.setLineTotalCqNum(customizedProductLine.getLineCqNum()==null?null:customizedProductLine.getLineCqNum().multiply(customizedProductLine.getCpQuantity()));
                //共多少数量乘单价等于总金额
                customizedProductLine.setCpAmount(customizedProductLine.getLineTotalCqNum()==null?null:customizedProductLine.getLineTotalCqNum().multiply(customizedProductLine.getLatestPrice()));
            }
        }
    }
}
