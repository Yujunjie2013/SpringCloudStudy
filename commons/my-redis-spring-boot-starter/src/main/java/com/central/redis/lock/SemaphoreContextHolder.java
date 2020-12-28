package com.central.redis.lock;

import com.alibaba.ttl.TransmittableThreadLocal;

import java.util.concurrent.Semaphore;

public class SemaphoreContextHolder {
    private static final ThreadLocal<Semaphore> semaphoreThreadLocal = new TransmittableThreadLocal<>();

    public static void setSemaphore(Semaphore semaphore) {
        semaphoreThreadLocal.set(semaphore);
    }

    public static Semaphore getSemaphore() {
        return semaphoreThreadLocal.get();
    }

    public static void clear() {
        semaphoreThreadLocal.remove();
    }
}
