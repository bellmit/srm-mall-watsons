package org.srm.mall.common.feign.dto.product;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

import java.util.List;
import java.util.Map;

/**
 * SKU应用服务
 *
 * @author yuhao.guo@hand-china.com 2020-12-15 11:18:00
 */
public interface SkuService {

    /**
     * 商品列表查询（供&采）
     *
     * @param pageRequest   分页
     * @param skuPublishDTO 参数
     * @return page
     */
    Page<SkuPublishDTO> querySkuPage(PageRequest pageRequest, SkuPublishDTO skuPublishDTO);

    /**
     * 商品列表查询（供应商维度）
     *
     * @param pageRequest   分页
     * @param skuPublishDTO 参数
     * @return page
     */
    Page<SkuPublishDTO> querySkuPageForSupplierTenant(PageRequest pageRequest, SkuPublishDTO skuPublishDTO);

    /**
     * 商品发布---保存/更新
     *
     * @param tenantId      租户id
     * @param spuPublishDTO 参数
     * @param submit 是否提交
     * @return spuId
     */
    Long saveOrUpdateSpu(Long tenantId, SpuPublishDTO spuPublishDTO, boolean submit);

    /**
     * 商品发布---保存&发布
     *
     * @param tenantId      租户id
     * @param spuPublishDTO 参数
     * @return spuId
     */
    Long saveAndPublishSpu(Long tenantId, SpuPublishDTO spuPublishDTO);

    /**
     * 供应商提交采购方商品  待审批
     *
     * @param tenantId      参数
     * @param spuPublishDTO 参数
     * @return spuId
     */
    Long publishSpuAndWaiting(Long tenantId, SpuPublishDTO spuPublishDTO);

    /**
     * 是否启用平台审批
     *
     * @return
     */
    boolean checkPlatformApprove();

    /**
     * 商品发布页面，商品组详情
     *
     * @param tenantId 租户id
     * @param spuId    商品组id
     * @return spuPublishDTO
     */
    SpuPublishDTO querySpuInfo(Long tenantId, Long spuId);

    /**
     * 查询sku明细
     *
     * @param skuPublishDTO 参数
     */
    void querySkuInfoDetail(SkuPublishDTO skuPublishDTO);

    /**
     * 发送sku信息到es
     *
     * @param skuIds 参数
     */
    void sendSkuInfoForSearch(Long tenantId, List<Long> skuIds);

    /**
     * 更新商品状态
     *
     * @param skuPublishDTO
     */
    void changeSkuStatus(SkuPublishDTO skuPublishDTO);

    /**
     * sku批量提交
     *
     * @param tenant            参数
     * @param skuPublishDTOList 参数
     */
    void batchPublishBySku(Long tenant, List<SkuPublishDTO> skuPublishDTOList);

    /**
     * 查询sku临时表
     *
     * @param pageRequest   参数
     * @param skuPublishDTO 参数
     * @return page
     */
    Page<SkuPublishDTO> querySkuTemporary(PageRequest pageRequest, SkuPublishDTO skuPublishDTO);

    /**
     * 待审批商品对比
     *
     * @param tenantId      参数
     * @param skuApproveDTO 参数
     * @return skuTemporary
     */
    SkuApproveDTO skuComparison(Long tenantId, SkuApproveDTO skuApproveDTO);

    /**
     * 协议明细   审批商品
     *
     * @param tenantId      参数
     * @param skuApproveDTO 参数
     */
    void skuApprove(Long tenantId, SkuApproveDTO skuApproveDTO);

    /**
     * 明细对比
     *
     * @param tenantId      参数
     * @param spuPublishDTO 参数
     * @return spu
     */
    SpuPublishDTO skuDetailComparison(Long tenantId, SpuPublishDTO spuPublishDTO);

    /**
     * 查询sku审批表，待审批明细信息
     *
     * @param tenantId      参数
     * @param skuApproveDTO 参数
     * @return spu
     */
    SpuPublishDTO querySkuTemporaryDetail(Long tenantId, SkuApproveDTO skuApproveDTO);


    /**
     * sku导入相关  ↓↓↓↓↓↓↓↓↓↓
     */


    /**
     * 根据三级分类code，判断属性是否允许自定义
     * 1支持 0不支持
     *
     * @return boolean
     */
    Map<String, Integer> allowCustomizeFlag(String categoryCode);

    /**
     * 必输的销售属性校验，构造校验对象的属性名和值的map
     * 品牌、销售单位、税率、币种
     *
     * @param skuImportDTO
     * @return
     */
    Map<String, String> initValueMap(SkuImportDTO skuImportDTO);

    /**
     * sku导入校验处理
     *
     * @param skuImportDTOS
     */
    void importCheck(List<SkuImportDTO> skuImportDTOS, Long tenantId);

    /**
     * sku导入处理
     *
     * @param tenantId         参数
     * @param sku              参数
     * @param productImportDTO 参数
     * @param category         参数
     * @param skuAttr          参数
     * @param images           参数
     * @param attributeMap     参数
     */
    void insertOrUpdateSkuImport(Long tenantId, Sku sku, SkuImportDTO productImportDTO, Category category, SkuAttr skuAttr, List<String> images, Map<String, Long> attributeMap);

    /**
     * sku预览页面
     * @param skuId
     * @return
     */
    SkuPublishDTO skuPreview(Long tenantId, Long skuId);
    /**
     * 商品主图单个查询
     * @param tenantId
     * @param skuId
     * @return
     */
    SkuMedia queryPrimaryImage(Long tenantId, Long skuId);

    /**
     * 商品主图批量查询
     * @param tenantId
     * @param skuIds
     * @return
     */
    List<SkuMedia> batchQueryPrimaryImage(Long tenantId, List<Long> skuIds);

    /**
     * 商品分类单个查询
     * @param tenantId
     * @param skuId
     * @return
     */
    Category queryCategory(Long tenantId, Long skuId);

    /**
     * 商品分类批量查询
     * @param tenantId
     * @param skuIds
     * @return
     */
    List<Category> batchQueryCategory(Long tenantId, List<Long> skuIds);

    /**
     * 批量修改商品新
     * @param tenantId
     * @param skuPublishDTOS
     */
    void batchSkuUpdate(Long tenantId, List<SkuPublishDTO> skuPublishDTOS);
}
