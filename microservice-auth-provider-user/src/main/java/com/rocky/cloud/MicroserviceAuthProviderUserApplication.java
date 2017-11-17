package com.rocky.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class MicroserviceAuthProviderUserApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroserviceAuthProviderUserApplication.class, args);
	}
}
