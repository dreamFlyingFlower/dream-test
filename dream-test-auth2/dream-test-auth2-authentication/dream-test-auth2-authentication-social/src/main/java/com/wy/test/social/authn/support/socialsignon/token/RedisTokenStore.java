package com.wy.test.social.authn.support.socialsignon.token;

import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;

import com.wy.test.core.constants.ConstsTimeInterval;

public class RedisTokenStore {

	protected int validitySeconds = ConstsTimeInterval.ONE_MINUTE * 2;

	private final ConcurrentHashMap<String, String> tokenStore = new ConcurrentHashMap<String, String>();

	public RedisTokenStore() {
		super();
	}

	public static String PREFIX = "REDIS_QRSCRAN_SERVICE_";

	public void store(String token) {
		tokenStore.put(PREFIX + token, "-1");
		/*
		 * DateTime currentDateTime = new DateTime(); RedisConnection conn =
		 * connectionFactory.getConnection(); conn.getConn().setex(PREFIX + token,
		 * validitySeconds, "-1"); conn.close();
		 */
	}

	public boolean bindtoken(String token, String loginname) {
		boolean flag = false;
		try {
			/*
			 * DateTime currentDateTime = new DateTime(); RedisConnection conn =
			 * connectionFactory.getConnection(); conn.getConn().setex(PREFIX + token,
			 * validitySeconds, loginname); //conn.setexObject(PREFIX + token,
			 * validitySeconds, loginname); conn.close();
			 */
			tokenStore.put(PREFIX + token, loginname);
			return true;
		} catch (Exception e) {

		}
		return flag;
	}

	public String get(String token) {
		/*
		 * RedisConnection conn = connectionFactory.getConnection(); String value =
		 * conn.get(PREFIX + token); if(StringUtils.isNotEmpty(value) &&
		 * !"-1".equalsIgnoreCase(value)) { conn.delete(PREFIX + token); return value; }
		 */

		String value = tokenStore.get(PREFIX + token);
		if (StringUtils.isNotEmpty(value) && !"-1".equalsIgnoreCase(value)) {
			tokenStore.remove(PREFIX + token);
			return value;
		}
		return value;
	}

}
