package org.srm.mall.other.app.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.ctrip.framework.apollo.util.ExceptionUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import io.choerodon.core.exception.CommonException;
import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.servicecomb.pack.omega.context.annotations.SagaStart;
import org.hzero.core.base.BaseConstants;
import org.hzero.core.message.MessageAccessor;
import org.hzero.core.util.ResponseUtils;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.srm.boot.common.CustomizeSettingCode;
import org.srm.boot.common.cache.impl.AbstractKeyGenerator;
import org.srm.boot.platform.configcenter.CnfHelper;
import org.srm.boot.platform.customizesetting.CustomizeSettingHelper;
import org.srm.boot.saga.utils.SagaClient;
import org.srm.common.convert.bean.BeanConvertor;
import org.srm.mall.agreement.app.service.PostageService;
import org.srm.mall.agreement.app.service.ProductPoolService;
import org.srm.mall.agreement.domain.entity.AgreementLine;
import org.srm.mall.agreement.domain.entity.ProductPool;
import org.srm.mall.agreement.domain.entity.ProductPoolLadder;
import org.srm.mall.agreement.domain.repository.AgreementLineRepository;
import org.srm.mall.common.constant.ScecConstants;
import org.srm.mall.common.feign.SmdmRemoteNewService;
import org.srm.mall.common.feign.SmdmRemoteService;
import org.srm.mall.common.feign.SpcmRemoteNewService;
import org.srm.mall.common.feign.WatsonsCeInfoRemoteService;
import org.srm.mall.common.task.MallOrderAsyncTask;
import org.srm.mall.common.utils.snapshot.SnapshotUtil;
import org.srm.mall.common.utils.snapshot.SnapshotUtilErrorBean;
import org.srm.mall.context.dto.ProductDTO;
import org.srm.mall.context.entity.ItemCategory;
import org.srm.mall.infra.constant.WatsonsConstants;
import org.srm.mall.order.api.dto.PreRequestOrderDTO;
import org.srm.mall.order.api.dto.PreRequestOrderResponseDTO;
import org.srm.mall.order.app.service.MallOrderCenterService;
import org.srm.mall.order.app.service.MallOrderService;
import org.srm.mall.order.app.service.OmsOrderService;
import org.srm.mall.order.app.service.WatsonsOmsOrderService;
import org.srm.mall.order.domain.vo.PurchaseRequestVO;
import org.srm.mall.other.api.dto.*;
import org.srm.mall.other.app.service.*;
import org.srm.mall.other.domain.entity.*;
import org.srm.mall.other.domain.repository.AllocationInfoRepository;
import org.srm.mall.other.domain.repository.BudgetInfoRepository;
import org.srm.mall.other.domain.repository.MinPurchaseConfigRepository;
import org.srm.mall.other.domain.repository.ShoppingCartRepository;
import org.srm.mall.platform.domain.entity.*;
import org.srm.mall.platform.domain.repository.EcClientRepository;
import org.srm.mall.platform.domain.repository.EcCompanyAssignRepository;
import org.srm.mall.platform.domain.repository.EcPlatformRepository;
import org.srm.mall.product.api.dto.ItemCategoryDTO;
import org.srm.mall.product.api.dto.ItemCategorySearchDTO;
import org.srm.mall.product.api.dto.LadderPriceResultDTO;
import org.srm.mall.product.api.dto.PriceResultDTO;
import org.srm.mall.product.app.service.ProductStockService;
import org.srm.mall.product.domain.entity.ScecProductCategory;
import org.srm.mall.product.domain.repository.ComCategoryCatalogMapRepository;
import org.srm.mall.product.infra.mapper.ScecProductCategoryMapper;
import org.srm.mall.region.api.dto.AddressDTO;
import org.srm.mall.region.api.dto.RegionDTO;
import org.srm.mall.region.domain.entity.Address;
import org.srm.mall.region.domain.entity.Region;
import org.srm.mall.region.domain.repository.AddressRepository;
import org.srm.mq.service.producer.MessageProducer;
import org.srm.web.annotation.Tenant;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

@Service("watsonsShoppingCartService")
@Tenant(WatsonsConstants.TENANT_NUMBER)
public class WatsonsShoppingCartServiceImpl extends ShoppingCartServiceImpl implements WatsonsShoppingCartService {

    private static final Logger logger = LoggerFactory.getLogger(ShoppingCartServiceImpl.class);

    private static final String BUSINESS_CARD_CATEGORY_CODE = "BUSINESS_CARD";

    @Autowired
    private AllocationInfoRepository allocationInfoRepository;

    @Autowired
    private BudgetInfoRepository budgetInfoRepository;

    @Autowired
    private EcCompanyAssignRepository ecCompanyAssignRepository;

    @Autowired
    private EcClientRepository ecClientRepository;

    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    @Autowired
    private EcPlatformRepository ecPlatformRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private PunchoutService punchoutService;

    @Autowired
    private ProductPoolService productPoolService;

    @Autowired
    private ComCategoryCatalogMapRepository comCategoryCatalogMapRepository;

    @Autowired
    private ScecProductCategoryMapper scecProductCategoryMapper;

    @Autowired
    private CatalogPriceLimitService catalogPriceLimitService;

    @Autowired
    private MallOrderCenterService mallOrderCenterService;

    @Autowired
    private BudgetInfoService budgetInfoService;

    @Autowired
    private PostageService postageService;

    @Autowired
    private SnapshotUtil snapshotUtil;

    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
    private CustomizeSettingHelper customizeSettingHelper;

    @Autowired
    private MinPurchaseConfigRepository minPurchaseConfigRepository;

    @Autowired
    private SmdmRemoteService smdmRemoteService;

    @Autowired
    private ProductStockService productStockService;

    @Autowired
    private MallOrderService mallOrderService;

    @Autowired
    private MallOrderAsyncTask mallOrderAsyncTask;

    @Autowired
    private MixDeploymentService mixDeploymentService;

    @Autowired
    private WatsonsOmsOrderService watsonsOmsOrderService;

    @Autowired
    private SmdmRemoteNewService smdmRemoteNewService;

    @Autowired
    private AgreementLineRepository agreementLineRepository;

    @Autowired
    private WatsonsCeInfoRemoteService watsonsCeInfoRemoteService;

    @Autowired
    private MessageProducer messageProducer;

    @Autowired
    @Lazy
    private ProductService productService;

    @Autowired
    private SpcmRemoteNewService spcmRemoteNewService;


    @Override
    public List<ShoppingCartDTO> shppingCartEnter(Long organizationId, ShoppingCart shoppingCart) {
        //加入了取费用分配的过程
        List<ShoppingCartDTO> shoppingCartDTOList = super.shppingCartEnter(organizationId, shoppingCart);
        if (!CollectionUtils.isEmpty(shoppingCartDTOList)) {
            List<AllocationInfo> allocationInfoList = allocationInfoRepository.selectByCondition(Condition.builder(AllocationInfo.class).andWhere(Sqls.custom()
                    .andIn(AllocationInfo.FIELD_CART_ID, shoppingCartDTOList.stream().map(ShoppingCartDTO::getCartId).collect(Collectors.toList()))).build());
            if (!CollectionUtils.isEmpty(allocationInfoList)){
                for (AllocationInfo allocationInfo : allocationInfoList){
                    allocationInfo.setTotalAmount(allocationInfo.getPrice().multiply(new BigDecimal(allocationInfo.getQuantity())));
                }
                Map<Long, List<AllocationInfo>> map = allocationInfoList.stream().collect(Collectors.groupingBy(AllocationInfo::getCartId));
                return shoppingCartDTOList.stream().map(s -> {
                    WatsonsShoppingCartDTO watsonsShoppingCart = new WatsonsShoppingCartDTO();
                    BeanUtils.copyProperties(s, watsonsShoppingCart);
                    watsonsShoppingCart.setAllocationInfoList(map.get(s.getCartId()));
                    return watsonsShoppingCart;
                }).collect(Collectors.toList());
            }
        }
        return shoppingCartDTOList;
    }

