package org.srm.mall.common.feign.dto.product;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * 商品中心信息查询  DTO
 *
 * @author yuhao.guo  2021年1月18日10:59:03
 */
public class SkuCenterQueryDTO {

    @ApiModelProperty("租户ID")
    private Long tenantId;
    @ApiModelProperty("商品ID列表")
    private List<Long> skuIds;
    @ApiModelProperty("商品ID")
    private Long skuId;
    @ApiModelProperty("商品编码")
    private String skuCode;
    @ApiModelProperty("第三方商品编码")
    private String thirdSkuCode;
    @ApiModelProperty("商品名称")
    private String skuName;
    @ApiModelProperty("商品编码列表")
    private List<String> skuCodeList;
    @ApiModelProperty("三方商品编码列表")
    private List<String> thirdSkuCodes;
    @ApiModelProperty("商品来源")
    private String sourceFrom;
    @ApiModelProperty("分类编码")
    private String categoryCode;
    @ApiModelProperty("是否过滤punchOut商品，1：不查询pouchOut商品；0或null：查询pouchOut商品")
    private Integer punchOutFlag;

    @ApiModelProperty("分类ID列表")
    private List<Long> categoryIdList;
    @ApiModelProperty("分类编码列表")
    private List<String> categoryCodeList;

    public SkuCenterQueryDTO() {
    }

    public SkuCenterQueryDTO(Long tenantId, List<Long> skuIds) {
        this.tenantId = tenantId;
        this.skuIds = skuIds;
    }

    public SkuCenterQueryDTO(Long tenantId, Long skuId) {
        this.tenantId = tenantId;
        this.skuId = skuId;
    }

    public SkuCenterQueryDTO(Long skuId) {
        this.skuId = skuId;
    }

    public List<String> getSkuCodeList() {
        return skuCodeList;
    }

    public void setSkuCodeList(List<String> skuCodeList) {
        this.skuCodeList = skuCodeList;
    }

    public List<String> getThirdSkuCodes() {
        return thirdSkuCodes;
    }

    public void setThirdSkuCodes(List<String> thirdSkuCodes) {
        this.thirdSkuCodes = thirdSkuCodes;
    }

    public String getSourceFrom() {
        return sourceFrom;
    }

    public void setSourceFrom(String sourceFrom) {
        this.sourceFrom = sourceFrom;
    }

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public Integer getPunchOutFlag() {
        return punchOutFlag;
    }

    public void setPunchOutFlag(Integer punchOutFlag) {
        this.punchOutFlag = punchOutFlag;
    }

    public List<Long> getCategoryIdList() {
        return categoryIdList;
    }

    public void setCategoryIdList(List<Long> categoryIdList) {
        this.categoryIdList = categoryIdList;
    }

    public List<String> getCategoryCodeList() {
        return categoryCodeList;
    }

    public void setCategoryCodeList(List<String> categoryCodeList) {
        this.categoryCodeList = categoryCodeList;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public List<Long> getSkuIds() {
        return skuIds;
    }

    public void setSkuIds(List<Long> skuIds) {
        this.skuIds = skuIds;
    }

    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    public String getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
    }

    public String getThirdSkuCode() {
        return thirdSkuCode;
    }

    public void setThirdSkuCode(String thirdSkuCode) {
        this.thirdSkuCode = thirdSkuCode;
    }

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }
}
