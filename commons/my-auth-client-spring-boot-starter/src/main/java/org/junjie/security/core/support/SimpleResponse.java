package org.junjie.security.core.support;

import lombok.Data;

import java.io.Serializable;

@Data
public class SimpleResponse<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private String status;
    private String message;
    private Object desc;
    private T data;
    private String[] msgParam;

    private SimpleResponse() {
    }

    private SimpleResponse(String status, String message) {
    }

    public static <T> SimpleResponse<T> success(String status, String message) {
        return new SimpleResponse<>(status, message);
    }

    public static <T> SimpleResponse<T> faild(String message) {
        return new SimpleResponse<>("9999", message);
    }
}
