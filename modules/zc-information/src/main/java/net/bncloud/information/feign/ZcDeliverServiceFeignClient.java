package net.bncloud.information.feign;

import net.bncloud.common.api.R;
import net.bncloud.information.feign.fallback.ZcContractClientFallbackFactory;
import net.bncloud.information.feign.fallback.ZcDeliverServiceFallbackFactory;
import net.bncloud.security.client.AuthorizedFeignClient;
import org.springframework.web.bind.annotation.GetMapping;


@AuthorizedFeignClient(name = "zc-delivery", contextId = "ZcDeliverServiceFeignClient" ,fallbackFactory = ZcDeliverServiceFallbackFactory.class, decode404 = true)
public interface ZcDeliverServiceFeignClient {


    @GetMapping("/zc/delivery/delivery-note/statistics")
    R getMsgCount();


}
