package net.bncloud.delivery.feign.fallback;

import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import net.bncloud.common.api.R;
import net.bncloud.delivery.feign.InformationServiceFeignClient;
import org.springframework.stereotype.Component;

/**
 * @ClassName InformationServiceFeignClientFallbackFactory
 * @Description: 基础平台服务异常处理
 * @Author Administrator
 * @Date 2021/4/7
 * @Version V1.0
 **/
@Component
@Slf4j
public  class InformationServiceFeignClientFallbackFactory implements FallbackFactory<InformationServiceFeignClient> {


    @Override
    public InformationServiceFeignClient create(Throwable throwable) {
        log.error("消息服务异常",throwable);
        return (code) -> R.fail("消息服务暂时不可用，获取参数信息失败，请稍后再试");
    }
}
