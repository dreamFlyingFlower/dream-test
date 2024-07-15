package com.wy.test.core.authn.jwt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wy.test.persistence.redis.RedisConnection;
import com.wy.test.persistence.redis.RedisConnectionFactory;

public class RedisCongressService implements CongressService {

	private static final Logger _logger = LoggerFactory.getLogger(RedisCongressService.class);

	protected int validitySeconds = 60 * 3; // default 3 minutes.

	RedisConnectionFactory connectionFactory;

	public static String PREFIX = "REDIS_CONGRESS_";

	/**
	 * @param connectionFactory
	 */
	public RedisCongressService(RedisConnectionFactory connectionFactory) {
		super();
		this.connectionFactory = connectionFactory;
	}

	/**
	 * 
	 */
	public RedisCongressService() {

	}

	public void setConnectionFactory(RedisConnectionFactory connectionFactory) {
		this.connectionFactory = connectionFactory;
	}

	@Override
	public void store(String congress, AuthJwt authJwt) {
		RedisConnection conn = connectionFactory.getConnection();
		conn.setexObject(PREFIX + congress, validitySeconds, authJwt);
		conn.close();
	}

	@Override
	public AuthJwt remove(String congress) {
		RedisConnection conn = connectionFactory.getConnection();
		AuthJwt authJwt = conn.getObject(PREFIX + congress);
		conn.delete(PREFIX + congress);
		conn.close();
		return authJwt;
	}

	@Override
	public AuthJwt get(String congress) {
		RedisConnection conn = connectionFactory.getConnection();
		AuthJwt authJwt = conn.getObject(PREFIX + congress);
		conn.close();
		return authJwt;
	}

	@Override
	public AuthJwt consume(String congress) {
		RedisConnection conn = connectionFactory.getConnection();
		AuthJwt authJwt = conn.getObject(PREFIX + congress);
		conn.delete(PREFIX + congress);
		conn.close();
		return authJwt;
	}

}
