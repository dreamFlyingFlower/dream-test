package com.wy.test.authentication.core.autoconfigure;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import com.nimbusds.jose.JOSEException;
import com.wy.test.authentication.core.jwt.AuthRefreshTokenService;
import com.wy.test.authentication.core.jwt.AuthTokenService;
import com.wy.test.authentication.core.jwt.CongressService;
import com.wy.test.authentication.core.jwt.InMemoryCongressService;
import com.wy.test.authentication.core.jwt.RedisCongressService;
import com.wy.test.core.cache.MomentaryService;
import com.wy.test.core.enums.StoreType;
import com.wy.test.core.properties.DreamAuthJwkProperties;
import com.wy.test.core.properties.DreamAuthStoreProperties;
import com.wy.test.core.redis.RedisConnectionFactory;

import lombok.extern.slf4j.Slf4j;

@EnableConfigurationProperties(DreamAuthJwkProperties.class)
@AutoConfiguration
@Slf4j
public class TokenAutoConfiguration implements InitializingBean {

	@Bean
	AuthTokenService authTokenService(DreamAuthJwkProperties dreamJwkProperties,
			RedisConnectionFactory redisConnFactory, MomentaryService momentaryService,
			AuthRefreshTokenService refreshTokenService, DreamAuthStoreProperties dreamAuthRedisProperties)
			throws JOSEException {
		CongressService congressService;
		log.debug("cache persistence {}", dreamAuthRedisProperties.getStoreType());
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