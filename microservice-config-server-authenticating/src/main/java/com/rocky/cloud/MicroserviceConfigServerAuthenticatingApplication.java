package com.rocky.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer
public class MicroserviceConfigServerAuthenticatingApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroserviceConfigServerAuthenticatingApplication.class, args);
	}
}
