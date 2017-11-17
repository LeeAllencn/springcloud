package com.rocky.cloud;

import com.rocky.cloud.filters.AccessFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;

/**
 * API网关服务
 * @ EnableZuulProxy 启用网关路由
 * @ EnableOAuth2Sso 启用OAuth2单点登录
 */
@SpringBootApplication
@EnableZuulProxy
@EnableOAuth2Sso
public class MicroserviceAuthGatewayZuulApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroserviceAuthGatewayZuulApplication.class, args);
	}

	/**
	 * 资源过滤器
	 * @return 资源过滤器
	 */
	@Bean
	public AccessFilter accessFilter(){
		return new AccessFilter();
	}
}
