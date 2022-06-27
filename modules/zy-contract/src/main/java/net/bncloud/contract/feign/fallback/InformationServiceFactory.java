package net.bncloud.contract.feign.fallback;

import feign.hystrix.FallbackFactory;
import net.bncloud.common.api.R;
import net.bncloud.contract.feign.InformationServiceFeignClient;
import org.springframework.stereotype.Component;

@Component
public class InformationServiceFactory implements FallbackFactory<InformationServiceFeignClient> {
    @Override
    public InformationServiceFeignClient create(Throwable throwable) {
        System.out.println(throwable.getMessage());


        return null;
    }
}
