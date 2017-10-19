package com.rocky.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class MicroserviceDockerEurekaHaApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroserviceDockerEurekaHaApplication.class, args);
	}
}
