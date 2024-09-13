package com.wy.test.authentication.otp.autoconfigure;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

import com.wy.test.authentication.otp.password.onetimepwd.MailOtpAuthnService;
import com.wy.test.authentication.otp.password.onetimepwd.token.RedisOtpTokenStore;
import com.wy.test.core.enums.StoreType;
import com.wy.test.core.properties.DreamAuthStoreProperties;
import com.wy.test.core.redis.RedisConnectionFactory;
import com.wy.test.persistence.service.EmailSenderService;

import lombok.extern.slf4j.Slf4j;

@AutoConfiguration
@Slf4j
public class OneTimePasswordAutoConfiguration implements InitializingBean {

	@Bean(name = "mailOtpAuthnService")
	MailOtpAuthnService mailOtpAuthnService(DreamAuthStoreProperties dreamAuthStoreProperties,
			EmailSenderService emailSenderService, RedisConnectionFactory redisConnFactory) {
		MailOtpAuthnService otpAuthnService = new MailOtpAuthnService(emailSenderService);

		if (StoreType.REDIS == dreamAuthStoreProperties.getStoreType()) {
			RedisOtpTokenStore redisOptTokenStore = new RedisOtpTokenStore(redisConnFactory);
			otpAuthnService.setRedisOptTokenStore(redisOptTokenStore);
		}

		log.debug("MailOtpAuthnService {} inited.",
				StoreType.REDIS == dreamAuthStoreProperties.getStoreType() ? "Redis" : "InMemory");
		return otpAuthnService;
	}

	@Override
	public void afterPropertiesSet() throws Exception {

	}
}