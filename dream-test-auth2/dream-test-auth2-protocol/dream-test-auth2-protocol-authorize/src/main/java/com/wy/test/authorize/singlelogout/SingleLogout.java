package com.wy.test.authorize.singlelogout;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;

import com.wy.test.entity.apps.Apps;
import com.wy.test.web.HttpRequestAdapter;

public abstract class SingleLogout {

	private static final Logger _logger = LoggerFactory.getLogger(SingleLogout.class);

	public abstract void sendRequest(Authentication authentication, Apps logoutApp);

	public void postMessage(String url, Map<String, Object> paramMap) {
		_logger.debug("post logout message to url {}", url);
		(new HttpRequestAdapter()).post(url, paramMap);
	}
}
