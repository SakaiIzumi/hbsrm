package net.bncloud.service.api.contract.feign;


import net.bncloud.common.api.R;
import net.bncloud.security.client.AuthorizedFeignClient;
import net.bncloud.service.api.contract.dto.ContractDTO;
import net.bncloud.service.api.contract.feign.fallback.ContractFeignClientFallbackFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;


@AuthorizedFeignClient(name = "zc-contract", contextId = "contractFeignClient", fallbackFactory = ContractFeignClientFallbackFactory.class, decode404 = true)
public interface ContractFeignClient {

    /**
     * 同步合同数据
     *
     */
    @PostMapping("/zc/contract/sync/syncdata")
    R<Object> syncdata(@RequestBody List<ContractDTO> contractDTOList);




}
