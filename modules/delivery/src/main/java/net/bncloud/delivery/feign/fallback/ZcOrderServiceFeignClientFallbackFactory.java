package net.bncloud.delivery.feign.fallback;

import feign.hystrix.FallbackFactory;
import net.bncloud.common.api.R;
import net.bncloud.delivery.feign.ZcOrderServiceFeignClient;
import net.bncloud.delivery.param.StockParam;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @ClassName FileServiceFeignClientFallbackFactory
 * @Description: 文件中心服务异常处理
 * @Author Administrator
 * @Date 2021/4/7
 * @Version V1.0
 **/
@Component
public  class ZcOrderServiceFeignClientFallbackFactory implements FallbackFactory<ZcOrderServiceFeignClient> {

    @Override
    public ZcOrderServiceFeignClient create(Throwable throwable) {
        return new ZcOrderServiceFeignClient() {
            @Override
            public R subtractionProductStock(List<StockParam> stockParamList) {
                return R.fail("订单服务暂时不可用，请稍后再试");
            }

            @Override
            public R addProductStock(List<StockParam> stockParamList) {
                return R.fail("订单服务暂时不可用，请稍后再试");
            }
        };
    }
}
