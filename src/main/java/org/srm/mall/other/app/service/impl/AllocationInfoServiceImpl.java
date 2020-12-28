package org.srm.mall.other.app.service.impl;

import io.choerodon.core.exception.CommonException;
import org.apache.commons.collections4.CollectionUtils;
import org.hzero.core.base.BaseAppService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.srm.mall.other.api.dto.AllocationInfoDTO;
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


    private static final Logger logger = LoggerFactory.getLogger(AllocationInfoServiceImpl.class);


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
                allocationInfo.setTotalAmount(allocationInfo.getPrice().multiply(new BigDecimal(allocationInfo.getQuantity())));
            }
        }
        return allocationInfoList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<AllocationInfo> create(Long tenantId, WatsonsShoppingCart watsonsShoppingCart) {
        List<AllocationInfo> allocationInfoList = watsonsShoppingCart.getAllocationInfoList();
        if(CollectionUtils.isEmpty(allocationInfoList)){
            logger.error("未进行费用分配！:{}", allocationInfoList);
            throw new CommonException("未进行费用分配!");
        }
        for (AllocationInfo allocationInfo : allocationInfoList){
            //查询对应的地址，待确认查询逻辑,此处先写成-1
            allocationInfo.setAddressId(876578765352876L);

            if (allocationInfo.getAllocationId() == null){
                allocationInfoRepository.insertSelective(allocationInfo);
            } else {
                allocationInfoRepository.updateByPrimaryKeySelective(allocationInfo);
            }
        }
        //校验对应的地址商品是否可售,等待价格服务提供接口

        //合并相同数据
        watsonsShoppingCart.setAllocationInfoList(mergeSameAllocationInfo(watsonsShoppingCart));
        return allocationInfoList;
    }

    private List<AllocationInfo> mergeSameAllocationInfo(WatsonsShoppingCart watsonsShoppingCart) {
        List<AllocationInfo> result = new ArrayList<>();
        List<AllocationInfo> allocationInfoList = allocationInfoRepository.select(AllocationInfo.FIELD_CART_ID, watsonsShoppingCart.getCartId());
        //按费用承担店铺id  费用承担部门id 仓转店收获仓id分组  该行商品对应的费用分配
        //按费用承担店铺id  费用承担部门id 仓转店收获仓id  这三个维度作为相同数据
        Map<String, List<AllocationInfo>> allocationMap = allocationInfoList.stream().collect(Collectors.groupingBy(AllocationInfo::groupKey));
        for (Map.Entry<String, List<AllocationInfo>> entry : allocationMap.entrySet()){
            List<AllocationInfo> tempInfoList = entry.getValue();
            if (tempInfoList.size() > 1){
                AllocationInfo allocationInfo = tempInfoList.get(0);
                for (int i=1; i < tempInfoList.size(); i++){
                    //每个相同组的  费用分配组的第一个费用分配的数量设置为自己和该相同组的其他剩余费用分配的数量  即合并
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
        watsonsShoppingCart.setAllocationInfoList(allocationInfoList);
        return watsonsShoppingCart;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public WatsonsShoppingCart remove(WatsonsShoppingCart watsonsShoppingCart) {
        if (!CollectionUtils.isEmpty(watsonsShoppingCart.getAllocationInfoList())){
            allocationInfoRepository.batchDeleteByPrimaryKey(watsonsShoppingCart.getAllocationInfoList());
            updateShoppingCart(watsonsShoppingCart);
        }
        return watsonsShoppingCart;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AllocationInfoDTO batchCreate(Long organizationId, AllocationInfoDTO allocationInfoDTO) {
        //传来的是多个商品，每个商品会带自己的所要购买的全部数量    传来的多个费用分配， 多个商品用同一套费用分配   每个商品都要和所有的费用分配进行计算与分配

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
            //在该商品下    遍历所有费用分配信息
            for (AllocationInfo allocationInfo : allocationInfoDTO.getAllocationInfoList()){
                //每个商品，在所有的费用分配下        承担的数量      watsonsShoppingCart.getQuantity()是这个商品需要的总数量
                BigDecimal quantity = new BigDecimal(watsonsShoppingCart.getQuantity()).multiply(allocationInfo.getPercent()).divide(new BigDecimal(100), 5, BigDecimal.ROUND_HALF_UP);
                //判断数量是否有小数，若有小数则抛异常
                if (new BigDecimal(quantity.intValue()).compareTo(quantity) != 0){
                    throw new CommonException("商品：" + watsonsShoppingCart.getProductName() + "的百分比:" + allocationInfo.getPercent() + "数量计算为小数");
                }
                AllocationInfo result = new AllocationInfo();
                BeanUtils.copyProperties(allocationInfo, result);
                result.setQuantity(quantity.longValue());
                result.setPrice(watsonsShoppingCart.getLatestPrice());
                result.setCartId(watsonsShoppingCart.getCartId());
                resultList.add(result);
            }
            //此时该商品已经和所有的费用分配信息生成了一个费用分配行放入了resultList
            //2. 插入费用分配行
            watsonsShoppingCart.setAllocationInfoList(resultList);
            create(organizationId, watsonsShoppingCart);
        }
        return allocationInfoDTO;
    }

}
