package com.wy.test.core.persistence.cache;

public interface MomentaryService {

	public void put(String sessionId, String name, Object value);

	public Object get(String sessionId, String name);

	public Object remove(String sessionId, String name);

}
