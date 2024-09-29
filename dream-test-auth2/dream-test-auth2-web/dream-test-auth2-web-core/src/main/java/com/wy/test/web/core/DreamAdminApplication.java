package com.wy.test.web.core;

import javax.servlet.ServletException;

import org.joda.time.DateTime;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;

import com.wy.test.core.web.InitializeContext;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication(scanBasePackages = { 
		// 基础数据
		"com.wy.test.core", "com.wy.test.persistence",
		// 验证码
		"com.wy.test.authentication.captcha",
		// social
		"com.wy.test.authentication.social",
		"com.wy.test.authentication.core",
		"com.wy.test.provision", "com.wy.test.web.core" })
@MapperScan("com.wy.test.persistence.mapper")
@Slf4j
public class DreamAdminApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		log.info("Start dream Application ...");

		ConfigurableApplicationContext applicationContext = SpringApplication.run(DreamAdminApplication.class, args);
		InitializeContext initWebContext = new InitializeContext(applicationContext);
		try {
			initWebContext.init(null);
		} catch (ServletException e) {
			log.error("ServletException", e);
		}
		log.info("dream at {}", new DateTime());
		log.info("dream Server Port {}", applicationContext.getBean(ServerProperties.class).getPort());
		log.info("dream started.");
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(DreamAdminApplication.class);
	}
}