    @Override
    @SagaStart
    @Transactional(rollbackFor = Exception.class)
    public PreRequestOrderResponseDTO watsonsPreRequestOrder(Long tenantId, String customizeUnitCode, List<WatsonsPreRequestOrderDTO> preRequestOrderDTOList) {
        //进行ceNo和discription存表
        for (WatsonsPreRequestOrderDTO watsonsPreRequestOrderDTO : preRequestOrderDTOList) {
            if(!ObjectUtils.isEmpty(watsonsPreRequestOrderDTO.getCeNumber())){
                for (WatsonsShoppingCartDTO watsonsShoppingCartDTO : watsonsPreRequestOrderDTO.getWatsonsShoppingCartDTOList()) {
                    for (AllocationInfo allocationInfo : watsonsShoppingCartDTO.getAllocationInfoList()) {
                        allocationInfo.setCeNumber(watsonsPreRequestOrderDTO.getCeNumber());
                        if(!ObjectUtils.isEmpty(watsonsPreRequestOrderDTO.getDiscription())){
                            allocationInfo.setCeDiscription(watsonsPreRequestOrderDTO.getDiscription());
                        }
                        allocationInfoRepository.updateByPrimaryKeySelective(allocationInfo);
                    }
                }
            }
        }


        //进行cms合同号校验
        for (WatsonsPreRequestOrderDTO watsonsPreRequestOrderDTO : preRequestOrderDTOList) {
            //拆单完后的每个订单的所有商品的费用分配不一样  但是放一起做cms校验  所以每个订单所有的商品校验一次
            List<PcOccupyDTO> pcOccupyDTOS = new ArrayList<>();
            //取到该订单所有商品
            for (WatsonsShoppingCartDTO watsonsShoppingCartDTO : watsonsPreRequestOrderDTO.getWatsonsShoppingCartDTOList()) {
                //每个订单下面装的购物车是entry.getValue   entry为经过拆单后的每个经过所有费用分配行拆分后商品
                if(!ObjectUtils.isEmpty(watsonsShoppingCartDTO.getCmsNumber())){
                    PcOccupyDTO pcOccupyDTO = new PcOccupyDTO();
                    pcOccupyDTO.setTenantId(watsonsShoppingCartDTO.getTenantId());
                    pcOccupyDTO.setSourceId(watsonsShoppingCartDTO.getAllocationInfoList().get(0).getAllocationId());
                    pcOccupyDTO.setSourceType(WatsonsConstants.smalSourceType.SMAL_PRE);
                    //传商品的含税价
                    BigDecimal includeTaxPrice = new BigDecimal(0);
                    ProductDTO productDTO = productService.selectByProduct(watsonsShoppingCartDTO.getProductId(), tenantId, watsonsShoppingCartDTO.getCompanyId(), watsonsShoppingCartDTO.getPurchaseType(), watsonsShoppingCartDTO.getSecondRegionId(), watsonsShoppingCartDTO.getLevelPath());
                    if(!ObjectUtils.isEmpty(productDTO.getSellPrice())){
                        BigDecimal quantity = BigDecimal.valueOf(watsonsShoppingCartDTO.getQuantity());
                        BigDecimal includeTaxPriceParam = productDTO.getSellPrice().multiply(quantity);
                        includeTaxPrice = includeTaxPrice.add(includeTaxPriceParam);
                    }
                    if(productDTO.getLadderEnableFlag().equals(1L)){
                        Integer quantityInteger= watsonsShoppingCartDTO.getQuantity();
                        BigDecimal quantity = BigDecimal.valueOf(quantityInteger);
                        for (ProductPoolLadder productPoolLadder : productDTO.getProductLadderPrices()) {
                            if(quantity.compareTo(productPoolLadder.getLadderFrom()) > -1 && quantity.compareTo(productPoolLadder.getLadderTo()) < 1){
                                BigDecimal includeTaxPriceParam = productPoolLadder.getTaxPrice().multiply(quantity);
                                includeTaxPrice = includeTaxPrice.add(includeTaxPriceParam);
                            }
                        }
                    }
                    pcOccupyDTO.setOccupyAmount(includeTaxPrice);
                    pcOccupyDTO.setOperationType(WatsonsConstants.operationTypeCode.SPCM_OCCUPY);
                    pcOccupyDTO.setPcNum(watsonsShoppingCartDTO.getCmsNumber());
                    pcOccupyDTO.setVersion(1L);
                    pcOccupyDTOS.add(pcOccupyDTO);
                }
            }
            if(!CollectionUtils.isEmpty(pcOccupyDTOS)){
                String sagaKey = SagaClient.getSagaKey();
                ResponseEntity<String> cmsOccupyResult = spcmRemoteNewService.occupy(sagaKey, tenantId, pcOccupyDTOS);
                if (ResponseUtils.isFailed(cmsOccupyResult)) {
                    logger.error("occupy CMS price error! param pcOccupyDTOS: {}", pcOccupyDTOS);
                    throw new CommonException("CMS金额预占出现异常!");
                }
                ItfBaseBO itfBaseBO  = ResponseUtils.getResponse(cmsOccupyResult, new TypeReference<ItfBaseBO>() {
                });
                if(itfBaseBO.getErrorFlag() == 1 && !ObjectUtils.isEmpty(itfBaseBO.getErrorMessage())){
                    logger.error("occupy CMS price error! param pcOccupyDTOS: {}", pcOccupyDTOS);
                    throw new CommonException("预占CMS合同号报错,错误原因:",itfBaseBO.getErrorMessage());
                }
                logger.info("occupy CMS price success! param pcOccupyDTOS: {}", pcOccupyDTOS);
            }
        }


//        进行ceNo校验
        for (WatsonsPreRequestOrderDTO watsonsPreRequestOrderDTO : preRequestOrderDTOList) {
            if(!ObjectUtils.isEmpty(watsonsPreRequestOrderDTO.getCeNumber())){
                CheckCeInfoDTO checkCeInfoDTO = new CheckCeInfoDTO();
                checkCeInfoDTO.setCeId(watsonsPreRequestOrderDTO.getCeId());
                //取含税价格  每个订单检验一次
                BigDecimal includeTaxPriceTotal = new BigDecimal(0);
                for (WatsonsShoppingCartDTO watsonsShoppingCartDTO : watsonsPreRequestOrderDTO.getWatsonsShoppingCartDTOList()) {
                    ProductDTO productDTO = productService.selectByProduct(watsonsShoppingCartDTO.getProductId(), tenantId, watsonsShoppingCartDTO.getCompanyId(), watsonsShoppingCartDTO.getPurchaseType(), watsonsShoppingCartDTO.getSecondRegionId(), watsonsShoppingCartDTO.getLevelPath());
                    if(!ObjectUtils.isEmpty(productDTO.getSellPrice())){
                        BigDecimal quantity = BigDecimal.valueOf(watsonsShoppingCartDTO.getQuantity());
                        BigDecimal includeTaxPriceParam = productDTO.getSellPrice().multiply(quantity);
                        //加上这个商品的价格
                        includeTaxPriceTotal = includeTaxPriceTotal.add(includeTaxPriceParam);
                    }
                    if(productDTO.getLadderEnableFlag().equals(1L)){
                       Integer quantityInteger= watsonsShoppingCartDTO.getQuantity();
                        BigDecimal quantity = BigDecimal.valueOf(quantityInteger);
                        for (ProductPoolLadder productPoolLadder : productDTO.getProductLadderPrices()) {
                            if(quantity.compareTo(productPoolLadder.getLadderFrom()) > -1 && quantity.compareTo(productPoolLadder.getLadderTo()) < 1){
                                BigDecimal includeTaxPriceParam = productPoolLadder.getTaxPrice().multiply(quantity);
                                includeTaxPriceTotal = includeTaxPriceTotal.add(includeTaxPriceParam);
                            }
                        }
                    }
                }
                checkCeInfoDTO.setChangeAmount(includeTaxPriceTotal);
                checkCeInfoDTO.setItemName(watsonsPreRequestOrderDTO.getItemName());
                checkCeInfoDTO.setTranscationId(watsonsPreRequestOrderDTO.getPreRequestOrderNumber());
                ResponseEntity<String> checkCeInfoRes = watsonsCeInfoRemoteService.checkCeInfo(tenantId,checkCeInfoDTO);
                if(ResponseUtils.isFailed(checkCeInfoRes)){
                    String message = null;
                    try {
                        Exception exception = JSONObject.parseObject(checkCeInfoRes.getBody(),Exception.class);
                        message = exception.getMessage();
                    }catch (Exception e){
                        message = checkCeInfoRes.getBody();
                    }
                    logger.error("check CE info for order total amount error! {}",watsonsPreRequestOrderDTO.getCeId());
                    throw new CommonException("检验CE号"+watsonsPreRequestOrderDTO.getCeNumber()+"报错,"+message);
                }
                logger.info("check CE info for order total amount success! {}" ,watsonsPreRequestOrderDTO.getCeId());
            }
        }

        preRequestOrderDTOList.stream().forEach(preRequestOrderDTO -> {
                    if (ObjectUtils.nullSafeEquals(preRequestOrderDTO.getPriceHiddenFlag(), 1)) {
                        Iterator iterator = preRequestOrderDTO.getShoppingCartDTOList().iterator();
                        Map<String, Map<Long, PriceResultDTO>> priceResultDTOMap = queryPriceResult(preRequestOrderDTO.getShoppingCartDTOList());
                        while (iterator.hasNext()) {
                            ShoppingCartDTO shoppingCartDTO = (ShoppingCartDTO) iterator.next();
                            Map<Long, PriceResultDTO> resultDTOMap = priceResultDTOMap.get(shoppingCartDTO.skuRegionGroupKey());
                            if (ObjectUtils.isEmpty(resultDTOMap)) {
                                throw new CommonException(ScecConstants.ErrorCode.PRODUCT_CANNOT_SELL);
                            }
                            PriceResultDTO priceResultDTO = resultDTOMap.get(shoppingCartDTO.getProductId());

                            // FIXME
                            if (ObjectUtils.nullSafeEquals(shoppingCartDTO.getPriceHiddenFlag(),1)) {
                                shoppingCartDTO.setLatestPrice(priceResultDTO.getSellPrice());
                                shoppingCartDTO.setUnitPrice(priceResultDTO.getSellPrice());
                                shoppingCartDTO.setPurLastPrice(priceResultDTO.getPurchasePrice());
                                shoppingCartDTO.setTotalPrice(shoppingCartDTO.getLatestPrice().multiply(BigDecimal.valueOf(shoppingCartDTO.getQuantity())));
                            }
                        }

                        BigDecimal price = preRequestOrderDTO.getShoppingCartDTOList().stream().map(ShoppingCartDTO::getTotalPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
                        preRequestOrderDTO.setPrice(price.add(preRequestOrderDTO.getFreight()));
                        if (ScecConstants.AgreementType.SALE.equals(preRequestOrderDTO.getShoppingCartDTOList().get(0).getAgreementType())) {
                            //  如果是销售协议 运费需要用采购协议价来计算
                            preRequestOrderDTO.setPurPrice(price.add(preRequestOrderDTO.getFreight()));
                        }
                    }
                    snapshotUtil.compare(AbstractKeyGenerator.getKey(ScecConstants.CacheCode.SERVICE_NAME, ScecConstants.CacheCode.PURCHASE_REQUISITION_PREVIEW, preRequestOrderDTO.getPreRequestOrderNumber()), preRequestOrderDTO.getPreRequestOrderNumber(), preRequestOrderDTO, new SnapshotUtilErrorBean());
                }
        );
        PreRequestOrderResponseDTO preRequestOrderResponseDTO = new PreRequestOrderResponseDTO();
        //过滤出cansubmit的预采申请
        List<PreRequestOrderDTO> canSubmitList = preRequestOrderDTOList.stream().filter(item -> ScecConstants.ConstantNumber.INT_1 == item.getMinPurchaseFlag()).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(canSubmitList)) {
            throw new CommonException(ScecConstants.ErrorCode.PRODUCT_IS_NULL_ERROR);
        }

        //查询预算是否开启
        String budgetSwitch = budgetInfoService.queryBudgetSwitch(tenantId, preRequestOrderDTOList.get(0).getCompanyId());

        //遍历每个可以提交的预采申请订单
        for (PreRequestOrderDTO preRequestOrderDTO : canSubmitList) {
//            if (ScecConstants.PurchaseType.COMPANY.equals(preRequestOrderDTO.getPurchaseType())) {
//                //判断采购组织是否有效
//                PurOrganizationDTO purOrganizationDTO = new PurOrganizationDTO();
//                purOrganizationDTO.setOrganizationName(preRequestOrderDTO.getPurOrganizationName());
//                purOrganizationDTO.setPurchaseOrgId(preRequestOrderDTO.getPurOrganizationId());
//                // 预采购申请页面ownerId与当前用户一定相同
//                String result = getPurchaseOrganization(tenantId, purOrganizationDTO, null);
//                if (StringUtils.isEmpty(result)) {
//                    throw new CommonException(ScecConstants.ErrorCode.PURCHASE_ORGANIZATION_FINDED_FLAG_ERROR);
//                }
//            }
            //如果有服务商品，从底层list取出放到上层list

            //取出该预采申请下的所有商品包括子商品
            List<ShoppingCartDTO> re = new ArrayList<>();

            //拿到canSubmit的预采申请的每个购物车
            Iterator iterator = preRequestOrderDTO.getShoppingCartDTOList().iterator();
            while (iterator.hasNext()) {
                ShoppingCartDTO shoppingCartDTO = (ShoppingCartDTO) iterator.next();
                re.add(shoppingCartDTO);
                if (CollectionUtils.isNotEmpty(shoppingCartDTO.getSeSkuList())) {
                    re.addAll(shoppingCartDTO.getSeSkuList());
                }
                //判断是否开启预算，如果有，校验是否已占用
                checkBudgetInfo(tenantId, shoppingCartDTO, budgetSwitch);
            }

            //判断商品存在购物车
            validateShoppingExistCar(re);

            //把该采购申请下经过校验后的商品再重放入该采购申请的购物车中
            preRequestOrderDTO.setShoppingCartDTOList(re);


            // 目录化商品库存扣减
            for (ShoppingCartDTO shoppingCart : re) {
                if (ScecConstants.SourceType.CATALOGUE.equals(shoppingCart.getProductSource())) {
                    productStockService.productStockConsumption(null, shoppingCart.getProductId(), shoppingCart.getQuantity().longValue(), true);
                }
                updateBudgetInfoResult(shoppingCart, budgetSwitch);
                List<ShoppingCart> shoppingCarts = shoppingCartRepository.selectByIds(shoppingCart.getCartId().toString());
                if(CollectionUtils.isNotEmpty(shoppingCarts)){
                    ShoppingCart currentShoppingCart = shoppingCarts.get(0);
                    if(currentShoppingCart.getQuantity()-shoppingCart.getQuantity()<=0L){
                        //删除购物车对象
                        shoppingCartRepository.deleteByPrimaryKey(shoppingCart.getCartId());
                    }else {
                        currentShoppingCart.setQuantity(currentShoppingCart.getQuantity()-shoppingCart.getQuantity());
                        shoppingCartRepository.updateOptional(currentShoppingCart,ShoppingCart.FIELD_QUANTITY);
                    }
                }
            }
            //遍历每个可以提交的预采申请订单结束
        }
        PurchaseRequestVO result;
        if (ScecConstants.enableOrderCenterFlag(tenantId)) {
            List<WatsonsPreRequestOrderDTO> watsonsCanSubmitList = preRequestOrderDTOList.stream().filter(item -> ScecConstants.ConstantNumber.INT_1 == item.getMinPurchaseFlag()).collect(Collectors.toList());
            //oms订单中心启用
            result = watsonsOmsOrderService.watsonsCreateOrder(tenantId, customizeUnitCode, watsonsCanSubmitList);
        } else {
            result = mallOrderService.createPurchaseReq(tenantId, canSubmitList);
        }
        if (!StringUtils.isEmpty(result.getLotNum())) {
            preRequestOrderResponseDTO.setResult(result.getLotNum());
            BeanUtils.copyProperties(result, preRequestOrderResponseDTO);
            //回调协同
            mallOrderAsyncTask.synCallBackBudgetMallOrderNum(tenantId, result.getPrHeaderList(), budgetSwitch);
            //根据业务规则判断是否自动创建协议
            List<String> list = preRequestOrderDTOList.stream().map(PreRequestOrderDTO::getPreRequestOrderNumber).collect(Collectors.toList());
            messageProducer.sendMessageAfterTransactionCommit(tenantId, ScecConstants.EventCode.SMAL_OMS_ORDER, null, "ORDER_CONFIRM", list);
        } else {
            //创建预采购订单失败
            throw new CommonException(ScecConstants.ErrorCode.PRE_REQUEST_ORDER_FAILED);
        }
        // 将支付信息缓存
        shoppingCartRepository.putPaymentInfoToRedis(tenantId, canSubmitList);
        //目录化订单发送混合部署消息
        if (mixDeploymentService.query(tenantId)) {
            mixDeploymentService.getCataSubmitMessageAndSend(tenantId);
        }
        return preRequestOrderResponseDTO;
    }

