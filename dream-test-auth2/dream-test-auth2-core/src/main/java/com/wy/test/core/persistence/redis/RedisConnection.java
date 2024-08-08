package com.wy.test.core.persistence.redis;

import java.io.Serializable;
import java.util.List;

import dream.flying.flower.lang.SerializableHelper;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;

@Slf4j
public class RedisConnection {

	Jedis conn;

	RedisConnectionFactory connectionFactory;

	Pipeline pipeline;

	public RedisConnection() {

	}

	public RedisConnection(RedisConnectionFactory connectionFactory) {
		this.conn = connectionFactory.open();
		this.connectionFactory = connectionFactory;
	}

	/**
	 * @param key
	 * @param value
	 */
	public void set(String key, String value) {
		conn.set(key, value);
	}

	/**
	 * @param key
	 * @param value
	 */
	public void setObject(String key, Object value) {
		if (value instanceof Serializable) {
			set(key, SerializableHelper.serializeHex((Serializable) value));
		} else {
			log.error("value must implements of Serializable .");
		}
	}

	public void setexObject(String key, long seconds, Object value) {
		if (value instanceof Serializable) {
			setex(key, seconds, SerializableHelper.serializeHex((Serializable) value));
		} else {
			log.error("value must implements of Serializable .");
		}
	}

	/**
	 * @param key
	 * @param seconds
	 * @param value
	 */
	public void setex(String key, long seconds, String value) {
		log.trace("setex key {} ...", key);
		if (seconds == 0) {
			conn.setex(key, RedisConnectionFactory.DEFAULT_CONFIG.DEFAULT_LIFETIME, value);
		} else {
			conn.setex(key, seconds, value);
		}
		log.trace("setex successful .");
	}

	/**
	 * @param key
	 * @return String
	 */
	public String get(String key) {
		log.trace("get key {} ...", key);
		String value = null;
		if (key != null) {
			value = conn.get(key);
		}
		return value;
	}

	/**
	 * @param key
	 * @return String
	 */
	public <T> T getObject(String key) {
		String value = null;
		if (key != null) {
			value = get(key);
			if (value != null) {
				return SerializableHelper.deserializeHex(value);
			}
		}
		return null;
	}

	public void expire(String key, long seconds) {
		log.trace("expire key {} , {}", key, seconds);
		conn.expire(key, seconds);
	}

	public void delete(String key) {
		log.trace("del key {}", key);
		conn.del(key);
	}

	public void rPush(String key, Serializable object) {
		conn.rpush(key, SerializableHelper.serializeHex(object));
	}

	public long lRem(String key, int count, String value) {
		return conn.lrem(key, count, value);
	}

	public List<String> lRange(String key, int start, int end) {
		return conn.lrange(key, start, end);
	}

	@SuppressWarnings("deprecation")
	public void openPipeline() {
		this.pipeline = conn.pipelined();
	}

	public List<Object> closePipeline() {
		return pipeline.syncAndReturnAll();
	}

	/**
	 * 释放jedis资源
	 * 
	 * @param jedis
	 */
	public void close() {
		if (conn != null) {
			connectionFactory.close(conn);
		}
	}

	public Jedis getConn() {
		return conn;
	}

	public void setConn(Jedis conn) {
		this.conn = conn;
	}

	public Pipeline getPipeline() {
		return pipeline;
	}

}
