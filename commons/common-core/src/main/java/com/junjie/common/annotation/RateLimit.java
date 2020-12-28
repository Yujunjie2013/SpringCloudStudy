package com.junjie.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RateLimit {
    /**
     * 锁标识
     * <p>
     * RateLimit(key = "'customerInfoByPhone_' + #phone")，表示取方法中参数名为phone的值
     */
    String key() default "";

    /**
     * 流量控制
     *
     * @return 默认10
     */
    long rate() default 10;

    /**
     * 默认等待时长
     *
     * @return 1
     */
    long timeOut() default 1;
}
