package com.junjie.common.limiter;

public interface Limiter {
    boolean tryAcquire(String key, long rate, long rateInterval);

    void release();
}
