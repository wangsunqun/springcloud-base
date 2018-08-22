package com.wsq.zuul;

import com.wsq.common.zuulStandard.ZuulStandard;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.ComponentScan;

@ZuulStandard
@ComponentScan("com.wsq")
public class ZuulApplication {

	public static void main(String[] args) {
		SpringApplication.run(ZuulApplication.class, args);
	}
}
