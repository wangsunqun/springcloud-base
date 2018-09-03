package com.wsq.common.serviceStandard.http;

import org.slf4j.MDC;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Configuration
public class ServiceInterceptor extends WebMvcConfigurerAdapter {
    public static final ThreadLocal<Map<String, String>> headerRepertory = new InheritableThreadLocal();

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(requestInterceptor());
    }

    private HandlerInterceptorAdapter requestInterceptor() {
        return new HandlerInterceptorAdapter() {
            @Override
            public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
                return requestHandler(request, response, handler);
            }
        };
    }

    private boolean requestHandler(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //========= 传递请求头 ==========//
        Map<String, String> headerMap = new HashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();

        while (headerNames.hasMoreElements()) {
            String key = headerNames.nextElement();
            String value = request.getHeader(key);
            headerMap.put(key, value);
        }

        headerRepertory.set(headerMap);

        //========= 设置traceId ==========//
        String traceId = StringUtils.isEmpty(request.getHeader("traceId")) ? UUID.randomUUID().toString() : request.getHeader("traceId");
        MDC.put("traceId", traceId);

        //========= 设置返回请求头 ==========//
        response.setHeader("traceId", traceId);

        return true;
    }
}
