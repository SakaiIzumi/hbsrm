package net.bncloud.information.feign;

import net.bncloud.common.api.R;
import net.bncloud.information.feign.fallback.ZcDeliverServiceFallbackFactory;
import net.bncloud.information.feign.fallback.ZyDeliverServiceFallbackFactory;
import net.bncloud.security.client.AuthorizedFeignClient;
import org.springframework.web.bind.annotation.GetMapping;


@AuthorizedFeignClient(name = "zy-delivery", contextId = "ZyDeliverServiceFeignClient" ,fallbackFactory = ZyDeliverServiceFallbackFactory.class, decode404 = true)
public interface ZyDeliverServiceFeignClient {


    @GetMapping("/zy/delivery/delivery-note/statistics")
    R getMsgCount();


}
