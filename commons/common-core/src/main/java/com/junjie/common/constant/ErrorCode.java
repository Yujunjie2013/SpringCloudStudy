package com.junjie.common.constant;

public enum ErrorCode {
    SUCCESS("0000", "请求成功"),
    UN_KNOW("9999", "请求失败");


    private String status;
    private String value;

    ErrorCode(String status, String value) {
        this.status = status;
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
