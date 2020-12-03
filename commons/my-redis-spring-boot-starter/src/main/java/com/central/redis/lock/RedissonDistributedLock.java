package com.central.redis.lock;

import com.junjie.common.lock.DistributedLock;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

import java.util.concurrent.TimeUnit;

/**
 * redisson分布式锁实现，基本锁功能的抽象实现
 * 本接口能满足绝大部分的需求，高级的锁功能，请自行扩展或直接使用原生api
 * https://gitbook.cn/gitchat/activity/5f02746f34b17609e14c7d5a
 */
@ConditionalOnClass(RedissonClient.class)
@ConditionalOnProperty(prefix = "com.mylock", name = "lockerType", havingValue = "REDIS", matchIfMissing = true)
@Slf4j
public class RedissonDistributedLock implements DistributedLock {
    @Autowired
    private Redisson redisson;


    @Override
    public boolean tryLock(String key, long waitTime, TimeUnit timeUnit, boolean isFair) {
        RLock lock = isFair ? redisson.getFairLock(key) : redisson.getLock(key);
        try {
            return lock.tryLock(waitTime, timeUnit);
        } catch (InterruptedException e) {
            log.error("加锁失败", e);
            return false;
        }
    }

    @Override
    public boolean tryLock(String key, long waitTime, long leaseTime, TimeUnit timeUnit, boolean isFair) {
        RLock lock = isFair ? redisson.getFairLock(key) : redisson.getLock(key);
        try {
            return lock.tryLock(waitTime, leaseTime, timeUnit);
        } catch (InterruptedException e) {
            log.error("加锁失败", e);
            return false;
        }
    }

    @Override
    public void unlock() {

    }
}
