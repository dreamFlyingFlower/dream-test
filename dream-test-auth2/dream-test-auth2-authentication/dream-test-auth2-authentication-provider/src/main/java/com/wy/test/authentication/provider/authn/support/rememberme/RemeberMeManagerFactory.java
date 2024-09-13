package com.wy.test.authentication.provider.authn.support.rememberme;

import org.springframework.jdbc.core.JdbcTemplate;

import com.wy.test.core.enums.StoreType;
import com.wy.test.core.redis.RedisConnectionFactory;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RemeberMeManagerFactory {

	public AbstractRemeberMeManager getService(StoreType storeType, JdbcTemplate jdbcTemplate,
			RedisConnectionFactory redisConnFactory) {

		AbstractRemeberMeManager remeberMeService = null;
		if (StoreType.INMEMORY == storeType) {
			remeberMeService = new InMemoryRemeberMeManager();
			log.debug("InMemoryRemeberMeService");
		} else if (StoreType.JDBC == storeType) {
			// remeberMeService = new JdbcRemeberMeService(jdbcTemplate);
			log.debug("JdbcRemeberMeService not support ");
		} else if (StoreType.REDIS == storeType) {
			log.debug("RedisRemeberMeService  not support ");
		}
		return remeberMeService;
	}
}