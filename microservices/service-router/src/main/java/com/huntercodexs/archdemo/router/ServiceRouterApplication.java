package com.huntercodexs.archdemo.router;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.stereotype.Controller;

@SpringBootApplication
@Controller
@EnableZuulProxy
@EnableDiscoveryClient
public class ServiceRouterApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServiceRouterApplication.class, args);
	}

}
