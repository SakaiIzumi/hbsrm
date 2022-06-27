package net.bncloud.event.service;

import com.alibaba.fastjson.JSONObject;
import net.bncloud.api.event.PublishEventRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SmsReceiverService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SmsReceiverService.class);

    @Autowired
    private EventService eventService;
        /**
         * 接收到消息的方法，message就是指从主题获取的消息，主题配置在RedisMessageListener配置类做配置
         * @param message
         */
    public void receiveSmsMessage(String message) {
        LOGGER.info("接收消息体 <" + message + ">");

        JSONObject event = JSONObject.parseObject(message);
        PublishEventRequest request = new PublishEventRequest();
        request.setEventCode(event.getString("eventCode"));
        request.setUserId(event.getJSONObject("loginInfo").getLong("id"));
        request.setUserName(event.getJSONObject("loginInfo").getString("name"));
        request.setEventData(event.getString("data"));
        request.setSources(event.getString("sourcesName"));
        request.setSourcesCode(event.getString("sources"));
        eventService.publishEvent(request);
    }

}
