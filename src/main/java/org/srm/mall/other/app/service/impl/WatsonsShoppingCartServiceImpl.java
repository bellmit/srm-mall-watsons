package org.srm.mall.other.app.service.impl;

import io.choerodon.core.exception.CommonException;
import org.apache.commons.collections4.CollectionUtils;
import org.hzero.core.base.BaseConstants;
import org.hzero.core.message.MessageAccessor;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import org.srm.mall.agreement.app.service.ProductPoolService;
import org.srm.mall.agreement.domain.entity.ProductPool;
import org.srm.mall.agreement.domain.entity.ProductPoolLadder;
import org.srm.mall.common.constant.ScecConstants;
import org.srm.mall.infra.constant.WatsonsConstants;
import org.srm.mall.other.api.dto.WatsonsShoppingCartDTO;
import org.srm.mall.other.api.dto.ShoppingCartDTO;
import org.srm.mall.other.app.service.PunchoutService;
import org.srm.mall.other.domain.entity.AllocationInfo;
import org.srm.mall.other.domain.entity.BudgetInfo;
import org.srm.mall.other.domain.entity.ShoppingCart;
import org.srm.mall.other.domain.repository.AllocationInfoRepository;
import org.srm.mall.other.domain.repository.BudgetInfoRepository;
import org.srm.mall.other.domain.repository.ShoppingCartRepository;
import org.srm.mall.platform.domain.entity.EcClient;
import org.srm.mall.platform.domain.entity.EcCompanyAssign;
import org.srm.mall.platform.domain.entity.EcPlatform;
import org.srm.mall.platform.domain.repository.EcClientRepository;
import org.srm.mall.platform.domain.repository.EcCompanyAssignRepository;
import org.srm.mall.platform.domain.repository.EcPlatformRepository;
import org.srm.mall.product.domain.repository.ComCategoryCatalogMapRepository;
import org.srm.mall.region.domain.repository.AddressRepository;
import org.srm.web.annotation.Tenant;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service("watsonsShoppingCartService")
@Tenant(WatsonsConstants.TENANT_NUMBER)
public class WatsonsShoppingCartServiceImpl extends ShoppingCartServiceImpl {

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

    @Override
    public List<ShoppingCartDTO> shppingCartEnter(Long organizationId, ShoppingCart shoppingCart) {
        List<ShoppingCartDTO> shoppingCartDTOList = super.shppingCartEnter(organizationId, shoppingCart);

        if (!CollectionUtils.isEmpty(shoppingCartDTOList)) {
            List<AllocationInfo> allocationInfoList = allocationInfoRepository.selectByCondition(Condition.builder(AllocationInfo.class).andWhere(Sqls.custom()
                    .andIn(AllocationInfo.FIELD_CART_ID, shoppingCartDTOList.stream().map(ShoppingCartDTO::getCartId).collect(Collectors.toList()))).build());
            if (!CollectionUtils.isEmpty(allocationInfoList)){
                for (AllocationInfo allocationInfo : allocationInfoList){
                    allocationInfo.setAmount(allocationInfo.getPrice().multiply(new BigDecimal(allocationInfo.getQuantity())));
                }
                Map<Long, List<AllocationInfo>> map = allocationInfoList.stream().collect(Collectors.groupingBy(AllocationInfo::getCartId));
                return shoppingCartDTOList.stream().map(s -> {
                    WatsonsShoppingCartDTO watsonsShoppingCart = new WatsonsShoppingCartDTO();
                    BeanUtils.copyProperties(s, watsonsShoppingCart);
                    watsonsShoppingCart.setCostAllocationInfoList(map.get(s.getCartId()));
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
}
