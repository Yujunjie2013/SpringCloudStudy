package com.junjie.common.lock;

import java.util.concurrent.TimeUnit;

/**
 * 分布式锁顶级接口
 */
public interface DistributedLock {
    boolean tryLock(String key, long waitTime, TimeUnit timeUnit, boolean isFair);

    boolean tryLock(String key, long waitTime, long leaseTime, TimeUnit timeUnit, boolean isFair);

    void unlock();
}
