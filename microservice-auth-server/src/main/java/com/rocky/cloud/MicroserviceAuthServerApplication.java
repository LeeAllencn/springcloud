package com.rocky.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class MicroserviceAuthServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroserviceAuthServerApplication.class, args);
	}
}
