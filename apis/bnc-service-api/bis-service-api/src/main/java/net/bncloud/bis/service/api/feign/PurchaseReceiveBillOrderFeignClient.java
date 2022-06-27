package net.bncloud.bis.service.api.feign;

import net.bncloud.bis.service.api.dto.PurchaseReceiveBillCreateOrderDto;
import net.bncloud.bis.service.api.fallbackfactory.PurchaseReceiveBillOrderFeignClientFallbackFactory;
import net.bncloud.bis.service.api.vo.PurchaseReceiveBillCallCreateVo;
import net.bncloud.common.api.R;
import net.bncloud.security.client.AuthorizedFeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * desc: 收料通知单 Feign Client
 *
 * @author Rao
 * @Date 2022/01/17
 **/
@AuthorizedFeignClient(name = "bis",path = "bis",contextId = "purchaseReceiveBillOrderFeignClient",fallbackFactory = PurchaseReceiveBillOrderFeignClientFallbackFactory.class)
public interface PurchaseReceiveBillOrderFeignClient {

    /**
     * 创建收料通知单 (有主键Id即更新)
     * @param purchaseReceiveBillCreateOrderDto 一个送货单 对应 一个 收料通知单
     */
    @PostMapping("/createPurchaseInStock")
    R<PurchaseReceiveBillCallCreateVo> createPurchaseReceiveBillOrder(@RequestBody PurchaseReceiveBillCreateOrderDto purchaseReceiveBillCreateOrderDto);

}
