package com.central.redis.lock;

import com.junjie.common.limiter.Limiter;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * 单机限流，基于Semaphore信号量来控制
 */
public class SemaphoreLimiter implements Limiter {

    private final ConcurrentHashMap<String, Semaphore> semaphoreMap = new ConcurrentHashMap<>();


    @Override
    public boolean tryAcquire(String key, long rate, long rateInterval) {
        Semaphore semaphore;
        if ((semaphore = semaphoreMap.get(key)) == null) {
            semaphoreMap.put(key, semaphore = new Semaphore((int) rate));
        }
        try {
            boolean b = semaphore.tryAcquire(rateInterval, TimeUnit.SECONDS);
            if (b) {
                SemaphoreContextHolder.setSemaphore(semaphore);
            }
            return b;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void release() {
        Semaphore semaphore = SemaphoreContextHolder.getSemaphore();
        if (semaphore != null) {
            semaphore.release();
        }
    }
}
