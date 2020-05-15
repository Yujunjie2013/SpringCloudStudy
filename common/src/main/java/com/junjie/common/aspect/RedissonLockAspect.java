package com.junjie.common.aspect;

import com.junjie.common.lock.RedisLock;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Aspect
@Component
@Order(1)//该order必须设置，很关键
public class RedissonLockAspect {
    private Logger log = LoggerFactory.getLogger(getClass());
    @Resource
    private Redisson redisson;

    @Around("@annotation(redissonLock)")
    public Object around(ProceedingJoinPoint joinPoint, RedisLock redissonLock) throws Throwable {

        //方法内的所有参数
        Object[] params = joinPoint.getArgs();

        int lockIndex = redissonLock.lockIndex();
        //取得方法名
        String key = joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint
                .getSignature().getName();
        //-1代表锁整个方法，而非具体锁哪条数据
        if (lockIndex != -1) {
            key += params[lockIndex];
            log.info("Redis key:" + key);
        }

        RLock rLock = redisson.getLock(key);
        boolean res = rLock.tryLock(redissonLock.waitTime(), redissonLock.leaseTime(), TimeUnit.SECONDS);
        try {
            if (res) {
                log.info("取到锁成功:" + rLock.getName());
                return joinPoint.proceed();
            } else {
                log.info("----------nono----------");
                throw new RuntimeException("没有获得锁");
            }
        } finally {
            rLock.unlock();
            log.info("释放锁");
        }
    }
}
