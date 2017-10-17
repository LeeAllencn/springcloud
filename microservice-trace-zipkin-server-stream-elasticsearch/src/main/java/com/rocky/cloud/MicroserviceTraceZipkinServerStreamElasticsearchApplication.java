package com.rocky.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.sleuth.zipkin.stream.EnableZipkinStreamServer;

@SpringBootApplication
@EnableZipkinStreamServer
public class MicroserviceTraceZipkinServerStreamElasticsearchApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroserviceTraceZipkinServerStreamElasticsearchApplication.class, args);
	}
}
