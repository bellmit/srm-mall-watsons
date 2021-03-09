package org.srm.mall.common.feign.fallback;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.srm.mall.common.feign.SmpcRemoteService;
import org.srm.mall.common.feign.dto.product.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * 重构商品中心feign超时策略
 *
 * @author jianhao.zhang01@hand-china.com 2021/02/22 11:13
 */
@Component
public class SmpcRemoteServiceFallbackImpl implements SmpcRemoteService {

    private static final Logger logger = LoggerFactory.getLogger(SmpcRemoteServiceFallbackImpl.class);

    @Override
    public ResponseEntity<String> querySkuSrmInfo(Long tenantId, SkuCenterQueryDTO skuCenterQueryDTO) {
        logger.info("查询商品srm信息失败，参数{}", JSONObject.toJSONString(skuCenterQueryDTO));
        return null;
    }

    @Override
    public ResponseEntity<String> querySkuBaseInfo(Long tenantId, SkuCenterQueryDTO skuCenterQueryDTO) {
        logger.info("查询商品基本信息失败，参数{}", JSONObject.toJSONString(skuCenterQueryDTO));
        return null;
    }

    @Override
    public ResponseEntity<String> querySkuAttrInfo(Long tenantId, SkuCenterQueryDTO skuCenterQueryDTO) {
        logger.info("查询商品属性信息失败，参数{}", JSONObject.toJSONString(skuCenterQueryDTO));
        return null;
    }

    @Override
    public ResponseEntity<String> querySkuDetailsInfo(Long tenantId, SkuCenterQueryDTO skuCenterQueryDTO) {
        logger.info("查询商品细节信息失败，参数{}", JSONObject.toJSONString(skuCenterQueryDTO));
        return null;
    }

//    @Override
//    public SkuCenterResultDTO insertOrUpdateSku(SpuCreateDTO spuCreateDTO, BindingResult result) {
//        logger.info("新增更新商品信息失败，参数{}", JSONObject.toJSONString(spuCreateDTO));
//        return null;
//    }

    @Override
    public ResponseEntity<String> stockBatchQuery(Long tenantId, List<Long> skuIds) {
        logger.info("批量查询库存信息失败，skuid参数{}", JSONObject.toJSONString(skuIds));
        return null;
    }

    @Override
    public ResponseEntity<String> stockQuery(Long tenantId, Long skuId) {
        logger.info("单个skuId查询库存信息失败，skuid参数{}", JSONObject.toJSONString(skuId));
        return null;
    }

    @Override
    public ResponseEntity<String> getCatalogBySkuId(Long tenantId, Long skuId) {
        logger.info("单个skuId查询目录信息失败，skuid参数{}", JSONObject.toJSONString(skuId));
        return null;
    }

    @Override
    public ResponseEntity<String> querySkuCatalogAndCategoryPath(Long tenantId,SkuCenterQueryDTO skuCenterQueryDTO){
        logger.info("查询商品分类&目录查询(一二三级)失败，参数{}",JSONObject.toJSONString(skuCenterQueryDTO));
        return null;
    }

    @Override
    public ResponseEntity<String> stockDeduction(Long tenantId, String sagaKey, Long skuId, BigDecimal quantity) {
        logger.info("库存扣减失败，参数{},{},{},{}",tenantId, sagaKey, skuId, quantity);
        return null;
    }

    @Override
    public ResponseEntity<String> batchQuerySkuAttr(Long tenantId, SkuCenterQueryDTO skuCenterQueryDTO) {
        logger.info("批量查询商品属性信息失败，skuCenterQueryDTO {}", skuCenterQueryDTO);
        return null;
    }

    @Override
    public ResponseEntity<String> queryPriceLimit(Long tenantId, CatalogPriceLimit catalogPriceLimit) {
        logger.info("查询目录价格限制信息失败，catalogPriceLimit {}", catalogPriceLimit);
        return null;
    }

    @Override
    public ResponseEntity<String> queryCategoryInfo(Long tenantId, SkuCenterQueryDTO skuCenterQueryDTO) {
        logger.info("查询分类信息失败，skuCenterQueryDTO {}", skuCenterQueryDTO);
        return null;
    }
}
