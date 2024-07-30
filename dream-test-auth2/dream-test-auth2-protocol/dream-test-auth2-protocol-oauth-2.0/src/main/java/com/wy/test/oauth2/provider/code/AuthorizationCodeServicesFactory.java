package com.wy.test.oauth2.provider.code;

import org.springframework.jdbc.core.JdbcTemplate;

import com.wy.test.core.enums.StoreType;
import com.wy.test.core.persistence.redis.RedisConnectionFactory;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AuthorizationCodeServicesFactory {

	public AuthorizationCodeServices getService(StoreType storeType, JdbcTemplate jdbcTemplate,
			RedisConnectionFactory redisConnFactory) {
		AuthorizationCodeServices authorizationCodeServices = null;
		if (StoreType.INMEMORY == storeType) {
			authorizationCodeServices = new InMemoryAuthorizationCodeServices();
			log.debug("InMemoryAuthorizationCodeServices");
		} else if (StoreType.JDBC == storeType) {
			// authorizationCodeServices = new JdbcAuthorizationCodeServices(jdbcTemplate);
			log.debug("JdbcAuthorizationCodeServices not support ");
		} else if (StoreType.REDIS == storeType) {
			authorizationCodeServices = new RedisAuthorizationCodeServices(redisConnFactory);
			log.debug("RedisAuthorizationCodeServices");
		}
		return authorizationCodeServices;
	}
}