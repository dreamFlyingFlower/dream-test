package com.wy.test.oauth2.provider.token.store;

import org.springframework.jdbc.core.JdbcTemplate;

import com.wy.test.core.enums.StoreType;
import com.wy.test.core.persistence.redis.RedisConnectionFactory;
import com.wy.test.oauth2.provider.token.TokenStore;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TokenStoreFactory {

	public TokenStore getTokenStore(StoreType storeType, JdbcTemplate jdbcTemplate,
			RedisConnectionFactory redisConnFactory) {
		TokenStore tokenStore = null;
		if (StoreType.INMEMORY == storeType) {
			tokenStore = new InMemoryTokenStore();
			log.debug("InMemoryTokenStore");
		} else if (StoreType.JDBC == storeType) {
			// tokenStore = new JdbcTokenStore(jdbcTemplate);
			log.debug("JdbcTokenStore not support ");
		} else if (StoreType.REDIS == storeType) {
			tokenStore = new RedisTokenStore(redisConnFactory);
			log.debug("RedisTokenStore");
		}
		return tokenStore;
	}
}