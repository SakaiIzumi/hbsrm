package net.bncloud.integrated.redisson.globallock;

import lombok.extern.slf4j.Slf4j;
import net.bncloud.common.base.globallock.DistributedLock;
import net.bncloud.common.base.globallock.lock.LockWrapper;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.util.function.Supplier;

/**
 * 基于 Redisson 实现的
 *   看门狗机制的处理。
 *   不是很好用，待优化！
 * @author Rao
 * @Date 2021/11/16
 **/
@Slf4j
public class RedissonDistributedLock implements DistributedLock {

    /**
     * 需要初始化
     */
    public final RedissonClient redissonClient;

    public RedissonDistributedLock(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    /**
     * 获取锁后立即返回true 。 如果该锁当前被这个或分布式系统中的
     * 任何其他进程中的另一个线程持有，则此方法会在放弃并返回false
     * 之前一直尝试获取锁直到waitTime 。 如果获得了锁，
     * 它将一直保持到调用unlock为止，或者直到授予锁后的leaseTime已过 -
     * 以先到者为准。
     *
     * 需要控制好  leaseTime 的时长，设置该值不会 看门狗不会激活，
     *
     * @param lockWrapper
     * @param successLockSupplier
     * @param <T>
     * @return
     */
    @Override
    public <T> T tryLock(LockWrapper lockWrapper, Supplier<T> successLockSupplier, Supplier<T> failLockSupplier){

        // 没拿到锁的线程 调用 unlock 会抛出异常
        RLock lock = redissonClient.getLock(lockWrapper.getKey());

        try {
            // 尝试等待到 waitTime 时间
            if (lock.tryLock(lockWrapper.getWaitTime(), lockWrapper.getLeaseTime(), lockWrapper.getUnit())) {
                try {
                    return successLockSupplier.get();
                } finally {
                    try {
                        lock.unlock();
                    } catch (Exception ex){
                        //  抛出锁的问题 人工处理。
                        log.error("[DistributedLock] The dog is useless!",ex);
                    }
                }
            }
        } catch (InterruptedException e) {
            // 被中断，当作抢锁失败处理，无关紧要。
            log.warn("[DistributedLock] The lock be interrupted,but not get lock! ");
        } catch (Exception ex){
            // 业务执行异常
            log.error("[DistributedLock] error ! ",ex);
            throw ex;
        }
        return failLockSupplier.get();

    }


}
