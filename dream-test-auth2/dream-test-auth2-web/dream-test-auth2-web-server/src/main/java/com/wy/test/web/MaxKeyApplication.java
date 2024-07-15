package com.wy.test.web;

import javax.servlet.ServletException;

import org.apache.ibatis.io.VFS;
import org.dromara.mybatis.jpa.spring.SpringBootVFS;
import org.joda.time.DateTime;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ConfigurableApplicationContext;

import com.wy.test.configuration.ApplicationConfig;
import com.wy.test.web.InitializeContext;

@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("org.maxkey.persistence.mapper,")
public class MaxKeyApplication extends SpringBootServletInitializer {
    private static final Logger _logger = LoggerFactory.getLogger(MaxKeyApplication.class);

    /**
     * @param args start parameter 
     */
    public static void main(String[] args) {
        _logger.info("Start MaxKey Application ...");
        
        VFS.addImplClass(SpringBootVFS.class);
        ConfigurableApplicationContext applicationContext = 
                SpringApplication.run(MaxKeyApplication.class, args);
        InitializeContext initWebContext = new InitializeContext(applicationContext);
        try {
            initWebContext.init(null);
        } catch (ServletException e) {
            _logger.error("ServletException", e);
        }
        _logger.info("MaxKey at {}" , new DateTime());
        _logger.info("MaxKey Server Port {}"
        		,applicationContext.getBean(ApplicationConfig.class).getPort());
        _logger.info("MaxKey started.");
    }

    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(MaxKeyApplication.class);
    }

}
