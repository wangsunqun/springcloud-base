package com.wsq.providercore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class ProviderCoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProviderCoreApplication.class, args);
	}
}
