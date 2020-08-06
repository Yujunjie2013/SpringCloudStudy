package com.junjie.common.bean;

import com.junjie.common.constant.ErrorCode;
import lombok.Data;

import java.io.Serializable;

/**
 * 出参规范
 */
@Data
public class BaseResponseBody<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    private String status;
    private String message;
    private T data;

    public BaseResponseBody() {
    }

    public static <T> BaseResponseBody<T> success() {
        return success(null);
    }

    public static <T> BaseResponseBody<T> success(T data) {
        BaseResponseBody<T> body = new BaseResponseBody<>();
        body.setStatus(ErrorCode.SUCCESS.getValue());
        body.setData(data);
        return body;
    }

    public static <T> BaseResponseBody<T> failure() {
        return failure(null);
    }

    public static <T> BaseResponseBody<T> failure(T data) {
        BaseResponseBody<T> body = new BaseResponseBody<>();
        body.setStatus(ErrorCode.UN_KNOW.getValue());
        body.setData(data);
        return body;
    }

    public static <T> BaseResponseBody<T> from(ErrorCode ErrorCode) {
        return from(ErrorCode, (String[]) null);
    }

    /**
     * 返回业务错误
     *
     * @param ErrorCode 错误码
     * @param msgParam  错误码对应的信息中可变参数
     */
    public static <T> BaseResponseBody<T> from(ErrorCode ErrorCode, String... msgParam) {
        BaseResponseBody<T> body = new BaseResponseBody<>();
        body.setStatus(ErrorCode.getValue());
        return body;
    }

    @Deprecated
    public static <T> BaseResponseBody<T> msg(String msg) {
        BaseResponseBody<T> body = new BaseResponseBody<>();
        body.setMessage(msg);
        return body;
    }

    public BaseResponseBody<T> withData(T data) {
        this.data = data;
        return this;
    }
}
