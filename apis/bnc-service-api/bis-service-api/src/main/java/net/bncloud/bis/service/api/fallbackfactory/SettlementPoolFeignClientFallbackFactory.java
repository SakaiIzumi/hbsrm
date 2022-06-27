package net.bncloud.bis.service.api.fallbackfactory;

import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import net.bncloud.bis.service.api.feign.SettlementPoolFeignClient;
import net.bncloud.common.api.R;
import org.springframework.stereotype.Component;


@Component
@Slf4j
public class SettlementPoolFeignClientFallbackFactory implements FallbackFactory<SettlementPoolFeignClient> {

    @Override
    public SettlementPoolFeignClient create(Throwable throwable) {
        return (timeSyncCount) -> {
            log.error("feign error!",throwable);
            return R.fail("数据服务不可用，请稍后重试！");
        };
    }
}
