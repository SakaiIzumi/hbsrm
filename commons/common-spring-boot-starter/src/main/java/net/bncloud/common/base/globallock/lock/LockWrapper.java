package net.bncloud.common.base.globallock.lock;

import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import java.util.concurrent.TimeUnit;

/**
 *
 * @author Rao
 * @Date 2021/11/16
 **/
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Setter
@Getter
@ToString
@Accessors(chain = true)
public class LockWrapper {

    private String key;
    /**
     * 等待获取锁的时间
     */
    private long waitTime = 0;
    /**
     * 拥有锁的时间
     *   默认 30s ,-1 带有看门狗续命
     */
    private long leaseTime = -1;
    /**
     * 单位
     */
    private TimeUnit unit = TimeUnit.SECONDS;

}
