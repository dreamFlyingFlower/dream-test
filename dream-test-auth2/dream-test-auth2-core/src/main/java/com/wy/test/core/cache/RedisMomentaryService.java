package com.wy.test.core.cache;

import com.wy.test.core.redis.RedisConnection;
import com.wy.test.core.redis.RedisConnectionFactory;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RedisMomentaryService implements MomentaryService {

	protected int validitySeconds = 60 * 5;

	RedisConnectionFactory connectionFactory;

	public static String PREFIX = "DREAM_MOMENTARY_";

	/**
	 * @param connectionFactory
	 */
	public RedisMomentaryService(RedisConnectionFactory connectionFactory) {
		super();
		this.connectionFactory = connectionFactory;
	}

	public RedisMomentaryService() {

	}

	public void setConnectionFactory(RedisConnectionFactory connectionFactory) {
		this.connectionFactory = connectionFactory;
	}

	@Override
	public void put(String sessionId, String name, Object value) {
		RedisConnection conn = connectionFactory.getConnection();
		conn.setexObject(getSessionKey(sessionId, name), validitySeconds, value);
		log.trace("key {}, validitySeconds {}, value {}", getSessionKey(sessionId, name), validitySeconds, value);
		conn.close();
	}

	@Override
	public Object get(String sessionId, String name) {
		RedisConnection conn = connectionFactory.getConnection();
		Object value = conn.getObject(getSessionKey(sessionId, name));
		log.trace("key {}, value {}", getSessionKey(sessionId, name), value);
		conn.close();
		return value;
	}

	@Override
	public Object remove(String sessionId, String name) {
		RedisConnection conn = connectionFactory.getConnection();
		Object value = conn.getObject(getSessionKey(sessionId, name));
		conn.delete(getSessionKey(sessionId, name));
		conn.close();
		log.trace("key {}, value {}", getSessionKey(sessionId, name), value);
		return value;
	}

	private String getSessionKey(String sessionId, String name) {
		return PREFIX + sessionId + name;
	}

}
