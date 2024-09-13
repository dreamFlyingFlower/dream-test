package com.wy.test.core.autoconfigure;

import java.time.Duration;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;

import com.wy.test.core.redis.RedisConnectionFactory;

import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.JedisPoolConfig;

@AutoConfiguration
@Slf4j
public class RedisAutoConfiguration implements InitializingBean {

	/**
	 * RedisConnectionFactory.
	 * 
	 * @param host String
	 * @param port int
	 * @param timeout int
	 * @param password String
	 * @param maxActive int
	 * @param maxWait int
	 * @param maxIdle int
	 * @param minIdle int
	 * @return RedisConnectionFactory
	 */
	@Bean
	RedisConnectionFactory redisConnFactory(RedisProperties redisProperties) {
		log.debug("redisConnFactory init .");
		RedisConnectionFactory factory = new RedisConnectionFactory();
		factory.setHostName(redisProperties.getHost());
		factory.setPort(redisProperties.getPort() <= 0 ? 6379 : redisProperties.getPort());
		long timeout = redisProperties.getTimeout().getSeconds();
		factory.setTimeOut(timeout <= 0 ? 10000 : (int) timeout);
		factory.setPassword(redisProperties.getPassword());

		JedisPoolConfig poolConfig = new JedisPoolConfig();

		int maxIdle = redisProperties.getJedis().getPool().getMaxIdle();
		poolConfig.setMaxIdle(maxIdle <= 0 ? 100 : maxIdle);

		int minIdle = redisProperties.getJedis().getPool().getMinIdle();
		poolConfig.setMinIdle(minIdle <= 0 ? 0 : maxIdle);

		int maxActive = redisProperties.getJedis().getPool().getMaxActive();
		poolConfig.setMaxTotal(maxActive <= 0 ? -1 : maxActive);

		Duration maxWait = redisProperties.getJedis().getPool().getMaxWait();
		poolConfig.setMaxWait(maxWait == null ? Duration.ofMillis(1000) : maxWait);

		factory.setPoolConfig(poolConfig);

		return factory;
	}

	@Override
	public void afterPropertiesSet() throws Exception {

	}
}