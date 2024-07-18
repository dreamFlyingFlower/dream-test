package com.wy.test.openapi;

import javax.servlet.ServletException;

import org.joda.time.DateTime;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import com.wy.test.core.configuration.ApplicationConfig;
import com.wy.test.core.web.InitializeContext;

@ComponentScan(basePackages = { "org.maxkey.authn", "org.maxkey.configuration", "org.maxkey.entity",
		"org.maxkey.entity.apps", "org.maxkey.entity.userinfo", "org.maxkey.web.apis.identity.kafka",
		"org.maxkey.web.apis.identity.rest", "org.maxkey.web.apis.identity.scim", "org.maxkey.persistence",
		"org.maxkey.provision", "org.maxkey.web", "org.maxkey.web.api.endpoint", "org.maxkey.web.contorller",
		"org.maxkey.web.endpoint", "org.maxkey.web.interceptor", })
@MapperScan("org.maxkey.persistence.mapper,")
@SpringBootApplication
public class MaxKeyOpenApiApplication extends SpringBootServletInitializer {

	private static final Logger _logger = LoggerFactory.getLogger(MaxKeyOpenApiApplication.class);

	public static void main(String[] args) {
		_logger.info("Start MaxKey OpenApi Application ...");

		ConfigurableApplicationContext applicationContext = SpringApplication.run(MaxKeyOpenApiApplication.class, args);
		InitializeContext initWebContext = new InitializeContext(applicationContext);

		try {
			initWebContext.init(null);
		} catch (ServletException e) {
			_logger.error("Exception ", e);
		}
		_logger.info("MaxKey OpenApi at {}", new DateTime());
		_logger.info("MaxKey OpenApi Server Port {}", applicationContext.getBean(ApplicationConfig.class).getPort());
		_logger.info("MaxKey OpenApi started.");
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(MaxKeyOpenApiApplication.class);
	}
}