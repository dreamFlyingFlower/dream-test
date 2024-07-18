package com.wy.test.otp.autoconfigure;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

import com.wy.test.core.constants.ConstsPersistence;
import com.wy.test.core.persistence.redis.RedisConnectionFactory;
import com.wy.test.otp.password.onetimepwd.MailOtpAuthnService;
import com.wy.test.otp.password.onetimepwd.token.RedisOtpTokenStore;
import com.wy.test.persistence.service.EmailSendersService;

@AutoConfiguration
public class OneTimePasswordAutoConfiguration implements InitializingBean {

	private static final Logger _logger = LoggerFactory.getLogger(OneTimePasswordAutoConfiguration.class);

	@Bean(name = "mailOtpAuthnService")
	MailOtpAuthnService mailOtpAuthnService(@Value("${maxkey.server.persistence}") int persistence,
			EmailSendersService emailSendersService, RedisConnectionFactory redisConnFactory) {
		MailOtpAuthnService otpAuthnService = new MailOtpAuthnService(emailSendersService);

		if (persistence == ConstsPersistence.REDIS) {
			RedisOtpTokenStore redisOptTokenStore = new RedisOtpTokenStore(redisConnFactory);
			otpAuthnService.setRedisOptTokenStore(redisOptTokenStore);
		}

		_logger.debug("MailOtpAuthnService {} inited.", persistence == ConstsPersistence.REDIS ? "Redis" : "InMemory");
		return otpAuthnService;
	}

	@Override
	public void afterPropertiesSet() throws Exception {

	}
}