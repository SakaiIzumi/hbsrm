package net.bncloud.bis.service.api.fallbackfactory;

import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import net.bncloud.bis.service.api.feign.SyncContractDataFeignClient;
import net.bncloud.bis.service.api.feign.SyncMaterialFeignClient;
import net.bncloud.common.api.R;
import org.springframework.stereotype.Component;


@Component
@Slf4j
public class SyncContractDataFeignClientFallbackFactory implements FallbackFactory<SyncContractDataFeignClient> {
    @Override
    public SyncContractDataFeignClient create(Throwable throwable) {
        return new SyncContractDataFeignClient() {
            @Override
            public R<Object> syncContractData() {
                log.error("feign error!",throwable);
                return R.fail("数据服务不可用，请稍后重试！");
            }
        };
    }

}
