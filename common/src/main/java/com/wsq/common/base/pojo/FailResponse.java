package com.wsq.common.base.pojo;

public class FailResponse extends Result{
    public FailResponse(int code, Object data, String msg) {
        super(code, data, msg);
    }
}