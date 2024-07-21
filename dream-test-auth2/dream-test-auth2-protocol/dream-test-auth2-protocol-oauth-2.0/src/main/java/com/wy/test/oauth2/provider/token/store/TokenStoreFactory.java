package com.wy.test.oauth2.provider.token.store;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import com.wy.test.core.constants.ConstsPersistence;
import com.wy.test.core.persistence.redis.RedisConnectionFactory;
import com.wy.test.oauth2.provider.token.TokenStore;

public class TokenStoreFactory {

	private static final Logger _logger = LoggerFactory.getLogger(TokenStoreFactory.class);

	public TokenStore getTokenStore(int persistence, JdbcTemplate jdbcTemplate,
			RedisConnectionFactory redisConnFactory) {
		TokenStore tokenStore = null;
		if (persistence == ConstsPersistence.INMEMORY) {
			tokenStore = new InMemoryTokenStore();
			_logger.debug("InMemoryTokenStore");
		} else if (persistence == ConstsPersistence.JDBC) {
			// tokenStore = new JdbcTokenStore(jdbcTemplate);
			_logger.debug("JdbcTokenStore not support ");
		} else if (persistence == ConstsPersistence.REDIS) {
			tokenStore = new RedisTokenStore(redisConnFactory);
			_logger.debug("RedisTokenStore");
		}
		return tokenStore;
	}
}
