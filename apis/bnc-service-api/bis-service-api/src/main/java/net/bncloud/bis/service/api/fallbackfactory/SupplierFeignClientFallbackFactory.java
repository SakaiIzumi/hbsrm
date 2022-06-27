package net.bncloud.bis.service.api.fallbackfactory;

import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import net.bncloud.bis.service.api.feign.SupplierFeignClient;
import net.bncloud.common.api.R;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@Slf4j
public class SupplierFeignClientFallbackFactory implements FallbackFactory<SupplierFeignClient> {

    @Override
    public SupplierFeignClient create(Throwable throwable) {
        return new SupplierFeignClient() {
            @Override
            public R<Object> syncSupplierInfo(List<Integer> ids) {
                log.error("feign error!",throwable);
                return R.fail("bis暂不可用，请稍后再试");
            }

            @Override
            public R<String> syncSupplierInfoWithTable(List<Integer> ids) {
                log.error("feign error!",throwable);
                return R.fail("bis暂不可用，请稍后再试");
            }
        };
    }
}
