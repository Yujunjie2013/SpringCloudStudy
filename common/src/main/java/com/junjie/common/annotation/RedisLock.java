package com.junjie.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * 自定义分布式锁
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RedisLock {
    /**
     * 默认获取锁等待时间
     */
    int waitTime() default 5000;

    /**
     * 锁标识
     * <p>
     * RedisLock(key = "'customerInfoByPhone_' + #phone")，表示取方法中参数名为phone的值
     */
    String key() default "";

    /**
     * 锁多久后自动释放,默认10000自动释放,可以一定程度防止死锁；设置-1表示不会自动释放
     */
    int leaseTime() default 10000;

    /**
     * 时间单位
     */
    TimeUnit timeUnit() default TimeUnit.MILLISECONDS;

}
