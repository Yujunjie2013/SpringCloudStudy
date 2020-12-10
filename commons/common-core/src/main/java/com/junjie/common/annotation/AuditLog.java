package com.junjie.common.annotation;

import java.lang.annotation.*;

/**
 * 审计日志操作注解
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AuditLog {
    /**
     * 操作信息
     */
    String operation();
}
