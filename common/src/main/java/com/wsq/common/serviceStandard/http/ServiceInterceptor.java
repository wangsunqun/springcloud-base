package com.wsq.common.serviceStandard.http;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class ServiceInterceptor extends WebMvcConfigurationSupport {
    public static final ThreadLocal<Map<String, String>> headerRepertory = new InheritableThreadLocal();

    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(requestInterceptor());
        super.addInterceptors(registry);
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
        //========= 传递请求头开始 ==========//
        Map<String, String> headerMap = new HashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();

        while (headerNames.hasMoreElements()) {
            String key = headerNames.nextElement();
            String value = request.getHeader(key);
            headerMap.put(key, value);
        }

        headerRepertory.set(headerMap);
        //========= 传递请求头结束 ==========//

        return true;
    }
}