    @Override
    public List<WatsonsAddressDTO> checkAddress(Long organizationId, Long watsonsOrganizationId, String watsonsOrganizationCode) {

        if(ObjectUtils.isEmpty(watsonsOrganizationId) && ObjectUtils.isEmpty(watsonsOrganizationCode)){
            throw new CommonException("仓转店或店铺的id和编码都为空, 无法根据仓转店或店铺自动带出详细地址和地址区域!");
        }
        //优先用id查
        if(!ObjectUtils.isEmpty(watsonsOrganizationId)){
            logger.info("当前正在使用id查询详细地址和地址区域!");
            List<WatsonsAddressDTO> watsonsAddressDTOS = new ArrayList<>();
            //找到地址表信息迁移到watsonsAddress
            List<Address> addressList = addressRepository.selectByCondition(Condition.builder(Address.class).andWhere(
                    Sqls.custom().andEqualTo(Address.FIELD_TENANTID_ID,organizationId).andEqualTo(Address.FIELD_ADDRESS_TYPE, ScecConstants.AdressType.RECEIVER)
                            .andEqualTo(Address.FIELD_INV_ORGANIZATION_ID,watsonsOrganizationId)).build());
            if(!CollectionUtils.isEmpty(addressList)) {
                //address转移到watsonsAddress
                for (Address address : addressList) {
                    WatsonsAddressDTO watsonsAddressDTO = new WatsonsAddressDTO();
                    BeanUtils.copyProperties(address, watsonsAddressDTO);
                    watsonsAddressDTOS.add(watsonsAddressDTO);
                }
                //添加地址区域
                for (WatsonsAddressDTO watsonsAddressDTO : watsonsAddressDTOS) {
                    Long regionId = watsonsAddressDTO.getRegionId();
                    WatsonsRegionDTO watsonsRegionDTO = allocationInfoRepository.selectRegionInfoByRegionId(regionId);
                    if (ObjectUtils.isEmpty(watsonsRegionDTO)) {
                        throw new CommonException("未查到该地区id对应的地区信息!");
                    }
                    String levelPath = watsonsRegionDTO.getLevelPath();
                    String[] splitRes = levelPath.split("\\.");
                    StringBuffer sb = new StringBuffer();
                    for (int i = 0; i < splitRes.length; i++) {
                        WatsonsRegionDTO res = allocationInfoRepository.selectRegionInfoByRegionCode(splitRes[i]);
                        if (ObjectUtils.isEmpty(res)) {
                            throw new CommonException("未查到该地区code对应的地区信息!");
                        }
                        sb.append(res.getRegionName());
                    }
                    String regionRes = sb.toString();
                    watsonsAddressDTO.setAddressRegion(regionRes);
                }
                return watsonsAddressDTOS;
            }else{
                WhLovResultDTO infoDTO = allocationInfoRepository.selectInvInfoByInvId(watsonsOrganizationId, organizationId);
                logger.error(infoDTO.getInventoryCode()+"-"+infoDTO.getInventoryName()+"的相关地址信息不存在，请手工补充收货地址!");
                throw new CommonException(infoDTO.getInventoryCode()+"-"+infoDTO.getInventoryName()+"的相关地址信息不存在，请手工补充收货地址!");
            }
        }

        //id没有用code查
        if(!ObjectUtils.isEmpty(watsonsOrganizationCode)){
            logger.info("当前正在使用code查询详细地址和地址区域!");
            List<WatsonsAddressDTO> watsonsAddressDTOS = new ArrayList<>();
            //找到地址表信息迁移到watsonsAddress
            //hpfm通过code找到id
            AddressDTO addressDTO = allocationInfoRepository.selectIdByCode(organizationId,watsonsOrganizationCode);
            if(!ObjectUtils.isEmpty(addressDTO.getInvOrganizationId())) {
                List<Address> addressList = addressRepository.selectByCondition(Condition.builder(Address.class).andWhere(
                        Sqls.custom().andEqualTo(Address.FIELD_TENANTID_ID, organizationId).andEqualTo(Address.FIELD_ADDRESS_TYPE, ScecConstants.AdressType.RECEIVER)
                                .andEqualTo(Address.FIELD_INV_ORGANIZATION_ID, addressDTO.getInvOrganizationId())).build());
                if (!CollectionUtils.isEmpty(addressList)) {
                    //address转移到watsonsAddress
                    for (Address address : addressList) {
                        WatsonsAddressDTO watsonsAddressDTO = new WatsonsAddressDTO();
                        BeanUtils.copyProperties(address, watsonsAddressDTO);
                        watsonsAddressDTOS.add(watsonsAddressDTO);
                    }
                    //添加地区信息
                    for (WatsonsAddressDTO watsonsAddressDTO : watsonsAddressDTOS) {
                        Long regionId = watsonsAddressDTO.getRegionId();
                        WatsonsRegionDTO watsonsRegionDTO = allocationInfoRepository.selectRegionInfoByRegionId(regionId);
                        if (ObjectUtils.isEmpty(watsonsRegionDTO)) {
                            throw new CommonException("未查到该地区id对应的地区信息!");
                        }
                        String levelPath = watsonsRegionDTO.getLevelPath();
                        String[] splitRes = levelPath.split("\\.");
                        StringBuffer sb = new StringBuffer();
                        for (int i = 0; i < splitRes.length; i++) {
                            WatsonsRegionDTO res = allocationInfoRepository.selectRegionInfoByRegionCode(splitRes[i]);
                            if (ObjectUtils.isEmpty(res)) {
                                throw new CommonException("未查到该地区code对应的地区信息!");
                            }
                            sb.append(res.getRegionName());
                        }
                        String regionRes = sb.toString();
                        watsonsAddressDTO.setAddressRegion(regionRes);
                    }
                    return watsonsAddressDTOS;
                }else {
                    AddressDTO infoDTO = allocationInfoRepository.selectIdByCode(organizationId, watsonsOrganizationCode);
                    logger.error(infoDTO.getInvOrganizationCode()+"-"+infoDTO.getInvOrganizationName()+"的相关地址信息不存在，请手工补充收货地址!");
                    throw new CommonException(infoDTO.getInvOrganizationCode()+"-"+infoDTO.getInvOrganizationName()+"的相关地址信息不存在，请手工补充收货地址!");
                }
            }else {
                logger.warn("该库存组织code没有找到库存组织id!");
                throw new CommonException("在查询地址区域和详细地址时用到的库存组织编码表中没有对应的库存组织id!该编码为"+watsonsOrganizationCode);
            }
        }
        return null;
    }

