package com.wy.test.web;

import javax.servlet.ServletException;

import org.apache.ibatis.io.VFS;
import org.dromara.mybatis.jpa.spring.SpringBootVFS;
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

@SpringBootApplication
@MapperScan("com.wy.persistence.mapper,")
@Slf4j
public class DreamTestApplication extends SpringBootServletInitializer {

	/**
	 * @param args start parameter
	 */
	public static void main(String[] args) {
		log.info("Start dream Application ...");

		VFS.addImplClass(SpringBootVFS.class);
		ConfigurableApplicationContext applicationContext = SpringApplication.run(DreamTestApplication.class, args);
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
		return application.sources(DreamTestApplication.class);
	}
}
