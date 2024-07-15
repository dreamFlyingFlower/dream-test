package com.wy.test.provider.authn.support.rememberme;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import com.wy.test.constants.ConstsPersistence;
import com.wy.test.persistence.redis.RedisConnectionFactory;

public class RemeberMeManagerFactory {

	private static final Logger _logger = LoggerFactory.getLogger(RemeberMeManagerFactory.class);

	public AbstractRemeberMeManager getService(int persistence, JdbcTemplate jdbcTemplate,
			RedisConnectionFactory redisConnFactory) {

		AbstractRemeberMeManager remeberMeService = null;
		if (persistence == ConstsPersistence.INMEMORY) {
			remeberMeService = new InMemoryRemeberMeManager();
			_logger.debug("InMemoryRemeberMeService");
		} else if (persistence == ConstsPersistence.JDBC) {
			// remeberMeService = new JdbcRemeberMeService(jdbcTemplate);
			_logger.debug("JdbcRemeberMeService not support ");
		} else if (persistence == ConstsPersistence.REDIS) {
			_logger.debug("RedisRemeberMeService  not support ");
		}
		return remeberMeService;
	}
}
