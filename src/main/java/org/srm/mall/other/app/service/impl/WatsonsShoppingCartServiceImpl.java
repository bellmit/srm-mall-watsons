package org.srm.mall.other.app.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ctrip.framework.apollo.util.ExceptionUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import io.choerodon.core.exception.CommonException;
import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.servicecomb.pack.omega.context.annotations.SagaStart;
import org.hzero.core.base.BaseConstants;
import org.hzero.core.util.ResponseUtils;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.srm.boot.common.CustomizeSettingCode;
import org.srm.boot.common.cache.impl.AbstractKeyGenerator;
import org.srm.boot.platform.customizesetting.CustomizeSettingHelper;
import org.srm.boot.saga.utils.SagaClient;
import org.srm.common.convert.bean.BeanConvertor;
import org.srm.mall.common.constant.ScecConstants;
import org.srm.mall.common.feign.*;
import org.srm.mall.common.feign.dto.agreemnet.AgreementLine;
import org.srm.mall.common.feign.dto.agreemnet.PostageCalculateDTO;
import org.srm.mall.common.feign.dto.agreemnet.PostageCalculateLineDTO;
import org.srm.mall.common.feign.dto.product.*;
import org.srm.mall.common.feign.dto.wflCheck.WatsonsWflCheckDTO;
import org.srm.mall.common.feign.dto.wflCheck.WatsonsWflCheckResultVO;
import org.srm.mall.common.task.MallOrderAsyncTask;
import org.srm.mall.common.utils.TransactionalComponent;
import org.srm.mall.common.utils.snapshot.SnapshotUtil;
import org.srm.mall.context.dto.ProductDTO;
import org.srm.mall.context.entity.ECResult;
import org.srm.mall.context.entity.Item;
import org.srm.mall.context.entity.ItemCategory;
import org.srm.mall.context.entity.ECResult;
import org.srm.mall.infra.constant.WatsonsConstants;
import org.srm.mall.order.api.dto.PreRequestOrderResponseDTO;
import org.srm.mall.order.app.service.MallOrderCenterService;
import org.srm.mall.order.app.service.MallOrderService;
import org.srm.mall.order.app.service.OmsOrderService;
import org.srm.mall.order.domain.vo.PurchaseRequestVO;
import org.srm.mall.other.api.dto.*;
import org.srm.mall.other.app.service.*;
import org.srm.mall.other.domain.entity.*;
import org.srm.mall.other.domain.repository.*;
import org.srm.mall.platform.api.dto.PrHeaderCreateDTO;
import org.srm.mall.platform.domain.entity.*;
import org.srm.mall.platform.domain.repository.EcClientRepository;
import org.srm.mall.platform.domain.repository.EcCompanyAssignRepository;
import org.srm.mall.platform.domain.repository.EcPlatformRepository;
import org.srm.mall.product.api.dto.ItemCategoryDTO;
import org.srm.mall.product.api.dto.LadderPriceResultDTO;
import org.srm.mall.product.api.dto.PriceResultDTO;
import org.srm.mall.product.api.dto.SkuBaseInfoDTO;
import org.srm.mall.product.app.service.ProductStockService;
import org.srm.mall.product.domain.repository.ProductWorkbenchRepository;
import org.srm.mall.product.domain.repository.ProductWorkbenchRepository;
import org.srm.mall.product.domain.entity.ScecProductCategory;
import org.srm.mall.product.domain.repository.ProductWorkbenchRepository;
import org.srm.mall.region.api.dto.AddressDTO;
import org.srm.mall.region.domain.entity.Address;
import org.srm.mall.region.domain.entity.MallRegion;
import org.srm.mall.region.domain.repository.AddressRepository;
import org.srm.mall.region.domain.repository.MallRegionRepository;
import org.srm.mq.service.producer.MessageProducer;
import org.srm.web.annotation.Tenant;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Function;
import java.util.function.Predicate;
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
    private MallOrderCenterService mallOrderCenterService;

    @Autowired
    private BudgetInfoService budgetInfoService;

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
    private SmdmRemoteNewService smdmRemoteNewService;


    @Autowired
    private WatsonsCeInfoRemoteService watsonsCeInfoRemoteService;

    @Autowired
    private MessageProducer messageProducer;

    @Autowired
    @Lazy
    private ProductService productService;

    @Autowired
    private SpcmRemoteNewService spcmRemoteNewService;

    @Autowired
    private SmpcRemoteService smpcRemoteService;

    @Autowired
    private SagmRemoteService sagmRemoteService;

    @Autowired
    private WatsonsSagmRemoteService watsonsSagmRemoteService;

    @Autowired
    private MallRegionRepository mallRegionRepository;

    @Autowired
    private WatsonsWflCheckRemoteService watsonsWflCheckRemoteService;

    @Autowired
    private ProductWorkbenchRepository productWorkbenchRepository;

    @Autowired
    private SifgOrderRemoteService sifgOrderRemoteService;


    @Autowired
    private CustomizedProductLineService customizedProductLineService;

    @Autowired
    private CustomizedProductLineRepository customizedProductLineRepository;

    @Autowired
    private CustomizedProductValueRepository customizedProductValueRepository;

    @Autowired
    private WatsonsCustomizedProductLineService watsonsCustomizedProductLineService;

    @Autowired
    @Lazy
    private AllocationInfoService allocationInfoService;

    private static final String erpForWatsons = "SRM";

    @Autowired
    private TransactionalComponent transactionalComponent;


    @Override
    public List<ShoppingCartDTO> watsonsShppingCartEnter(Long organizationId, ShoppingCart shoppingCart) {
        //加入了取费用分配的过程
        List<ShoppingCartDTO> shoppingCartDTOList = super.shppingCartEnter(organizationId, shoppingCart);
        List<WatsonsShoppingCartDTO> watsonsShoppingCartDTOS = transferToWatsonsShoppingCartDTOS(shoppingCartDTOList);
        selectCustomizedProductListForWatsons(organizationId,watsonsShoppingCartDTOS);
        if (!CollectionUtils.isEmpty(watsonsShoppingCartDTOS)) {
            List<AllocationInfo> allocationInfoList = allocationInfoRepository.selectByCondition(Condition.builder(AllocationInfo.class).andWhere(Sqls.custom()
                    .andIn(AllocationInfo.FIELD_CART_ID, watsonsShoppingCartDTOS.stream().map(WatsonsShoppingCartDTO::getCartId).collect(Collectors.toList()))).build());
            if (!CollectionUtils.isEmpty(allocationInfoList)) {
                for (AllocationInfo allocationInfo : allocationInfoList) {
                    allocationInfo.setTotalAmount(allocationInfo.getPrice().multiply(new BigDecimal(allocationInfo.getQuantity())));
                }
                Map<Long, List<AllocationInfo>> map = allocationInfoList.stream().collect(Collectors.groupingBy(AllocationInfo::getCartId));
                return watsonsShoppingCartDTOS.stream().map(s -> {
                    s.setAllocationInfoList(map.get(s.getCartId()));
                    String itemCode = checkItemCodeByItemId(s.getItemId(), organizationId, erpForWatsons);
                    logger.info("item code is " + itemCode);
                    String deliveryType = checkDeliveryType(itemCode, erpForWatsons, organizationId);
                    logger.info("delivery type is " + deliveryType);
                    if (!ObjectUtils.isEmpty(deliveryType)) {
                        if (deliveryType.equals(ScecConstants.ConstantNumber.STRING_1)) {
                            logger.info("set DIRECT_DELIVERY");
                            s.setDeliveryType("DIRECT_DELIVERY");
                            s.setDeliveryTypeMeaning("直送");
                        }
                    }
                    return s;
                }).collect(Collectors.toList());
            }
            return watsonsShoppingCartDTOS.stream().map(watsonsShoppingCartDTO -> {
                String itemCode = checkItemCodeByItemId(watsonsShoppingCartDTO.getItemId(), organizationId, erpForWatsons);
                logger.info("item code is " + itemCode);
                String deliveryType = checkDeliveryType(itemCode, erpForWatsons, organizationId);
                logger.info("delivery type is " + deliveryType);
                if (!ObjectUtils.isEmpty(deliveryType)) {
                    if (deliveryType.equals(ScecConstants.ConstantNumber.STRING_1)) {
                        logger.info("set DIRECT_DELIVERY");
                        watsonsShoppingCartDTO.setDeliveryType("DIRECT_DELIVERY");
                        watsonsShoppingCartDTO.setDeliveryTypeMeaning("直送");
                    }
                }
                return watsonsShoppingCartDTO;
            }).collect(Collectors.toList());
        }
        return shoppingCartDTOList;
    }

    private List<WatsonsShoppingCartDTO> transferToWatsonsShoppingCartDTOS(List<ShoppingCartDTO> shoppingCartDTOList) {
        List<WatsonsShoppingCartDTO> watsonsShoppingCartDTOS = new ArrayList<>();
        for (ShoppingCartDTO shoppingCartDTO : shoppingCartDTOList) {
            WatsonsShoppingCartDTO watsonsShoppingCartDTO = new WatsonsShoppingCartDTO();
            BeanUtils.copyProperties(shoppingCartDTO,watsonsShoppingCartDTO);
            watsonsShoppingCartDTOS.add(watsonsShoppingCartDTO);
        }
        return watsonsShoppingCartDTOS;
    }

    private String checkItemCodeByItemId(Long itemId, Long tenantId, String sourceCode) {
        return allocationInfoRepository.checkItemCodeByItemId(itemId, tenantId, sourceCode);
    }

    private String checkDeliveryType(String itemCode, String sourceCode, Long tenantId) {
        return allocationInfoRepository.checkDeliveryType(itemCode, sourceCode, tenantId);
    }

    @Override
    @SagaStart
    @Transactional(rollbackFor = Exception.class)
    public PreRequestOrderResponseDTO watsonsPreRequestOrder(Long tenantId, String customizeUnitCode, List<WatsonsPreRequestOrderDTO> watsonsPreRequestOrderDTOList) {
        //校验定制品
        watsonsPreRequestOrderDTOList.forEach(watsonsPreRequestOrderDTO -> {
            checkCustomizedProductInfoForWatsons(tenantId, watsonsPreRequestOrderDTO.getWatsonsShoppingCartDTOList());
        });
        //进行ceNo和discription存表
        saveCeAndCMS(watsonsPreRequestOrderDTOList);
        //根据ce信息修改费用项目信息
        modifyProjectCostByCeInfo(watsonsPreRequestOrderDTOList);
        //wlf工作流校验
        checkWLFFlow(tenantId, watsonsPreRequestOrderDTOList);
        //进行cms合同号校验
        List<PcOccupyDTO> pcOccupyDTOS = occupyCMS(tenantId, watsonsPreRequestOrderDTOList);
        //        进行ceNo校验
        checkCeInfo(tenantId, watsonsPreRequestOrderDTOList);
        //生成oms后进行错误订单ce回滚
        //如果oms全部异常 最后会抛出 cms会全部回滚
        //如果oms部分异常 则考虑把没占用成功的去掉
        List<PrHeaderCreateDTO> errorListForWatsonsPrHeaderCreateDTO = new ArrayList<>();
        List<WatsonsPreRequestOrderDTO> errorListForWatsonsPreOrderDTOForCE = new ArrayList<>();
        Exception omsException = null;
        PreRequestOrderResponseDTO preRequestOrderResponseDTO = new PreRequestOrderResponseDTO();
        try {
            watsonsPreRequestOrderDTOList.forEach(watsonsPreRequestOrderDTO -> {watsonsPreRequestOrderDTO.priceFinancialPrecisionSetting();});
                preRequestOrderResponseDTO = super.preRequestOrder(tenantId, customizeUnitCode, new ArrayList<>(watsonsPreRequestOrderDTOList));
        }catch (Exception e){
                logger.error("oms create order error. all orders are failed!");
                logger.error("start to rollback ce occupy");
            errorListForWatsonsPreOrderDTOForCE.addAll(watsonsPreRequestOrderDTOList);
                omsException = e;
        }finally {
            if(!ObjectUtils.isEmpty(preRequestOrderResponseDTO.getPrResult())) {
                if(!CollectionUtils.isEmpty(preRequestOrderResponseDTO.getPrResult().getErrorList())) {
                    errorListForWatsonsPrHeaderCreateDTO.addAll(preRequestOrderResponseDTO.getPrResult().getErrorList());
                    logger.info("the errorListForWatsonsPrHeaderCreateDTO is {}", JSONObject.toJSON(errorListForWatsonsPrHeaderCreateDTO));
                }
            }
        }
        //部分出错
        processPrheaderCreateDTOExceptionCERollback(tenantId, watsonsPreRequestOrderDTOList, errorListForWatsonsPrHeaderCreateDTO);
        processPrheaderCreateDTOExceptionCMSUpdate(tenantId, watsonsPreRequestOrderDTOList,errorListForWatsonsPrHeaderCreateDTO,pcOccupyDTOS);
        //全部出错
        processOmsAllFailedExceptionCERollback(tenantId, errorListForWatsonsPreOrderDTOForCE);
        if(!ObjectUtils.isEmpty(omsException)){
            throw  new CommonException(omsException);
        }
        return preRequestOrderResponseDTO;
    }

    private void modifyProjectCostByCeInfo(List<WatsonsPreRequestOrderDTO> watsonsPreRequestOrderDTOList) {
        for (WatsonsPreRequestOrderDTO watsonsPreRequestOrderDTO : watsonsPreRequestOrderDTOList) {
            if(!ObjectUtils.isEmpty(watsonsPreRequestOrderDTO.getCeNumber())){
                for (WatsonsShoppingCartDTO watsonsShoppingCartDTO : watsonsPreRequestOrderDTO.getWatsonsShoppingCartDTOList()) {
                    for (AllocationInfo allocationInfo : watsonsShoppingCartDTO.getAllocationInfoList()) {
                        allocationInfo.setProjectCostCode("1406");
                        allocationInfo.setProjectCostName("固定资产采购");
                    }
                }
            }
        }
    }

    private void processPrheaderCreateDTOExceptionCMSUpdate(Long tenantId, List<WatsonsPreRequestOrderDTO> watsonsPreRequestOrderDTOList, List<PrHeaderCreateDTO> errorListForWatsonsPrHeaderCreateDTO,List<PcOccupyDTO> pcOccupyDTOS) {
        if(CollectionUtils.isEmpty(errorListForWatsonsPrHeaderCreateDTO) || CollectionUtils.isEmpty(pcOccupyDTOS)){
            return;
        }
        List<PcOccupyDTO> pcOccupyDTOListNeedToCancelForThisOrder = new ArrayList<>();
        for (PrHeaderCreateDTO prHeaderCreateDTO : errorListForWatsonsPrHeaderCreateDTO) {
            for (WatsonsPreRequestOrderDTO watsonsPreRequestOrderDTO : watsonsPreRequestOrderDTOList) {
                if(watsonsPreRequestOrderDTO.getPreRequestOrderNumber().equals(prHeaderCreateDTO.getPreRequestOrderNumber())){
                    logger.info("the oms error order is {}",JSONObject.toJSON(watsonsPreRequestOrderDTO));
                    for (WatsonsShoppingCartDTO watsonsShoppingCartDTO : watsonsPreRequestOrderDTO.getWatsonsShoppingCartDTOList()) {
                        for (PcOccupyDTO pcOccupyDTO : pcOccupyDTOS) {
                            if(!ObjectUtils.isEmpty(watsonsShoppingCartDTO.getCmsNumber()) && pcOccupyDTO.getPcNum().equals(watsonsShoppingCartDTO.getCmsNumber())){
                                pcOccupyDTOListNeedToCancelForThisOrder.add(pcOccupyDTO);
                            }
                        }
                    }
                }
                pcOccupyDTOListNeedToCancelForThisOrder.forEach(pcOccupyDTO -> {
                    pcOccupyDTO.setOperationType(WatsonsConstants.operationTypeCode.SPCM_CANCEL);
                    Long version = pcOccupyDTO.getVersion();
                    pcOccupyDTO.setVersion(version+1L);
                });
                logger.info("pcOccupyDTOListNeedToCancelForThisOrder is {}",JSONObject.toJSON(pcOccupyDTOListNeedToCancelForThisOrder));
                cancelBySpcmForPreOrder(tenantId,pcOccupyDTOListNeedToCancelForThisOrder);
            }
        }
    }

    private void processOmsAllFailedExceptionCERollback(Long tenantId, List<WatsonsPreRequestOrderDTO> errorListForWatsonsPreOrderDTO) {
        if (CollectionUtils.isEmpty(errorListForWatsonsPreOrderDTO)) {
            return;
        }
        for (WatsonsPreRequestOrderDTO watsonsPreRequestOrderDTO : errorListForWatsonsPreOrderDTO) {
            if (!ObjectUtils.isEmpty(watsonsPreRequestOrderDTO.getCeNumber())) {
                CheckCeInfoDTO checkCeInfoDTO = buildCECheckInfoDTO(tenantId, watsonsPreRequestOrderDTO);
                ResponseEntity<String> checkCeInfoRes = watsonsCeInfoRemoteService.checkCeInfo(tenantId, checkCeInfoDTO);
                if (ResponseUtils.isFailed(checkCeInfoRes)) {
                    String message = null;
                    try {
                        Exception exception = JSONObject.parseObject(checkCeInfoRes.getBody(), Exception.class);
                        message = exception.getMessage();
                    } catch (Exception e) {
                        message = checkCeInfoRes.getBody();
                    }
                    logger.error("check CE info for order total amount error!  ce id is " + watsonsPreRequestOrderDTO.getCeId());
                    throw new CommonException("检验CE号" + watsonsPreRequestOrderDTO.getCeNumber() + "报错," + message);
                }
                logger.info("check CE info for order total amount success! ce id is" + watsonsPreRequestOrderDTO.getCeId());
            }
        }
    }
    private void processPrheaderCreateDTOExceptionCERollback(Long tenantId, List<WatsonsPreRequestOrderDTO> watsonsPreRequestOrderDTOList, List<PrHeaderCreateDTO> errorListForWatsonsPrHeaderCreateDTO) {
        if (CollectionUtils.isEmpty(errorListForWatsonsPrHeaderCreateDTO)) {
            return;
        }
        for (PrHeaderCreateDTO prHeaderCreateDTO : errorListForWatsonsPrHeaderCreateDTO) {
            watsonsPreRequestOrderDTOList.forEach(watsonsPreRequestOrderDTO -> {
                if (watsonsPreRequestOrderDTO.getPreRequestOrderNumber().equals(prHeaderCreateDTO.getPreRequestOrderNumber())) {
                    if (!ObjectUtils.isEmpty(watsonsPreRequestOrderDTO.getCeNumber())) {
                        CheckCeInfoDTO checkCeInfoDTO = buildCECheckInfoDTO(tenantId, watsonsPreRequestOrderDTO);
                        ResponseEntity<String> checkCeInfoRes = watsonsCeInfoRemoteService.checkCeInfo(tenantId, checkCeInfoDTO);
                        if (ResponseUtils.isFailed(checkCeInfoRes)) {
                            String message = null;
                            try {
                                Exception exception = JSONObject.parseObject(checkCeInfoRes.getBody(), Exception.class);
                                message = exception.getMessage();
                            } catch (Exception e) {
                                message = checkCeInfoRes.getBody();
                            }
                            logger.error("check CE info for order total amount error!  ce id is " + watsonsPreRequestOrderDTO.getCeId());
                            throw new CommonException("检验CE号" + watsonsPreRequestOrderDTO.getCeNumber() + "报错," + message);
                        }
                        logger.info("check CE info for order total amount success! ce id is" + watsonsPreRequestOrderDTO.getCeId());
                    }
                }
            });
        }
    }
    private CheckCeInfoDTO buildCECheckInfoDTO(Long tenantId, WatsonsPreRequestOrderDTO watsonsPreRequestOrderDTO) {
        CheckCeInfoDTO checkCeInfoDTO = new CheckCeInfoDTO();
        checkCeInfoDTO.setCeId(watsonsPreRequestOrderDTO.getCeId());
        BigDecimal withoutTaxPriceTotal = queryWithoutTaxPrice(tenantId, watsonsPreRequestOrderDTO);
        //释放时价格传负数  其余参数不变
        checkCeInfoDTO.setChangeAmount(withoutTaxPriceTotal.negate());
        checkCeInfoDTO.setItemName(watsonsPreRequestOrderDTO.getItemName());
        checkCeInfoDTO.setTranscationId(watsonsPreRequestOrderDTO.getPreRequestOrderNumber());
        return checkCeInfoDTO;
    }

    private BigDecimal queryWithoutTaxPrice(Long tenantId, WatsonsPreRequestOrderDTO watsonsPreRequestOrderDTO) {
        //取含税价格  每个订单检验一次
        BigDecimal withoutTaxPriceTotal = new BigDecimal(0);
        for (WatsonsShoppingCartDTO watsonsShoppingCartDTO : watsonsPreRequestOrderDTO.getWatsonsShoppingCartDTOList()) {
            ProductDTO productDTO = productService.selectByProduct(watsonsShoppingCartDTO.getProductId(), tenantId, watsonsShoppingCartDTO.getCompanyId(), watsonsShoppingCartDTO.getPurchaseType(), watsonsShoppingCartDTO.getSecondRegionId(), watsonsShoppingCartDTO.getLevelPath());
            if (!ObjectUtils.isEmpty(productDTO.getWithoutTaxPrice())) {
                BigDecimal quantity = watsonsShoppingCartDTO.getQuantity();
                BigDecimal withoutTaxPriceParam = productDTO.getWithoutTaxPrice().multiply(quantity);
                //加上这个商品的价格
                withoutTaxPriceTotal = withoutTaxPriceTotal.add(withoutTaxPriceParam);
            }
            if ("LADDER_PRICE".equals(productDTO.getPriceType())) {
                BigDecimal quantity = watsonsShoppingCartDTO.getQuantity();
                for (LadderPriceResultDTO ladderPriceResultDTO : productDTO.getLadderPriceList()) {
                    if (ladderPriceResultDTO.getLadderFrom().compareTo(quantity) <= 0 && (ObjectUtils.isEmpty(ladderPriceResultDTO.getLadderTo()) || ladderPriceResultDTO.getLadderTo().compareTo(quantity) > 0)) {
                        BigDecimal withoutTaxPriceParam = ladderPriceResultDTO.getWithoutTaxPrice().multiply(quantity);
                        withoutTaxPriceTotal = withoutTaxPriceTotal.add(withoutTaxPriceParam);
                    }
                }
            }
        }
        withoutTaxPriceTotal = withoutTaxPriceTotal.add(watsonsPreRequestOrderDTO.getWithoutTaxFreightPrice());
        return withoutTaxPriceTotal;
    }

    private void checkWLFFlow(Long tenantId, List<WatsonsPreRequestOrderDTO> preRequestOrderDTOList) {
        //一个商品多个店铺   一个商品一个一级分类
        //校验wfl工作流
        List<WatsonsPreRequestOrderDTO> watsonsCheckSubmitList = preRequestOrderDTOList.stream().filter(item -> ScecConstants.ConstantNumber.INT_1 == item.getMinPurchaseFlag()).collect(Collectors.toList());
        List<WatsonsWflCheckDTO> watsonsWflCheckDTOS = buildWflCheckParams(tenantId, watsonsCheckSubmitList);
        ResponseEntity<String> watsonsWflCheckResultVOResponseEntity = watsonsWflCheckRemoteService.wflStartCheck(tenantId, watsonsWflCheckDTOS);
        if (ResponseUtils.isFailed(watsonsWflCheckResultVOResponseEntity)) {
            logger.error("协同异常:校验wfl工作流时出现网络错误");
            throw new CommonException("协同异常:校验wfl工作流时出现网络错误");
        } else {
            logger.info("check wfl flow success");
            WatsonsWflCheckResultVO response = ResponseUtils.getResponse(watsonsWflCheckResultVOResponseEntity, new TypeReference<WatsonsWflCheckResultVO>() {
            });
            if (response.getErrorFlag().equals(BaseConstants.Flag.YES)) {
                logger.error(response.getErrorMessage());
                throw new CommonException(response.getErrorMessage());
            }
        }
    }

    private void checkCeInfo(Long tenantId, List<WatsonsPreRequestOrderDTO> preRequestOrderDTOList) {
        for (WatsonsPreRequestOrderDTO watsonsPreRequestOrderDTO : preRequestOrderDTOList) {
            if (!ObjectUtils.isEmpty(watsonsPreRequestOrderDTO.getCeNumber())) {
                CheckCeInfoDTO checkCeInfoDTO = new CheckCeInfoDTO();
                checkCeInfoDTO.setCeId(watsonsPreRequestOrderDTO.getCeId());
                //取含税价格  每个订单检验一次
                BigDecimal withoutTaxPriceTotal = queryWithoutTaxPrice(tenantId, watsonsPreRequestOrderDTO);
                checkCeInfoDTO.setChangeAmount(withoutTaxPriceTotal);
                checkCeInfoDTO.setItemName(watsonsPreRequestOrderDTO.getItemName());
                checkCeInfoDTO.setTranscationId(watsonsPreRequestOrderDTO.getPreRequestOrderNumber());
                ResponseEntity<String> checkCeInfoRes = watsonsCeInfoRemoteService.checkCeInfo(tenantId, checkCeInfoDTO);
                if (ResponseUtils.isFailed(checkCeInfoRes)) {
                    String message = null;
                    try {
                        Exception exception = JSONObject.parseObject(checkCeInfoRes.getBody(), Exception.class);
                        message = exception.getMessage();
                    } catch (Exception e) {
                        message = checkCeInfoRes.getBody();
                    }
                    logger.error("check CE info for order total amount error!  ce id is " + watsonsPreRequestOrderDTO.getCeId());
                    throw new CommonException("检验CE号" + watsonsPreRequestOrderDTO.getCeNumber() + "报错," + message);
                }
                logger.info("check CE info for order total amount success! ce id is" + watsonsPreRequestOrderDTO.getCeId());
            }
        }
    }

    private List<PcOccupyDTO> occupyCMS(Long tenantId, List<WatsonsPreRequestOrderDTO> preRequestOrderDTOList) {
        List<PcOccupyDTO> pcOccupyDTOS4Show = new ArrayList<>();
        List<PcOccupyDTO> pcOccupyDTOS4Occupy = new ArrayList<>();
        for (WatsonsPreRequestOrderDTO watsonsPreRequestOrderDTO : preRequestOrderDTOList) {
            pcOccupyDTOS4Occupy.clear();
            //拆单完后的每个订单的所有商品的费用分配不一样  但是放一起做cms校验  所以每个订单所有的商品校验一次
            // 取到该订单所有商品
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
                    if (!ObjectUtils.isEmpty(productDTO.getSellPrice())) {
                        BigDecimal quantity = watsonsShoppingCartDTO.getQuantity();
                        BigDecimal includeTaxPriceParam = productDTO.getSellPrice().multiply(quantity);
                        includeTaxPrice = includeTaxPrice.add(includeTaxPriceParam);
                    }
                    if (productDTO.getLadderEnableFlag().equals(1L)) {
                        BigDecimal quantity = watsonsShoppingCartDTO.getQuantity();
                        if (!CollectionUtils.isEmpty(productDTO.getLadderPriceList())) {
                            List<LadderPriceResultDTO> productPoolLadders = productDTO.getLadderPriceList().stream().map(LadderPriceResultDTO::new)
                                    .sorted(Comparator.comparing(LadderPriceResultDTO::getLadderFrom)).collect(Collectors.toList());
                            // 计算阶梯价
                            boolean hasFlag = true;
                            for (LadderPriceResultDTO productPoolLadder : productPoolLadders) {
                                if (productPoolLadder.getLadderFrom().compareTo(quantity) <= 0 && (ObjectUtils.isEmpty(productPoolLadder.getLadderTo()) || productPoolLadder.getLadderTo().compareTo(quantity) > 0)) {
                                    BigDecimal includeTaxPriceParam = productPoolLadder.getSalePrice().multiply(quantity);
                                    includeTaxPrice = includeTaxPrice.add(includeTaxPriceParam);
                                    hasFlag = false;
                                    break;
                                }
                            }
                            if (hasFlag) {
//                        超出范围取最后一个阶梯的
                                LadderPriceResultDTO productPoolLadder = productPoolLadders.get(productPoolLadders.size() - 1);
                                BigDecimal includeTaxPriceParam = productPoolLadder.getSalePrice().multiply(quantity);
                                includeTaxPrice = includeTaxPrice.add(includeTaxPriceParam);
                            }
                        } else {
                            logger.warn("该商品没有未含税阶梯价!商品编码为:" + productDTO.getProductNum());
                        }
                    }
                    pcOccupyDTO.setOccupyAmount(includeTaxPrice);
                    pcOccupyDTO.setOperationType(WatsonsConstants.operationTypeCode.SPCM_OCCUPY);
                    pcOccupyDTO.setPcNum(watsonsShoppingCartDTO.getCmsNumber());
                    pcOccupyDTO.setVersion(1L);
                    pcOccupyDTOS4Occupy.add(pcOccupyDTO);
                    pcOccupyDTOS4Show.add(pcOccupyDTO);
                }
            }
            //一个订单调用一次  一个订单所有cms商品放一起调用一次
            occupyBySpcmForPreOrder(tenantId, pcOccupyDTOS4Occupy);
        }
        return pcOccupyDTOS4Show;
    }

    private void occupyBySpcmForPreOrder(Long tenantId, List<PcOccupyDTO> pcOccupyDTOS) {
        if(!CollectionUtils.isEmpty(pcOccupyDTOS)){
            String sagaKey = SagaClient.getSagaKey();
            ResponseEntity<String> cmsOccupyResult = spcmRemoteNewService.occupy(sagaKey, tenantId, pcOccupyDTOS);
            if (ResponseUtils.isFailed(cmsOccupyResult)) {
                logger.error("occupy CMS price error! param pcOccupyDTOS: {}", JSONObject.toJSON(pcOccupyDTOS));
                throw new CommonException("CMS金额预占出现异常!");
            }
            ItfBaseBO itfBaseBO  = ResponseUtils.getResponse(cmsOccupyResult, new TypeReference<ItfBaseBO>() {
            });
            if(itfBaseBO.getErrorFlag() == 1 && !ObjectUtils.isEmpty(itfBaseBO.getErrorMessage())){
                logger.error("occupy CMS price error! param pcOccupyDTOS: {}", JSONObject.toJSON(pcOccupyDTOS));
                throw new CommonException("预占CMS合同号报错,错误原因: " + itfBaseBO.getErrorMessage());
            }
            logger.info("occupy CMS price success! param pcOccupyDTOS: {}", JSONObject.toJSON(pcOccupyDTOS));
        }
    }

    private void cancelBySpcmForPreOrder(Long tenantId, List<PcOccupyDTO> pcOccupyDTOS) {
        if(!CollectionUtils.isEmpty(pcOccupyDTOS)){
            String sagaKey = SagaClient.getSagaKey();
            ResponseEntity<String> cmsOccupyResult = spcmRemoteNewService.occupy(sagaKey, tenantId, pcOccupyDTOS);
            if (ResponseUtils.isFailed(cmsOccupyResult)) {
                logger.error("cancel CMS price error! param pcOccupyDTOS: {}", JSONObject.toJSON(pcOccupyDTOS));
                throw new CommonException("CMS金额取消占用出现异常!");
            }
            ItfBaseBO itfBaseBO  = ResponseUtils.getResponse(cmsOccupyResult, new TypeReference<ItfBaseBO>() {
            });
            if(itfBaseBO.getErrorFlag() == 1 && !ObjectUtils.isEmpty(itfBaseBO.getErrorMessage())){
                logger.error("cancel CMS price error! param pcOccupyDTOS: {}", JSONObject.toJSON(pcOccupyDTOS));
                throw new CommonException("CMS取消占用报错,错误原因: " + itfBaseBO.getErrorMessage());
            }
            logger.info("cancel CMS price success! param pcOccupyDTOS: {}", JSONObject.toJSON(pcOccupyDTOS));
        }
    }

    private void saveCeAndCMS(List<WatsonsPreRequestOrderDTO> preRequestOrderDTOList) {
        for (WatsonsPreRequestOrderDTO watsonsPreRequestOrderDTO : preRequestOrderDTOList) {
            if (!ObjectUtils.isEmpty(watsonsPreRequestOrderDTO.getCeNumber())) {
                for (WatsonsShoppingCartDTO watsonsShoppingCartDTO : watsonsPreRequestOrderDTO.getWatsonsShoppingCartDTOList()) {
                    for (AllocationInfo allocationInfo : watsonsShoppingCartDTO.getAllocationInfoList()) {
                        allocationInfo.setCeNumber(watsonsPreRequestOrderDTO.getCeNumber());
                        if (!ObjectUtils.isEmpty(watsonsPreRequestOrderDTO.getDiscription())) {
                            allocationInfo.setCeDiscription(watsonsPreRequestOrderDTO.getDiscription());
                        }
                        allocationInfoRepository.updateByPrimaryKeySelective(allocationInfo);
                    }
                }
            }
        }
    }

    private ItemCategoryDTO queryItemCategoryInfoById(Long tenantId, Long itemCategoryId) {
        if (ObjectUtils.isEmpty(itemCategoryId)) {
            throw new CommonException("没有拿到商品的物料品类信息，无法校验工作流!");
        }
        ResponseEntity<String> resString = smdmRemoteNewService.queryById(tenantId, itemCategoryId.toString());
        if (ResponseUtils.isFailed(resString)) {
            logger.error("主数据中心异常：查询商品物料品类信息失败，无法校验工作流!");
            throw new CommonException("主数据中心异常：查询商品物料品类信息失败，无法校验工作流!");
        } else {
            logger.info("query item category info success!");
            ItemCategoryDTO response = ResponseUtils.getResponse(resString, new TypeReference<ItemCategoryDTO>() {
            });
            return response;
        }
    }

    private Integer checkLevelOfItemCategory(Long tenantId, Long itemCategoryId) {
        if (ObjectUtils.isEmpty(itemCategoryId)) {
            throw new CommonException("没有拿到商品的物料品类信息，无法校验工作流! ");
        }
        ResponseEntity<String> stringResponseEntity = smdmRemoteNewService.queryById(tenantId, itemCategoryId.toString());
        if (ResponseUtils.isFailed(stringResponseEntity)) {
            logger.error("主数据中心异常：查询商品物料品类信息失败，无法校验工作流!");
            throw new CommonException("主数据中心异常：查询商品物料品类信息失败，无法校验工作流!");
        } else {
            logger.info("query item category info success!");
            ItemCategoryDTO response = ResponseUtils.getResponse(stringResponseEntity, new TypeReference<ItemCategoryDTO>() {
            });
            return response.getLevelPath().split("\\|").length;
        }
    }

    private List<WatsonsWflCheckDTO> buildWflCheckParams(Long tenantId, List<WatsonsPreRequestOrderDTO> canSubmitList) {
        List<WatsonsWflCheckDTO> watsonsWflCheckDTOS = new ArrayList<>();
        for (WatsonsPreRequestOrderDTO watsonsPreRequestOrderDTO : canSubmitList) {
            for (WatsonsShoppingCartDTO watsonsShoppingCartDTO : watsonsPreRequestOrderDTO.getWatsonsShoppingCartDTOList()) {
                Long firstItemCategoryId = null;
                Integer level = null;
                Long id = watsonsShoppingCartDTO.getItemCategoryId();
                level = checkLevelOfItemCategory(tenantId, id);
                while (level > 2) {
                    Integer levelRes = checkLevelOfItemCategory(tenantId, id);
                    level = levelRes;
                    ItemCategoryDTO itemCategoryDTO = queryItemCategoryInfoById(tenantId, id);
                    id = itemCategoryDTO.getParentCategoryId();
                }
                firstItemCategoryId = id;
                WatsonsWflCheckDTO watsonsWflCheckDTO = new WatsonsWflCheckDTO();
                if (ObjectUtils.isEmpty(firstItemCategoryId)) {
                    logger.error("未映射该商品的一级品类{}", JSONObject.toJSON(watsonsShoppingCartDTO));
                    throw new CommonException("未映射该商品的一级品类{}", JSONObject.toJSON(watsonsShoppingCartDTO));
                }
                watsonsWflCheckDTO.setCategoryId(firstItemCategoryId);
                List<String> costShopCodes = watsonsShoppingCartDTO.getAllocationInfoList().stream().map(AllocationInfo::getCostShopCode).collect(Collectors.toList());
                watsonsWflCheckDTO.setStoreIdList(costShopCodes);
                watsonsWflCheckDTOS.add(watsonsWflCheckDTO);
            }
        }
        return watsonsWflCheckDTOS;
    }


    @Override
    public List<WatsonsAddressDTO> checkAddress(Long organizationId, Long watsonsOrganizationId, String watsonsOrganizationCode) {

        if (ObjectUtils.isEmpty(watsonsOrganizationId) && ObjectUtils.isEmpty(watsonsOrganizationCode)) {
            throw new CommonException("仓转店或店铺的id和编码都为空, 无法根据仓转店或店铺自动带出详细地址和地址区域!");
        }
        //优先用id查
        if (!ObjectUtils.isEmpty(watsonsOrganizationId)) {
            logger.info("当前正在使用id查询详细地址和地址区域!");
            List<WatsonsAddressDTO> watsonsAddressDTOS = new ArrayList<>();
            //找到地址表信息迁移到watsonsAddress
            List<Address> addressList = addressRepository.selectByCondition(Condition.builder(Address.class).andWhere(
                    Sqls.custom().andEqualTo(Address.FIELD_TENANTID_ID, organizationId).andEqualTo(Address.FIELD_ADDRESS_TYPE, ScecConstants.AdressType.RECEIVER)
                            .andEqualTo(Address.FIELD_INV_ORGANIZATION_ID, watsonsOrganizationId)).build());
            if (!CollectionUtils.isEmpty(addressList)) {
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
            } else {
                WhLovResultDTO infoDTO = allocationInfoRepository.selectInvInfoByInvId(watsonsOrganizationId, organizationId);
                logger.error(infoDTO.getInventoryCode() + "-" + infoDTO.getInventoryName() + "的相关地址信息不存在，请手工补充收货地址!");
                throw new CommonException(infoDTO.getInventoryCode() + "-" + infoDTO.getInventoryName() + "的相关地址信息不存在，请手工补充收货地址!");
            }
        }

        //id没有用code查
        if (!ObjectUtils.isEmpty(watsonsOrganizationCode)) {
            logger.info("当前正在使用code查询详细地址和地址区域!");
            List<WatsonsAddressDTO> watsonsAddressDTOS = new ArrayList<>();
            //找到地址表信息迁移到watsonsAddress
            //hpfm通过code找到id
            AddressDTO addressDTO = allocationInfoRepository.selectIdByCode(organizationId, watsonsOrganizationCode);
            if (!ObjectUtils.isEmpty(addressDTO.getInvOrganizationId())) {
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
                } else {
                    AddressDTO infoDTO = allocationInfoRepository.selectIdByCode(organizationId, watsonsOrganizationCode);
                    logger.error(infoDTO.getInvOrganizationCode() + "-" + infoDTO.getInvOrganizationName() + "的相关地址信息不存在，请手工补充收货地址!");
                    throw new CommonException(infoDTO.getInvOrganizationCode() + "-" + infoDTO.getInvOrganizationName() + "的相关地址信息不存在，请手工补充收货地址!");
                }
            } else {
                logger.warn("该库存组织code没有找到库存组织id!");
                throw new CommonException("在查询地址区域和详细地址时用到的库存组织编码表中没有对应的库存组织id!该编码为" + watsonsOrganizationCode);
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
            String address4Check = value.get(0).getAddressRegion() + value.get(0).getFullAddress();
            for (AllocationInfo allocationInfo : value) {
                if (!((allocationInfo.getAddressRegion() + allocationInfo.getFullAddress()).equals(address4Check))) {
                    throw new CommonException(
                            allocationInfo.getFromWhichShoppingCart() + allocationInfo.getCostShopCode() + allocationInfo.getCostShopName() + "分配的地址不一致，请修改!");
                }
            }
        }
        return null;
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
            BigDecimal quantity = shoppingCart.getQuantity();
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
        //校验每个商品的每个费用分配当【费用承担写字楼/店铺/仓库】相同时,【地址区域】+【收货地址】是否相同
        checkAddressRegionAndFullAddress(watsonsShoppingCartDTOList);
        //定制品校验计价属性
        checkCustomizedProductInfoForWatsons(tenantId, watsonsShoppingCartDTOList);
        //如果有服务商品，从底层list取出放到上层list
        List<ShoppingCartDTO> re = new ArrayList<>();
        //获取名片分类的categoryId
        SkuCenterQueryDTO skuCenterQueryDTO = new SkuCenterQueryDTO();
        skuCenterQueryDTO.setCategoryCodeList(Collections.singletonList(BUSINESS_CARD_CATEGORY_CODE));
        SkuCenterResultDTO<List<Category>> response = ResponseUtils.getResponse(smpcRemoteService.queryCategoryInfo(tenantId, skuCenterQueryDTO), new TypeReference<SkuCenterResultDTO<List<Category>>>() {
        });
        List<Category> categories = response.isSuccess() ? response.getResult() : null;
        Long businessCardCategoryId = -1L;
        if (!CollectionUtils.isEmpty(categories)) {
//            businessCardCategoryId = businessCardCategory.getId();
            businessCardCategoryId = categories.get(0).getCategoryId();
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
            BigDecimal number = shoppingCartDTO.getQuantity().divide(shoppingCartDTO.getMinPackageQuantity(), 10, BigDecimal.ROUND_HALF_UP);
            if (new BigDecimal(number.intValue()).compareTo(number) != 0) {
                throw new CommonException("不满足最小包装量" + shoppingCartDTO.getMinPackageQuantity() + "的整数倍");
            }
            if (businessCardCategoryId.equals(shoppingCartDTO.getCid())) {
                shoppingCartDTO.setBusinessCardFlag(1);
            }
            shoppingCartDTO.setAgreementLineId(priceResultDTO.getPurAgreementLineId());
            if (!ObjectUtils.isEmpty(shoppingCartDTO.getCatalogId())) {
                //校验目录价格限制
                BigDecimal priceLimit = ResponseUtils.getResponse(smpcRemoteService.queryPriceLimit(tenantId, new org.srm.mall.common.feign.dto.product.CatalogPriceLimit(shoppingCartDTO.getTenantId(), shoppingCartDTO.getOwnerId(), shoppingCartDTO.getProductId(), shoppingCartDTO.getCatalogId(), null)), BigDecimal.class);
//                BigDecimal priceLimit = catalogPriceLimitService.priceLimit(new CatalogPriceLimit(shoppingCartDTO.getTenantId(), shoppingCartDTO.getOwnerId(), shoppingCartDTO.getProductId(), shoppingCartDTO.getCatalogId(), null));
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
            refreshInvOrganizationAndAddress(watsonsShoppingCartDTOList);
            PurReqMergeRule purReqMergeRule = PurReqMergeRule.getDefaultMergeRule();
            getPostageInfo(tenantId, watsonsShoppingCartDTOList);
            splitShoppingCartByCostConfig(watsonsShoppingCartDTOList);
            //此时shoppingCartDTOList已经有   每一个商品根据自己的多个费用条拆成的多个订单行
            Map<String, List<WatsonsShoppingCartDTO>> result = watsonsShoppingCartDTOList.stream().collect(Collectors.groupingBy(s -> s.groupKey(purReqMergeRule)));
            checkNeedToSplitByFreightType(watsonsShoppingCartDTOList, purReqMergeRule);
            result = watsonsGroupPurchaseRequest(tenantId, purReqMergeRule, result);
            //拆单完成后判断同一分组是否还有同一种的相同商品（有很多种不同种的商品）拆成不同的单子   因为后续业务需要这样做
            recursionSplitShoppingCart(result);
            //用于前端区分采购申请s
            int distinguishId = 0;
            for (Map.Entry<String, List<WatsonsShoppingCartDTO>> entry : result.entrySet()) {
                WatsonsPreRequestOrderDTO watsonsPreRequestOrderDTO = new WatsonsPreRequestOrderDTO();
                watsonsPreRequestOrderDTO.setKeyForView(entry.getKey());
                List<WatsonsShoppingCartDTO> watsonsShoppingCartDTOList4Trans = entry.getValue();
                List<ShoppingCartDTO> shoppingCartDTO4Freight = new ArrayList<>();

                for (WatsonsShoppingCartDTO watsonsShoppingCartDTO : watsonsShoppingCartDTOList4Trans) {
                    ShoppingCartDTO shoppingCartDTO = new ShoppingCartDTO();
                    BeanUtils.copyProperties(watsonsShoppingCartDTO, shoppingCartDTO);
                    shoppingCartDTO4Freight.add(shoppingCartDTO);
                }
                watsonsPreRequestOrderDTO.setShoppingCartDTOList(shoppingCartDTO4Freight);
                watsonsPreRequestOrderDTO.setDistinguishId(++distinguishId);
                watsonsPreRequestOrderDTO.setCount(entry.getValue().stream().map(WatsonsShoppingCartDTO::getQuantity).reduce(BigDecimal.ZERO, BigDecimal::add));
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
                String addressRegion = watsonsShoppingCartDTOList4Trans.get(0).getAllocationInfoList().get(0).getAddressRegion();
                String fullAddress = watsonsShoppingCartDTOList4Trans.get(0).getAllocationInfoList().get(0).getFullAddress();
                //一个拆好的订单的所有商品行的详细地址+地址区域要一样  所以这里可以取任意一个
                watsonsPreRequestOrderDTO.setReceiverAddress(addressRegion + fullAddress);
                watsonsPreRequestOrderDTO.setWatsonsShoppingCartDTOList(watsonsShoppingCartDTOList4Trans);
                // 订单总价(不含运费)
                BigDecimal price = entry.getValue().stream().map(WatsonsShoppingCartDTO::getTotalPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
                validateMinPurchaseAmount(tenantId, watsonsShoppingCartDTO, price, watsonsPreRequestOrderDTO);
                // 代理运费和代理总价问题处理 运费以采购价来计算
                BigDecimal freightPrice = price;
                if (ScecConstants.AgreementType.SALE.equals(watsonsShoppingCartDTO.getAgreementType())) {
                    //  如果是销售协议 运费需要用采购协议价来计算
                    freightPrice = entry.getValue().stream().map(WatsonsShoppingCartDTO::getPurTotalPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
                }
                queryFreight(tenantId, entry, watsonsPreRequestOrderDTO, freightPrice);
                //小计金额  金额+运费
                watsonsPreRequestOrderDTO.setPrice(price.add(watsonsPreRequestOrderDTO.getFreight()));
                if (ScecConstants.AgreementType.SALE.equals(watsonsShoppingCartDTO.getAgreementType())) {
                    //  如果是销售协议 运费需要用采购协议价来计算
                    watsonsPreRequestOrderDTO.setPurPrice(freightPrice.add(watsonsPreRequestOrderDTO.getFreight()));
                }
                PaymentInfo paymentInfo = shoppingCartRepository.queryPaymentInfo(watsonsShoppingCartDTO);
                if (null != paymentInfo) {
                    BeanConvertor.convert(paymentInfo, watsonsPreRequestOrderDTO);
                }
                watsonsPreRequestOrderDTO.setPreRequestOrderNumber(UUID.randomUUID().toString());
                watsonsPreRequestOrderDTO.setMobile(watsonsShoppingCartDTO.getMobile());
                CustomUserDetails userDetails = DetailsHelper.getUserDetails();
                watsonsPreRequestOrderDTO.setReceiverContactName(userDetails.getRealName());
                watsonsPreRequestOrderDTO.setStoreNo(watsonsShoppingCartDTOList4Trans.get(0).getAllocationInfoList().get(0).getCostShopCode());
                snapshotUtil.saveSnapshot(AbstractKeyGenerator.getKey(ScecConstants.CacheCode.SERVICE_NAME, ScecConstants.CacheCode.PURCHASE_REQUISITION_PREVIEW, watsonsPreRequestOrderDTO.getPreRequestOrderNumber()), watsonsPreRequestOrderDTO.getPreRequestOrderNumber(), watsonsPreRequestOrderDTO, 5, TimeUnit.MINUTES);
                watsonsPreRequestOrderDTOList.add(watsonsPreRequestOrderDTO);
            }
            setCMSInfo(tenantId, watsonsPreRequestOrderDTOList);
            // handleCheck()
            return watsonsPreRequestOrderDTOList;
        }
        return null;
    }
    private void checkCustomizedProductInfoForWatsons(Long tenantId, List<WatsonsShoppingCartDTO> watsonsShoppingCartDTOS) {
        //查询商品计价属性
        List<Long> productIdList = watsonsShoppingCartDTOS.stream().filter(s -> s.getCustomFlag() != null && s.getCustomFlag() == 1).map(ShoppingCartDTO::getProductId).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(productIdList)) {
            return;
        }
        List<SkuCustomDTO> skuCustomList = productWorkbenchRepository.selectSkuListCustomAttrNoException(tenantId, productIdList);
        Map<Long, SkuCustomDTO> skuCustomDTOMap = skuCustomList.stream().collect(Collectors.toMap(SkuCustomDTO::getSkuId, Function.identity(), (k1, k2) -> k1));
        //校验计价属性
        for (WatsonsShoppingCartDTO watsonsShoppingCartDTO : watsonsShoppingCartDTOS) {
            if (watsonsShoppingCartDTO.getCustomFlag() != null && watsonsShoppingCartDTO.getCustomFlag() == 1) {
                SkuCustomDTO skuCustomDTO = skuCustomDTOMap.get(watsonsShoppingCartDTO.getProductId());
                //校验必输字段是否填写
                watsonsShoppingCartDTO.checkCustomizedProductInfo(skuCustomDTO.getSpuCustomGroupList());
                //校验定制品属性是否有变更
                if ((ObjectUtils.isEmpty(skuCustomDTO) && !CollectionUtils.isEmpty(watsonsShoppingCartDTO.getCustomizedProductLineList()))
                        || !ObjectUtils.isEmpty(skuCustomDTO) && CollectionUtils.isEmpty(watsonsShoppingCartDTO.getCustomizedProductLineList())) {
                    throw new CommonException(ScecConstants.ProductCustomized.ERROR_PRODUCT_CUSTOMIZED_CHANGE);
                }
                for (CustomizedProductLine customizedProductLine : watsonsShoppingCartDTO.getCustomizedProductLineList()) {
                    CustomizedProductCheckDTO customizedProductCheckDTO = customizedProductLine.check(skuCustomDTO.getSpuCustomGroupList());
                    customizedProductLineService.updateCustomizedProductInfo(customizedProductCheckDTO);
                    if (customizedProductCheckDTO.getSuccess() == 0) {
                        throw new CommonException(ScecConstants.ProductCustomized.ERROR_PRODUCT_CUSTOMIZED_CHANGE);
                    }
                }
                calculateCustomizedProductForShoppingCartDTO(watsonsShoppingCartDTO);
            }
        }
    }

    private void checkAddressRegionAndFullAddress(List<WatsonsShoppingCartDTO> watsonsShoppingCartDTOList) {
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
            String address4Check = value.get(0).getAddressRegion() + value.get(0).getFullAddress();
            for (AllocationInfo allocationInfo : value) {
                if (!((allocationInfo.getAddressRegion() + allocationInfo.getFullAddress()).equals(address4Check))) {
                    throw new CommonException(
                            "商品" + value.get(0).getFromWhichShoppingCart() + "的" + value.get(0).getCostShopCode() + value.get(0).getCostShopName() +
                                    "与商品" + allocationInfo.getFromWhichShoppingCart() + "的" + allocationInfo.getCostShopCode() + allocationInfo.getCostShopName() + "分配的地址不一致，请修改!");
                }
            }
        }
    }

    private void queryFreight(Long tenantId, Map.Entry<String, List<WatsonsShoppingCartDTO>> entry, WatsonsPreRequestOrderDTO watsonsPreRequestOrderDTO, BigDecimal freightPrice) {
        watsonsPreRequestOrderDTO.setFreight(BigDecimal.ZERO);
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
        } else if(ScecConstants.SourceType.CATALOGUE.equals(entry.getValue().get(0).getProductSource())){
            //目录化商品
            this.orderFreight(tenantId, watsonsPreRequestOrderDTO);
        }else {
            processWatsonsEcFreight(tenantId, entry.getValue(), watsonsPreRequestOrderDTO);
        }
    }

    private void processWatsonsEcFreight(Long tenantId, List<WatsonsShoppingCartDTO> shoppingCartDTOS, WatsonsPreRequestOrderDTO preRequestOrderDTO) {
        logger.info("using yb freight");
        //其余电商查电商平台定义决定是否调用接口
        List<EcPlatform> ecPlatforms = ecPlatformRepository.selectByCondition(Condition.builder(EcPlatform.class).andWhere(Sqls.custom()
                .andEqualTo(EcPlatform.FIELD_EC_PLATFORM_CODE, shoppingCartDTOS.get(0).getProductSource())).build());
        if(ScecConstants.Flags.ENABLE_FLAG == (ecPlatforms.get(0).getFreightQueryEnabled())){
            String countyId = null;
            String provinceId = null;
            String cityId = null;
            String regionId = null;
            String address = null;
            List<EcSkuInfo> ecSkuInfos = new ArrayList<>();
            Assert.notNull(shoppingCartDTOS.get(0).getRegionLevelPath(),"该电商订单商品没有地址区域，无法查询运费!");
            String[] split = shoppingCartDTOS.get(0).getRegionLevelPath().split("\\.");
            if(split.length<3){
                logger.error("该电商订单商品地址区域不满足三级以上，无法查询运费!,商品信息为{}", JSONObject.toJSON(shoppingCartDTOS));
                throw new CommonException("该电商订单商品地址区域不满足三级以上，无法查询运费!,商品信息为{}", JSONObject.toJSON(shoppingCartDTOS));
            }else{
                List<MallRegion> province = mallRegionRepository.selectByCondition(Condition.builder(MallRegion.class).andWhere(Sqls.custom()
                        .andEqualTo(MallRegion.FIELD_ENABLED_FLAG, 1)
                        .andEqualTo(MallRegion.FIELD_REGION_CODE, split[0])).build());
                provinceId = province.get(0).getRegionCode();
                List<MallRegion> city = mallRegionRepository.selectByCondition(Condition.builder(MallRegion.class).andWhere(Sqls.custom()
                        .andEqualTo(MallRegion.FIELD_ENABLED_FLAG, 1)
                        .andEqualTo(MallRegion.FIELD_REGION_CODE, split[1])).build());
                cityId = city.get(0).getRegionCode();
                List<MallRegion> region = mallRegionRepository.selectByCondition(Condition.builder(MallRegion.class).andWhere(Sqls.custom()
                        .andEqualTo(MallRegion.FIELD_ENABLED_FLAG, 1)
                        .andEqualTo(MallRegion.FIELD_REGION_CODE, split[2])).build());
                regionId =  region.get(0).getRegionCode();
                address += province.get(0).getRegionName()+city.get(0).getRegionName()+region.get(0).getRegionName();
                if(split.length > 3){
                    List<MallRegion> county = mallRegionRepository.selectByCondition(Condition.builder(MallRegion.class).andWhere(Sqls.custom()
                            .andEqualTo(MallRegion.FIELD_ENABLED_FLAG, 1)
                            .andEqualTo(MallRegion.FIELD_REGION_CODE, split[3])).build());
                    countyId =  county.get(0).getRegionCode();
                    address+=county.get(0).getRegionName();
                }
            }
            if (!ObjectUtils.isEmpty(provinceId) && !ObjectUtils.isEmpty(cityId) && !ObjectUtils.isEmpty(regionId) && !ObjectUtils.isEmpty(address)) {
                logger.info("the full address is {}",JSONObject.toJSON(address));
                FreightPriceDto freightPriceDto = new FreightPriceDto();
                freightPriceDto.setSupplierCode(preRequestOrderDTO.getShoppingCartDTOList().get(0).getProductSource());
                freightPriceDto.setTenantId(tenantId);
                freightPriceDto.setProvinceId(provinceId);
                freightPriceDto.setCityId(cityId);
                freightPriceDto.setCountyId(regionId);
                freightPriceDto.setAddress(address);
                List<Long> skuIds = new ArrayList<>();
                shoppingCartDTOS.forEach(c->{skuIds.add(c.getProductId());});
                List<SkuBaseInfoDTO> skuBaseInfoDTOS = productWorkbenchRepository.querySkuBaseInfoList(tenantId, skuIds);
                for (ShoppingCartDTO cartDTO : shoppingCartDTOS) {
                    skuBaseInfoDTOS.forEach(skuBaseInfoDTO -> {
                        if(skuBaseInfoDTO.getSkuId().equals(cartDTO.getProductId())){
                            EcSkuInfo ecSkuInfo = new EcSkuInfo();
                            ecSkuInfo.setSkuId(skuBaseInfoDTO.getThirdSkuCode());
                            ecSkuInfo.setSkuNum(cartDTO.getQuantity().intValue());
                            ecSkuInfos.add(ecSkuInfo);
                        }
                    });
                }
                freightPriceDto.setSkuInfos(ecSkuInfos);
                logger.info("the ec freigth param is {}", JSONObject.toJSON(freightPriceDto));
                ResponseEntity<String> stringResponseEntity = sifgOrderRemoteService.getFreightPrice(freightPriceDto);
                if(ResponseUtils.isFailed(stringResponseEntity)){
                    logger.error("电商运费异常,原因为{}",JSON.toJSONString(stringResponseEntity));
                    throw new CommonException("电商运费异常,"+JSON.toJSONString(stringResponseEntity));
                }else {
                    ECResult<String> response = ResponseUtils.getResponse(stringResponseEntity, new TypeReference<ECResult<String>>() {
                    });
                    logger.info("电商查询运费成功,运费为"+JSONObject.toJSON(response));
                    BigDecimal includeTaxFreight = BigDecimal.valueOf(Long.parseLong(response.getResult()));
                    preRequestOrderDTO.setFreight(includeTaxFreight);
                    BigDecimal withoutTaxFreightPrice = includeTaxFreight.subtract(includeTaxFreight.multiply(new BigDecimal("0.06")));
                    preRequestOrderDTO.setWithoutTaxFreightPrice(withoutTaxFreightPrice);
                }
            }else {
                logger.error("该电商商品地址映射不全，请补全该商品地址区域再查运费! 商品为{}",JSONObject.toJSON(shoppingCartDTOS));
                throw new CommonException("该电商商品地址映射不全，请补全该商品地址区域再查运费!");
            }
        }
    }

    private void setCMSInfo(Long tenantId, List<WatsonsPreRequestOrderDTO> watsonsPreRequestOrderDTOList) {
        //        进行cms合同号取值
        watsonsPreRequestOrderDTOList.stream().forEach(watsonsPreRequestOrderDTO -> {
            for (WatsonsShoppingCartDTO watsonsShoppingCartDTO : watsonsPreRequestOrderDTO.getWatsonsShoppingCartDTOList()) {
                logger.info("开始调用协议中心查询cms号码");
                ResponseEntity<String> stringResponseEntity = watsonsSagmRemoteService.queryAgreementLineById(tenantId, watsonsShoppingCartDTO.getAgreementLineId());
                if (ResponseUtils.isFailed(stringResponseEntity)) {
                    logger.error("调用协议中心查询cms合同号异常!");
                } else {
                    AgreementLine agreementLine = ResponseUtils.getResponse(stringResponseEntity, new TypeReference<AgreementLine>() {
                    });
                    //attributeVarchar1是cms合同号
                    if (ObjectUtils.isEmpty(agreementLine)) {
                        logger.error(watsonsShoppingCartDTO.getProductName() + "没有查询到该商品的协议行!");
                    }
                    if (!ObjectUtils.isEmpty(agreementLine) && ObjectUtils.isEmpty(agreementLine.getAttributeVarchar1())) {
                        logger.error(watsonsShoppingCartDTO.getProductName() + "没有查询到该商品的CMS合同号!");
                    }
                    if (!ObjectUtils.isEmpty(agreementLine) && !ObjectUtils.isEmpty(agreementLine.getAttributeVarchar1())) {
                        watsonsShoppingCartDTO.setCmsNumber(agreementLine.getAttributeVarchar1());
                    }
                    if (!ObjectUtils.isEmpty(agreementLine) && !ObjectUtils.isEmpty(agreementLine.getAttributeVarchar2())) {
                        //发票类型
                        watsonsShoppingCartDTO.setAttributeVarchar3(agreementLine.getAttributeVarchar2());
                    }
                }
            }
        });
    }
    private void refreshInvOrganizationAndAddress(List<WatsonsShoppingCartDTO> watsonsShoppingCartDTOList) {
        //先把addressId和ouid赋值成一样 防止影响拆单
        for (WatsonsShoppingCartDTO watsonsShoppingCartDTO : watsonsShoppingCartDTOList) {
            watsonsShoppingCartDTO.setAddressId(watsonsShoppingCartDTOList.get(0).getAddressId());
            watsonsShoppingCartDTO.setInvOrganizationId(watsonsShoppingCartDTOList.get(0).getInvOrganizationId());
            watsonsShoppingCartDTO.setOuId(watsonsShoppingCartDTOList.get(0).getOuId());
        }
    }

    private void checkNeedToSplitByFreightType(List<WatsonsShoppingCartDTO> shoppingCartDTOList, PurReqMergeRule purReqMergeRule) {
        for (ShoppingCartDTO shoppingCartDTO : shoppingCartDTOList) {
            logger.info("the postage info for each shoppingcart is {}", JSONObject.toJSON(shoppingCartDTO.getFreightPricingMethod() + "-" + shoppingCartDTO.getVolumeUnitPrice()));
            if (!ObjectUtils.isEmpty(shoppingCartDTO.getVolumeUnitPrice()) && ScecConstants.CacheCode.ACTUAL_CALCULATION.equals(shoppingCartDTO.getFreightPricingMethod())) {
                purReqMergeRule.setFreightType(BaseConstants.Flag.YES);
                break;
            } else {
                purReqMergeRule.setFreightType(BaseConstants.Flag.NO);
            }
        }
    }

    /**
     * 在商品行上加入计价方式和单位体积价格
     *
     * @param tenantId
     * @param shoppingCartDTOList
     */
    private void getPostageInfo(Long tenantId, List<WatsonsShoppingCartDTO> shoppingCartDTOList) {
        //封装查询条件
        Map<Long, List<ShoppingCartDTO>> cartByAddressId = shoppingCartDTOList.stream().collect(Collectors.groupingBy(ShoppingCartDTO::getAddressId));
        List<PostageCalculateDTO> postageCalculateDTOS = buildPostageInfoParamsForShoppingCart(shoppingCartDTOList, cartByAddressId);
        logger.info("query freight dto is {}", JSONObject.toJSON(postageCalculateDTOS));
        ResponseEntity<String> queryPostageInfoRes = sagmRemoteService.queryPostageInfo(tenantId, postageCalculateDTOS);
        if (ResponseUtils.isFailed(queryPostageInfoRes)) {
            throw new CommonException("协议服务异常: 暂时无法查询运费信息进行商品行赋值");
        }
        List<PostageCalculateDTO> queryPostageResult = ResponseUtils.getResponse(queryPostageInfoRes, new TypeReference<List<PostageCalculateDTO>>() {
        });
        logger.info("query Postage info result is {}", JSONObject.toJSON(queryPostageResult));
        //每个订单只返回一个运费行
        for (PostageCalculateDTO postageCalculateDTO : queryPostageResult) {
            for (Map.Entry<Long, List<ShoppingCartDTO>> entry : cartByAddressId.entrySet()) {
                if (entry.getKey().equals(postageCalculateDTO.getAddressId())) {
                    entry.getValue().forEach(shoppingCartDTO -> {
                        postageCalculateDTO.getPostageCalculateLineDTOS().forEach(postageCalculateLineDTO -> {
                            if (shoppingCartDTO.getAgreementLineId().equals(postageCalculateLineDTO.getAgreementLineId())) {
                                if (!ObjectUtils.isEmpty(postageCalculateLineDTO.getPostage())) {
                                    //运费计价方式
                                    shoppingCartDTO.setFreightPricingMethod(postageCalculateLineDTO.getPostage().getPricingMethod());
                                    //体积单价
                                    shoppingCartDTO.setVolumeUnitPrice(postageCalculateLineDTO.getPostage().getPostageLine().getVolumeUnitPrice());
                                    //运费税率
                                    shoppingCartDTO.setFreightTaxId(postageCalculateLineDTO.getPostage().getTaxId());
                                    shoppingCartDTO.setFreightTaxCode(postageCalculateLineDTO.getPostage().getTaxCode());
                                    shoppingCartDTO.setFreightTaxRate(new BigDecimal(postageCalculateLineDTO.getPostage().getTaxRate()));
                                    //运费物料
                                    shoppingCartDTO.setFreightItemId(postageCalculateLineDTO.getPostage().getItemId());
                                    shoppingCartDTO.setFreightItemCode(postageCalculateLineDTO.getPostage().getItemCode());
                                    shoppingCartDTO.setFreightItemName(postageCalculateLineDTO.getPostage().getItemName());
                                } else {
                                    logger.info("该商品" + shoppingCartDTO.getProductId() + "查运费时没有返回运费行");
                                }
                            }
                        });
                    });
                }
            }
        }
        logger.info("the shopping carts after built postage {}", JSON.toJSON(shoppingCartDTOList));
    }
    private  List<PostageCalculateDTO>  buildPostageInfoParamsForShoppingCart(List<WatsonsShoppingCartDTO> shoppingCartDTOList, Map<Long, List<ShoppingCartDTO>> cartByAddressId) {
        List<PostageCalculateDTO> postageCalculateDTOS = new ArrayList<>();
        for (Map.Entry<Long, List<ShoppingCartDTO>> cart : cartByAddressId.entrySet()) {
            List<PostageCalculateLineDTO> postageCalculateLineDTOS = new ArrayList<>();
            List<ShoppingCartDTO> shoppingCartDTOS = cart.getValue();
            PostageCalculateDTO postageCalculateDTO = new PostageCalculateDTO();
            //regionId可以不给
            postageCalculateDTO.setAddressId(shoppingCartDTOS.get(0).getAddressId());
            shoppingCartDTOS.forEach(s -> {
                PostageCalculateLineDTO postageCalculateLineDTO = new PostageCalculateLineDTO();
                postageCalculateLineDTO.setAgreementLineId(s.getAgreementLineId());
                postageCalculateLineDTO.setProductSource(s.getProductSource());
                postageCalculateLineDTO.setPurPrice(s.getPurTotalPrice());
                postageCalculateLineDTO.setPrice(s.getTotalPrice());
                postageCalculateLineDTO.setQuantity(s.getQuantity());
                postageCalculateLineDTO.setAgreementType(s.getAgreementType());
                postageCalculateLineDTO.setCartId(s.getCartId());
                postageCalculateLineDTOS.add(postageCalculateLineDTO);
            });
            postageCalculateDTO.setPostageCalculateLineDTOS(postageCalculateLineDTOS);
            postageCalculateDTOS.add(postageCalculateDTO);
        }
        return postageCalculateDTOS;
    }

    /**
     * 处理订单运费
     */
    private void orderFreight(Long tenantId,WatsonsPreRequestOrderDTO watsonsPreRequestOrderDTO){
        BigDecimal withoutTaxFreightPrice = BigDecimal.ZERO;
        List<PostageCalculateDTO> postageCalculateDTOS = buildPostageInfoParamsForPreReq(watsonsPreRequestOrderDTO);
        ResponseEntity<String> calculatePostageRes = sagmRemoteService.freightCalculateNew(tenantId, postageCalculateDTOS);
        if (ResponseUtils.isFailed(calculatePostageRes)) {
            throw new CommonException("协议服务异常: 暂时无法查询运费");
        }
        List<PostageCalculateDTO> calculatePostage = ResponseUtils.getResponse(calculatePostageRes, new TypeReference<List<PostageCalculateDTO>>() {
        });
        logger.info("calculate freight result is {}", JSONObject.toJSON(calculatePostage));
        watsonsPreRequestOrderDTO.setFreight(calculatePostage.get(0).getFreightPrice());
        logger.info("calculate without tax freight result is {}", JSONObject.toJSON(calculatePostage.get(0).getWithoutTaxFreightPrice()));
        withoutTaxFreightPrice = calculatePostage.get(0).getWithoutTaxFreightPrice();
        if(ObjectUtils.isEmpty(calculatePostage.get(0).getWithoutTaxFreightPrice())){
            withoutTaxFreightPrice = BigDecimal.ZERO;
        }
        watsonsPreRequestOrderDTO.setWithoutTaxFreightPrice(withoutTaxFreightPrice);
    }

    private List<PostageCalculateDTO> buildPostageInfoParamsForPreReq(WatsonsPreRequestOrderDTO preRequestOrderDTO) {
        List<PostageCalculateDTO> postageCalculateDTOS = new ArrayList<>();
        List<PostageCalculateLineDTO> postageCalculateLineDTOS = new ArrayList<>();
        PostageCalculateDTO postageCalculateDTO = new PostageCalculateDTO();
        Long lastRegionId = preRequestOrderDTO.getWatsonsShoppingCartDTOList().get(0).getAllocationInfoList().get(0).getLastRegionId();
        processSecondRegionIdForWatsons(postageCalculateDTO, lastRegionId);
        preRequestOrderDTO.getShoppingCartDTOList().forEach(s -> {
            PostageCalculateLineDTO postageCalculateLineDTO = new PostageCalculateLineDTO();
            postageCalculateLineDTO.setAgreementLineId(s.getAgreementLineId());
            postageCalculateLineDTO.setProductSource(s.getProductSource());
            postageCalculateLineDTO.setPurPrice(s.getPurTotalPrice());
            postageCalculateLineDTO.setPrice(s.getTotalPrice());
            postageCalculateLineDTO.setQuantity(s.getQuantity());
            postageCalculateLineDTO.setAgreementType(s.getAgreementType());
            postageCalculateLineDTO.setCartId(s.getCartId());
            postageCalculateLineDTOS.add(postageCalculateLineDTO);
        });
        postageCalculateDTO.setPostageCalculateLineDTOS(postageCalculateLineDTOS);
        postageCalculateDTOS.add(postageCalculateDTO);
        return postageCalculateDTOS;
    }

    private void processSecondRegionIdForWatsons(PostageCalculateDTO postageCalculateDTO, Long lastRegionId) {
        logger.info("the last region id is" + lastRegionId);
        List<MallRegion> region = mallRegionRepository.selectByCondition(Condition.builder(MallRegion.class).andWhere(Sqls.custom()
                .andEqualTo(MallRegion.FIELD_ENABLED_FLAG, 1)
                .andEqualTo(MallRegion.FIELD_REGION_ID, lastRegionId)).build());
        MallRegion param = region.get(0);
        if (param.getLevelPath().split("\\.").length < 2) {
            throw new CommonException("该商品费用分配行上地址已经是一级地址，无法计算运费!");
        } else if (param.getLevelPath().split("\\.").length == 2) {
            logger.info("the second region id is " + param.getRegionId());
            postageCalculateDTO.setRegionId(param.getRegionId());
        } else {
            while (param.getLevelPath().split("\\.").length > 2) {
                List<MallRegion> temp = mallRegionRepository.selectByCondition(Condition.builder(MallRegion.class).andWhere(Sqls.custom()
                        .andEqualTo(MallRegion.FIELD_ENABLED_FLAG, 1)
                        .andEqualTo(MallRegion.FIELD_REGION_ID, param.getRegionId())).build());
                List<MallRegion> temp_2 = mallRegionRepository.selectByCondition(Condition.builder(MallRegion.class).andWhere(Sqls.custom()
                        .andEqualTo(MallRegion.FIELD_ENABLED_FLAG, 1)
                        .andEqualTo(MallRegion.FIELD_REGION_CODE, temp.get(0).getParentRegionCode())).build());
                param = temp_2.get(0);
            }
            logger.info("the second region id is " + param.getRegionId());
            postageCalculateDTO.setRegionId(param.getRegionId());
        }
    }

    private void splitShoppingCartByCostConfig(List<WatsonsShoppingCartDTO> watsonsShoppingCartDTOList) {
        //所有商品按所有的费用分配拆行  每个费用分配对应一个定制品行 每个newShoppingCart一个费用分配一个定制品行
        //前端pre-req只传了一个定制品行
        List<WatsonsShoppingCartDTO> splitCosttInfoList = new ArrayList<>();
        Iterator<WatsonsShoppingCartDTO> it = watsonsShoppingCartDTOList.iterator();
        //还是按费用分配行分散 但是以定制品的价格为准 不以商品行的价格为准
        while (it.hasNext()) {
            WatsonsShoppingCartDTO watsonsShoppingCartDTO = it.next();
            List<AllocationInfo> allocationInfoList = watsonsShoppingCartDTO.getAllocationInfoList();
            if (!CollectionUtils.isEmpty(allocationInfoList) && allocationInfoList.size() > 1) {
                for (int i = 0; i < allocationInfoList.size(); i++) {
                    WatsonsShoppingCartDTO newWatsonsShoppingCartDTO = new WatsonsShoppingCartDTO();
                    BeanUtils.copyProperties(watsonsShoppingCartDTO, newWatsonsShoppingCartDTO);
                    newWatsonsShoppingCartDTO.setQuantity(new BigDecimal(allocationInfoList.get(i).getQuantity()));
                    newWatsonsShoppingCartDTO.setAllocationInfoList(Collections.singletonList(allocationInfoList.get(i)));
                    newWatsonsShoppingCartDTO.setTotalPrice(ObjectUtils.isEmpty(newWatsonsShoppingCartDTO.getLatestPrice()) ? BigDecimal.ZERO : newWatsonsShoppingCartDTO.getLatestPrice().multiply(newWatsonsShoppingCartDTO.getQuantity()));
                    //定制品数据设置
                    if (newWatsonsShoppingCartDTO.getCustomFlag() != null && newWatsonsShoppingCartDTO.getCustomFlag() == 1 && !CollectionUtils.isEmpty(newWatsonsShoppingCartDTO.getCustomizedProductLineList())){
                        //如果是开启了计价属性
                        CustomizedProductLine check = newWatsonsShoppingCartDTO.getCustomizedProductLineList().get(0);
                        if (check.getShipperFlag() == 1){
                            CustomizedProductLine customizedProductLine = newWatsonsShoppingCartDTO.getCustomizedProductLineList().get(i);
                            customizedProductLine.setLatestPrice(newWatsonsShoppingCartDTO.getLatestPrice());
                            allocationInfoService.calculateForCpLine(customizedProductLine);
                            newWatsonsShoppingCartDTO.setTotalPrice(ObjectUtils.isEmpty(customizedProductLine.getCpAmount()) ? BigDecimal.ZERO : customizedProductLine.getCpAmount());
                            newWatsonsShoppingCartDTO.setCustomizedProductLineList(Collections.singletonList(customizedProductLine));
                        } else {
                            //没开启计价属性
                            newWatsonsShoppingCartDTO.setCustomizedProductLineList( i < newWatsonsShoppingCartDTO.getCustomizedProductLineList().size() ? Collections.singletonList(newWatsonsShoppingCartDTO.getCustomizedProductLineList().get(i)) : new ArrayList<>());
                        }
                    }
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
        BigDecimal totalPrice = ObjectUtils.isEmpty(priceResultDTO.getSellPrice()) ? BigDecimal.ZERO : (priceResultDTO.getSellPrice().multiply(shoppingCartDTO.getQuantity()));
        //punchout 不计算价格
        if (punchoutService.isPuhchout(shoppingCartDTO.getProductSource())) {
            return;
        }
        //开启了定制品计价属性的商品计算价格
        if (shoppingCartDTO.getCustomFlag() != null && shoppingCartDTO.getCustomFlag() == 1 && shoppingCartDTO.getShipperFlag() != null && shoppingCartDTO.getShipperFlag() == 1){
            totalPrice = ObjectUtils.isEmpty(priceResultDTO.getSellPrice()) ? BigDecimal.ZERO : (priceResultDTO.getSellPrice().multiply(shoppingCartDTO.getTotalCqNum()));
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
        BigDecimal proxyTotalPrice = ObjectUtils.isEmpty(shoppingCartDTO.getPurLastPrice()) ? BigDecimal.ZERO : (shoppingCartDTO.getPurLastPrice().multiply(shoppingCartDTO.getQuantity()));
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
        //可能存在拆完单同商品id在一个单子里 不允许 人为分开
        Map<String, List<WatsonsShoppingCartDTO>> splitResultMap = new HashMap<>();
        List<WatsonsShoppingCartDTO> nonCustomizedProductList = new ArrayList<>();
        List<String> removeKeyList = new ArrayList<>();
        logger.info("the all values are {}",JSONObject.toJSON(result));
        for (Map.Entry<String, List<WatsonsShoppingCartDTO>> entry : result.entrySet()) {
            logger.info("the entry values are {}",JSONObject.toJSON(entry.getValue()));
            Set<WatsonsShoppingCartDTO> set = new TreeSet<>(Comparator.comparing(WatsonsShoppingCartDTO::getProductId));
            nonCustomizedProductList = entry.getValue().stream().filter(watsonsShoppingCartDTO -> {
                    return !(watsonsShoppingCartDTO.getCustomFlag() != null && watsonsShoppingCartDTO.getCustomFlag().equals(ScecConstants.ConstantNumber.INT_1));
                }).collect(Collectors.toList());
            logger.info("the nonCustomizedProductList are {}",JSONObject.toJSON(nonCustomizedProductList));
            if(CollectionUtils.isEmpty(nonCustomizedProductList)){
                logger.info("all customized product");
                continue;
            }
            set.addAll(nonCustomizedProductList);
            //没有重复的商品，拆单成功
            if (set.size() == nonCustomizedProductList.size()) {
                logger.info("the set size is equal  nonCustomizedProductList size ");
                splitResultMap.put(entry.getKey(), nonCustomizedProductList);
            } else {
                logger.info("the set size is not equal  nonCustomizedProductList size start filter");
                //有重复的商品，将重复的数据取出，继续拆单，由于该list已经是拆单好之后的list，因此只需要将重复的数据取出，生成一个新的list即可
                //取出所有重复的商品  不止一种商品重复    拿出所有重复商品的id   把两个不同的商品组合在一起  重复的同样的商品不能组合
                Map<Long, List<WatsonsShoppingCartDTO>> map = nonCustomizedProductList.stream().collect(Collectors.groupingBy(WatsonsShoppingCartDTO::getProductId));
                Set<Long> productIdList = nonCustomizedProductList.stream().map(WatsonsShoppingCartDTO::getProductId).filter(Objects::nonNull).collect(Collectors.toSet());
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
                if(nonCustomizedProductList.size() == entry.getValue().size()){
                    removeKeyList.add(entry.getKey());
                }
                logger.info("removeKeyList are {}",JSONObject.toJSON(removeKeyList));
            }
        }
        result.putAll(splitResultMap);
        for (String key : removeKeyList) {
            logger.info("delete keys");
            result.remove(key);
        }
        logger.info("the final result is {}",JSONObject.toJSON(result));
    }


    public Map<String, List<WatsonsShoppingCartDTO>> watsonsGroupPurchaseRequest(Long tenantId, PurReqMergeRule purReqMergeRule, Map<String, List<WatsonsShoppingCartDTO>> groupMap) {

        Map<String, List<WatsonsShoppingCartDTO>> resultMap = new HashMap<>();
        for (String key : groupMap.keySet()) {
            List<WatsonsShoppingCartDTO> watsonsShoppingCartDTOList = groupMap.get(key);
            this.setPurMergeRuleForWatsons(purReqMergeRule);
            if (BaseConstants.Flag.YES.equals(purReqMergeRule.getWarehousing())) {
                for (WatsonsShoppingCartDTO watsonsShoppingCartDTO : watsonsShoppingCartDTOList) {
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
                }
            }
            if (!ObjectUtils.isEmpty(purReqMergeRule)) {
                for (WatsonsShoppingCartDTO watsonsShoppingCartDTO : watsonsShoppingCartDTOList) {
                    StringBuffer keyRes = new StringBuffer();
                    //既没有映射品类,也没有映射物料,报错
                    if (ObjectUtils.isEmpty(watsonsShoppingCartDTO.getItemId()) && ObjectUtils.isEmpty(watsonsShoppingCartDTO.getItemCategoryId())) {
                        throw new CommonException("商品既没有映射物料也没有映射品类,请重新选择商品!");
                    }
                    //电商商品可能没有映射itemId  所以要判断
                    //有itemId查一级品类  正常走流程
                    //没有itemId  用ItemCategoryId去查levelPath
                    //如果是三级的levelPath  证明就是三级品类找一级品类即可
                    //如果是二级的levelPath  证明是二级品类找parentCategoryId即可
                    //如果是一级的品类直接用即可
                    //如果是多级的，直接报错
                    processCheckFirstItemCategoryByItemId(tenantId, purReqMergeRule, watsonsShoppingCartDTO, keyRes);
                    processCheckFirstItemCategoryByItemCategoryId(tenantId, purReqMergeRule, watsonsShoppingCartDTO, keyRes);
                }
                Map<String, List<WatsonsShoppingCartDTO>> result = watsonsShoppingCartDTOList.stream().collect(Collectors.groupingBy(WatsonsShoppingCartDTO::getKey));
                resultMap.putAll(result);
            } else {
                resultMap.put(key, groupMap.get(key));
            }
        }
        return resultMap;
    }

    private void processCheckFirstItemCategoryByItemId(Long tenantId, PurReqMergeRule purReqMergeRule, WatsonsShoppingCartDTO watsonsShoppingCartDTO, StringBuffer keyRes) {
        //如果有itemId
        //包括单独有itemId 或者 有两个
        if (!ObjectUtils.isEmpty(watsonsShoppingCartDTO.getItemId())) {
            ResponseEntity<String> responseOne = smdmRemoteService.selectCategoryByItemId(tenantId, watsonsShoppingCartDTO.getItemId(), BaseConstants.Flag.YES);
            if (ResponseUtils.isFailed(responseOne)) {
                logger.error("selectCategoryByItemId error:{}", JSONObject.toJSON(responseOne));
                throw new CommonException("根据物料查询一级品类失败!");
            }
            List<WatsonsItemCategoryDTO> itemCategoryResultOne = ResponseUtils.getResponse(responseOne, new TypeReference<List<WatsonsItemCategoryDTO>>() {
            });
            if (CollectionUtils.isEmpty(itemCategoryResultOne)) {
                logger.error("selectCategoryByItemId error:{}", JSONObject.toJSON(itemCategoryResultOne));
                throw new CommonException("根据物料查询一级品类为空!");
            }
            logger.info("selectCategoryByItemId success:{}", JSONObject.toJSON(itemCategoryResultOne));
            if (itemCategoryResultOne.size() > 1) {
                throw new CommonException("该物料id " + watsonsShoppingCartDTO.getItemId() + "映射了多个物料品类!");
            }
            WatsonsItemCategoryDTO watsonsItemCategoryDTO = itemCategoryResultOne.get(0);
            while (watsonsItemCategoryDTO.getLevelPath().split("\\|").length > 1) {
                if (ObjectUtils.isEmpty(watsonsItemCategoryDTO.getParentCategoryId())) {
                    throw new CommonException("该物料品类编码" + watsonsItemCategoryDTO.getCategoryCode() + "未映射父级物料品类id!");
                }
                ResponseEntity<String> paramResponse = smdmRemoteNewService.queryById(tenantId, watsonsItemCategoryDTO.getParentCategoryId().toString());
                if (ResponseUtils.isFailed(paramResponse)) {
                    throw new CommonException("主数据服务异常:查询物料品类时发生网络错误");
                }
                ItemCategoryDTO response = ResponseUtils.getResponse(paramResponse, new TypeReference<ItemCategoryDTO>() {
                });
                logger.info("the item category info is {}", JSONObject.toJSON(response));
                BeanUtils.copyProperties(response, watsonsItemCategoryDTO);
                logger.info("the final item category info is {}", JSONObject.toJSON(watsonsItemCategoryDTO));
            }
            handleNormalSplit(purReqMergeRule, watsonsShoppingCartDTO, keyRes);
            if (BaseConstants.Flag.YES.equals(purReqMergeRule.getCategory())) {
                keyRes.append(watsonsItemCategoryDTO.getCategoryId()).append("-");
            }
            String keyFinal = String.valueOf(keyRes);
            logger.info("the split key is" + keyFinal);
            watsonsShoppingCartDTO.setItemCategoryId(watsonsItemCategoryDTO.getCategoryId());
            watsonsShoppingCartDTO.setItemCategoryName(watsonsItemCategoryDTO.getCategoryName());
            watsonsShoppingCartDTO.setKey(keyFinal);
        }
    }

    private void processCheckFirstItemCategoryByItemCategoryId(Long tenantId, PurReqMergeRule purReqMergeRule, WatsonsShoppingCartDTO watsonsShoppingCartDTO, StringBuffer keyRes) {
        //如果只有itemCategoryId
        //没有itemId  用ItemCategoryId去查levelPath
        //如果是三级的levelPath  证明就是三级品类找一级品类即可
        //如果是二级的levelPath
        //如果是一级的品类直接用即可
        //如果是多级的，直接报错
        if (ObjectUtils.isEmpty(watsonsShoppingCartDTO.getItemId()) && !ObjectUtils.isEmpty(watsonsShoppingCartDTO.getItemCategoryId())) {

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
                    logger.info("the split key is" + keyFinal);
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
                    logger.info("the split key is" + keyFinal);
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
                    logger.info("the split key is" + keyFinal);
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
        if (BaseConstants.Flag.YES.equals(purReqMergeRule.getFreightType())) {
            keyRes.append(watsonsShoppingCartDTO.getVolumeUnitPrice()).append("-");
        }
    }

    private void setPurMergeRuleForWatsons(PurReqMergeRule purReqMergeRule) {
        purReqMergeRule.setCategory(BaseConstants.Flag.YES);
        purReqMergeRule.setWarehousing(BaseConstants.Flag.YES);
//        purReqMergeRule.setAddressFlag(BaseConstants.Flag.YES);
    }


    private void selectCustomizedProductListForWatsons(Long tenantId,List<WatsonsShoppingCartDTO> watsonsShoppingCartDTOS) {
        for (WatsonsShoppingCartDTO watsonsShoppingCartDTO : watsonsShoppingCartDTOS) {
            List<AllocationInfo> allocationInfoList = allocationInfoRepository.selectByCondition(Condition.builder(AllocationInfo.class).andWhere(Sqls.custom()
                    .andEqualTo(AllocationInfo.FIELD_CART_ID, watsonsShoppingCartDTO.getCartId())).build());
            watsonsShoppingCartDTO.setAllocationInfoList(allocationInfoList);
        }
        List<WatsonsShoppingCartDTO> filterShoppingCart = watsonsShoppingCartDTOS.stream().filter(s -> s.getCustomFlag() != null && s.getCustomFlag() == 1).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(filterShoppingCart)) {
            return;
        }
        WatsonsCustomizedProductDTO watsonsCustomizedProductDTO = new WatsonsCustomizedProductDTO(watsonsShoppingCartDTOS);
        List<CustomizedProductLine> customizedProductLineList = watsonsCustomizedProductLineService.selectCustomizedProductList(tenantId, watsonsCustomizedProductDTO);
        logger.info("the customizedProductLineList are {}",JSONObject.toJSON(customizedProductLineList));
        //校验定制品属性是否有变更
        customizedProductLineService.checkCustomizedProduct(tenantId, customizedProductLineList);
        //往shoppingCartDTO赋值
        Map<Long, List<CustomizedProductLine>> map = customizedProductLineList.stream().collect(Collectors.groupingBy(CustomizedProductLine::getRelationId));
        //查询商品定制品属性
        List<Long> productIdList = filterShoppingCart.stream().map(WatsonsShoppingCartDTO::getProductId).collect(Collectors.toList());
        List<SkuCustomDTO> skuCustomList = productWorkbenchRepository.selectSkuListCustomAttrNoException(tenantId, productIdList);
        Map<Long, SkuCustomDTO> skuCustomMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(skuCustomList)){
            skuCustomMap = skuCustomList.stream().collect(Collectors.toMap(SkuCustomDTO::getSkuId, Function.identity(), (k1,k2)->k1));
        }
        for (WatsonsShoppingCartDTO watsonsShoppingCartDTO: filterShoppingCart) {
            SkuCustomDTO skuCustomDTO = skuCustomMap.get(watsonsShoppingCartDTO.getProductId());
            watsonsShoppingCartDTO.assignmentShipperInfo(ObjectUtils.isEmpty(skuCustomDTO) ? new ArrayList<>() : skuCustomDTO.getSpuCustomGroupList());
            assignmentCustomizedProductList(map,watsonsShoppingCartDTO);
            calculateCustomizedProductForShoppingCartDTO(watsonsShoppingCartDTO);
            watsonsShoppingCartDTO.checkCustomizedProductChange();
        }
        logger.info("the customized shopping carts are {}",JSONObject.toJSON(watsonsShoppingCartDTOS));
    }

    @Override
    public ShoppingCart creates(ShoppingCart shoppingCart, Long organizationId) {
        ShoppingCart shoppingCartParam = super.creates(shoppingCart,organizationId);
        List<AllocationInfo> allocationInfoList = new ArrayList<>();
        if (!ObjectUtils.isEmpty(shoppingCart.getUpdateOrganizationFlag()) && shoppingCart.getUpdateOrganizationFlag() == 1) {
            //    更新购物车组织时，将原购物车数据删除，添加新的购物车数据 重走并单规则, 需要更新费用分配的cartId, 需要更新定制品绑定的cartId
            //判断是否有费用分配数据，若有，需要将费用分配cartId更新为最新的cartId
            allocationInfoList = allocationInfoRepository.select(AllocationInfo.FIELD_CART_ID, shoppingCart.getCartId());
        }
        if (!ObjectUtils.isEmpty(shoppingCartParam.getUpdateOrganizationFlag()) && shoppingCartParam.getUpdateOrganizationFlag() == 1) {
            //判断是否有预算数据，若有，需要将预算表cartId更新为最新的cartId
            if (!CollectionUtils.isEmpty(allocationInfoList)) {
                for (AllocationInfo allocationInfo : allocationInfoList) {
                    allocationInfo.setCartId(shoppingCartParam.getCartId());
                    allocationInfoRepository.updateOptional(allocationInfo, AllocationInfo.FIELD_CART_ID);
                }
            }
        }
        return shoppingCartParam;
    }

    public void calculateCustomizedProductForShoppingCartWhenAllocationUpdate(WatsonsShoppingCart watsonsShoppingCart){
        //定制品属性配置为空或者没有启用计价属性则直接返回
        if (ObjectUtils.isEmpty(watsonsShoppingCart.getCustomFlag()) || watsonsShoppingCart.getCustomFlag() != 1 || ObjectUtils.isEmpty(watsonsShoppingCart.getShipperFlag()) || watsonsShoppingCart.getShipperFlag() == 0 ){
            return;
        }
        if (CollectionUtils.isEmpty(watsonsShoppingCart.getCustomizedProductLineList())){
            watsonsShoppingCart.setTotalPrice(watsonsShoppingCart.getQuantity().add(watsonsShoppingCart.getLatestPrice()));
            return;
        }
        //初始化总金额，通过定制品计算
        BigDecimal calTotalPrice = null;
        //初始化定制品总量数据
        BigDecimal calTotalCqNum = null;
        for (CustomizedProductLine customizedProductLine : watsonsShoppingCart.getCustomizedProductLineList()){
            customizedProductLine.setLatestPrice(watsonsShoppingCart.getLatestPrice());
            allocationInfoService.calculateForCpLine(customizedProductLine);
            if (ObjectUtils.isEmpty(customizedProductLine.getCpAmount()) || ObjectUtils.isEmpty(customizedProductLine.getLineCqNum()) || ObjectUtils.isEmpty(customizedProductLine.getLineTotalCqNum())) {
                continue;
            }
            //计算所有定制商品总金额
            calTotalPrice = customizedProductLine.getCpAmount().add(calTotalPrice == null ? BigDecimal.ZERO : calTotalPrice);
            //计算所有定制品行 定制数量
            calTotalCqNum = customizedProductLine.getLineTotalCqNum().add(calTotalCqNum == null ? BigDecimal.ZERO : calTotalCqNum);
        }
        watsonsShoppingCart.setTotalPrice(calTotalPrice);
        watsonsShoppingCart.setTotalCqNum(calTotalCqNum);
        logger.info("after calculate customized the shopping cart is {}",JSONObject.toJSON(watsonsShoppingCart));
    }

    /**
     * 计算总金额，定制品总额等
     */
    public void calculateCustomizedProductForShoppingCartDTO(WatsonsShoppingCartDTO watsonsShoppingCartDTO){
        //定制品属性配置为空或者没有启用计价属性则直接返回
        if (watsonsShoppingCartDTO.getCustomFlag() == null || watsonsShoppingCartDTO.getCustomFlag() != 1 || watsonsShoppingCartDTO.getShipperFlag() == null || watsonsShoppingCartDTO.getShipperFlag() == 0){
            return;
        }
        if (org.springframework.util.CollectionUtils.isEmpty(watsonsShoppingCartDTO.getCustomizedProductLineList())){
            watsonsShoppingCartDTO.setTotalPrice(watsonsShoppingCartDTO.getQuantity().multiply(watsonsShoppingCartDTO.getLatestPrice()));
            return;
        }
        //初始化总金额，通过定制品计算
        BigDecimal calTotalPrice = null;
        //初始化定制品总量数据
        BigDecimal calTotalCqNum = null;
        for (CustomizedProductLine customizedProductLine : watsonsShoppingCartDTO.getCustomizedProductLineList()){
            customizedProductLine.setLatestPrice(watsonsShoppingCartDTO.getLatestPrice());
            allocationInfoService.calculateForCpLine(customizedProductLine);
            if (ObjectUtils.isEmpty(customizedProductLine.getCpAmount()) || ObjectUtils.isEmpty(customizedProductLine.getLineCqNum()) || ObjectUtils.isEmpty(customizedProductLine.getLineTotalCqNum())) {
                continue;
            }
            //计算商品行总金额
            calTotalPrice = customizedProductLine.getCpAmount().add(calTotalPrice == null ? BigDecimal.ZERO : calTotalPrice);
            //计算定制品总额
            calTotalCqNum = customizedProductLine.getLineTotalCqNum().add(calTotalCqNum == null ? BigDecimal.ZERO : calTotalCqNum);
        }
        watsonsShoppingCartDTO.setTotalPrice(calTotalPrice);
        watsonsShoppingCartDTO.setTotalCqNum(calTotalCqNum);
        logger.info("after calculate price the watsonsShoppingCart is {}",JSONObject.toJSON(watsonsShoppingCartDTO));
    }

    public void assignmentCustomizedProductList(Map<Long, List<CustomizedProductLine>> customizedProductMap,WatsonsShoppingCartDTO watsonsShoppingCartDTO){
            //根据预算id关联
            List<CustomizedProductLine> allList = new ArrayList<>();
            if (org.springframework.util.CollectionUtils.isEmpty(watsonsShoppingCartDTO.getAllocationInfoList())){
                watsonsShoppingCartDTO.setCustomizedProductLineList(new ArrayList<>());
            } else {
                watsonsShoppingCartDTO.setCustomizedProductLineList(new ArrayList<>());
                for (AllocationInfo allocationInfo : watsonsShoppingCartDTO.getAllocationInfoList()){
                    List<CustomizedProductLine> customizedProductLineList = customizedProductMap.getOrDefault(allocationInfo.getAllocationId(), new ArrayList<>());
                    allList.addAll(customizedProductLineList);
                }
                watsonsShoppingCartDTO.setCustomizedProductLineList(allList);
            }
            logger.info("after assignmentCustomizedProductList, the watsonsShoppingCart is {}",JSONObject.toJSON(watsonsShoppingCartDTO));
    }

    @Override
    public void calculatePrice(ShoppingCart shoppingCart) {
        //定制品属性配置为空或者没有启用计价属性则直接返回
        if (ObjectUtils.isEmpty(shoppingCart.getCustomFlag()) || shoppingCart.getCustomFlag() != 1 || ObjectUtils.isEmpty(shoppingCart.getShipperFlag()) || shoppingCart.getShipperFlag() == 0){
            return;
        }
        if (CollectionUtils.isEmpty(shoppingCart.getCustomizedProductLineList())){
            shoppingCart.setTotalPrice(shoppingCart.getQuantity().multiply(shoppingCart.getLatestPrice()));
            return;
        }
        //初始化总金额，通过定制品计算
        BigDecimal calTotalPrice = null;
        //初始化定制品总量数据
        BigDecimal calTotalCqNum = null;
        for (CustomizedProductLine customizedProductLine : shoppingCart.getCustomizedProductLineList()){
            customizedProductLine.setLatestPrice(shoppingCart.getLatestPrice());
            customizedProductLine.calculate();
            if (ObjectUtils.isEmpty(customizedProductLine.getCpAmount()) || ObjectUtils.isEmpty(customizedProductLine.getLineCqNum()) || ObjectUtils.isEmpty(customizedProductLine.getLineTotalCqNum())) {
                continue;
            }
            //计算商品行总金额
            calTotalPrice = customizedProductLine.getCpAmount().add(calTotalPrice == null ? BigDecimal.ZERO : calTotalPrice);
            //计算定制品总额
            calTotalCqNum = customizedProductLine.getLineTotalCqNum().add(calTotalCqNum == null ? BigDecimal.ZERO : calTotalCqNum);
        }
        shoppingCart.setTotalPrice(calTotalPrice);
        shoppingCart.setTotalCqNum(calTotalCqNum);
        logger.info("after calculate price the watsonsShoppingCart is {}",JSONObject.toJSON(shoppingCart));
    }

    @Override
    public CustomizedSameResultDTO checkCustomizedProductLine(ShoppingCart shoppingCart, List<ShoppingCart> existShoppingCarts) {
        Long tenantId = DetailsHelper.getUserDetails().getTenantId();
        SkuBaseInfoDTO skuBaseInfoDTO = productWorkbenchRepository.querySingleSkuBaseInfo(tenantId, shoppingCart.getProductId());
        if (ObjectUtils.isEmpty(skuBaseInfoDTO) || ObjectUtils.isEmpty(skuBaseInfoDTO.getCustomFlag()) || skuBaseInfoDTO.getCustomFlag() != 1) {
            shoppingCart.setNeedInsertCustomized(0);
            return new CustomizedSameResultDTO(skuBaseInfoDTO.getCustomFlag());
        }
        return new CustomizedSameResultDTO();
    }
}
