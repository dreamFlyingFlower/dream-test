package com.wy.test.web.mgt;

import javax.servlet.ServletException;

import org.joda.time.DateTime;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import com.wy.test.core.web.InitializeContext;

import lombok.extern.slf4j.Slf4j;

/**
 * {@link ServletWebServerFactory}
 *
 * @author 飞花梦影
 * @date 2024-07-31 16:01:49
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@MapperScan("com.wy.test.persistence.mapper")
@ComponentScan(basePackages = { "com.wy.test.authn", "com.wy.test.configuration", "com.wy.test.entity",
		"com.wy.test.entity.apps", "com.wy.test.entity.userinfo", "com.wy.test.web.apis.identity.kafka",
		"com.wy.test.web.apis.identity.rest", "com.wy.test.web.apis.identity.scim", "com.wy.test.persistence",
		"com.wy.test.provision", "com.wy.test.sync", "com.wy.test.web", "com.wy.test.web.access.contorller",
		"com.wy.test.web.api.endpoint", "com.wy.test.web.apps.contorller", "com.wy.test.web.contorller",
		"com.wy.test.web.endpoint", "com.wy.test.web.interceptor", "com.wy.test.web.permission.contorller",
		"com.wy.test.web.tag" })
@SpringBootApplication
@Slf4j
public class MgtApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		log.info("Start Mgt Application ...");

		ConfigurableApplicationContext applicationContext = SpringApplication.run(MgtApplication.class, args);
		InitializeContext initWebContext = new InitializeContext(applicationContext);

		try {
			initWebContext.init(null);
		} catch (ServletException e) {
			log.error("Exception ", e);
		}
		log.info("Mgt at {}", new DateTime());
		log.info("Mgt Server Port {}", applicationContext.getBean(ServerProperties.class).getPort());
		log.info("Mgt started.");
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(MgtApplication.class);
	}
}