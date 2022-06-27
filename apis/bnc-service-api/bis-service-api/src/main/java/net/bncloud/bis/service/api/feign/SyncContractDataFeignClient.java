package net.bncloud.bis.service.api.feign;

import net.bncloud.bis.service.api.fallbackfactory.SyncContractDataFeignClientFallbackFactory;
import net.bncloud.bis.service.api.fallbackfactory.SyncMaterialFeignClientFallbackFactory;
import net.bncloud.common.api.R;
import net.bncloud.security.client.AuthorizedFeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@AuthorizedFeignClient(name = "bis",path = "bis", contextId = "syncContractDataFeignClient", fallbackFactory = SyncContractDataFeignClientFallbackFactory.class, decode404 = true)
public interface SyncContractDataFeignClient {
    @GetMapping("/syncContract/syncContractData")
    R<Object> syncContractData();
}
