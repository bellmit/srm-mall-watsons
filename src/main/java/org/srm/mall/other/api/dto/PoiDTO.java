package org.srm.mall.other.api.dto;

import lombok.Data;

@Data
public class PoiDTO {

    private String addr;

    private String cp;

    private String direction;

    private Integer distance;

    private String name;

    private String tag;

    private Float point;

    private Integer tel;

    private String uid;

    private Integer zip;

    private PoiDTO parent_poi;

}
