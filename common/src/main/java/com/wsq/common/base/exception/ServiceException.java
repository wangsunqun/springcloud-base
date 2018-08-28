package com.wsq.common.base.exception;

public class ServiceException extends GlobalException {
    public ServiceException(int code, String message) {
        super(code, message);
    }
}
