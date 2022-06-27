package net.bncloud.bis.config;

import net.bncloud.common.base.globallock.DistributedLock;
import net.bncloud.integrated.redisson.globallock.RedissonDistributedLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * desc: 分布式锁配置
 *
 * @author Rao
 * @Date 2022/01/17
 **/
@Configuration
public class DistributedLockConfig {

    /**
     * 配置分布式锁组件
     * @param redissonClient
     * @return
     */
    @Bean
    public DistributedLock distributedLock(RedissonClient redissonClient){
        return new RedissonDistributedLock( redissonClient);
    }



}
