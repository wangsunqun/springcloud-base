package com.wsq.zuul.filter;

import com.netflix.zuul.context.RequestContext;
import com.wsq.common.base.exception.GlobalException;
import com.wsq.common.base.exception.SystemException;
import com.wsq.common.base.pojo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/*
    该类是补充restControllerAdvice异常拦截
    restControllerAdvice只拦截进入restController的异常
    该类可以拦截容器抛出的异常
 */
@RestController
public class ContainerException implements ErrorController{

    @Override
    public String getErrorPath() {
        return "error";
    }

    @RequestMapping("error")
    public Result error(HttpServletRequest request, HttpServletResponse response) {
        RequestContext ctx = RequestContext.getCurrentContext();
        Throwable throwable = ctx.getThrowable();
        GlobalException exception = (GlobalException) throwable.getCause();
        return new Result(exception.getCode(), null, exception.getMessage());
    }
}
