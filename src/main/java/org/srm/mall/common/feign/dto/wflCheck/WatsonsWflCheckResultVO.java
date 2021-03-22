package org.srm.mall.common.feign.dto.wflCheck;

import org.hzero.core.base.BaseConstants;

/**
 * @author yande.wang@hand-china.com 2021/3/12 14:27
 * @description
 */
public class WatsonsWflCheckResultVO {

    public void successRes(){
        this.errorFlag = BaseConstants.Flag.NO;
    }

    public void failedRes(String errorMessage){
        this.errorFlag = BaseConstants.Flag.YES;
        this.errorMessage = errorMessage;
    }

    private Integer errorFlag;
    private String errorMessage;

    public Integer getErrorFlag() {
        return errorFlag;
    }

    public void setErrorFlag(Integer errorFlag) {
        this.errorFlag = errorFlag;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
