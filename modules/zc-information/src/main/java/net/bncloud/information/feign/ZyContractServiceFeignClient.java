package net.bncloud.information.feign;

import net.bncloud.common.api.R;
import net.bncloud.information.feign.fallback.ZcContractClientFallbackFactory;
import net.bncloud.information.feign.fallback.ZyContractClientFallbackFactory;
import net.bncloud.security.client.AuthorizedFeignClient;
import org.springframework.web.bind.annotation.GetMapping;


@AuthorizedFeignClient(name = "zy-contract", contextId = "zyContractClient" ,fallbackFactory = ZyContractClientFallbackFactory.class, decode404 = true)
public interface ZyContractServiceFeignClient {


    @GetMapping("/zy/contract/count")
    R count();


}
