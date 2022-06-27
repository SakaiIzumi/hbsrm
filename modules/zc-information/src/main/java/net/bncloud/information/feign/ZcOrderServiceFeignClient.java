package net.bncloud.information.feign;

import net.bncloud.common.api.R;
import net.bncloud.information.feign.fallback.ZcContractClientFallbackFactory;
import net.bncloud.information.feign.fallback.ZcOrderClientFallbackFactory;
import net.bncloud.security.client.AuthorizedFeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;


@AuthorizedFeignClient(name = "zc-order", contextId = "ZcOrderServiceFeignClient" ,fallbackFactory = ZcOrderClientFallbackFactory.class, decode404 = true)
public interface ZcOrderServiceFeignClient {


    @PostMapping("/zc/order/getMsgCount")
    R getMsgCount();


}
