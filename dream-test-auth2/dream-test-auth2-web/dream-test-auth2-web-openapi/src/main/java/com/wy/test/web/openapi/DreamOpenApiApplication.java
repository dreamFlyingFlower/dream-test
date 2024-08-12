package com.wy.test.web.openapi;

import javax.servlet.ServletException;

import org.joda.time.DateTime;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import com.wy.test.core.web.InitializeContext;

import lombok.extern.slf4j.Slf4j;

@ComponentScan(basePackages = { "com.wy.test.authn", "com.wy.test.configuration", "com.wy.test.entity",
		"com.wy.test.entity.apps", "com.wy.test.entity.userinfo", "com.wy.test.web.apis.identity.kafka",
		"com.wy.test.web.apis.identity.rest", "com.wy.test.web.apis.identity.scim", "com.wy.test.persistence",
		"com.wy.test.provision", "org.dream.web", "com.wy.test.web.api.endpoint", "com.wy.test.web.contorller",
		"com.wy.test.web.endpoint", "com.wy.test.web.interceptor", })
@MapperScan("com.wy.test.persistence.mapper,")
@SpringBootApplication
@Slf4j
public class DreamOpenApiApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		log.info("Start dream OpenApi Application ...");

		ConfigurableApplicationContext applicationContext = SpringApplication.run(DreamOpenApiApplication.class, args);
		InitializeContext initWebContext = new InitializeContext(applicationContext);

		try {
			initWebContext.init(null);
		} catch (ServletException e) {
			log.error("Exception ", e);
		}
		log.info("dream OpenApi at {}", new DateTime());
		log.info("dream OpenApi Server Port {}", applicationContext.getBean(ServerProperties.class).getPort());
		log.info("dream OpenApi started.");
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(DreamOpenApiApplication.class);
	}
}