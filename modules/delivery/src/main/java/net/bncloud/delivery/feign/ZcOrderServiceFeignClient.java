package net.bncloud.delivery.feign;

import net.bncloud.common.api.R;
import net.bncloud.delivery.feign.fallback.ZcOrderServiceFeignClientFallbackFactory;
import net.bncloud.delivery.param.StockParam;
import net.bncloud.security.client.AuthorizedFeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @ClassName ZcOrderServiceFeignClient
 * @Description: 订单服务feign
 * @Author Administrator
 * @Date 2021/4/12
 * @Version V1.0
 **/
@AuthorizedFeignClient(name = "zc-order", contextId = "zcOrderClient" ,fallbackFactory = ZcOrderServiceFeignClientFallbackFactory.class, decode404 = true)
public interface ZcOrderServiceFeignClient {

    /**
     * 扣减库存
     * @param stockParamList
     * @return
     */
    @PostMapping("/zc/order/product-details/deliverGoodsStock")
    R subtractionProductStock(@RequestBody List<StockParam> stockParamList);

    /**
     * 释放库存
     * @param stockParamList
     * @return
     */
    @PostMapping("/zc/order/product-details/releaseStock")
    R addProductStock(@RequestBody List<StockParam> stockParamList);

}
