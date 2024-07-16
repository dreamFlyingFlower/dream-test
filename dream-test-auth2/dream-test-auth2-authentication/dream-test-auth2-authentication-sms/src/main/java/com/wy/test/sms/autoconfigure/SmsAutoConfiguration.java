package com.wy.test.sms.autoconfigure;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

import com.wy.test.constants.ConstsPersistence;
import com.wy.test.otp.password.onetimepwd.token.RedisOtpTokenStore;
import com.wy.test.persistence.redis.RedisConnectionFactory;
import com.wy.test.persistence.service.EmailSendersService;
import com.wy.test.persistence.service.SmsProviderService;
import com.wy.test.sms.password.sms.SmsOtpAuthnService;

import lombok.extern.slf4j.Slf4j;

@AutoConfiguration
@Slf4j
public class SmsAutoConfiguration implements InitializingBean {

	@Bean(name = "smsOtpAuthnService")
	SmsOtpAuthnService smsOtpAuthnService(@Value("${maxkey.server.persistence}") int persistence,
			SmsProviderService smsProviderService, EmailSendersService emailSendersService,
			RedisConnectionFactory redisConnFactory) {
		SmsOtpAuthnService smsOtpAuthnService = new SmsOtpAuthnService(smsProviderService, emailSendersService);

		if (persistence == ConstsPersistence.REDIS) {
			RedisOtpTokenStore redisOptTokenStore = new RedisOtpTokenStore(redisConnFactory);
			smsOtpAuthnService.setRedisOptTokenStore(redisOptTokenStore);
		}

		log.debug("SmsOtpAuthnService {} inited.", persistence == ConstsPersistence.REDIS ? "Redis" : "InMemory");
		return smsOtpAuthnService;
	}

	@Override
	public void afterPropertiesSet() throws Exception {

	}
}