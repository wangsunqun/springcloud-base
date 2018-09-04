package com.wsq.common.base.exception;

public class SystemException extends GlobalException {
    public SystemException(String message) {
        super(-1, message);
    }
}
