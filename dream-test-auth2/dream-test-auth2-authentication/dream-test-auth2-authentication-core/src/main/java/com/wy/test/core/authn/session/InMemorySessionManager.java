package com.wy.test.core.authn.session;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.wy.test.core.entity.HistoryLogin;

public class InMemorySessionManager implements SessionManager {

	private static final Logger _logger = LoggerFactory.getLogger(InMemorySessionManager.class);

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
			_logger.debug("refresh session Id {} at refreshTime {}", sessionId, refreshTime);
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
			_logger.debug("refresh session Id {} at time {}", sessionId, currentTime);
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
	public List<HistoryLogin> querySessions() {
		// not need implement
		return null;
	}

	@Override
	public void terminate(String sessionId, String userId, String username) {
		// not need implement
	}

}
