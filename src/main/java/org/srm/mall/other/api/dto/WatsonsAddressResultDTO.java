package org.srm.mall.other.api.dto;

import lombok.Data;

import java.util.List;

@Data
public class WatsonsAddressResultDTO {

    private int status;

    private AddressLocationDTO location;

    private String formatted_address;

    private String business;

    private AddressComponentDTO addressComponent;

    private List<PoiDTO> pois;

    private List<RoadDTO> roads;

    private List<PoiRegionDTO> poiRegions;

    private String sematic_description;

    private Integer cityCode;

}
