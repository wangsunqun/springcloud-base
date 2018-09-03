package com.wsq.common.base.exception;

public enum ExceptionCode {
    SYSTEM_ERROR(-1, "系统异常"),
    RATELIMIT_ERROR(500, "接口被限流");

    private int code;
    private String msg;

    ExceptionCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
