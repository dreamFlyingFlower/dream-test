package com.wy.test.openapi;

import javax.servlet.ServletException;

import org.joda.time.DateTime;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import com.wy.test.core.web.InitializeContext;

@ComponentScan(basePackages = { "com.wy.test.authn", "com.wy.test.configuration", "com.wy.test.entity",
		"com.wy.test.entity.apps", "com.wy.test.entity.userinfo", "com.wy.test.web.apis.identity.kafka",
		"com.wy.test.web.apis.identity.rest", "com.wy.test.web.apis.identity.scim", "com.wy.test.persistence",
		"com.wy.test.provision", "org.dream.web", "com.wy.test.web.api.endpoint", "com.wy.test.web.contorller",
		"com.wy.test.web.endpoint", "com.wy.test.web.interceptor", })
@MapperScan("com.wy.test.persistence.mapper,")
@SpringBootApplication
public class DreamOpenApiApplication extends SpringBootServletInitializer {

	private static final Logger _logger = LoggerFactory.getLogger(DreamOpenApiApplication.class);

	public static void main(String[] args) {
		_logger.info("Start dream OpenApi Application ...");

		ConfigurableApplicationContext applicationContext = SpringApplication.run(DreamOpenApiApplication.class, args);
		InitializeContext initWebContext = new InitializeContext(applicationContext);

		try {
			initWebContext.init(null);
		} catch (ServletException e) {
			_logger.error("Exception ", e);
		}
		_logger.info("dream OpenApi at {}", new DateTime());
		_logger.info("dream OpenApi Server Port {}", applicationContext.getBean(ServerProperties.class).getPort());
		_logger.info("dream OpenApi started.");
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(DreamOpenApiApplication.class);
	}
}