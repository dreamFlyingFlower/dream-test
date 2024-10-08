package com.wy.test.authentication.sms.autoconfigure;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

import com.wy.test.authentication.otp.onetimepwd.token.RedisOtpTokenStore;
import com.wy.test.authentication.sms.password.sms.SmsOtpAuthnService;
import com.wy.test.core.enums.StoreType;
import com.wy.test.core.properties.DreamAuthStoreProperties;
import com.wy.test.core.redis.RedisConnectionFactory;
import com.wy.test.persistence.service.EmailSenderService;
import com.wy.test.persistence.service.SmsProviderService;

import lombok.extern.slf4j.Slf4j;

@AutoConfiguration
@Slf4j
public class SmsAutoConfiguration implements InitializingBean {

	@Bean(name = "smsOtpAuthnService")
	SmsOtpAuthnService smsOtpAuthnService(DreamAuthStoreProperties dreamAuthStoreProperties,
			SmsProviderService smsProviderService, EmailSenderService emailSenderService,
			RedisConnectionFactory redisConnFactory) {
		SmsOtpAuthnService smsOtpAuthnService = new SmsOtpAuthnService(smsProviderService, emailSenderService);

		if (StoreType.REDIS == dreamAuthStoreProperties.getStoreType()) {
			RedisOtpTokenStore redisOptTokenStore = new RedisOtpTokenStore(redisConnFactory);
			smsOtpAuthnService.setRedisOptTokenStore(redisOptTokenStore);
		}

		log.debug("SmsOtpAuthnService {} inited.",
				StoreType.REDIS == dreamAuthStoreProperties.getStoreType() ? "Redis" : "InMemory");
		return smsOtpAuthnService;
	}

	@Override
	public void afterPropertiesSet() throws Exception {

	}
}