    @Override
    public String checkAddressValidate(Long organizationId, List<WatsonsShoppingCartDTO> watsonsShoppingCartDTOS) {
        //降维处理  把商品行维度降为费用分配维度
        List<AllocationInfo> allocationInfos = new ArrayList<>();
        for (WatsonsShoppingCartDTO watsonsShoppingCartDTO : watsonsShoppingCartDTOS) {
            for (AllocationInfo allocationInfo : watsonsShoppingCartDTO.getAllocationInfoList()) {
                allocationInfo.setFromWhichShoppingCart(watsonsShoppingCartDTO.getProductName());
                allocationInfos.add(allocationInfo);
            }
        }

        Map<Long, List<AllocationInfo>> collectRes = allocationInfos.stream().collect(Collectors.groupingBy(AllocationInfo::getCostShopId));
        for (Map.Entry<Long, List<AllocationInfo>> longListEntry : collectRes.entrySet()) {
            List<AllocationInfo> value = longListEntry.getValue();
            String address4Check = value.get(0).getAddressRegion()+value.get(0).getFullAddress();
            for (AllocationInfo allocationInfo : value) {
                if(!((allocationInfo.getAddressRegion()+allocationInfo.getFullAddress()).equals(address4Check))){
                    throw new CommonException(
                            allocationInfo.getFromWhichShoppingCart()+allocationInfo.getCostShopCode()+allocationInfo.getCostShopName() + "分配的地址不一致，请修改!");
                    }
                }
            }
        return null;
    }

    private void checkBudgetInfo(Long tenantId, ShoppingCartDTO shoppingCartDTO, String budgetSwitch){
        if (ScecConstants.ConstantNumber.STRING_1.equals(budgetSwitch)) {
            List<BudgetInfo> budgetInfoList = shoppingCartDTO.getBudgetInfoList();
            if (CollectionUtils.isEmpty(budgetInfoList)){
                throw new CommonException("请选择预算信息");
            }
            for (BudgetInfo budgetInfo : budgetInfoList){
                //判断是否进行了预算校验
                if (budgetInfo.getOccupancyFlag() == ScecConstants.ConstantNumber.INT_0) {
                    throw new CommonException(ScecConstants.ErrorCode.BUDGET_OCCUPANCY);
                }
            }
        }
    }

    private void updateBudgetInfoResult(ShoppingCartDTO shoppingCartDTO, String budgetSwitch){
        //修改预算校验状态，2  表示生成采购申请成功
        if (ScecConstants.ConstantNumber.STRING_1.equals(budgetSwitch)){
            if (!CollectionUtils.isEmpty(shoppingCartDTO.getBudgetInfoList())) {
                for (BudgetInfo budgetInfo : shoppingCartDTO.getBudgetInfoList()) {
                    budgetInfo.setOccupancyFlag(ScecConstants.Budget.OCCUPANCY_CREATE_PURCHASE);
                    budgetInfoRepository.updateByPrimaryKey(budgetInfo);
                }
            }
        }
    }

    /**
     * 阶梯价处理
     *
     * @param shoppingCart 购物车
     * @param productPool  商品池
     */
    private void handLadderPrice(ShoppingCart shoppingCart, ProductPool productPool) {
        List<ProductPoolLadder> productPoolLadders = productPool.getLadderPriceList().stream().map(ProductPoolLadder::new).sorted(Comparator.comparing(ProductPoolLadder::getLadderFrom)).collect(Collectors.toList());
        // 计算阶梯价
        boolean hasFlag = true;
        for (ProductPoolLadder productPoolLadder : productPoolLadders) {
            BigDecimal quantity = new BigDecimal(shoppingCart.getQuantity());
            if (productPoolLadder.getLadderFrom().compareTo(quantity) <= 0 && (ObjectUtils.isEmpty(productPoolLadder.getLadderTo()) || productPoolLadder.getLadderTo().compareTo(quantity) > 0)) {
                shoppingCart.setLatestPrice(productPoolLadder.getTaxPrice());
                shoppingCart.setUnitPrice(productPoolLadder.getTaxPrice());
                hasFlag = false;
                break;
            }
        }
        if (hasFlag) {
            //超出范围取最后一个阶梯的
            ProductPoolLadder productPoolLadder = productPoolLadders.get(productPoolLadders.size() - 1);
            shoppingCart.setLatestPrice(productPoolLadder.getTaxPrice());
            shoppingCart.setUnitPrice(productPoolLadder.getTaxPrice());
        }
    }


