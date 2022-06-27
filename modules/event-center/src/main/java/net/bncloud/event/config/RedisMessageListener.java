package net.bncloud.event.config;

import net.bncloud.event.service.SmsReceiverService;
import net.bncloud.event.service.ReceiverService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;



/**
 * @Author : JCccc
 * @CreateTime : 2019-1-2
 * @Description :
 * @Point: Keep a good mood
 **/
@Configuration
public class RedisMessageListener {

    /**
     * redis消息监听器容器
     * 可以添加多个监听不同话题的redis监听器，只需要把消息监听器和相应的消息订阅处理器绑定，该消息监听器
     * 通过反射技术调用消息订阅处理器的相关方法进行一些业务处理
     * @param connectionFactory
     * @param listenerAdapter
     * @return
     */

    @Bean
    RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory,
                                            @Qualifier("listenerAdapter")MessageListenerAdapter listenerAdapter,
                                            @Qualifier("smslistenerAdapter")MessageListenerAdapter smslistenerAdapter) {

        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        //订阅了一个叫chat 的通道
        container.addMessageListener(listenerAdapter, new PatternTopic("chat"));
        //这个container 可以添加多个 messageListener
        container.addMessageListener(smslistenerAdapter, new PatternTopic("sms"));
        return container;
    }
    /**
     * 消息监听器适配器，绑定消息处理器，利用反射技术调用消息处理器的业务方法
     * @param receiverService
     * @return
     */
    @Bean(name = "listenerAdapter")
    MessageListenerAdapter listenerAdapter(ReceiverService receiverService) {
        //这个地方 是给messageListenerAdapter 传入一个消息接受的处理器，利用反射的方法调用“receiveMessage”
        //也有好几个重载方法，这边默认调用处理器的方法 叫handleMessage 可以自己到源码里面看
        //receiveMessage就是对应消费者那边的消费方法吗,而Receiver是自己弄的一个消费者类
        return new MessageListenerAdapter(receiverService, "receiveMessage");
    }

    /**
     * 发送短信消息监听器适配器，绑定消息处理器，利用反射技术调用消息处理器的业务方法
     *  @param sMSReceiverService
     * @return
     */
    @Bean(name = "smslistenerAdapter")
    MessageListenerAdapter smslistenerAdapter(SmsReceiverService sMSReceiverService) {
        //这个地方 是给messageListenerAdapter 传入一个消息接受的处理器，利用反射的方法调用“receiveMessage”
        //也有好几个重载方法，这边默认调用处理器的方法 叫handleMessage 可以自己到源码里面看
        //receiveMessage就是对应消费者那边的消费方法吗,而Receiver是自己弄的一个消费者类
        return new MessageListenerAdapter(sMSReceiverService, "receiveSmsMessage");
    }


}
