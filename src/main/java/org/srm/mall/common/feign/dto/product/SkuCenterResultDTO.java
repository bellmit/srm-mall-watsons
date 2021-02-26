package org.srm.mall.common.feign.dto.product;

import io.swagger.annotations.ApiModelProperty;
import org.srm.mall.product.infra.gatewayinterface.response.ResponseEnum;

import java.io.Serializable;

/**
 * 商品中心feign结果  DTO
 *
 * @author yuhao.guo  2021年1月18日11:10:33
 */
public class SkuCenterResultDTO<T> implements Serializable {

    @ApiModelProperty("成功标识")
    private boolean success;
    @ApiModelProperty("结果码")
    private String resultCode;
    @ApiModelProperty("异常信息")
    private String resultMessage;
    @ApiModelProperty("结果")
    private T result;

    public static<T> SkuCenterResultDTO<T> error(String resultCode, String resultMessage) {
        SkuCenterResultDTO<T> errorResult = new SkuCenterResultDTO<>();
        errorResult.setSuccess(false);
        errorResult.setResultCode(resultCode);
        errorResult.setResultMessage(resultMessage);
        errorResult.setResult(null);
        return errorResult;
    }

    public static<T> SkuCenterResultDTO<T> success(T result) {
        SkuCenterResultDTO<T> successResult = new SkuCenterResultDTO<>();
        successResult.setSuccess(true);
        successResult.setResultCode(ResponseEnum.SUCCESS.getCode());
        successResult.setResultMessage(ResponseEnum.SUCCESS.getMessage());
        successResult.setResult(result);
        return successResult;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultMessage() {
        return resultMessage;
    }

    public void setResultMessage(String resultMessage) {
        this.resultMessage = resultMessage;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }
}
