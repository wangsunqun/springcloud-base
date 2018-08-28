package com.wsq.common.base.exception;

public class SystemException extends GlobalException {
    public SystemException(String message) {
        super(ExceptionCode.SYSTEM_ERROR.getCode(), message);
    }
}
