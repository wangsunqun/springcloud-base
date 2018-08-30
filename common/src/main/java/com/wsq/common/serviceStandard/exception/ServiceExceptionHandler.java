package com.wsq.common.serviceStandard.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ServiceExceptionHandler {
    private Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    @ExceptionHandler(Exception.class)
    public void handlerException(Exception exception) throws Exception {
        logger.error("全局异常拦截:", exception);
        throw exception;
    }
}