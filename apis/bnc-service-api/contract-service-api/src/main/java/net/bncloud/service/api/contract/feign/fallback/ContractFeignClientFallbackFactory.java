package net.bncloud.service.api.contract.feign.fallback;


import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import net.bncloud.common.api.R;
import net.bncloud.service.api.contract.dto.ContractDTO;
import net.bncloud.service.api.contract.feign.ContractFeignClient;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@Slf4j
public class ContractFeignClientFallbackFactory implements FallbackFactory<ContractFeignClient> {


    @Override
    public ContractFeignClient create(Throwable cause) {

        return new ContractFeignClient(){

            @Override
            public R<Object> syncdata (List<ContractDTO> ContractDTOList) {
                log.error("feign error!",ContractDTOList);
                return R.fail("数据服务不可用，请稍后重试！");

            }
        };
    }
}
