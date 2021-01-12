package org.srm.mall.common.feign.fallback;

import feign.hystrix.FallbackFactory;
import io.choerodon.core.domain.Page;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.srm.mall.common.feign.SmdmRemoteNewService;
import org.srm.mall.common.feign.SmdmRemoteService;
import org.srm.mall.context.dto.ItemListDTO;
import org.srm.mall.other.api.dto.WatsonsItemCategorySearchDTO;
import org.srm.mall.product.api.dto.ItemCategoryDTO;
import org.srm.mall.product.api.dto.ItemCategorySearchDTO;

import java.util.List;


/**
 * SMDM远程服务失败回调
 *
 * @author minzhen.you@hand-china.com 2019年3月21日下午2:12:06
 */
@Component
public class SmdmRemoteNewServiceFallbackImpl implements FallbackFactory<SmdmRemoteNewService> {

    private static final Logger logger = LoggerFactory.getLogger(SmdmRemoteServiceFallbackImpl.class);

    @Override
    public SmdmRemoteNewService create(Throwable throwable) {
        return new SmdmRemoteNewService() {

            @Override
            public ResponseEntity<String> selectSecondaryByThirdItemCategory(Long organizationId, @RequestParam("queryCategoryId") String queryCategoryId) {
                logger.error("query Secondary ItemCategory By Third ItemCategory error! param itemCategoryId:{}",queryCategoryId);
                return null;
            }

            @Override
            public ResponseEntity<String> queryById(Long organizationId, String queryCategoryId) {
                logger.error("query itemCategory info By itemCategoryId error! param itemCategoryId :{}",queryCategoryId);
                return null;
            }

            @Override
            public ResponseEntity<String> queryLevelPathByItemId(Long organizationId, Long itemId) {
                logger.error("query LevelPath By itemId error! param itemId :{}",itemId);
                return null;
            }
        };
    }
}
