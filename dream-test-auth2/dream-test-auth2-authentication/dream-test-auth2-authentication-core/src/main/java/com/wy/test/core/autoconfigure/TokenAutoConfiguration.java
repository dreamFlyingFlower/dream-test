package com.wy.test.core.autoconfigure;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

import com.nimbusds.jose.JOSEException;
import com.wy.test.core.authn.jwt.AuthRefreshTokenService;
import com.wy.test.core.authn.jwt.AuthTokenService;
import com.wy.test.core.authn.jwt.CongressService;
import com.wy.test.core.authn.jwt.InMemoryCongressService;
import com.wy.test.core.authn.jwt.RedisCongressService;
import com.wy.test.core.enums.StoreType;
import com.wy.test.core.persistence.cache.MomentaryService;
import com.wy.test.core.persistence.redis.RedisConnectionFactory;
import com.wy.test.core.properties.DreamAuthJwkProperties;
import com.wy.test.core.properties.DreamAuthStoreProperties;

@AutoConfiguration
public class TokenAutoConfiguration implements InitializingBean {

	private static final Logger _logger = LoggerFactory.getLogger(TokenAutoConfiguration.class);

	@Bean
	AuthTokenService authTokenService(DreamAuthJwkProperties dreamJwkProperties,
			RedisConnectionFactory redisConnFactory, MomentaryService momentaryService,
			AuthRefreshTokenService refreshTokenService, DreamAuthStoreProperties dreamAuthRedisProperties)
			throws JOSEException {
		CongressService congressService;
		_logger.debug("cache persistence {}", dreamAuthRedisProperties.getStoreType());
		if (StoreType.REDIS == dreamAuthRedisProperties.getStoreType()) {
			congressService = new RedisCongressService(redisConnFactory);
		} else {
			congressService = new InMemoryCongressService();
		}

		AuthTokenService authTokenService =
				new AuthTokenService(dreamJwkProperties, congressService, momentaryService, refreshTokenService);

		return authTokenService;
	}

	@Bean
	AuthRefreshTokenService refreshTokenService(DreamAuthJwkProperties dreamJwkProperties) throws JOSEException {
		return new AuthRefreshTokenService(dreamJwkProperties);
	}

	@Override
	public void afterPropertiesSet() throws Exception {

	}
}