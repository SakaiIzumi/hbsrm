package net.bncloud.logging.support;

import com.alibaba.fastjson.JSONObject;
import net.bncloud.logging.LogConstants;
import net.bncloud.logging.LogManager;
import net.bncloud.logging.context.LogContext;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RedisChannelLogManager implements LogManager {

    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    private final StringRedisTemplate stringRedisTemplate;

    public RedisChannelLogManager(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    public void setExecutorService(ExecutorService executorService) {
        this.executorService = executorService;
    }

    @Override
    public void record(LogContext logContext) {
        executorService.execute(() -> stringRedisTemplate.convertAndSend(LogConstants.REDIS_CHANNEL_SYS_LOG, JSONObject.toJSONString(logContext)));
    }
}
