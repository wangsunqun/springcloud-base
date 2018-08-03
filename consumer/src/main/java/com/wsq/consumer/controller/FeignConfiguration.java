package com.wsq.consumer.controller;

import com.wsq.providerapi.feignInterface.fallback.UserHystrix;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfiguration {

    @Bean
    public UserHystrix userHystrix() {
        return new UserHystrix();
    }
}
