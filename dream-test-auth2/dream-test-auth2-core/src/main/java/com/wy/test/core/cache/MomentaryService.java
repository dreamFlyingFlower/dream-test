package com.wy.test.core.cache;

/**
 * Session存储
 *
 * @author 飞花梦影
 * @date 2024-09-17 08:13:32
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public interface MomentaryService {

	void put(String sessionId, String name, Object value);

	Object get(String sessionId, String name);

	Object remove(String sessionId, String name);
}