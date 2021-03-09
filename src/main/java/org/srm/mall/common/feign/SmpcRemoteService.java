package org.srm.mall.common.feign;

import org.hzero.starter.keyencrypt.core.Encrypt;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.srm.mall.common.feign.dto.product.*;
import org.srm.mall.common.feign.fallback.SmpcRemoteServiceFallbackImpl;

import java.math.BigDecimal;
import java.util.List;

/**
 * 重构商品中心远程服务
 *
 * @author jianhao.zhang01@hand-china.com 2021/02/22 11:13
 */
@FeignClient(value = "small-product",
        fallback = SmpcRemoteServiceFallbackImpl.class,
        path = "/v1")
public interface SmpcRemoteService {
    /**
     * 查询商品映射的SRM信息（物料、品类、单位、币种、税率）
     * <p>
     * 支持批量查询   true
     * 支持查询条件   skuId
     *
     * @param tenantId          参数
     * @param skuCenterQueryDTO 参数
     * @return list
     */
    @RequestMapping(value = "/{organizationId}/sku-center-interface/query-sku-srm-info", method = RequestMethod.GET)
    ResponseEntity<String> querySkuSrmInfo(@PathVariable("organizationId") Long tenantId, @Encrypt SkuCenterQueryDTO skuCenterQueryDTO);

    /**
     * 查询商品基础信息
     * <p>
     * 支持批量查询   true
     * 支持查询条件   skuId
     *
     * @param tenantId          参数
     * @param skuCenterQueryDTO 参数
     * @return list
     */
    @RequestMapping(value = "/{organizationId}/sku-center-interface/query-sku-base-info", method = RequestMethod.POST)
    ResponseEntity<String> querySkuBaseInfo(@PathVariable("organizationId") Long tenantId, @Encrypt SkuCenterQueryDTO skuCenterQueryDTO);

    /**
     * 查询商品属性信息
     * <p>
     * 支持批量查询   false
     * 支持查询条件   skuId
     *
     * @param tenantId          参数
     * @param skuCenterQueryDTO 参数
     * @return list
     */
    @RequestMapping(value = "/{organizationId}/sku-center-interface/query-sku-attr-info", method = RequestMethod.GET)
    ResponseEntity<String> querySkuAttrInfo(@PathVariable("organizationId") Long tenantId, @Encrypt SkuCenterQueryDTO skuCenterQueryDTO);

    /**
     * 查询商品详情信息
     * <p>
     * 支持批量查询   false
     * 支持查询条件   skuId
     *
     * @param tenantId          参数
     * @param skuCenterQueryDTO 参数
     * @return list
     */
    @RequestMapping(value = "/{organizationId}/sku-center-interface/query-sku-details-info", method = RequestMethod.GET)
    ResponseEntity<String> querySkuDetailsInfo(@PathVariable("organizationId") Long tenantId, @Encrypt SkuCenterQueryDTO skuCenterQueryDTO);

    /**
     * 新增或更新商品信息
     *
     * @param spuCreateDTO 参数
     * @return result
     */
//    @PostMapping("/{organizationId}/sku-center-interface/insert-or-update")
//    SkuCenterResultDTO insertOrUpdateSku(@RequestBody SpuCreateDTO spuCreateDTO, BindingResult result);

    /**
     * 根据skuId批量获取库存
     *
     * @param tenantId
     * @param skuIds
     * @return  List<SkuStock>
     */
    @RequestMapping(value = "/{organizationId}/sku-stocks/batch-query", method = RequestMethod.POST)
    ResponseEntity<String> stockBatchQuery(@PathVariable("organizationId") Long tenantId, @RequestBody List<Long> skuIds);

    /**
     * 根据skuId单个获取库存
     *
     * @param tenantId
     * @param skuId
     * @return SkuStock
     */
    @RequestMapping(value = "/{organizationId}/sku-stocks/query/{skuId}", method = RequestMethod.GET)
    ResponseEntity<String> stockQuery(@PathVariable("organizationId") Long tenantId, @PathVariable("skuId") Long skuId);

    /**
     * 商品目录单个查询
     *
     * @param tenantId
     * @param skuId
     * @return Catalog
     */
    @RequestMapping(value = "/{organizationId}/catalogs/query/{skuId}", method = RequestMethod.GET)
    ResponseEntity<String> getCatalogBySkuId(@PathVariable("organizationId") Long tenantId, @PathVariable("skuId") Long skuId);

    /**
     * 查询商品价格限制
     */
    @RequestMapping(value = "/{organizationId}/catalog-price-limits/query-price-limit", method = RequestMethod.GET)
    ResponseEntity<String> queryPriceLimit(@PathVariable("organizationId") Long tenantId, @Encrypt CatalogPriceLimit catalogPriceLimit);

    /**
     * 分类信息查询
     */
    @RequestMapping(value = "/{organizationId}/sku-center-interface/query-category-info", method = RequestMethod.POST)
    ResponseEntity<String> queryCategoryInfo(@PathVariable("organizationId") Long tenantId, @RequestBody @Encrypt SkuCenterQueryDTO skuCenterQueryDTO);

    /**
     * 商品分类&目录查询(一二三级)
     * @param tenantId
     * @param skuCenterQueryDTO
     * @return
     */
    @RequestMapping(value = "/{organizationId}/sku-center-interface/query-category-catalog-level", method = RequestMethod.GET)
    ResponseEntity<String> querySkuCatalogAndCategoryPath(@PathVariable("organizationId") Long tenantId,@Encrypt SkuCenterQueryDTO skuCenterQueryDTO);

    /**
     * 商品库存扣减
     * @param tenantId
     * @param sagaKey
     * @param skuId
     * @param quantity
     * @return
     */
    @RequestMapping(value = "/{organizationId}/sku-stocks/stock-deduction", method = RequestMethod.POST)
    ResponseEntity<String> stockDeduction(@PathVariable("organizationId") Long tenantId,
                                           @RequestParam String sagaKey,
                                           @RequestParam Long skuId,
                                           @RequestParam BigDecimal quantity);


    /**
     * 批量查询商品销售属性
     * <p>
     * 支持批量查询   true
     * 支持查询条件   skuIds
     *
     * @param tenantId
     * @param skuCenterQueryDTO
     * @return result
     */
    @RequestMapping(value = "/{organizationId}/sku-center-interface/batch-query-sku-attr", method = RequestMethod.GET)
    ResponseEntity<String> batchQuerySkuAttr(@PathVariable("organizationId") Long tenantId, @Encrypt SkuCenterQueryDTO skuCenterQueryDTO);
}
