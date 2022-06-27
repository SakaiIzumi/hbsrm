package net.bncloud.event.publisher;

import com.alibaba.fastjson.JSONObject;
import net.bncloud.event.AbstractBncEvent;
import net.bncloud.event.BizEvent;
import net.bncloud.event.InternalBncEvent;
import net.bncloud.event.SmsBizEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.io.Serializable;


public class DefaultEventPublisher implements ApplicationEventPublisherAware, ApplicationEventPublisher {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultEventPublisher.class);

    private ApplicationEventPublisher publisher;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.publisher = applicationEventPublisher;
    }

    @Override
    public void publishEvent(ApplicationEvent event) {
        publishEvent((Object) event);
    }

    @Override
    public void publishEvent(Object event) {
        if (event instanceof AbstractBncEvent) {
            log((AbstractBncEvent<? extends Serializable>) event);
        }
        if (event instanceof InternalBncEvent) {
            publisher.publishEvent(event);
        }
        if (event instanceof BizEvent) {
            publishBizEvent((BizEvent<? extends Serializable>) event);
        }
        if (event instanceof SmsBizEvent) {
            publishSmsBizEvent((SmsBizEvent<? extends Serializable>) event);
        }
    }

    private void log(AbstractBncEvent<? extends Serializable> event) {
        LOGGER.info("Publish Bnc Event: {}", event.toString());
    }

    private void publishBizEvent(BizEvent<? extends Serializable> event) {
        LOGGER.info("jsonString: {}", JSONObject.toJSONString(event));
//        System.err.println(JSONObject.toJSONString(event));
        stringRedisTemplate.convertAndSend("chat", JSONObject.toJSONString(event));
    }

    private void publishSmsBizEvent(SmsBizEvent<? extends Serializable> event) {
        LOGGER.info("jsonString: {}", JSONObject.toJSONString(event));
//        System.err.println(JSONObject.toJSONString(event));
        stringRedisTemplate.convertAndSend("sms", JSONObject.toJSONString(event));
    }
}
