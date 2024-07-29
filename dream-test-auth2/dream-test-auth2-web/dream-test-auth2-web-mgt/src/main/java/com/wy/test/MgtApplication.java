package com.wy.test;

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

@ComponentScan(basePackages = { "com.wy.test.authn", "com.wy.test.configuration", "com.wy.test.entity",
		"com.wy.test.entity.apps", "com.wy.test.entity.userinfo", "com.wy.test.web.apis.identity.kafka",
		"com.wy.test.web.apis.identity.rest", "com.wy.test.web.apis.identity.scim", "com.wy.test.persistence",
		"com.wy.test.provision", "com.wy.test.synchronizer", "com.wy.test.web", "com.wy.test.web.access.contorller",
		"com.wy.test.web.api.endpoint", "com.wy.test.web.apps.contorller", "com.wy.test.web.contorller",
		"com.wy.test.web.endpoint", "com.wy.test.web.interceptor", "com.wy.test.web.permissions.contorller",
		"com.wy.test.web.tag" })
@MapperScan("com.wy.test.persistence.mapper,")
@SpringBootApplication
public class MgtApplication extends SpringBootServletInitializer {

	private static final Logger _logger = LoggerFactory.getLogger(MgtApplication.class);

	public static void main(String[] args) {
		_logger.info("Start Mgt Application ...");

		ConfigurableApplicationContext applicationContext = SpringApplication.run(MgtApplication.class, args);
		InitializeContext initWebContext = new InitializeContext(applicationContext);

		try {
			initWebContext.init(null);
		} catch (ServletException e) {
			_logger.error("Exception ", e);
		}
		_logger.info("Mgt at {}", new DateTime());
		_logger.info("Mgt Server Port {}", applicationContext.getBean(ApplicationConfig.class).getPort());
		_logger.info("Mgt started.");

	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(MgtApplication.class);
	}
}