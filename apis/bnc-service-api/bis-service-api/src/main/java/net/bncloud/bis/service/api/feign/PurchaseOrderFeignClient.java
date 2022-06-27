package net.bncloud.bis.service.api.feign;

import net.bncloud.bis.service.api.fallbackfactory.PurchaseOrderFeignClientFallbackFactory;
import net.bncloud.common.api.R;
import net.bncloud.security.client.AuthorizedFeignClient;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * desc: 采购订单执行 client
 *
 * @author Rao
 * @Date 2022/01/21
 **/
@AuthorizedFeignClient(name = "bis",path = "bis", contextId = "purchaseOrderFeignClient", fallbackFactory = PurchaseOrderFeignClientFallbackFactory.class)
public interface PurchaseOrderFeignClient {

    /**
     * 同步数据
     * @return
     */
    @Deprecated
    @PostMapping("purchaseOrder/syncData")
    R<Object> syncData();

    /**
     * 同步订单
     * @return
     */
    @PostMapping("purchaseOrder/syncOrder")
    R<Object> syncOrder();

    /**
     * 同步送货计划
     * @return
     */
    @PostMapping("purchaseOrder/syncDeliveryPlan")
    R<Object> syncDeliveryPlan();

}
