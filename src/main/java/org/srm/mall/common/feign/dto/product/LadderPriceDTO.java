package org.srm.mall.common.feign.dto.product;

import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

public class LadderPriceDTO {

    @ApiModelProperty(value = "行号")
    private Long ladderLineNum;
    @ApiModelProperty(value = "数量从（含）")
    private BigDecimal ladderFrom;
    @ApiModelProperty(value = "数量至")
    private BigDecimal ladderTo;
    @ApiModelProperty(value = "阶梯价格")
    private BigDecimal ladderPrice;

    public Long getLadderLineNum() {
        return ladderLineNum;
    }

    public void setLadderLineNum(Long ladderLineNum) {
        this.ladderLineNum = ladderLineNum;
    }

    public BigDecimal getLadderFrom() {
        return ladderFrom;
    }

    public void setLadderFrom(BigDecimal ladderFrom) {
        this.ladderFrom = ladderFrom;
    }

    public BigDecimal getLadderTo() {
        return ladderTo;
    }

    public void setLadderTo(BigDecimal ladderTo) {
        this.ladderTo = ladderTo;
    }

    public BigDecimal getLadderPrice() {
        return ladderPrice;
    }

    public void setLadderPrice(BigDecimal ladderPrice) {
        this.ladderPrice = ladderPrice;
    }

}
