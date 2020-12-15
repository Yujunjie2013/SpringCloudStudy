package com.junjie.common.bean;

import com.junjie.common.constant.ErrorCode;
import lombok.Data;

import java.io.Serializable;

/**
 * 出参规范
 */
@Data
public class Result<T> implements Serializable {

    private String status;
    private String message;
    private T data;

    public Result() {
    }

    public static <T> Result<T> succeed() {
        return succeed(null);
    }

    public static <T> Result<T> succeed(T data) {
        Result<T> body = new Result<>();
        body.setStatus(ErrorCode.SUCCESS.getValue());
        body.setData(data);
        return body;
    }

    public static <T> Result<T> failed() {
        return failed(null);
    }

    public static <T> Result<T> failed(T data) {
        Result<T> body = new Result<>();
        body.setStatus(ErrorCode.UN_KNOW.getValue());
        body.setData(data);
        return body;
    }

    public static <T> Result<T> from(ErrorCode ErrorCode) {
        return from(ErrorCode, (String[]) null);
    }

    /**
     * 返回业务错误
     *
     * @param ErrorCode 错误码
     * @param msgParam  错误码对应的信息中可变参数
     */
    public static <T> Result<T> from(ErrorCode ErrorCode, String... msgParam) {
        Result<T> body = new Result<>();
        body.setStatus(ErrorCode.getValue());
        return body;
    }

    public static <T> Result<T> from(String status, String message) {
        Result<T> body = new Result<>();
        body.setStatus(status);
        body.setMessage(message);
        return body;
    }

    @Deprecated
    public static <T> Result<T> msg(String msg) {
        Result<T> body = new Result<>();
        body.setMessage(msg);
        return body;
    }

    public Result<T> withData(T data) {
        this.data = data;
        return this;
    }
}
