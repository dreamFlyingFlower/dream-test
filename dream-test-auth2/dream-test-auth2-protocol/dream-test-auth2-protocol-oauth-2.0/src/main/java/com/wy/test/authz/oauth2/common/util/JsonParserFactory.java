package com.wy.test.authz.oauth2.common.util;

import org.springframework.util.ClassUtils;

public class JsonParserFactory {

	public static JsonParser create() {
		if (ClassUtils.isPresent("com.fasterxml.jackson.databind.ObjectMapper", null)) {
			return new Jackson2JsonParser();
		}

		throw new IllegalStateException("No Jackson parser found. Please add Jackson to your classpath.");
	}

}