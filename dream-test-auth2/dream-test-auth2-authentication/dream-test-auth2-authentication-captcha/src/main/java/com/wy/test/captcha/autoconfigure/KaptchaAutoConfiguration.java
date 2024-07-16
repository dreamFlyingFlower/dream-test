package com.wy.test.captcha.autoconfigure;

import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.google.code.kaptcha.Producer;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;

@AutoConfiguration
public class KaptchaAutoConfiguration implements InitializingBean {

	private static final Logger _logger = LoggerFactory.getLogger(KaptchaAutoConfiguration.class);

	public static final String kaptchaPropertySource = "/kaptcha.properties";

	/**
	 * Captcha Producer Config .
	 * 
	 * @return Producer
	 * @throws IOException kaptcha.properties is null
	 */
	@Bean
	Producer captchaProducer() throws IOException {
		Resource resource = new ClassPathResource(kaptchaPropertySource);
		_logger.debug("Kaptcha config file " + resource.getURL());
		DefaultKaptcha kaptcha = new DefaultKaptcha();
		Properties properties = new Properties();
		properties.load(resource.getInputStream());
		Config config = new Config(properties);
		kaptcha.setConfig(config);
		return kaptcha;
	}

	@Override
	public void afterPropertiesSet() throws Exception {

	}
}
