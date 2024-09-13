package com.wy.test.authentication.core.authn.session;

import java.time.LocalDateTime;
import java.util.List;

import com.wy.test.core.entity.HistoryLoginEntity;
import com.wy.test.core.redis.RedisConnection;
import com.wy.test.core.redis.RedisConnectionFactory;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RedisSessionManager implements SessionManager {

	protected int validitySeconds = 60 * 30; // default 30 minutes.

	RedisConnectionFactory connectionFactory;

	public static String PREFIX = "DREAM_SESSION_";

	public String getKey(String sessionId) {
		return PREFIX + sessionId;
	}

	/**
	 * @param connectionFactory
	 */
	public RedisSessionManager(RedisConnectionFactory connectionFactory, int validitySeconds) {
		super();
		this.connectionFactory = connectionFactory;
		this.validitySeconds = validitySeconds;
	}

	/**
	 * 
	 */
	public RedisSessionManager() {

	}

	public void setConnectionFactory(RedisConnectionFactory connectionFactory) {
		this.connectionFactory = connectionFactory;
	}

	@Override
	public void create(String sessionId, Session session) {
		log.debug("store session key {} .", sessionId);
		session.setExpiredTime(session.getLastAccessTime().plusSeconds(validitySeconds));
		RedisConnection conn = connectionFactory.getConnection();
		log.trace("store session {} ...", sessionId);
		conn.setexObject(getKey(sessionId), validitySeconds, session);
		log.debug("store session {} successful .", sessionId);
		log.trace("close conn ...");
		conn.close();
		log.trace("close conn successful .");
	}

	@Override
	public Session remove(String sessionId) {
		RedisConnection conn = connectionFactory.getConnection();
		Session ticket = conn.getObject(getKey(sessionId));
		conn.delete(getKey(sessionId));
		conn.close();
		return ticket;
	}

	@Override
	public Session get(String sessionId) {
		RedisConnection conn = connectionFactory.getConnection();
		Session session = conn.getObject(getKey(sessionId));
		conn.close();
		return session;
	}

	@Override
	public int getValiditySeconds() {
		return validitySeconds;
	}

	public void setValiditySeconds(int validitySeconds) {
		this.validitySeconds = validitySeconds;
	}

	@Override
	public Session refresh(String sessionId, LocalDateTime refreshTime) {
		Session session = get(sessionId);
		if (session != null) {
			log.debug("refresh session Id {} at {}", sessionId, refreshTime);
			session.setLastAccessTime(refreshTime);
			create(sessionId, session);
		}
		return session;
	}

	@Override
	public Session refresh(String sessionId) {
		Session session = get(sessionId);
		if (session != null) {
			LocalDateTime currentTime = LocalDateTime.now();
			log.debug("refresh session Id {} at time {}", sessionId, currentTime);
			session.setLastAccessTime(currentTime);
			create(sessionId, session);
		}
		return session;
	}

	@Override
	public List<HistoryLoginEntity> querySessions() {
		// not need implement
		return null;
	}

	@Override
	public void terminate(String sessionId, String userId, String username) {
		// not need implement
	}
}