package net.bncloud.bis.service.api.fallbackfactory;

import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import net.bncloud.bis.service.api.dto.PurchaseReceiveBillCreateOrderDto;
import net.bncloud.bis.service.api.feign.PurchaseReceiveBillOrderFeignClient;
import net.bncloud.bis.service.api.vo.PurchaseReceiveBillCallCreateVo;
import net.bncloud.common.api.R;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * 采购收料通知单
 */
@Component
@Slf4j
public class PurchaseReceiveBillOrderFeignClientFallbackFactory implements FallbackFactory<PurchaseReceiveBillOrderFeignClient> {

    @Override
    public PurchaseReceiveBillOrderFeignClient create(Throwable throwable) {
        return new PurchaseReceiveBillOrderFeignClient() {
            @PostMapping("/createPurchaseInStock")
            @Override
            public R<PurchaseReceiveBillCallCreateVo> createPurchaseReceiveBillOrder(PurchaseReceiveBillCreateOrderDto purchaseReceiveBillCreateOrderDto) {
                log.error("feign error!",throwable);
                return R.fail("数据服务不可用，请稍后重试！");
            }
        };
    }
}
