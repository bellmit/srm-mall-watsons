package org.srm.mall.order.app.service;

import org.srm.mall.order.api.dto.PreRequestOrderDTO;
import org.srm.mall.order.domain.vo.PurchaseRequestVO;
import org.srm.mall.other.api.dto.WatsonsPreRequestOrderDTO;

import java.util.List;

public interface WatsonsOmsOrderService {

    PurchaseRequestVO watsonsCreateOrder(Long tenantId, List<WatsonsPreRequestOrderDTO> preRequestOrderDTOs);
}
