package net.bncloud.common.base.globallock;


import net.bncloud.common.base.globallock.lock.LockWrapper;

import java.util.function.Supplier;

/**
 * @author Rao
 * @Date 2021/12/06
 **/
public interface DistributedLock {

    /**
     * tryLock
     * @param lockWrapper
     * @param successLockSupplier
     * @param failLockSupplier
     * @param <T>
     * @return
     */
    <T> T tryLock(LockWrapper lockWrapper, Supplier<T> successLockSupplier, Supplier<T> failLockSupplier);

}
