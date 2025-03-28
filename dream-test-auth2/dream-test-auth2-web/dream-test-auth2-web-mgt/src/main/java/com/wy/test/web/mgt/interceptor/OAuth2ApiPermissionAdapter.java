package com.wy.test.web.mgt.interceptor;

import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.AsyncHandlerInterceptor;

import com.wy.test.core.password.PasswordReciprocal;
import com.wy.test.protocol.oauth2.provider.OAuth2Authentication;
import com.wy.test.protocol.oauth2.provider.token.DefaultTokenServices;

import dream.flying.flower.framework.web.helper.TokenHelpers;
import lombok.extern.slf4j.Slf4j;

/**
 * OAuth v2.0 accessToken认证Interceptor处理
 *
 * @author 飞花梦影
 * @date 2024-08-08 11:50:23
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Component
@Slf4j
public class OAuth2ApiPermissionAdapter implements AsyncHandlerInterceptor {

	@Autowired
	protected PasswordReciprocal passwordReciprocal;

	@Autowired
	protected DefaultTokenServices oauth2TokenServices;

	static ConcurrentHashMap<String, String> navigationsMap = null;

	/*
	 * 请求前处理 (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.web.servlet.handler.HandlerInterceptorAdapter#preHandle(
	 * javax.servlet.http.HttpServletRequest,
	 * javax.servlet.http.HttpServletResponse, java.lang.Object)
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		log.trace("OAuth20 API Permission Adapter pre handle");
		String accessToken = TokenHelpers.resolveAccessToken(request);
		log.trace("access_token {} ", accessToken);
		try {
			OAuth2Authentication authentication = oauth2TokenServices.loadAuthentication(accessToken);
			// 判断应用的accessToken信息
			if (authentication != null) {
				log.trace("authentication " + authentication);
				return true;
			}
		} catch (Exception e) {
			log.error("load Authentication Exception ! ", e);
		}

		log.trace("No Authentication ... forward to /login");
		RequestDispatcher dispatcher = request.getRequestDispatcher("/login");
		dispatcher.forward(request, response);

		return false;
	}
}