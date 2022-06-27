package net.bncloud.information.feign;

import net.bncloud.common.api.R;
import net.bncloud.information.feign.fallback.ZcOrderClientFallbackFactory;
import net.bncloud.information.feign.fallback.ZyOrderClientFallbackFactory;
import net.bncloud.security.client.AuthorizedFeignClient;
import org.springframework.web.bind.annotation.PostMapping;


@AuthorizedFeignClient(name = "zy-order", contextId = "ZyOrderServiceFeignClient" ,fallbackFactory = ZyOrderClientFallbackFactory.class, decode404 = true)
public interface ZyOrderServiceFeignClient {


    @PostMapping("/zy/order/getMsgCount")
    R getMsgCount();


}
