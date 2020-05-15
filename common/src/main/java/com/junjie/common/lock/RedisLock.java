package com.junjie.common.lock;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义分布式锁
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RedisLock {
    /**
     * 默认获取锁等待时间
     */
    int waitTime() default 5;

    /**
     * 要锁哪个参数,第一个参数就是0
     */
    int lockIndex() default -1;

    /**
     * 锁多久后自动释放（单位：秒）
     */
    int leaseTime() default 10;

}
