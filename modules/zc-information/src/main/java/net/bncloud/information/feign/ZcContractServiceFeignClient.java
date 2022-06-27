package net.bncloud.information.feign;

import net.bncloud.common.api.R;

import net.bncloud.information.feign.fallback.ZcContractClientFallbackFactory;
import net.bncloud.security.client.AuthorizedFeignClient;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;


@AuthorizedFeignClient(name = "zc-contract", contextId = "zcContractClient" ,fallbackFactory = ZcContractClientFallbackFactory.class, decode404 = true)
public interface ZcContractServiceFeignClient {


    @GetMapping("/zc/contract/count")
    R count();


}