    @Override
    public List<WatsonsPreRequestOrderDTO> watsonsPreRequestOrderView(Long tenantId, List<WatsonsShoppingCartDTO> watsonsShoppingCartDTOList) {

        //校验所有的商品的地址是否一致  不一致的话后续默认拆单不一样 但是按费用分配的三个维度拆可能一样就会出现丢失
        Long addressId4Check = watsonsShoppingCartDTOList.get(0).getAddressId();
        for (WatsonsShoppingCartDTO watsonsShoppingCartDTO : watsonsShoppingCartDTOList) {
            Long addressId = watsonsShoppingCartDTO.getAddressId();
            if(!addressId4Check.equals(addressId)){
                throw new CommonException("必须选择同地址的商品!");
            }
        }

        //校验每个商品的每个费用分配当【费用承担写字楼/店铺/仓库】相同时,【地址区域】+【收货地址】是否相同
        List<AllocationInfo> allocationInfos = new ArrayList<>();
        for (WatsonsShoppingCartDTO watsonsShoppingCartDTO : watsonsShoppingCartDTOList) {
            for (AllocationInfo allocationInfo : watsonsShoppingCartDTO.getAllocationInfoList()) {
                allocationInfo.setFromWhichShoppingCart(watsonsShoppingCartDTO.getProductName());
                allocationInfos.add(allocationInfo);
            }
        }
        Map<Long, List<AllocationInfo>> collectRes = allocationInfos.stream().collect(Collectors.groupingBy(AllocationInfo::getCostShopId));
        for (Map.Entry<Long, List<AllocationInfo>> longListEntry : collectRes.entrySet()) {
            List<AllocationInfo> value = longListEntry.getValue();
            String address4Check = value.get(0).getAddressRegion()+value.get(0).getFullAddress();
            for (AllocationInfo allocationInfo : value) {
                if(!((allocationInfo.getAddressRegion()+allocationInfo.getFullAddress()).equals(address4Check))){
                    throw new CommonException(
                            "商品"+value.get(0).getFromWhichShoppingCart()+"的"+value.get(0).getCostShopCode()+value.get(0).getCostShopName()+
                            "与商品"+allocationInfo.getFromWhichShoppingCart()+"的"+allocationInfo.getCostShopCode()+allocationInfo.getCostShopName() + "分配的地址不一致，请修改!");
                }
            }
        }

        //如果有服务商品，从底层list取出放到上层list
        List<ShoppingCartDTO> re = new ArrayList<>();

        //获取名片分类的categoryId
        ScecProductCategory businessCardCategory = scecProductCategoryMapper.getCategoryByCode(BUSINESS_CARD_CATEGORY_CODE);
        Long businessCardCategoryId = -1L;
        if (businessCardCategory != null) {
            businessCardCategoryId = businessCardCategory.getId();
        }
        // 批量获取价格
        // 首先更加购物车的地址和组织进行分组调用获取价格


//        watsonsShoppingCartDTOList转换成的shoppingCartDTOList用于后续检测
        List<ShoppingCartDTO> shoppingCartDTOList = new ArrayList<>();
        for (WatsonsShoppingCartDTO watsonsShoppingCartDTO : watsonsShoppingCartDTOList) {
            ShoppingCartDTO shoppingCartDTO4Transfer = new ShoppingCartDTO();
            BeanUtils.copyProperties(watsonsShoppingCartDTO, shoppingCartDTO4Transfer);
            shoppingCartDTOList.add(shoppingCartDTO4Transfer);
        }


        // eric 首先根据购物车的地址id和组织层级进行分组调用   异步获取商品价格
        Map<String, Map<Long, PriceResultDTO>> priceResultDTOMap = queryPriceResult(shoppingCartDTOList);

        Iterator iterator = watsonsShoppingCartDTOList.iterator();
        while (iterator.hasNext()) {

            ShoppingCartDTO shoppingCartDTO = (ShoppingCartDTO) iterator.next();

            Map<Long, PriceResultDTO> resultDTOMap = priceResultDTOMap.get(shoppingCartDTO.skuRegionGroupKey());
            if (ObjectUtils.isEmpty(resultDTOMap)) {
                throw new CommonException(ScecConstants.ErrorCode.PRODUCT_CANNOT_SELL);
            }
            PriceResultDTO priceResultDTO = resultDTOMap.get(shoppingCartDTO.getProductId());
            if (ObjectUtils.isEmpty(priceResultDTO) || !BaseConstants.Flag.YES.equals(priceResultDTO.getSaleEnable())) {
                throw new CommonException(ScecConstants.ErrorCode.PRODUCT_CANNOT_SELL);
            }
            //判断是否满足最小包装量
            BigDecimal number = new BigDecimal(shoppingCartDTO.getQuantity()).divide(shoppingCartDTO.getMinPackageQuantity(), 10, BigDecimal.ROUND_HALF_UP);
            if (new BigDecimal(number.intValue()).compareTo(number) != 0) {
                throw new CommonException("不满足最小包装量" + shoppingCartDTO.getMinPackageQuantity() + "的整数倍");
            }
            if (businessCardCategoryId.equals(shoppingCartDTO.getCid())) {
                shoppingCartDTO.setBusinessCardFlag(1);
            }
            shoppingCartDTO.setAgreementLineId(priceResultDTO.getPurAgreementLineId());
            if (!ObjectUtils.isEmpty(shoppingCartDTO.getCatalogId())) {
                Integer count = comCategoryCatalogMapRepository.checkCatalog(shoppingCartDTO.getOwnerId(), priceResultDTO.getCatalogId(), shoppingCartDTO.getTenantId());
                if (count > 0) {
                    throw new CommonException(ScecConstants.ErrorCode.CATALOG_PERMISSION_NOT_PASS);
                }
                //校验目录价格限制
                BigDecimal priceLimit = catalogPriceLimitService.priceLimit(new CatalogPriceLimit(shoppingCartDTO.getTenantId(), shoppingCartDTO.getOwnerId(), shoppingCartDTO.getProductId(), shoppingCartDTO.getCatalogId(), null));
                if (Objects.nonNull(priceLimit) && priceLimit.compareTo(shoppingCartDTO.getLatestPrice()) == -1) {
                    //存在商品价格限制，不通过
                    //设置最小小数位2位，不足补0，最大小数位10位
                    NumberFormat nf = NumberFormat.getInstance();
                    nf.setMinimumFractionDigits(2);
                    nf.setMaximumFractionDigits(10);
                    nf.setGroupingUsed(false);
                    throw new CommonException(ScecConstants.ErrorCode.CATALOG_OVER_LIMIT_PRICE, nf.format(priceLimit));
                }
            } else {
                throw new CommonException(ScecConstants.ErrorCode.PRODUCT_CATALOG_NOT_EXISTS);
            }
            if (CollectionUtils.isNotEmpty(shoppingCartDTO.getSeSkuList())) {
                re.addAll(shoppingCartDTO.getSeSkuList());
            }
            if (!ObjectUtils.isEmpty(shoppingCartDTO.getItemId())) {
                shoppingCartDTO.setHasItemFlag(BaseConstants.Flag.YES);
            } else {
                shoppingCartDTO.setHasItemFlag(BaseConstants.Flag.NO);
                shoppingCartDTO.setWarehousing(BaseConstants.Flag.YES);
            }
            checkPrice(shoppingCartDTO, priceResultDTO);
            // 设置其余查询出来的参数
            convertParam(shoppingCartDTO, priceResultDTO);
        }

        //检测条件完毕

        //校验收货地址
        validateShppingAddress(shoppingCartDTOList);
        //验证商品存在购物车才能生成与采购申请单
        validateShoppingExistCar(shoppingCartDTOList);
        if (CollectionUtils.isNotEmpty(re)) {
            validateShoppingExistCar(re);
        }
        boolean hideSupplier = mallOrderCenterService.checkHideField(tenantId, shoppingCartDTOList.get(0).getCompanyId(), ScecConstants.HideField.SUPPLIER);


        if (CollectionUtils.isNotEmpty(shoppingCartDTOList)) {

            List<WatsonsPreRequestOrderDTO> watsonsPreRequestOrderDTOList = new ArrayList<>();
            //将每一个商品根据自己的多个费用拆成多个订单行
            splitShoppingCartByCostConfig(watsonsShoppingCartDTOList);
            //此时shoppingCartDTOList已经有   每一个商品根据自己的多个费用条拆成的多个订单行

            PurReqMergeRule purReqMergeRule = PurReqMergeRule.getDefaultMergeRule();

            //  拼上key  只能用watsonsShoppingCartDTO 进行数据获取  所以必须使用watsonsshoppingcart

            //  shoppingCart中的addressid 和 authention中的addressid是不同的  所以必须用watsons进行合单

            //  因为子订单做同样的合单操作   watsonsShoppingCartDTO中的子订单这个类型   也要改成watsonsShoppingCartDTO类型

            //  需要先用默认的排一次  然后再拆单

            Map<String, List<WatsonsShoppingCartDTO>> result = watsonsShoppingCartDTOList.stream().collect(Collectors.groupingBy(s -> s.groupKey(purReqMergeRule)));
            result = watsonsGroupPurchaseRequest(tenantId, purReqMergeRule, result);

            // eric即使是watsons中的seskulist 同样是shoppingcart类型的 天生没有合单归项的key的3个数据  所以还按原来的addressid排？差距就在addressid
//            Map<String, List<ShoppingCartDTO>> reResult = re.stream().collect(Collectors.groupingBy(s -> s.groupKey(purReqMergeRule)));
//            reResult = groupPurchaseRequest(tenantId, purReqMergeRule, reResult);

            //拆单完成后判断同一分组是否还有同一种的相同商品（有很多种不同种的商品）拆成不同的单子   因为后续业务需要这样做
            recursionSplitShoppingCart(result);

            //用于前端区分采购申请s
            int distinguishId = 0;


            for (Map.Entry<String, List<WatsonsShoppingCartDTO>> entry : result.entrySet()) {
                WatsonsPreRequestOrderDTO watsonsPreRequestOrderDTO = new WatsonsPreRequestOrderDTO();
                watsonsPreRequestOrderDTO.setKeyForView(entry.getKey());


                List<WatsonsShoppingCartDTO> watsonsShoppingCartDTOList4Trans = entry.getValue();
                List<ShoppingCartDTO> shoppingCartDTO4Freight= new ArrayList<>();

                for (WatsonsShoppingCartDTO watsonsShoppingCartDTO : watsonsShoppingCartDTOList4Trans) {
                    ShoppingCartDTO shoppingCartDTO = new ShoppingCartDTO();
                   BeanUtils.copyProperties(watsonsShoppingCartDTO,shoppingCartDTO);
                   shoppingCartDTO4Freight.add(shoppingCartDTO);
                }

                watsonsPreRequestOrderDTO.setShoppingCartDTOList(shoppingCartDTO4Freight);
                watsonsPreRequestOrderDTO.setDistinguishId(++distinguishId);
                watsonsPreRequestOrderDTO.setCount(entry.getValue().stream().mapToLong(WatsonsShoppingCartDTO::getQuantity).sum());
                WatsonsShoppingCartDTO watsonsShoppingCartDTO = entry.getValue().get(0);
                watsonsPreRequestOrderDTO.setAddress(watsonsShoppingCartDTO.getAddress());
                boolean checkHideSupplier = hideSupplier && ScecConstants.SourceType.CATALOGUE.equals(watsonsShoppingCartDTO.getProductSource());
                watsonsPreRequestOrderDTO.setSupplierCompanyName(checkHideSupplier ? ScecConstants.HideField.HIDE_SUPPLIER_NAME_CODE : watsonsShoppingCartDTO.getSupplierCompanyName());
                watsonsPreRequestOrderDTO.setOuName(watsonsShoppingCartDTO.getOuName());
                watsonsPreRequestOrderDTO.setPurOrganizationName(watsonsShoppingCartDTO.getPurOrganizationName());
                watsonsPreRequestOrderDTO.setPurOrganizationId(watsonsShoppingCartDTO.getPurOrganizationId());
                watsonsPreRequestOrderDTO.setOrganizationName(watsonsShoppingCartDTO.getOrganizationName());
                watsonsPreRequestOrderDTO.setReceiverEmailAddress(watsonsShoppingCartDTO.getContactEmail());
                watsonsPreRequestOrderDTO.setReceiverTelNum(watsonsShoppingCartDTO.getContactMobile());
                watsonsPreRequestOrderDTO.setProxySupplierCompanyId(watsonsShoppingCartDTO.getProxySupplierCompanyId());
                watsonsPreRequestOrderDTO.setProxySupplierCompanyName(watsonsShoppingCartDTO.getProxySupplierCompanyName());
                watsonsPreRequestOrderDTO.setShowSupplierCompanyId(watsonsShoppingCartDTO.getShowSupplierCompanyId());
                watsonsPreRequestOrderDTO.setShowSupplierName(checkHideSupplier ? ScecConstants.HideField.HIDE_SUPPLIER_NAME_CODE : watsonsShoppingCartDTO.getShowSupplierName());
                // 订单总价(不含运费)
                BigDecimal price = entry.getValue().stream().map(WatsonsShoppingCartDTO::getTotalPrice).reduce(BigDecimal.ZERO, BigDecimal::add);

//                if (reResult.containsKey(entry.getKey())) {
//                    price = reResult.get(entry.getKey()).stream()
//                            .filter(s -> s.getSkuType().equals(ScecConstants.ProductSkuType.SERVICE_PRODUCT))
//                            .map(ShoppingCartDTO::getTotalPrice)
//                            .reduce(price, BigDecimal::add);
//                }

                validateMinPurchaseAmount(tenantId, watsonsShoppingCartDTO, price, watsonsPreRequestOrderDTO);
                // 代理运费和代理总价问题处理 运费以采购价来计算
                BigDecimal freightPrice = price;
                if (ScecConstants.AgreementType.SALE.equals(watsonsShoppingCartDTO.getAgreementType())) {
                    //  如果是销售协议 运费需要用采购协议价来计算
                    freightPrice = entry.getValue().stream().map(WatsonsShoppingCartDTO::getPurTotalPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
//                    if (reResult.containsKey(entry.getKey())) {
//                        freightPrice = reResult.get(entry.getKey()).stream()
//                                .filter(s -> s.getSkuType().equals(ScecConstants.ProductSkuType.SERVICE_PRODUCT))
//                                .map(ShoppingCartDTO::getPurTotalPrice)
//                                .reduce(price, BigDecimal::add);
//                    }
                }
                //TODO 设置运费
                // 测试环境存在多个京东编码
                if (ScecConstants.ECommercePlatformCode.PLATFORM_JD.equals(entry.getValue().get(0).getProductSource()) || ScecConstants.SourceType.NJD.equals(entry.getValue().get(0).getProductSource())) {
                    //供应商为京东
                    if (freightPrice.compareTo(new BigDecimal(ScecConstants.JDFreightLevel.FREIGHT_FREE)) > -1) {
                        //金额超过99元，免运费
                        watsonsPreRequestOrderDTO.setFreight(BigDecimal.ZERO);
                    } else if (freightPrice.compareTo(new BigDecimal(ScecConstants.JDFreightLevel.FREIGHT_MIDDLE)) > -1) {
                        //金额在50-99之间，低运费
                        watsonsPreRequestOrderDTO.setFreight(new BigDecimal(ScecConstants.JDFreightLevel.FREIGHT_COST_LOW));
                    } else {
                        //金额在0-49之间，高运费
                        watsonsPreRequestOrderDTO.setFreight(new BigDecimal(ScecConstants.JDFreightLevel.FREIGHT_COST_HIGN));
                    }
                } else {
                    //非京东商品或目录化商品
                    //preRequestOrderDTO.setFreight(BigDecimal.ZERO);


                    // 这边用到了shoppingcartDTOlist做了一些事  但是仅仅是读取了watsonsPreRequestOrderDTO中的shoppingcartDTO   而并没有在shoppingcartDTO中更新数据而导致watsinsShoppingcartDTO中与shoppingcartDTO数据不一致
                    postageService.calculateFreight(Collections.singletonList(watsonsPreRequestOrderDTO));
                }
                
                
                //小计金额  金额+运费
                watsonsPreRequestOrderDTO.setPrice(price.add(watsonsPreRequestOrderDTO.getFreight()));
                if (ScecConstants.AgreementType.SALE.equals(watsonsShoppingCartDTO.getAgreementType())) {
                    //  如果是销售协议 运费需要用采购协议价来计算
                    watsonsPreRequestOrderDTO.setPurPrice(freightPrice.add(watsonsPreRequestOrderDTO.getFreight()));
                }

                //校验账户余额
//                checkBalance(entry.getValue().get(0).getCompanyId(),preRequestOrderDTO);
                // 获取缓存中电商支付信息
                PaymentInfo paymentInfo = shoppingCartRepository.queryPaymentInfo(watsonsShoppingCartDTO);
                if (null != paymentInfo) {
                    BeanConvertor.convert(paymentInfo, watsonsPreRequestOrderDTO);
                }
                watsonsPreRequestOrderDTO.setPreRequestOrderNumber(UUID.randomUUID().toString());
                watsonsPreRequestOrderDTO.setWatsonsShoppingCartDTOList(watsonsShoppingCartDTOList4Trans);
                watsonsPreRequestOrderDTO.setMobile(watsonsShoppingCartDTO.getMobile());
                CustomUserDetails userDetails = DetailsHelper.getUserDetails();
                watsonsPreRequestOrderDTO.setReceiverContactName(userDetails.getRealName());


                String addressRegion = watsonsShoppingCartDTOList4Trans.get(0).getAllocationInfoList().get(0).getAddressRegion();
                String fullAddress = watsonsShoppingCartDTOList4Trans.get(0).getAllocationInfoList().get(0).getFullAddress();
                //一个拆好的订单的所有商品行的详细地址+地址区域要一样  所以这里可以取任意一个
                watsonsPreRequestOrderDTO.setReceiverAddress(addressRegion+fullAddress);
                watsonsPreRequestOrderDTO.setStoreNo(watsonsShoppingCartDTOList4Trans.get(0).getAllocationInfoList().get(0).getCostShopCode());
                snapshotUtil.saveSnapshot(AbstractKeyGenerator.getKey(ScecConstants.CacheCode.SERVICE_NAME, ScecConstants.CacheCode.PURCHASE_REQUISITION_PREVIEW, watsonsPreRequestOrderDTO.getPreRequestOrderNumber()), watsonsPreRequestOrderDTO.getPreRequestOrderNumber(), watsonsPreRequestOrderDTO, 5, TimeUnit.MINUTES);
                watsonsPreRequestOrderDTOList.add(watsonsPreRequestOrderDTO);
            }
            //        进行cms合同号取值
            watsonsPreRequestOrderDTOList.stream().forEach(watsonsPreRequestOrderDTO -> {
                for (WatsonsShoppingCartDTO watsonsShoppingCartDTO : watsonsPreRequestOrderDTO.getWatsonsShoppingCartDTOList()) {
                    AgreementLine agreementLine = agreementLineRepository.selectByPrimaryKey(watsonsShoppingCartDTO.getAgreementLineId());
                    //attributeVarchar1是cms合同号
                    if(ObjectUtils.isEmpty(agreementLine)){
                        logger.error(watsonsShoppingCartDTO.getProductName()+"没有查询到该商品的协议行!");
                    }
                    if(!ObjectUtils.isEmpty(agreementLine) && ObjectUtils.isEmpty(agreementLine.getAttributeVarchar1())){
                        logger.error(watsonsShoppingCartDTO.getProductName()+"没有查询到该商品的CMS合同号!");
                    }
                    watsonsShoppingCartDTO.setCmsNumber(agreementLine.getAttributeVarchar1());
                }
            });
            // handleCheck()
            return watsonsPreRequestOrderDTOList;
        }
        return null;
    }

    private void splitShoppingCartByCostConfig(List<WatsonsShoppingCartDTO> watsonsShoppingCartDTOList) {
        //所有商品按所有的费用分配拆行
        List<WatsonsShoppingCartDTO> splitCosttInfoList = new ArrayList<>();
        Iterator<WatsonsShoppingCartDTO> it = watsonsShoppingCartDTOList.iterator();
        while (it.hasNext()) {
            WatsonsShoppingCartDTO watsonsShoppingCartDTO = it.next();
            List<AllocationInfo> allocationInfoList = watsonsShoppingCartDTO.getAllocationInfoList();
            if (!CollectionUtils.isEmpty(allocationInfoList) && allocationInfoList.size() > 1) {
                for (AllocationInfo allocationInfo : allocationInfoList) {
                    WatsonsShoppingCartDTO newWatsonsShoppingCartDTO = new WatsonsShoppingCartDTO();
                    BeanUtils.copyProperties(watsonsShoppingCartDTO, newWatsonsShoppingCartDTO);
                    newWatsonsShoppingCartDTO.setQuantity(allocationInfo.getQuantity().intValue());
                    newWatsonsShoppingCartDTO.setAllocationInfoList(Collections.singletonList(allocationInfo));
                    newWatsonsShoppingCartDTO.setTotalPrice(ObjectUtils.isEmpty(allocationInfo.getPrice()) ? BigDecimal.ZERO : allocationInfo.getPrice().multiply(BigDecimal.valueOf(newWatsonsShoppingCartDTO.getQuantity())));
                    splitCosttInfoList.add(newWatsonsShoppingCartDTO);
                }
                it.remove();
            }
        }
        watsonsShoppingCartDTOList.addAll(splitCosttInfoList);
    }

    private Map<String, Map<Long, PriceResultDTO>> queryPriceResult(List<ShoppingCartDTO> shoppingCartDTOList) {
        Map<String, List<ShoppingCartDTO>> skuShoppingCartDTO = shoppingCartDTOList.stream().collect(Collectors.groupingBy(ShoppingCartDTO::skuRegionGroupKey));
        Map<String, Map<Long, PriceResultDTO>> priceResultDTOMap = new HashMap<>();
        // 异步循环获取价格
        List<Future<Map<String, Map<Long, PriceResultDTO>>>> asyncCallback = new ArrayList<>();

        SecurityContext context = SecurityContextHolder.getContext();
        for (Map.Entry<String, List<ShoppingCartDTO>> entry : skuShoppingCartDTO.entrySet()) {
            Future<Map<String, Map<Long, PriceResultDTO>>> checkSaleResultResult = shoppingCartService.asyncQueryPrice(entry, context);
            asyncCallback.add(checkSaleResultResult);
        }
        for (Future<Map<String, Map<Long, PriceResultDTO>>> checkSaleResult : asyncCallback) {
            try {
                priceResultDTOMap.putAll(checkSaleResult.get(10, TimeUnit.SECONDS));
            } catch (InterruptedException | ExecutionException | TimeoutException e) {
                e.printStackTrace();
                logger.error("异步查询价格异常{}", ExceptionUtil.getDetailMessage(e));
            }
        }
        return priceResultDTOMap;
    }


    private void checkPrice(ShoppingCartDTO shoppingCartDTO, PriceResultDTO priceResultDTO) {
        // 便利购物车数据 比较价格 价格不一致报错
        if (!ObjectUtils.isEmpty(priceResultDTO.getLadderPriceList())) {
//            获取对应阶梯的价格
            ProductPool productPool = new ProductPool();
            productPool.setLadderPriceList(priceResultDTO.getLadderPriceList());
            ShoppingCart shoppingCart = new ShoppingCart();
            BeanUtils.copyProperties(shoppingCartDTO, shoppingCart);
            handLadderPrice(shoppingCart, productPool);
            priceResultDTO.setSellPrice(shoppingCart.getLatestPrice());
        }
        // 价格只保留两位
        if (shoppingCartDTO.getLatestPrice().compareTo(priceResultDTO.getSellPrice()) != 0) {
            throw new CommonException(ScecConstants.ErrorCode.ERROR_INCONSISTENT_PRODUCT_PRICE);
        }
        BigDecimal totalPrice = ObjectUtils.isEmpty(priceResultDTO.getSellPrice()) ? BigDecimal.ZERO : (priceResultDTO.getSellPrice().multiply(BigDecimal.valueOf(shoppingCartDTO.getQuantity())));
        //punchout 不计算价格
        if (punchoutService.isPuhchout(shoppingCartDTO.getProductSource())) {
            return;
        }
        if (shoppingCartDTO.getTotalPrice().compareTo(totalPrice) != 0) {
            throw new CommonException(ScecConstants.ErrorCode.ERROR_INCONSISTENT_PRODUCT_PRICE);
        }
    }


    /**
     * 设置购物车数据
     *
     * @param shoppingCartDTO 购物车
     * @param priceResultDTO  价格服务返回结果
     */
    private void convertParam(ShoppingCartDTO shoppingCartDTO, PriceResultDTO priceResultDTO) {
        shoppingCartDTO.setPurLastPrice(priceResultDTO.getPurchasePrice());
        BigDecimal proxyTotalPrice = ObjectUtils.isEmpty(shoppingCartDTO.getPurLastPrice()) ? BigDecimal.ZERO : (shoppingCartDTO.getPurLastPrice().multiply(BigDecimal.valueOf(shoppingCartDTO.getQuantity())));
        shoppingCartDTO.setPurTotalPrice(proxyTotalPrice);
        shoppingCartDTO.setShowSupplierCompanyId(priceResultDTO.getShowSupplierCompanyId());
        shoppingCartDTO.setShowSupplierName(priceResultDTO.getShowSupplierName());
        shoppingCartDTO.setSupplierCompanyId(priceResultDTO.getSupplierCompanyId());
        shoppingCartDTO.setSupplierCompanyName(priceResultDTO.getSupplierCompanyName());
        shoppingCartDTO.setProxySupplierCompanyId(priceResultDTO.getProxySupplierCompanyId());
        shoppingCartDTO.setProxySupplierCompanyName(priceResultDTO.getProxySupplierCompanyName());
        shoppingCartDTO.setAgreementType(priceResultDTO.getAgreementType());
        shoppingCartDTO.setAgreementBusinessType(priceResultDTO.getAgreementBusinessType());
    }


    private void validateShppingAddress(List<ShoppingCartDTO> shoppingCartDTOList) {
        List<Long> addressIds = shoppingCartDTOList.stream().map(ShoppingCartDTO::getAddressId).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(addressIds) || addressIds.size() != shoppingCartDTOList.size()) {
            throw new CommonException(ScecConstants.ErrorCode.ADDRESS_NOT_EXISTS);
        }
        Set<Long> addressIdSet = new HashSet(addressIds);
        List<Address> addressList = addressRepository.selectByIds(org.apache.commons.lang3.StringUtils.join(addressIdSet, ","));
        if (CollectionUtils.isEmpty(addressList) || addressList.size() != addressIdSet.size()) {
            throw new CommonException(ScecConstants.ErrorCode.ADDRESS_NOT_EXISTS);
        }
        String customerSetting = customizeSettingHelper.queryBySettingCode(shoppingCartDTOList.get(0).getTenantId(), CustomizeSettingCode.Purchase.CataloguePurchase.PERSONAL_ADDRESS);
        if (org.apache.commons.lang3.StringUtils.isNotEmpty(customerSetting) && customerSetting.equals(ScecConstants.ConstantNumber.STRING_0)) {
            //个人收货地址被禁用
            addressList.forEach(a -> {
                if (Objects.isNull(a.getBelongType()) || a.getBelongType() == 2) {
                    //不允许使用个人收货地址下单
                    throw new CommonException(ScecConstants.ErrorCode.PERSION_ADDRESS_DISABLED);
                }
            });
        }
    }


