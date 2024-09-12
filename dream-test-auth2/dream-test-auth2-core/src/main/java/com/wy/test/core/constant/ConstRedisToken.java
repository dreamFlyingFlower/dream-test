package com.wy.test.core.constant;

/**
 * Redis常量
 *
 * @author 飞花梦影
 * @date 2024-08-12 14:04:33
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public interface ConstRedisToken {

	String PREFIX = "AUTH_OAUTH_V20_";

	String ACCESS = PREFIX + "ACCESS_";

	String AUTH_TO_ACCESS = PREFIX + "AUTH_TO_ACCESS_";

	String AUTH = PREFIX + "AUTH_";

	String REFRESH_AUTH = PREFIX + "REFRESH_AUTH_";

	String ACCESS_TO_REFRESH = PREFIX + "ACCESS_TO_REFRESH_";

	String REFRESH = PREFIX + "REFRESH_";

	String REFRESH_TO_ACCESS = PREFIX + "REFRESH_TO_ACCESS_";

	String CLIENT_ID_TO_ACCESS = PREFIX + "CLIENT_ID_TO_ACCESS_";

	String UNAME_TO_ACCESS = PREFIX + "UNAME_TO_ACCESS_";
}