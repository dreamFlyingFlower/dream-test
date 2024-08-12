package com.wy.test.protocol.authorize.singlelogout;

import java.util.Map;

import org.springframework.security.core.Authentication;

import com.wy.test.core.vo.AppVO;
import com.wy.test.core.web.HttpRequestAdapter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class SingleLogout {

	public abstract void sendRequest(Authentication authentication, AppVO logoutApp);

	public void postMessage(String url, Map<String, Object> paramMap) {
		log.debug("post logout message to url {}", url);
		(new HttpRequestAdapter()).post(url, paramMap);
	}
}