    private void validateShoppingExistCar(List<ShoppingCartDTO> shoppingCartDTOList) {
        //验证商品存在购物车才能生成与采购申请单
        String ids = "";
        for (ShoppingCartDTO shoppingCartDTO : shoppingCartDTOList) {
            ids += shoppingCartDTO.getCartId() + ",";
        }
        if (org.apache.commons.lang3.StringUtils.isNotEmpty(ids)) {
            ids = ids.substring(0, ids.length() - 1);
        }
        List<ShoppingCart> shoppingCarts = shoppingCartRepository.selectByIds(ids);
        if (shoppingCarts == null || shoppingCarts.size() == 0 || shoppingCarts.size() < shoppingCartDTOList.size()) {
            throw new CommonException(ScecConstants.ErrorCode.ERROR_REFRESH_SHOPPINGCAR);
        }
    }

    private void splitShoppingCartByBudgetConfig(String configResult, PurReqMergeRule purReqMergeRule, List<ShoppingCartDTO> shoppingCartDTOList) {
        //预算拆单：同一个商品会有多个预算，此时需要将这个商品根据预算维度拆分，拆成多个不同的采购申请单,但是不同的商品不能按照预算不同进行拆分
        List<ShoppingCartDTO> splitBudgetInfoList = new ArrayList<>();
        if (ScecConstants.ConstantNumber.STRING_1.equals(configResult)) {
            //开启了预算
            //将有预算信息的购物车拆开
            Iterator<ShoppingCartDTO> it = shoppingCartDTOList.iterator();
            while (it.hasNext()) {
                ShoppingCartDTO shoppingCartDTO = it.next();
                List<BudgetInfo> budgetInfoList = shoppingCartDTO.getBudgetInfoList();

                if (!CollectionUtils.isEmpty(budgetInfoList) && budgetInfoList.size() > 1) {
                    for (BudgetInfo budgetInfo : budgetInfoList) {
                        ShoppingCartDTO newShoppingCart = new ShoppingCartDTO();
                        BeanUtils.copyProperties(shoppingCartDTO, newShoppingCart);
                        newShoppingCart.setQuantity(budgetInfo.getQuantity().intValue());
                        newShoppingCart.setBudgetInfoList(Collections.singletonList(budgetInfo));
                        newShoppingCart.setTotalPrice(ObjectUtils.isEmpty(newShoppingCart.getLatestPrice()) ? BigDecimal.ZERO : (newShoppingCart.getLatestPrice().multiply(BigDecimal.valueOf(newShoppingCart.getQuantity()))));
                        splitBudgetInfoList.add(newShoppingCart);
                    }
                    it.remove();
                }
            }
        }
        shoppingCartDTOList.addAll(splitBudgetInfoList);
    }


