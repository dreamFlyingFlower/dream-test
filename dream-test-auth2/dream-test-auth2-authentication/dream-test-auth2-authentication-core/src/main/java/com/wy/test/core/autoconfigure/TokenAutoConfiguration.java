package com.wy.test.core.autoconfigure;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

import com.nimbusds.jose.JOSEException;
import com.wy.test.core.authn.jwt.AuthRefreshTokenService;
import com.wy.test.core.authn.jwt.AuthTokenService;
import com.wy.test.core.authn.jwt.CongressService;
import com.wy.test.core.authn.jwt.InMemoryCongressService;
import com.wy.test.core.authn.jwt.RedisCongressService;
import com.wy.test.core.configuration.AuthJwkConfig;
import com.wy.test.core.constants.ConstsPersistence;
import com.wy.test.core.persistence.cache.MomentaryService;
import com.wy.test.core.persistence.redis.RedisConnectionFactory;

@AutoConfiguration
public class TokenAutoConfiguration implements InitializingBean {

	private static final Logger _logger = LoggerFactory.getLogger(TokenAutoConfiguration.class);

	@Bean
	AuthTokenService authTokenService(AuthJwkConfig authJwkConfig, RedisConnectionFactory redisConnFactory,
			MomentaryService momentaryService, AuthRefreshTokenService refreshTokenService,
			@Value("${dream.server.persistence}") int persistence) throws JOSEException {
		CongressService congressService;
		_logger.debug("cache persistence {}", persistence);
		if (persistence == ConstsPersistence.REDIS) {
			congressService = new RedisCongressService(redisConnFactory);
		} else {
			congressService = new InMemoryCongressService();
		}

		AuthTokenService authTokenService =
				new AuthTokenService(authJwkConfig, congressService, momentaryService, refreshTokenService);

		return authTokenService;
	}

	@Bean
	AuthRefreshTokenService refreshTokenService(AuthJwkConfig authJwkConfig) throws JOSEException {
		return new AuthRefreshTokenService(authJwkConfig);
	}

	@Override
	public void afterPropertiesSet() throws Exception {

	}
}
