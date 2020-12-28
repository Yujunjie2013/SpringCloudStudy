package com.central.redis.lock;

import com.junjie.common.limiter.Limiter;
import org.redisson.Redisson;
import org.redisson.api.RRateLimiter;
import org.redisson.api.RateIntervalUnit;
import org.redisson.api.RateType;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;

/**
 * 分布式限流
 */
@ConditionalOnClass(RedissonClient.class)
public class DistributedLimiter implements Limiter {

    @Autowired
    private Redisson redisson;

    @Override
    public boolean tryAcquire(String key, long rate, long rateInterval) {
        RRateLimiter rateLimiter = redisson.getRateLimiter(key);
        // 最大流速 = 每1秒钟产生10个令牌
//        rateLimiter.trySetRate(RateType.OVERALL, 10, 1, RateIntervalUnit.SECONDS);
        rateLimiter.trySetRate(RateType.OVERALL, rate, rateInterval, RateIntervalUnit.SECONDS);
        return rateLimiter.tryAcquire();
    }

    @Override
    public void release() {

    }
}
