package com.junjie.common.lock;

import java.util.concurrent.TimeUnit;

/**
 * 分布式锁顶级接口
 */
public interface DistributedLock {
    Object tryLock(String key, long waitTime, TimeUnit timeUnit, boolean isFair);

    Object tryLock(String key, long waitTime, long leaseTime, TimeUnit timeUnit, boolean isFair);

    void unlock(Object lock);
}
