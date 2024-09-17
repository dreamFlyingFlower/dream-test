package com.wy.test.core.cache;

import java.util.concurrent.TimeUnit;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MemoryMomentaryService implements MomentaryService {

	protected static Cache<String, Object> momentaryStore =
			Caffeine.newBuilder().expireAfterWrite(5, TimeUnit.MINUTES).maximumSize(200000).build();

	public MemoryMomentaryService() {
		super();
	}

	@Override
	public void put(String sessionId, String name, Object value) {
		log.trace("key {}, value {}", getSessionKey(sessionId, name), value);
		momentaryStore.put(getSessionKey(sessionId, name), value);
	}

	@Override
	public Object remove(String sessionId, String name) {
		Object value = momentaryStore.getIfPresent(getSessionKey(sessionId, name));
		momentaryStore.invalidate(getSessionKey(sessionId, name));
		log.trace("key {}, value {}", getSessionKey(sessionId, name), value);
		return value;
	}

	@Override
	public Object get(String sessionId, String name) {
		log.trace("key {}", getSessionKey(sessionId, name));
		return momentaryStore.getIfPresent(getSessionKey(sessionId, name));
	}

	private String getSessionKey(String sessionId, String name) {
		return sessionId + "_" + name;
	}
}