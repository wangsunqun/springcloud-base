package com.wsq.common.base.exception;

public enum ExceptionCode {
    //系统异常
    SYSTEM_ERROR(-1);

    private int code;

    ExceptionCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
