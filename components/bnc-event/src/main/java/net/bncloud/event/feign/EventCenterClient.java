package net.bncloud.event.feign;

import net.bncloud.api.event.PublishEventRequest;
import net.bncloud.common.api.R;
import net.bncloud.event.feign.fallback.EventCenterClientFallbackFactory;
import net.bncloud.security.client.AuthorizedFeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@AuthorizedFeignClient(name = "event-center", contextId = "eventCenterClient", fallbackFactory = EventCenterClientFallbackFactory.class, decode404 = true)
public interface EventCenterClient {

    @PostMapping("/event-center/events/publish")
    R<Void> addEvent(@RequestBody PublishEventRequest request);
}
