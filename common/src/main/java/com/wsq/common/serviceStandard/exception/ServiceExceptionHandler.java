package com.wsq.common.serviceStandard.exception;

import com.wsq.common.base.exception.ExceptionCode;
import com.wsq.common.base.exception.ServiceException;
import com.wsq.common.base.pojo.FailResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ServiceExceptionHandler {
    private Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    @ExceptionHandler(Throwable.class)
    public FailResponse handlerException(Throwable throwable) throws Exception {
        logger.error("全局异常拦截:", throwable);

        FailResponse failResponse = new FailResponse(ExceptionCode.SYSTEM_ERROR.getCode(), null, ExceptionCode.SYSTEM_ERROR.getMsg());

        if (throwable instanceof ServiceException) {
            ServiceException serviceException = (ServiceException) throwable;
            failResponse.setCode(((ServiceException) throwable).getCode());
            failResponse.setMsg(serviceException.getMessage());
        }

        return failResponse;
    }
}