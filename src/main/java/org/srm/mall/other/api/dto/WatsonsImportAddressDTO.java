package org.srm.mall.other.api.dto;

/**
 * 屈臣氏地址信息
 */
public class WatsonsImportAddressDTO {
    private String companyNum;
    private String companyName;
    private String invOrganizationCode;
    private String invOrganizationName;
    private String fullAddress;
    private String address;
    private String thirdRegionCode;
    private String fourthRegionCode;

    public String getCompanyNum() {
        return companyNum;
    }

    public void setCompanyNum(String companyNum) {
        this.companyNum = companyNum;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getInvOrganizationCode() {
        return invOrganizationCode;
    }

    public void setInvOrganizationCode(String invOrganizationCode) {
        this.invOrganizationCode = invOrganizationCode;
    }

    public String getInvOrganizationName() {
        return invOrganizationName;
    }

    public void setInvOrganizationName(String invOrganizationName) {
        this.invOrganizationName = invOrganizationName;
    }

    public String getFullAddress() {
        return fullAddress;
    }

    public void setFullAddress(String fullAddress) {
        this.fullAddress = fullAddress;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getThirdRegionCode() {
        return thirdRegionCode;
    }

    public void setThirdRegionCode(String thirdRegionCode) {
        this.thirdRegionCode = thirdRegionCode;
    }

    public String getFourthRegionCode() {
        return fourthRegionCode;
    }

    public void setFourthRegionCode(String fourthRegionCode) {
        this.fourthRegionCode = fourthRegionCode;
    }

    @Override
    public String toString() {
        return "WatsonsImportAddressDTO{" +
                "companyNum='" + companyNum + '\'' +
                ", companyName='" + companyName + '\'' +
                ", invOrganizationCode='" + invOrganizationCode + '\'' +
                ", invOrganizationName='" + invOrganizationName + '\'' +
                ", fullAddress='" + fullAddress + '\'' +
                ", address='" + address + '\'' +
                ", thirdRegionCode='" + thirdRegionCode + '\'' +
                ", fourthRegionCode='" + fourthRegionCode + '\'' +
                '}';
    }
}
