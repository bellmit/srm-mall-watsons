package org.srm.mall.other.app.service.impl;

import com.ctrip.framework.apollo.util.ExceptionUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import io.choerodon.core.exception.CommonException;
import org.apache.commons.collections4.CollectionUtils;
import org.hzero.core.base.BaseConstants;
import org.hzero.core.message.MessageAccessor;
import org.hzero.core.util.ResponseUtils;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import org.srm.boot.common.CustomizeSettingCode;
import org.srm.boot.common.cache.impl.AbstractKeyGenerator;
import org.srm.boot.platform.configcenter.CnfHelper;
import org.srm.boot.platform.customizesetting.CustomizeSettingHelper;
import org.srm.common.convert.bean.BeanConvertor;
import org.srm.mall.agreement.app.service.PostageService;
import org.srm.mall.agreement.app.service.ProductPoolService;
import org.srm.mall.agreement.domain.entity.ProductPool;
import org.srm.mall.agreement.domain.entity.ProductPoolLadder;
import org.srm.mall.common.constant.ScecConstants;
import org.srm.mall.common.feign.SmdmRemoteService;
import org.srm.mall.common.utils.snapshot.SnapshotUtil;
import org.srm.mall.context.entity.ItemCategory;
import org.srm.mall.infra.constant.WatsonsConstants;
import org.srm.mall.order.api.dto.PreRequestOrderDTO;
import org.srm.mall.order.app.service.MallOrderCenterService;
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
import org.srm.mall.product.api.dto.PriceResultDTO;
import org.srm.mall.product.domain.entity.ScecProductCategory;
import org.srm.mall.product.domain.repository.ComCategoryCatalogMapRepository;
import org.srm.mall.product.infra.mapper.ScecProductCategoryMapper;
import org.srm.mall.region.domain.entity.Address;
import org.srm.mall.region.domain.repository.AddressRepository;
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
    public ShoppingCart create(ShoppingCart shoppingCart) {
        List<BudgetInfo> budgetInfoList = new ArrayList<>();
        if (!ObjectUtils.isEmpty(shoppingCart.getUpdateOrganizationFlag()) && shoppingCart.getUpdateOrganizationFlag() == 1) {
            //    更新购物车组织时，将原购物车数据删除，添加新的购物车数据 重走并单规则, 需要更新预算的cartId
            List<ShoppingCart> shoppingCarts = new ArrayList<>(1);
            shoppingCarts.add(shoppingCart);
            //判断是否有预算数据，若有，需要将预算表cartId更新为最新的cartId
            budgetInfoList = budgetInfoRepository.select(BudgetInfo.FIELD_CART_ID, shoppingCart.getCartId());
            this.deleteShoppingCart(shoppingCarts);
            shoppingCart.setCartId(null);
        }

        //如果是JD需要设置供应商公司ID
        if (!ScecConstants.SourceType.CATALOGUE.equalsIgnoreCase(shoppingCart.getProductSource())) {
            EcCompanyAssign ecCompanyAssign = ecCompanyAssignRepository.selectByEcCompanyId(shoppingCart.getCompanyId(), shoppingCart.getTenantId(), shoppingCart.getProductSource());
            Assert.notNull(ecCompanyAssign, ScecConstants.ErrorCode.NO_PERMISSION_TO_BUY);
            EcClient ecClient = ecClientRepository.selectByPrimaryKey(ecCompanyAssign.getEcClientId());
            Assert.notNull(ecClient, ScecConstants.ErrorCode.EC_CLINET_NOT_EXISTS);
            shoppingCart.setSupplierCompanyId(ecClient.getEcCompanyId());
        }

        //1.判断商品是否存在购物车
        List<ShoppingCart> shoppingCarts = shoppingCartRepository.selectByCondition(Condition.builder(ShoppingCart.class)
                .andWhere(Sqls.custom().andEqualTo(ShoppingCart.FIELD_OWNER_ID, shoppingCart.getOwnerId())
                        .andEqualTo(ShoppingCart.FIELD_SUPPLIER_COMPANY_ID, shoppingCart.getSupplierCompanyId())
                        .andEqualTo(ShoppingCart.FIELD_INV_ORGANIZATION_ID, shoppingCart.getInvOrganizationId())
                        .andEqualTo(ShoppingCart.FIELD_PUR_ORGANIZATION_ID, shoppingCart.getPurOrganizationId())
                        .andEqualTo(ShoppingCart.FIELD_ADDRESS_ID, shoppingCart.getAddressId())
                        .andEqualTo(ShoppingCart.FIELD_PRODUCT_SOURCE, shoppingCart.getProductSource())
                        .andEqualTo(ShoppingCart.FIELD_PRODUCT_ID, shoppingCart.getProductId())
                        .andEqualTo(ShoppingCart.FIELD_PURCHASE_TYPE, shoppingCart.getPurchaseType())
                        .andEqualTo(ShoppingCart.FIELD_SKU_TYPE, shoppingCart.getSkuType() == null ? 0 : shoppingCart.getSkuType())
                        .andEqualTo(ShoppingCart.FIELD_MAIN_SKU_ID, shoppingCart.getMainSkuId())
                        .andEqualTo(ShoppingCart.FIELD_SERVICE_CATEGORYCODE, shoppingCart.getServiceCategoryCode())
                        .andEqualTo(ShoppingCart.FIELD_QT_HEADER_NO, shoppingCart.getQtHeaderNo())
                )
                .build());
        Long maxQuantity;
        EcPlatform ecPlatform = new EcPlatform();
        ecPlatform.setEcPlatformCode(shoppingCart.getProductSource());
        EcPlatform puchaseQuantity = ecPlatformRepository.selectOne(ecPlatform);
        if (ObjectUtils.isEmpty(puchaseQuantity) || puchaseQuantity.getPurchaseQuantity() == null) {
            maxQuantity = ScecConstants.ConstantNumber.LONG_99999;
        } else {
            maxQuantity = puchaseQuantity.getPurchaseQuantity();
        }

        //校验加购商品是否上架
        Long regionId = addressRepository.querySecondRegionId(shoppingCart.getAddressId());
        if (ObjectUtils.isEmpty(regionId)) {
            throw new CommonException("地址不合法，请检查你的地址");
        }
        shoppingCart.setSecondRegionId(regionId);
        //  取价格服务获取最新价格
//         查询价格服务 查询最新价格
        ProductPool productPool = null;
        if (!punchoutService.isPuhchout(shoppingCart.getProductSource())) {
            List<ProductPool> productPools = productPoolService.queryProductPools(Collections.singletonList(shoppingCart.getProductId()), shoppingCart.getTenantId(), shoppingCart.getCompanyId(), regionId, shoppingCart.getLevelPath(), Boolean.TRUE, shoppingCart.getAddressId());
            if (ObjectUtils.isEmpty(productPools) || ScecConstants.ShelfStatus.SHELF != productPools.get(0).getShelfFlag()) {
                throw new CommonException("该商品未找到，请确认是否上架！");
            }
            productPool = productPools.get(0);
            if (productPool.getSupplierCompanyId() != null) {
                shoppingCart.setSupplierCompanyId(productPool.getSupplierCompanyId());
            }
            shoppingCart.setLatestPrice(productPool.getSellPrice());
            shoppingCart.setMinPackageQuantity(shoppingCart.getMinPackageQuantity() == null ? BigDecimal.ONE : productPool.getMinPackageQuantity());
            shoppingCart.setMinPurchaseQuantity(shoppingCart.getMinPurchaseQuantity() == null ? BigDecimal.ONE : productPool.getMinPurchaseQuantity());
            //判断该商品对应的目录该用户是否有权限
            if (!ObjectUtils.isEmpty(productPool.getCatalogId())) {
                Integer count = comCategoryCatalogMapRepository.checkCatalog(shoppingCart.getOwnerId(), productPool.getCatalogId(), shoppingCart.getTenantId());
                if (count > 0) {
                    throw new CommonException(ScecConstants.ErrorCode.CATALOG_PERMISSION_NOT_PASS);
                }
            }
        }
        //已存在购物车修改操作
        if (CollectionUtils.isNotEmpty(shoppingCarts)) {
            ShoppingCart existShoppingCart = shoppingCarts.get(0);
            //更新 单价和数量 [订单回退时逻辑]
            if (!existShoppingCart.getCartId().equals(shoppingCart.getCartId())) {
                existShoppingCart.setQuantity(shoppingCart.getQuantity() + existShoppingCart.getQuantity());
                //更新punchout 总额
                if (punchoutService.isPuhchout(shoppingCart.getProductSource())) {
                    existShoppingCart.setTotalAmount(shoppingCart.getTotalAmount().add(existShoppingCart.getTotalAmount()));
                    existShoppingCart.setNakedTotalAmount(shoppingCart.getNakedTotalAmount().add(existShoppingCart.getNakedTotalAmount()));
                }
            } else {
                existShoppingCart.setQuantity(shoppingCart.getQuantity());
            }
            if (!punchoutService.isPuhchout(shoppingCart.getProductSource())) {
                if (productPool.getLadderEnableFlag() != null && productPool.getLadderEnableFlag() == 1) {
                    //更新最新价格
                    handLadderPrice(shoppingCart, productPool);
                }
            }
            existShoppingCart.setUnitPrice(shoppingCart.getLatestPrice());
            existShoppingCart.setRemark(shoppingCart.getRemark());
            //目录化商品 需要更新是否满足最小采购量
            if (ScecConstants.SourceType.CATALOGUE.equalsIgnoreCase(shoppingCart.getProductSource())) {
                BigDecimal minPurchaseQuantity = shoppingCart.getMinPurchaseQuantity();
                if (minPurchaseQuantity != null && (minPurchaseQuantity.compareTo(new BigDecimal(1))) < 0) {
                    shoppingCart.setErrorMessage(MessageAccessor.getMessage(ScecConstants.ErrorCode.EC_PRODUCT_MINIMUM_PURCHASE_ERROR).desc() + shoppingCart.getMinPurchaseQuantity().stripTrailingZeros().toPlainString());
                }
                if (BigDecimal.valueOf(shoppingCart.getQuantity()).compareTo(minPurchaseQuantity) < 0) {
                    shoppingCart.setPurchaseFlag(BaseConstants.Flag.YES);
                    shoppingCart.setQuantity(minPurchaseQuantity.longValue());
                }
                //判断数量是否满足是最小包装量的整数倍
                BigDecimal number = new BigDecimal(shoppingCart.getQuantity()).divide(shoppingCart.getMinPackageQuantity(), 10, BigDecimal.ROUND_HALF_UP);
                if (new BigDecimal(number.intValue()).compareTo(number) != 0) {
                    //不满足，判断最小包装量是否大于当前数量，大于则直接设置，小于则乘2，直到大于当前数量
                    BigDecimal result = shoppingCart.getMinPackageQuantity();
                    while (result.compareTo(new BigDecimal(shoppingCart.getQuantity())) < 0) {
                        result = result.multiply(new BigDecimal(2));
                    }
                    shoppingCart.setQuantity(result.longValue());
                }
            } else {
//                ProductDTO productDTO = productSkuRepository.selectByProduct(shoppingCart.getProductId(), shoppingCart.getTenantId(), shoppingCart.getCompanyId());
                BigDecimal lowestBuy = shoppingCart.getMinPurchaseQuantity();
                if (lowestBuy != null && (lowestBuy.longValue() > shoppingCart.getQuantity())) {
                    shoppingCart.setQuantity(lowestBuy.longValue());
                }
                //非目录化商品购物车数量需要做200上限
//                Long maxQuan = existShoppingCart.getQuantity() > maxQuantity ? maxQuantity : existShoppingCart.getQuantity();
                existShoppingCart.setQuantity(existShoppingCart.getQuantity() > maxQuantity ? maxQuantity : existShoppingCart.getQuantity());
                //checkMaxQuantity(existShoppingCart);
            }
            shoppingCart.setCartId(existShoppingCart.getCartId());
            shoppingCartRepository.updateOptional(existShoppingCart, ShoppingCart.FIELD_QUANTITY, ShoppingCart.FIELD_UNIT_PRICE, ShoppingCart.FIELD_REMARK);
        } else {
            //购物车没有已存在的商品做新增操作
            // 订单回退时带有购物车ID,但不使用,下次下单使用相同ID无法创建采购申请
            shoppingCart.setCartId(null);
            //非目录化商品购物车数量需要做200上限
            if (!"CATA".equalsIgnoreCase(shoppingCart.getProductSource()) && !punchoutService.isPuhchout(shoppingCart.getProductSource())) {
//                ProductDTO productDTO = productSkuRepository.selectByProduct(shoppingCart.getProductId(), shoppingCart.getTenantId(), shoppingCart.getCompanyId());
                BigDecimal lowestBuy = productPool.getMinPurchaseQuantity();
                if (lowestBuy != null && lowestBuy.longValue() > shoppingCart.getQuantity()) {
                    shoppingCart.setQuantity(lowestBuy.longValue());
                }
                shoppingCart.setQuantity(shoppingCart.getQuantity() > maxQuantity ? maxQuantity : shoppingCart.getQuantity());
            }
            //判断数量是否满足是最小包装量的整数倍
            BigDecimal number = new BigDecimal(shoppingCart.getQuantity()).divide(shoppingCart.getMinPackageQuantity(), 10, BigDecimal.ROUND_HALF_UP);
            if (new BigDecimal(number.intValue()).compareTo(number) != 0) {
                //不满足，判断最小包装量是否大于当前数量，大于则直接设置，小于则乘2，直到大于当前数量
                BigDecimal result = shoppingCart.getMinPackageQuantity();
                while (result.compareTo(new BigDecimal(shoppingCart.getQuantity())) < 0) {
                    result = result.multiply(new BigDecimal(2));
                }
                shoppingCart.setQuantity(result.longValue());
            }
            shoppingCartRepository.insertSelective(shoppingCart);
            if (!ObjectUtils.isEmpty(shoppingCart.getUpdateOrganizationFlag()) && shoppingCart.getUpdateOrganizationFlag() == 1) {
                //判断是否有预算数据，若有，需要将预算表cartId更新为最新的cartId
                if (!CollectionUtils.isEmpty(budgetInfoList)) {
                    for (BudgetInfo budgetInfo : budgetInfoList) {
                        budgetInfo.setCartId(shoppingCart.getCartId());
                        budgetInfoRepository.updateByPrimaryKeySelective(budgetInfo);
                    }
                }
            }
        }
        //设置 小计金额
        shoppingCart.setTotalPrice(shoppingCart.getLatestPrice().multiply(BigDecimal.valueOf(shoppingCart.getQuantity())));
        return shoppingCart;
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


        //watsonsShoppingCartDTOList转换成的shoppingCartDTOList用于后续检测
        List<ShoppingCartDTO> shoppingCartDTOList = new ArrayList<>();
        for (WatsonsShoppingCartDTO watsonsShoppingCartDTO : watsonsShoppingCartDTOList) {
            ShoppingCartDTO shoppingCartDTO4Transfer = new ShoppingCartDTO();
            BeanUtils.copyProperties(watsonsShoppingCartDTO, shoppingCartDTO4Transfer);
            shoppingCartDTOList.add(shoppingCartDTO4Transfer);
        }

        // eric 首先根据购物车的地址id和组织层级进行分组调用   异步获取商品价格
        Map<String, Map<Long, PriceResultDTO>> priceResultDTOMap = queryPriceResult(shoppingCartDTOList);

        Iterator iterator = shoppingCartDTOList.iterator();
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

            //拆单完成后判断是否还需要继续根据重复订单再行拆分
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
                watsonsPreRequestOrderDTO.setReceiverContactName(watsonsShoppingCartDTO.getContactName());
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

                // TODO: 2020/12/24   ce号设置

                watsonsPreRequestOrderDTO.setWatsonsShoppingCartDTOList(watsonsShoppingCartDTOList);
                watsonsPreRequestOrderDTO.setMobile(watsonsShoppingCartDTO.getMobile());
                watsonsPreRequestOrderDTO.setReceiverContactName(watsonsShoppingCartDTO.getContactName());
                snapshotUtil.saveSnapshot(AbstractKeyGenerator.getKey(ScecConstants.CacheCode.SERVICE_NAME, ScecConstants.CacheCode.PURCHASE_REQUISITION_PREVIEW, watsonsPreRequestOrderDTO.getPreRequestOrderNumber()), watsonsPreRequestOrderDTO.getPreRequestOrderNumber(), watsonsPreRequestOrderDTO, 5, TimeUnit.MINUTES);
                watsonsPreRequestOrderDTOList.add(watsonsPreRequestOrderDTO);
            }
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

        //用费用维度只是最大化拆分出一个个商品行，一个个商品行可能是相同的商品，只是费用不同，再用另外三个维度去合单时与价格不是一个维度，不能确保一定没有同样的商品被分到了由这三个维度组成的同一个key
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

            //eric 按照供应商+品类+费用分配的门店  更新并单规则
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

                    ResponseEntity<String> responseOne = smdmRemoteService.selectCategoryByItemId(tenantId, watsonsShoppingCartDTO.getItemId(), BaseConstants.Flag.YES);
                    if (ResponseUtils.isFailed(responseOne)) {
                        logger.error("selectCategoryByItemId:{}", responseOne);
                        throw new CommonException("根据物料查询一级品类失败!");
                    }
                    logger.info("selectCategoryByItemId:{}", responseOne);
                    List<WatsonsItemCategoryDTO> itemCategoryResultOne  = ResponseUtils.getResponse(responseOne, new TypeReference<List<WatsonsItemCategoryDTO>>() {});

                    if(CollectionUtils.isEmpty(itemCategoryResultOne)){
                        logger.error("selectCategoryByItemId:{}", "null");
                        throw new CommonException("根据物料查询一级品类失败!");
                    }

                    if(CollectionUtils.isNotEmpty(itemCategoryResultOne) && (itemCategoryResultOne.size()>1)){
                        logger.error("selectCategoryByItemId:{}", itemCategoryResultOne);
                        throw new CommonException("单个物料非法查询到多个品类！");
                    }


                    if (BaseConstants.Flag.YES.equals(purReqMergeRule.getSupplierFlag())) {
                        keyRes.append(watsonsShoppingCartDTO.getSupplierCompanyId()).append("-");
                    }
                    if (BaseConstants.Flag.YES.equals(purReqMergeRule.getAddressFlag())) {
                        if(watsonsShoppingCartDTO.getAllocationInfoList() == null){
                            logger.error("未进行费用分配！:{}", watsonsShoppingCartDTO.getAllocationInfoList());
                            throw new CommonException("未进行费用分配!");
                        }
                        keyRes.append(watsonsShoppingCartDTO.getAllocationInfoList().get(0).getAddressId()).append("-");
                    }
                    if (BaseConstants.Flag.YES.equals(purReqMergeRule.getCategory())){
                        keyRes.append(itemCategoryResultOne.get(0).getParentCategoryId()).append("-");
                    }

                    String keyFinal = String.valueOf(keyRes);
                    watsonsShoppingCartDTO.setItemCategoryId(itemCategoryResultOne.get(0).getParentCategoryId());
                    watsonsShoppingCartDTO.setItemCategoryName(itemCategoryResultOne.get(0).getParentCategoryName());
                    watsonsShoppingCartDTO.setKey(keyFinal);
                }
                Map<String, List<WatsonsShoppingCartDTO>> result = watsonsShoppingCartDTOList.stream().collect(Collectors.groupingBy(s->s.getKey()));
                resultMap.putAll(result);

            } else {
                resultMap.put(key, groupMap.get(key));
            }

            //eric 遍历以默认的并单规则分类购物车list组成map结束
        }
        return resultMap;
    }

    private void setPurMergeRuleForWatsons(PurReqMergeRule purReqMergeRule) {
        purReqMergeRule.setSupplierFlag(BaseConstants.Flag.YES);
        purReqMergeRule.setAddressFlag(BaseConstants.Flag.YES);
        purReqMergeRule.setCategory(BaseConstants.Flag.YES);
    }
}
