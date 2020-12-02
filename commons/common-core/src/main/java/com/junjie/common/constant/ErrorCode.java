package com.junjie.common.constant;

public enum ErrorCode {
    SUCCESS("0000"),
    UN_KNOW("9999");


    private String value;

    ErrorCode(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
