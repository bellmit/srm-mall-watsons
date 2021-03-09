package org.srm.mall.other.api.dto;

import lombok.Data;

@Data
public class AddressComponentDTO {

    private String country;

    private Integer country_code;

    private String country_code_iso;

    private String country_code_iso2;

    private String province;

    private String city;

    private Integer city_level;

    private String district;

    private String town;

    private String town_code;

    private String street;

    private String street_number;

    private Integer adcode;

    private String direction;

    private String distance;



}
