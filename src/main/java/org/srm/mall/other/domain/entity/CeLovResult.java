package org.srm.mall.other.domain.entity;

import org.srm.mall.other.api.dto.CeLovResultDTO;

import java.util.List;

/**
 * CE信息feign调用接收
 *
 * @author jianhao.zhang01@hand-china.com 2021/01/18 23:35
 */
public class CeLovResult {

    private int total;

    private List<CeLovResultDTO> list;


    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<CeLovResultDTO> getList() {
        return list;
    }

    public void setList(List<CeLovResultDTO> list) {
        this.list = list;
    }


}
