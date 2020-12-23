package org.srm.mall.other.app.service.impl;

import io.choerodon.core.domain.Page;
import io.choerodon.core.exception.CommonException;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.apache.commons.collections4.CollectionUtils;
import org.hzero.core.base.BaseAppService;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.srm.mall.other.api.dto.AllocationInfoDTO;
import org.srm.mall.other.api.dto.OrganizationInfoDTO;
import org.srm.mall.other.app.service.AllocationInfoService;
import org.srm.mall.other.app.service.ShoppingCartService;
import org.srm.mall.other.domain.entity.AllocationInfo;
import org.srm.mall.other.domain.entity.BudgetInfo;
import org.srm.mall.other.domain.entity.WatsonsShoppingCart;
import org.srm.mall.other.domain.repository.AllocationInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 屈臣氏费用分配表应用服务默认实现
 *
 * @author yuewen.wei@hand-china.com 2020-12-21 15:35:27
 */
@Service
public class AllocationInfoServiceImpl extends BaseAppService implements AllocationInfoService {

    @Autowired
    private AllocationInfoRepository allocationInfoRepository;

    @Autowired
    @Qualifier("watsonsShoppingCartService")
    private ShoppingCartService shoppingCartService;

    @Override
    public List<AllocationInfo> list(Long tenantId, Long cartId) {
        List<AllocationInfo> allocationInfoList = allocationInfoRepository.select(AllocationInfo.FIELD_CART_ID, cartId);
        if (!CollectionUtils.isEmpty(allocationInfoList)){
            for (AllocationInfo allocationInfo : allocationInfoList){
                allocationInfo.setAmount(allocationInfo.getPrice().multiply(new BigDecimal(allocationInfo.getQuantity())));
            }
        }
        return allocationInfoList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<AllocationInfo> create(Long tenantId, WatsonsShoppingCart watsonsShoppingCart) {
        List<AllocationInfo> allocationInfoList = watsonsShoppingCart.getCostAllocationInfoList();
        for (AllocationInfo allocationInfo : allocationInfoList){
            if (allocationInfo.getAllocationId() == null){
                //查询对应的地址,此处先写成-1
                allocationInfo.setAddressId(-1L);
                allocationInfoRepository.insertSelective(allocationInfo);
            } else {
                allocationInfoRepository.updateByPrimaryKeySelective(allocationInfo);
            }
        }
        //校验对应的地址商品是否可售
        //合并相同数据
        watsonsShoppingCart.setCostAllocationInfoList(mergeSameAllocationInfo(watsonsShoppingCart));
        return allocationInfoList;
    }

    private List<AllocationInfo> mergeSameAllocationInfo(WatsonsShoppingCart watsonsShoppingCart) {
        List<AllocationInfo> result = new ArrayList<>();
        List<AllocationInfo> allocationInfoList = allocationInfoRepository.select(AllocationInfo.FIELD_CART_ID, watsonsShoppingCart.getCartId());
        Map<String, List<AllocationInfo>> allocationMap = allocationInfoList.stream().collect(Collectors.groupingBy(AllocationInfo::groupKey));
        for (Map.Entry<String, List<AllocationInfo>> entry : allocationMap.entrySet()){
            List<AllocationInfo> tempInfoList = entry.getValue();
            if (tempInfoList.size() > 1){
                AllocationInfo allocationInfo = tempInfoList.get(0);
                for (int i=1; i < tempInfoList.size(); i++){
                    allocationInfo.setQuantity(allocationInfo.getQuantity() + tempInfoList.get(i).getQuantity());
                    allocationInfoRepository.deleteByPrimaryKey(tempInfoList.get(i));
                }
                allocationInfoRepository.updateByPrimaryKey(allocationInfo);
                result.add(allocationInfo);
            } else {
                result.addAll(tempInfoList);
            }
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public WatsonsShoppingCart updateShoppingCart(WatsonsShoppingCart watsonsShoppingCart) {
        List<AllocationInfo> allocationInfoList = allocationInfoRepository.select(BudgetInfo.FIELD_CART_ID, watsonsShoppingCart.getCartId());
        BigDecimal quantity = BigDecimal.ZERO;
        if (!CollectionUtils.isEmpty(allocationInfoList)) {
            for (AllocationInfo allocationInfo : allocationInfoList) {
                quantity = quantity.add(new BigDecimal(allocationInfo.getQuantity()));
            }
            watsonsShoppingCart.setQuantity(quantity.longValue());
            shoppingCartService.create(watsonsShoppingCart);
        }
        watsonsShoppingCart.setCostAllocationInfoList(allocationInfoList);
        return watsonsShoppingCart;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public WatsonsShoppingCart remove(WatsonsShoppingCart watsonsShoppingCart) {
        if (!CollectionUtils.isEmpty(watsonsShoppingCart.getCostAllocationInfoList())){
            allocationInfoRepository.batchDeleteByPrimaryKey(watsonsShoppingCart.getCostAllocationInfoList());
            updateShoppingCart(watsonsShoppingCart);
        }
        return watsonsShoppingCart;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AllocationInfoDTO batchCreate(Long organizationId, AllocationInfoDTO allocationInfoDTO) {
        //1. 计算每个商品数量
        if (CollectionUtils.isEmpty(allocationInfoDTO.getAllocationInfoList()) || CollectionUtils.isEmpty(allocationInfoDTO.getWatsonsShoppingCartList())){
            throw new CommonException("参数不能为空");
        }
        //百分比校验是否等于100%
        BigDecimal sumPercent = BigDecimal.ZERO;
        for (AllocationInfo allocationInfo : allocationInfoDTO.getAllocationInfoList()){
            sumPercent = sumPercent.add(allocationInfo.getPercent());
        }
        if (sumPercent.compareTo(new BigDecimal(100)) != 0){
            throw new CommonException("百分比不满足100%");
        }
        for (WatsonsShoppingCart watsonsShoppingCart : allocationInfoDTO.getWatsonsShoppingCartList()){
            //清空原来的分配
            AllocationInfo condition = new AllocationInfo();
            condition.setCartId(watsonsShoppingCart.getCartId());
            allocationInfoRepository.delete(condition);
            List<AllocationInfo> resultList = new ArrayList<>();
            for (AllocationInfo allocationInfo : allocationInfoDTO.getAllocationInfoList()){
                BigDecimal quantity = new BigDecimal(watsonsShoppingCart.getQuantity()).multiply(allocationInfo.getPercent());
                //判断数量是否有小数，若有小数则抛异常
                if (new BigDecimal(quantity.intValue()).compareTo(quantity) != 0){
                    throw new CommonException("商品：" + watsonsShoppingCart.getProductName() + "的百分比:" + allocationInfo.getPercent() + "数量计算为小数");
                }
                AllocationInfo result = new AllocationInfo();
                BeanUtils.copyProperties(allocationInfo, result);
                result.setQuantity(quantity.longValue());
                resultList.add(result);
            }
            //2. 插入费用分配行
            watsonsShoppingCart.setCostAllocationInfoList(resultList);
            create(organizationId, watsonsShoppingCart);
        }
        return allocationInfoDTO;
    }

}
