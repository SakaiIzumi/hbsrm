package net.bncloud.service.api.platform.supplier.feign;

import net.bncloud.common.api.R;
import net.bncloud.security.client.AuthorizedFeignClient;
import net.bncloud.service.api.platform.supplier.dto.OaSupplierDTO;
import net.bncloud.service.api.platform.supplier.feign.fallback.OaSupplierFeignClientFallbackFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@AuthorizedFeignClient(name = "platform", contextId = "oaSupplierClient", fallbackFactory = OaSupplierFeignClientFallbackFactory.class, decode404 = true)
public interface OaSupplierFeignClient {


    @PostMapping("/oa_supplier/batchHandlePush")
    R batchHandlePush(@RequestBody List<OaSupplierDTO> oaSupplierDTOS);




}
