package com.wsq.common.base.exception.handler;

import com.wsq.common.base.exception.ServiceException;
import com.wsq.common.base.pojo.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    @ExceptionHandler(Throwable.class)
    public Result handlerException(Throwable throwable) throws Exception {
        logger.error("全局异常拦截:", throwable);

        Result result = new Result(-1, null, "系统异常");

        if (throwable instanceof ServiceException) {
            ServiceException serviceException = (ServiceException) throwable;
            result.setCode(((ServiceException) throwable).getCode());
            result.setMsg(serviceException.getMessage());
        }

        return result;
    }
}