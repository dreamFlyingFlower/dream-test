package com.wy.test.sms.autoconfigure;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

@AutoConfiguration
public class SmsAutoConfiguration implements InitializingBean {

	private static final Logger _logger = LoggerFactory.getLogger(SmsAutoConfiguration.class);

	@Bean(name = "smsOtpAuthnService")
	SmsOtpAuthnService smsOtpAuthnService(@Value("${maxkey.server.persistence}") int persistence,
			SmsProviderService smsProviderService, EmailSendersService emailSendersService,
			RedisConnectionFactory redisConnFactory) {
		SmsOtpAuthnService smsOtpAuthnService = new SmsOtpAuthnService(smsProviderService, emailSendersService);

		if (persistence == ConstsPersistence.REDIS) {
			RedisOtpTokenStore redisOptTokenStore = new RedisOtpTokenStore(redisConnFactory);
			smsOtpAuthnService.setRedisOptTokenStore(redisOptTokenStore);
		}

		_logger.debug("SmsOtpAuthnService {} inited.", persistence == ConstsPersistence.REDIS ? "Redis" : "InMemory");
		return smsOtpAuthnService;
	}

	@Override
	public void afterPropertiesSet() throws Exception {

	}
}