    private void validateMinPurchaseAmount(Long tenantId, WatsonsShoppingCartDTO watsonsShoppingCartDTO, BigDecimal price, WatsonsPreRequestOrderDTO watsonsPreRequestOrderDTO) {
        //满足最小成单金额 1 满足 0 不满足
        watsonsPreRequestOrderDTO.setMinPurchaseFlag(ScecConstants.ConstantNumber.INT_1);
        //从配置中心读取是否开启了最小采买金额限制
        if (ScecConstants.ConstantNumber.STRING_1.equals(customizeSettingHelper.queryBySettingCode(tenantId, ScecConstants.SettingCenterCode.MIN_PURCHASE_AMOUNT_CODE))) {
            MinPurchaseConfig minPurchaseConfig = new MinPurchaseConfig();
            minPurchaseConfig.setTenantId(tenantId);
            minPurchaseConfig.setCurrencyName(watsonsShoppingCartDTO.getCurrencyCode());
            minPurchaseConfig.setSupplierCompanyId(watsonsShoppingCartDTO.getSupplierCompanyId());
            MinPurchaseConfig minPurchaseAmount = minPurchaseConfigRepository.selectOne(minPurchaseConfig);
            if (!ObjectUtils.isEmpty(minPurchaseAmount)) {
                //判断是否达到了最小采买金额限制
                if ((new BigDecimal(minPurchaseAmount.getMinPurchaseAmount())).compareTo(price) > ScecConstants.ConstantNumber.INT_0) {
                    watsonsPreRequestOrderDTO.setMinPurchaseResult("未达到最小采买金额 {" + minPurchaseAmount.getMinPurchaseAmount() + "}");
                    watsonsPreRequestOrderDTO.setMinPurchaseAmount(minPurchaseAmount.getMinPurchaseAmount());
                    watsonsPreRequestOrderDTO.setMinPurchaseFlag(ScecConstants.ConstantNumber.INT_0);
                }
            }
        }
    }

    private void recursionSplitShoppingCart(Map<String, List<WatsonsShoppingCartDTO>> result) {
        //拆单完成后将还未进行拆单的list再进行拆单,并将根据预算进行拆单设置为yes，与之前拆单的key区分，以防止拆单完成后key重复

        //用一级品类  地址  和供应商  可能有相同的商品被分在一组  不允许 要拆成不同的商品分在一组
        Map<String, List<WatsonsShoppingCartDTO>> splitResultMap = new HashMap<>();
        List<String> removeKeyList = new ArrayList<>();
        for (Map.Entry<String, List<WatsonsShoppingCartDTO>> entry : result.entrySet()) {
            Set<WatsonsShoppingCartDTO> set = new TreeSet<>(Comparator.comparing(WatsonsShoppingCartDTO::getProductId));
            set.addAll(entry.getValue());
            //没有重复的商品，拆单成功
            if (set.size() == entry.getValue().size()) {
                splitResultMap.put(entry.getKey(), entry.getValue());
            } else {
                //有重复的商品，将重复的数据取出，继续拆单，由于该list已经是拆单好之后的list，因此只需要将重复的数据取出，生成一个新的list即可
                //取出所有重复的商品  不止一种商品重复    拿出所有重复商品的id   把两个不同的商品组合在一起  重复的同样的商品不能组合
                Map<Long, List<WatsonsShoppingCartDTO>> map = entry.getValue().stream().collect(Collectors.groupingBy(WatsonsShoppingCartDTO::getProductId));
                Set<Long> productIdList = entry.getValue().stream().map(WatsonsShoppingCartDTO::getProductId).filter(Objects::nonNull).collect(Collectors.toSet());
                for (Long productId : productIdList) {
                    List<WatsonsShoppingCartDTO> list = map.get(productId);
                    //遍历
                    for (int i = 0; i < list.size(); i++) {
                        //创建新的key，与之前的key区分
                        String key = entry.getKey() + i;

                        if (splitResultMap.containsKey(key)) {
                            splitResultMap.get(key).add(list.get(i));
                        } else {
                            List<WatsonsShoppingCartDTO> l = new ArrayList<>();
                            l.add(list.get(i));
                            splitResultMap.put(key, l);
                        }
                    }
                }
                //需要将原来的拆单数据移除，因此记录
                removeKeyList.add(entry.getKey());
            }
        }
        result.putAll(splitResultMap);
        for (String key : removeKeyList) {
            result.remove(key);
        }
    }


