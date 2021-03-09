package org.srm.mall.common.feign.dto.product;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.choerodon.mybatis.domain.AuditDomain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hzero.starter.keyencrypt.core.Encrypt;

import java.math.BigDecimal;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

/**
 * sku库存
 *
 * @author min.wang01@hand-china.com 2020-12-11 16:28:03
 */
@ApiModel("sku库存")
@VersionAudit
@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "smpc_sku_stock")
public class SkuStock extends AuditDomain {

    public static final String FIELD_SKU_STOCK_ID = "skuStockId";
    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_SKU_ID = "skuId";
    public static final String FIELD_TOTAL_STOCK = "totalStock";
    public static final String FIELD_CONSUMED_STOCK = "consumedStock";
    public static final String FIELD_WARNING_STOCK = "warningStock";

    //
    // 业务方法(按public protected private顺序排列)
    // ------------------------------------------------------------------------------

    //
    // 数据库字段
    // ------------------------------------------------------------------------------


    @ApiModelProperty("")
    @Id
    @GeneratedValue
    @Encrypt
    private Long skuStockId;
    @ApiModelProperty(value = "租户ID  hpfm_tenant.tenant_id", required = true)
    @NotNull
    private Long tenantId;
    @ApiModelProperty(value = "SKU ID  smpc_sku.sku_id", required = true)
    @NotNull
    private Long skuId;
    @ApiModelProperty(value = "总库存，-1表示不限制库存", required = true)
    @NotNull
    private BigDecimal totalStock;
    @ApiModelProperty(value = "消耗库存", required = true)
    @NotNull
    private BigDecimal consumedStock;
    @ApiModelProperty(value = "预警阈值")
    @Transient
    private Long warningStock;


    //
    // 非数据库字段
    // ------------------------------------------------------------------------------
    @ApiModelProperty(value = "剩余库存")
    @Transient
    private Long surplusStock;

    @ApiModelProperty(value = "sku编码")
    @Transient
    private String skuCode;

    @ApiModelProperty(value = "sku名称")
    @Transient
    private String skuName;

    @ApiModelProperty(value = "分类ID")
    @Transient
    private Long categoryId;

    @ApiModelProperty(value = "spu编码")
    @Transient
    private String spuCode;

    @ApiModelProperty(value = "spu名称")
    @Transient
    private String spuName;

    @ApiModelProperty(value = "主图地址")
    @Transient
    private String mediaPath;

    @ApiModelProperty(value = "物料编码")
    @Transient
    private String itemCode;

    @ApiModelProperty(value = "物料名称")
    @Transient
    private String itemName;

    @ApiModelProperty(value = "分类名称路径")
    @Transient
    private String categoryNamePath;

    @ApiModelProperty(value = "补充库存")
    @Transient
    private BigDecimal replenishmentStock;

    @Transient
    private String stockType;

    @Transient
    private Long stockFromNum;

    @Transient
    private Long stockToNum;

    //
    // getter/setter

    public Long getSurplusStock() {
        return surplusStock;
    }

    public void setSurplusStock(Long surplusStock) {
        this.surplusStock = surplusStock;
    }

    public String getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
    }

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getSpuCode() {
        return spuCode;
    }

    public void setSpuCode(String spuCode) {
        this.spuCode = spuCode;
    }

    public String getSpuName() {
        return spuName;
    }

    public void setSpuName(String spuName) {
        this.spuName = spuName;
    }

    public String getMediaPath() {
        return mediaPath;
    }

    public void setMediaPath(String mediaPath) {
        this.mediaPath = mediaPath;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getCategoryNamePath() {
        return categoryNamePath;
    }

    public void setCategoryNamePath(String categoryNamePath) {
        this.categoryNamePath = categoryNamePath;
    }

    public BigDecimal getReplenishmentStock() {
        return replenishmentStock;
    }

    public SkuStock setReplenishmentStock(BigDecimal replenishmentStock) {
        this.replenishmentStock = replenishmentStock;
        return this;
    }

    public String getStockType() {
        return stockType;
    }

    public void setStockType(String stockType) {
        this.stockType = stockType;
    }

    public Long getStockFromNum() {
        return stockFromNum;
    }

    public void setStockFromNum(Long stockFromNum) {
        this.stockFromNum = stockFromNum;
    }

    public Long getStockToNum() {
        return stockToNum;
    }

    public void setStockToNum(Long stockToNum) {
        this.stockToNum = stockToNum;
    }

    // ------------------------------------------------------------------------------

    /**
     * @return
     */
    public Long getSkuStockId() {
        return skuStockId;
    }

    public SkuStock setSkuStockId(Long skuStockId) {
        this.skuStockId = skuStockId;
        return this;
    }

    /**
     * @return 租户ID  hpfm_tenant.tenant_id
     */
    public Long getTenantId() {
        return tenantId;
    }

    public SkuStock setTenantId(Long tenantId) {
        this.tenantId = tenantId;
        return this;
    }

    /**
     * @return SKU ID  smpc_sku.sku_id
     */
    public Long getSkuId() {
        return skuId;
    }

    public SkuStock setSkuId(Long skuId) {
        this.skuId = skuId;
        return this;
    }

    /**
     * @return 总库存，-1表示不限制库存
     */
    public BigDecimal getTotalStock() {
        return totalStock;
    }

    public SkuStock setTotalStock(BigDecimal totalStock) {
        this.totalStock = totalStock;
        return this;
    }

    /**
     * @return 消耗库存
     */
    public BigDecimal getConsumedStock() {
        return consumedStock;
    }

    public SkuStock setConsumedStock(BigDecimal consumedStock) {
        this.consumedStock = consumedStock;
        return this;
    }


    public Long getWarningStock() {
        return warningStock;
    }

    public void setWarningStock(Long warningStock) {
        this.warningStock = warningStock;
    }

}
