package net.bncloud.uaa.security.core;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.userdetails.UserCache;
import org.springframework.security.core.userdetails.UserDetails;

public class RedisUserCache implements UserCache {

    private final StringRedisTemplate stringRedisTemplate;

    public RedisUserCache(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public UserDetails getUserFromCache(String username) {
        return null;
    }

    @Override
    public void putUserInCache(UserDetails user) {

    }

    @Override
    public void removeUserFromCache(String username) {

    }
}
