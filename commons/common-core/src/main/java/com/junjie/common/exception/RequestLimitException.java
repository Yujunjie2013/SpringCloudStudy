package com.junjie.common.exception;

/**
 * 定义超过并发数异常
 */
public class RequestLimitException extends RuntimeException {
    public RequestLimitException(String message) {
        super(message);
    }

    public RequestLimitException() {
    }
}
