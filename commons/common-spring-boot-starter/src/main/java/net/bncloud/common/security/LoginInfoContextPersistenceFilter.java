package net.bncloud.common.security;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.util.Optional;

public class LoginInfoContextPersistenceFilter extends GenericFilterBean {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoginInfoContextPersistenceFilter.class);
    public static final String CACHE_KEY_PREFIX = "BNC:LOGIN_INFO:";

    private final StringRedisTemplate stringRedisTemplate;
    public LoginInfoContextPersistenceFilter(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    //过滤器-每次请求都会经过这里
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        try {
            SecurityUtils.getCurrentUser().ifPresent(userDetails -> getStringRedisTemplate().ifPresent(redis -> {
                //获取redis所存用户凭证-set到当前线程
                String json = redis.opsForValue().get(CACHE_KEY_PREFIX + userDetails.getId());
                LOGGER.info("got loginInfo from redis: {}", json);
                LoginInfo loginInfo = JSONObject.parseObject(json, LoginInfo.class);
                LoginInfoContextUtils.set(loginInfo);
            }));
            filterChain.doFilter(servletRequest, servletResponse);
        } finally {
            LoginInfoContextUtils.clear();
        }
    }

    private Optional<StringRedisTemplate> getStringRedisTemplate() {
        if (stringRedisTemplate == null) {
            LOGGER.warn("stringRedisTemplate 为 null");
        }
        return Optional.ofNullable(stringRedisTemplate);
    }
}
