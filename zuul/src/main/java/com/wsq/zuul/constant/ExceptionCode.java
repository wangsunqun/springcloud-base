package com.wsq.zuul.constant;

public enum ExceptionCode {
    SYSTEM_ERROR(-1, "系统异常"),
    REQUEST_SIGN_ERROR(1, "签名错误"),
    REQUEST_MSG_NULL(2, "请求参数为空"),
    DECODE_MSG_FAILED(3, "解析请求参数失败"),
    APP_ID_NULL(4, "appId为空"),
    ILLEGAL_ARGUMENT(5, "参数非法"),
    ILLEGAL_INTERFACE(6, "接口非法"),
    API_VERSION_NULL(7, "接口版本号为空");

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
