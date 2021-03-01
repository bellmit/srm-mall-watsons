package org.srm.mall.common.feign.dto.product;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class SkuAttrCreateDTO {

    /**
     * 属性ID
     */
    private String attId;
    /**
     * 属性名称
     */
    @NotBlank
    private String attName;
    /**
     * 属性值ID
     */
    private String attValueId;
    /**
     * 属性值名称
     */
    @NotBlank
    private String attValueName;
    /**
     * 是否销售属性
     */
    @NotNull
    private boolean isSaleAtt;


    public String getAttId() {
        return attId;
    }

    public void setAttId(String attId) {
        this.attId = attId;
    }

    public String getAttName() {
        return attName;
    }

    public void setAttName(String attName) {
        this.attName = attName;
    }

    public String getAttValueId() {
        return attValueId;
    }

    public void setAttValueId(String attValueId) {
        this.attValueId = attValueId;
    }

    public String getAttValueName() {
        return attValueName;
    }

    public void setAttValueName(String attValueName) {
        this.attValueName = attValueName;
    }

    public boolean isSaleAtt() {
        return isSaleAtt;
    }

    public void setSaleAtt(boolean saleAtt) {
        isSaleAtt = saleAtt;
    }
}
