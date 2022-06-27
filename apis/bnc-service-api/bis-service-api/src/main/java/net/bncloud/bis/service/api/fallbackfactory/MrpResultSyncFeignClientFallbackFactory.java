package net.bncloud.bis.service.api.fallbackfactory;

import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import net.bncloud.bis.service.api.feign.MrpResultSyncFeignClient;
import net.bncloud.common.api.R;
import org.springframework.stereotype.Component;

/**
 * desc: mep结果同步feignClient
 *
 * @author Rao
 * @Date 2022/05/23
 **/
@Slf4j
@Component
public class MrpResultSyncFeignClientFallbackFactory implements FallbackFactory<MrpResultSyncFeignClient> {
    @Override
    public MrpResultSyncFeignClient create(Throwable throwable) {

        log.error("[MrpResultSyncFeignClient] 服务调用失败！",throwable);

        return new MrpResultSyncFeignClient() {

            @Override
            public R<Object> syncMrpPlanOrderByErp(String computerNo) {
                return R.remoteFail();
            }
        };
    }
}
