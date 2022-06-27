package net.bncloud.bis.service.api.fallbackfactory;

import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import net.bncloud.bis.service.api.feign.PurchaseInStockOrderFeignClient;
import net.bncloud.bis.service.api.vo.PurchaseInStockOrderCreateVo;
import net.bncloud.common.api.R;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import java.util.List;


/**
 * @author Rao
 */
@Component
@Slf4j
public class PurchaseInStockOrderFeignClientFallbackFactory implements FallbackFactory<PurchaseInStockOrderFeignClient> {

    @Override
    public PurchaseInStockOrderFeignClient create(Throwable throwable) {

        log.error("PurchaseInStockOrderFeignClient >> bis服务调用失败！",throwable);

        return new PurchaseInStockOrderFeignClient() {

            @Override
            public R<Object> syncData() {
                return R.fail();
            }

            @Override
            public R<String> createPurchaseInStockOrder(PurchaseInStockOrderCreateVo purchaseInStockOrderCreateVo) {
                return R.fail();
            }

            @Override
            public R<List<String>> batchCreatePurchaseInStockOrder(@Valid List<PurchaseInStockOrderCreateVo> purchaseInStockOrderCreateVoList) {
                return R.fail();
            }

        };
    }
}
