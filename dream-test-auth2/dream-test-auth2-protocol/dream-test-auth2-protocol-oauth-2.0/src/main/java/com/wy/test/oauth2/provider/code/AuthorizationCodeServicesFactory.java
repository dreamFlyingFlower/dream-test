package com.wy.test.oauth2.provider.code;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import com.wy.test.core.constants.ConstsPersistence;
import com.wy.test.core.persistence.redis.RedisConnectionFactory;

public class AuthorizationCodeServicesFactory {

	private static final Logger _logger = LoggerFactory.getLogger(AuthorizationCodeServicesFactory.class);

	public AuthorizationCodeServices getService(int persistence, JdbcTemplate jdbcTemplate,
			RedisConnectionFactory redisConnFactory) {
		AuthorizationCodeServices authorizationCodeServices = null;
		if (persistence == ConstsPersistence.INMEMORY) {
			authorizationCodeServices = new InMemoryAuthorizationCodeServices();
			_logger.debug("InMemoryAuthorizationCodeServices");
		} else if (persistence == ConstsPersistence.JDBC) {
			// authorizationCodeServices = new JdbcAuthorizationCodeServices(jdbcTemplate);
			_logger.debug("JdbcAuthorizationCodeServices not support ");
		} else if (persistence == ConstsPersistence.REDIS) {
			authorizationCodeServices = new RedisAuthorizationCodeServices(redisConnFactory);
			_logger.debug("RedisAuthorizationCodeServices");
		}
		return authorizationCodeServices;
	}
}