    public Map<String, List<WatsonsShoppingCartDTO>> watsonsGroupPurchaseRequest(Long tenantId, PurReqMergeRule purReqMergeRule, Map<String, List<WatsonsShoppingCartDTO>> groupMap) {

        Map<String, List<WatsonsShoppingCartDTO>> resultMap = new HashMap<>();

        //eric 遍历以默认的并单规则分类       每个购物车和第一个预算信息创建成的购物车的list             组成map   groupMap
        for (String key : groupMap.keySet()) {

            //eric  拿到该并单规则下的每一个购物车
            List<WatsonsShoppingCartDTO> watsonsShoppingCartDTOList = groupMap.get(key);

            //eric 按照供应商+品类+费用分配的门店地址+送货方式 更新并单规则
            this.setPurMergeRuleForWatsons(purReqMergeRule);

            //如果开启了物料，先查询库存信息
            if (BaseConstants.Flag.YES.equals(purReqMergeRule.getWarehousing())) {

                //eric  遍历该并单规则下的购物车list
                for (WatsonsShoppingCartDTO watsonsShoppingCartDTO : watsonsShoppingCartDTOList) {

                    //eric  拿到每个购物车
                    //eric  如果有物料编码  设置warehousing 为yes
                    if (BaseConstants.Flag.YES.equals(watsonsShoppingCartDTO.getHasItemFlag())) {
                        //eric 查询物料为库存还是非库存
                        ResponseEntity<String> response = smdmRemoteService.detailByItemCOde(tenantId, watsonsShoppingCartDTO.getItemCode(), watsonsShoppingCartDTO.getInvOrganizationId());
                        if (ResponseUtils.isFailed(response)) {
                            logger.error("detailByItemCOde:{}", response);
                            continue;
                        }
                        logger.info("detailByItemCOde:{}", response);
                        ItemOrgRel itemOrgRelResponseEntity = ResponseUtils.getResponse(response, ItemOrgRel.class);
                        if (BaseConstants.Flag.YES.toString().equals(itemOrgRelResponseEntity.getAttributeVarchar1())) {
                            watsonsShoppingCartDTO.setWarehousing(BaseConstants.Flag.YES);
                        }
                    }
                    //eric  遍历该购并单下的购物车list结束  如果开启了物料，查询库存信息结束
                }
            }

            if (!ObjectUtils.isEmpty(purReqMergeRule)) {
                //eric 如果并单规则不为空
                //eric 把当前并单规则下的该购物车list进行新并单规则的分类为map  result  并更新结果集
                //即把第一个预算信息和购物车拆单的结果进行新的合并规则的合单
                for (WatsonsShoppingCartDTO watsonsShoppingCartDTO : watsonsShoppingCartDTOList) {
                    StringBuffer keyRes = new StringBuffer();
                    //既没有映射品类,也没有映射物料,报错
                    if(ObjectUtils.isEmpty(watsonsShoppingCartDTO.getItemId()) && ObjectUtils.isEmpty(watsonsShoppingCartDTO.getItemCategoryId())){
                        throw new CommonException("商品既没有映射物料也没有映射品类,请重新选择商品!");
                    }

                    //电商商品可能没有映射itemId  所以要判断
                        //有itemId查一级品类  正常走流程
                        //没有itemId  用ItemCategoryId去查levelPath
                            //如果是三级的levelPath  证明就是三级品类找一级品类即可
                            //如果是二级的levelPath  证明是二级品类找parentCategoryId即可
                            //如果是一级的品类直接用即可
                            //如果是多级的，直接报错

                    //如果有itemId
                        //包括单独有itemId 或者 有两个
                    if(!ObjectUtils.isEmpty(watsonsShoppingCartDTO.getItemId())) {
                        ResponseEntity<String> responseOne = smdmRemoteService.selectCategoryByItemId(tenantId, watsonsShoppingCartDTO.getItemId(), BaseConstants.Flag.YES);
                        if (ResponseUtils.isFailed(responseOne)) {
                            logger.error("selectCategoryByItemId:{}", responseOne);
                            throw new CommonException("根据物料查询一级品类失败!");
                        }
                        logger.info("selectCategoryByItemId:{}", responseOne);
                        List<WatsonsItemCategoryDTO> itemCategoryResultOne = ResponseUtils.getResponse(responseOne, new TypeReference<List<WatsonsItemCategoryDTO>>() {
                        });

                        if (CollectionUtils.isEmpty(itemCategoryResultOne)) {
                            logger.error("selectCategoryByItemId:{}", "null");
                            throw new CommonException("根据物料查询一级品类失败!");
                        }

                        if (CollectionUtils.isNotEmpty(itemCategoryResultOne) && (itemCategoryResultOne.size() > 1)) {
                            logger.error("selectCategoryByItemId:{}", itemCategoryResultOne);
                            throw new CommonException("单个物料非法查询到多个品类！");
                        }


//                    if (BaseConstants.Flag.YES.equals(purReqMergeRule.getSupplierFlag())) {
                        handleNormalSplit(purReqMergeRule, watsonsShoppingCartDTO, keyRes);
                        if (BaseConstants.Flag.YES.equals(purReqMergeRule.getCategory())) {
                            keyRes.append(itemCategoryResultOne.get(0).getParentCategoryId()).append("-");
                        }
                        String keyFinal = String.valueOf(keyRes);
                        watsonsShoppingCartDTO.setItemCategoryId(itemCategoryResultOne.get(0).getParentCategoryId());
                        watsonsShoppingCartDTO.setItemCategoryName(itemCategoryResultOne.get(0).getParentCategoryName());
                        watsonsShoppingCartDTO.setKey(keyFinal);
                    }

                    //如果只有itemCategoryId
                            //没有itemId  用ItemCategoryId去查levelPath
                            //如果是三级的levelPath  证明就是三级品类找一级品类即可
                            //如果是二级的levelPath
                            //如果是一级的品类直接用即可
                            //如果是多级的，直接报错
                    if(ObjectUtils.isEmpty(watsonsShoppingCartDTO.getItemId()) && !ObjectUtils.isEmpty(watsonsShoppingCartDTO.getItemCategoryId())){

                        ResponseEntity<String> itemCategoryInfoRes = smdmRemoteNewService.queryById(tenantId, String.valueOf(watsonsShoppingCartDTO.getItemCategoryId()));
                        if (ResponseUtils.isFailed(itemCategoryInfoRes)) {
                            logger.error("query itemCategory info By itemCategoryId error! param itemCategoryId: {}", watsonsShoppingCartDTO.getItemCategoryId());
                            throw new CommonException("根据物料品类id查询物料品类信息失败!");
                        }
                        logger.info("query itemCategory info By itemCategoryId success! param itemCategoryId: {}", watsonsShoppingCartDTO.getItemCategoryId());
                        ItemCategoryDTO itemCategoryResultOne = ResponseUtils.getResponse(itemCategoryInfoRes, new TypeReference<ItemCategoryDTO>() {
                        });
                        String levelPath = itemCategoryResultOne.getLevelPath();

                        if (!StringUtils.isEmpty(levelPath)) {
                            String[] splitRes = levelPath.split("\\|");
                            if (splitRes.length > 3) {
                                logger.info("此时只传入物料品类id, 且是大于三级的物料品类");
                                throw new CommonException("该商品有三级以上的品类映射,请重新选择商品!");
                            }
                            if (splitRes.length == 3) {
                                //该itemCategoryId就是三级品类id
                                logger.info("此时只传入物料品类id, 且是三级物料品类");
                                //先通过三级物料品类id找二级物料品类
                                ResponseEntity<String> bLevel = smdmRemoteNewService.queryById(tenantId, String.valueOf(itemCategoryResultOne.getParentCategoryId()));
                                if (ResponseUtils.isFailed(bLevel)) {
                                    logger.error("query itemCategory info By itemCategoryId error! param itemCategoryId: {}", itemCategoryResultOne.getParentCategoryId());
                                    throw new CommonException("根据二级物料品类id查询二级物料品类信息失败!");
                                }
                                logger.info("query itemCategory info By itemCategoryId success! param itemCategoryId: {}", itemCategoryResultOne.getParentCategoryId());
                                ItemCategoryDTO bLevelRes = ResponseUtils.getResponse(bLevel, new TypeReference<ItemCategoryDTO>() {
                                });

                                //再通过二级物料品类id找一级物料品类
                                ResponseEntity<String> alevel = smdmRemoteNewService.queryById(tenantId, String.valueOf(bLevelRes.getParentCategoryId()));
                                if (ResponseUtils.isFailed(alevel)) {
                                    logger.error("query itemCategory info By itemCategoryId error! param itemCategoryId: {}", bLevelRes.getParentCategoryId());
                                    throw new CommonException("根据一级物料品类id查询一级物料品类信息失败!");
                                }
                                logger.info("query itemCategory info By itemCategoryId success! param itemCategoryId: {}", bLevelRes.getParentCategoryId());
                                ItemCategoryDTO aLevelRes = ResponseUtils.getResponse(alevel, new TypeReference<ItemCategoryDTO>() {
                                });

                                //根据一级品类拆单
                                handleNormalSplit(purReqMergeRule, watsonsShoppingCartDTO, keyRes);
                                if (BaseConstants.Flag.YES.equals(purReqMergeRule.getCategory())) {
                                    keyRes.append(aLevelRes.getCategoryId()).append("-");
                                }
                                String keyFinal = String.valueOf(keyRes);
                                watsonsShoppingCartDTO.setItemCategoryId(aLevelRes.getCategoryId());
                                watsonsShoppingCartDTO.setItemCategoryName(aLevelRes.getCategoryName());
                                watsonsShoppingCartDTO.setKey(keyFinal);
                            }
                            if (splitRes.length == 2) {
                                logger.info("此时只传入物料品类id, 且是二级物料品类");
                                //该itemCategoryId就是二级品类id   找parentCategoryId即一级品类进行拆单
                                handleNormalSplit(purReqMergeRule, watsonsShoppingCartDTO, keyRes);
                                if (BaseConstants.Flag.YES.equals(purReqMergeRule.getCategory())) {
                                    keyRes.append(itemCategoryResultOne.getParentCategoryId()).append("-");
                                }
                                String keyFinal = String.valueOf(keyRes);
                                watsonsShoppingCartDTO.setItemCategoryId(itemCategoryResultOne.getParentCategoryId());
                                //查一级品类的name
                                ResponseEntity<String> itemCategoryALevel = smdmRemoteNewService.queryById(tenantId, String.valueOf(itemCategoryResultOne.getParentCategoryId()));
                                if (ResponseUtils.isFailed(itemCategoryALevel)) {
                                    logger.error("query itemCategory info By itemCategoryId error! param itemCategoryId: {}", itemCategoryResultOne.getParentCategoryId());
                                    throw new CommonException("根据一级物料品类id查询一级物料品类信息失败!");
                                }
                                logger.info("query itemCategory info By itemCategoryId success! param itemCategoryId: {}", itemCategoryResultOne.getParentCategoryId());
                                ItemCategoryDTO itemCategoryALevelRes = ResponseUtils.getResponse(itemCategoryALevel, new TypeReference<ItemCategoryDTO>() {
                                });
                                watsonsShoppingCartDTO.setItemCategoryName(itemCategoryALevelRes.getCategoryName());
                                watsonsShoppingCartDTO.setKey(keyFinal);
                            }
                            if (splitRes.length == 1) {
                                //该itemCategoryId就是一级品类id 直接拆单
                                logger.info("此时只传入物料品类id, 且是一级物料品类");
                                handleNormalSplit(purReqMergeRule, watsonsShoppingCartDTO, keyRes);
                                if (BaseConstants.Flag.YES.equals(purReqMergeRule.getCategory())) {
                                    keyRes.append(itemCategoryResultOne.getCategoryId()).append("-");
                                }
                                String keyFinal = String.valueOf(keyRes);
                                watsonsShoppingCartDTO.setItemCategoryId(itemCategoryResultOne.getCategoryId());
                                watsonsShoppingCartDTO.setItemCategoryName(itemCategoryResultOne.getCategoryName());
                                watsonsShoppingCartDTO.setKey(keyFinal);
                            }
                            if (splitRes.length == 0) {
                                logger.error("此时只传入物料品类id, 但没有找到levelPath");
                                throw new CommonException("根据品类id查询品类信息失败!");
                            }
                        } else {
                            logger.error("只传了物料品类下，当前商品多级映射路径levelPath为空");
                            throw new CommonException("当前商品没有映射多级映射路径!");
                        }
                    }
                }
                Map<String, List<WatsonsShoppingCartDTO>> result = watsonsShoppingCartDTOList.stream().collect(Collectors.groupingBy(WatsonsShoppingCartDTO::getKey));
                resultMap.putAll(result);
            } else {
                resultMap.put(key, groupMap.get(key));
            }
            //eric 遍历以默认的并单规则分类购物车list组成map结束
        }
        return resultMap;
    }

    private void handleNormalSplit(PurReqMergeRule purReqMergeRule, WatsonsShoppingCartDTO watsonsShoppingCartDTO, StringBuffer keyRes) {
        keyRes.append(watsonsShoppingCartDTO.getSupplierCompanyId()).append("-");
        if (BaseConstants.Flag.YES.equals(purReqMergeRule.getAddressFlag())) {
            if (watsonsShoppingCartDTO.getAllocationInfoList() == null) {
                logger.error("未进行费用分配！:{}", watsonsShoppingCartDTO.getAllocationInfoList());
                throw new CommonException("未进行费用分配!");
            }
            keyRes.append(watsonsShoppingCartDTO.getAllocationInfoList().get(0).getAddressRegion()).append("-").append(watsonsShoppingCartDTO.getAllocationInfoList().get(0).getFullAddress()).append("-");
        }
        keyRes.append(watsonsShoppingCartDTO.getAllocationInfoList().get(0).getDeliveryType()).append("-");
        keyRes.append(watsonsShoppingCartDTO.getAllocationInfoList().get(0).getCostShopId()).append("-");
    }

    private void setPurMergeRuleForWatsons(PurReqMergeRule purReqMergeRule) {
        purReqMergeRule.setCategory(BaseConstants.Flag.YES);
        purReqMergeRule.setWarehousing(BaseConstants.Flag.YES);
//        purReqMergeRule.setAddressFlag(BaseConstants.Flag.YES);
    }
}
