package com.wy.test.authentication.core.session;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.wy.test.core.entity.HistoryLoginEntity;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class InMemorySessionManager implements SessionManager {

	static final long CACHE_MAXIMUM_SIZE = 2000000;

	protected int validitySeconds = 60 * 30; // default 30 minutes.

	protected static Cache<String, Session> sessionStore =
			Caffeine.newBuilder().expireAfterWrite(10, TimeUnit.MINUTES).maximumSize(CACHE_MAXIMUM_SIZE).build();

	public InMemorySessionManager(int validitySeconds) {
		super();
		this.validitySeconds = validitySeconds;
		sessionStore = Caffeine.newBuilder().expireAfterWrite(validitySeconds, TimeUnit.SECONDS)
				.maximumSize(CACHE_MAXIMUM_SIZE).build();

	}

	@Override
	public void create(String sessionId, Session session) {
		session.setExpiredTime(session.getLastAccessTime().plusSeconds(validitySeconds));
		sessionStore.put(sessionId, session);
	}

	@Override
	public Session remove(String sessionId) {
		Session session = sessionStore.getIfPresent(sessionId);
		sessionStore.invalidate(sessionId);
		return session;
	}

	@Override
	public Session get(String sessionId) {
		Session session = sessionStore.getIfPresent(sessionId);
		return session;
	}

	@Override
	public Session refresh(String sessionId, LocalDateTime refreshTime) {
		Session session = get(sessionId);
		if (session != null) {
			log.debug("refresh session Id {} at refreshTime {}", sessionId, refreshTime);
			session.setLastAccessTime(refreshTime);
			// put new session
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
			// sessionId then renew one
			create(sessionId, session);
		}
		return session;
	}

	@Override
	public int getValiditySeconds() {
		return validitySeconds;
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