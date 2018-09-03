package com.wsq.common.serviceStandard.http;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class FeignInterceptor {

    @Bean
    public RequestInterceptor requestInterceptor() {
        return (template) -> {
            Map<String, String> map = ServiceInterceptor.headerRepertory.get();

            for (Map.Entry entry : map.entrySet()) {
                template.header((String)entry.getKey(), (String)entry.getValue());
            }

            //删除当前线程缓存，防止内存泄漏
            ServiceInterceptor.headerRepertory.remove();
        };
    }
}
