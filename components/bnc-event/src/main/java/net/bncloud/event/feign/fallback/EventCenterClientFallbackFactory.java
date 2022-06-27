package net.bncloud.event.feign.fallback;

import feign.hystrix.FallbackFactory;
import net.bncloud.api.event.PublishEventRequest;
import net.bncloud.common.api.R;
import net.bncloud.event.feign.EventCenterClient;
import org.springframework.stereotype.Component;

@Component
public class EventCenterClientFallbackFactory implements FallbackFactory<EventCenterClient> {
    @Override
    public EventCenterClient create(Throwable throwable) {
        return new EventCenterClient() {
            @Override
            public R<Void> addEvent(PublishEventRequest request) {
                return R.fail("用户服务暂时不可用，获取用户信息失败，请稍后再试");
            }
        };
    }
}
