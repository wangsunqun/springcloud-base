package com.wsq.common.serviceStandard;


import com.wsq.common.serviceStandard.exception.ServiceExceptionHandler;
import com.wsq.common.serviceStandard.http.FeignInterceptor;
import com.wsq.common.serviceStandard.http.ServiceInterceptor;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootApplication
@EnableEurekaClient
//过滤器监听器加载注释
@ServletComponentScan
@EnableCircuitBreaker
@Import({ServiceInterceptor.class, FeignInterceptor.class, ServiceExceptionHandler.class})
public @interface ServiceStandard {
}
