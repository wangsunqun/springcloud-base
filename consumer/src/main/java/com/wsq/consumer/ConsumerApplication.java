package com.wsq.consumer;

import com.wsq.common.serviceStandard.ServiceStandard;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

@ServiceStandard
@EnableFeignClients(basePackages = "com.wsq")
public class ConsumerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConsumerApplication.class, args);
	}
}
