package com.wsq.consumer;

import com.wsq.common.serviceStandard.ServiceStandard;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.openfeign.EnableFeignClients;

@ServiceStandard
@EnableFeignClients(basePackages = "com.wsq")
@ServletComponentScan
public class ConsumerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConsumerApplication.class, args);
	}
}
