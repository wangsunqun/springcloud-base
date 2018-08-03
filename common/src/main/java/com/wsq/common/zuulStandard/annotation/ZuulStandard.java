package com.wsq.common.zuulStandard.annotation;


import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@EnableEurekaClient
public @interface ZuulStandard {
}
