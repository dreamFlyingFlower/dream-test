package com.wy.test.persistence.cache;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

public class InMemoryMomentaryService implements MomentaryService {

	private static final Logger _logger = LoggerFactory.getLogger(InMemoryMomentaryService.class);

	protected static Cache<String, Object> momentaryStore =
			Caffeine.newBuilder().expireAfterWrite(5, TimeUnit.MINUTES).maximumSize(200000).build();

	public InMemoryMomentaryService() {
		super();
	}

	@Override
	public void put(String sessionId, String name, Object value) {
		_logger.trace("key {}, value {}", getSessionKey(sessionId, name), value);
		momentaryStore.put(getSessionKey(sessionId, name), value);
	}

	@Override
	public Object remove(String sessionId, String name) {
		Object value = momentaryStore.getIfPresent(getSessionKey(sessionId, name));
		momentaryStore.invalidate(getSessionKey(sessionId, name));
		_logger.trace("key {}, value {}", getSessionKey(sessionId, name), value);
		return value;
	}

	@Override
	public Object get(String sessionId, String name) {
		_logger.trace("key {}", getSessionKey(sessionId, name));
		return momentaryStore.getIfPresent(getSessionKey(sessionId, name));
	}

	private String getSessionKey(String sessionId, String name) {
		return sessionId + "_" + name;
	}
}
