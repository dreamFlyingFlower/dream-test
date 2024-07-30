package com.wy.test.openapi.web.interceptor;

import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.AsyncHandlerInterceptor;

import com.wy.test.core.password.PasswordReciprocal;
import com.wy.test.oauth2.provider.OAuth2Authentication;
import com.wy.test.oauth2.provider.token.DefaultTokenServices;

import dream.flying.flower.framework.core.helper.TokenHelpers;

/**
 * OAuth v2.0 accessToken认证Interceptor处理.
 */
@Component
public class Oauth20ApiPermissionAdapter implements AsyncHandlerInterceptor {

	private static final Logger _logger = LoggerFactory.getLogger(Oauth20ApiPermissionAdapter.class);

	@Autowired
	protected PasswordReciprocal passwordReciprocal;

	@Autowired
	private DefaultTokenServices oauth20TokenServices;

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
		_logger.trace("OAuth20 API Permission Adapter pre handle");
		String accessToken = TokenHelpers.resolveAccessToken(request);
		_logger.trace("access_token {} ", accessToken);
		try {
			OAuth2Authentication authentication = oauth20TokenServices.loadAuthentication(accessToken);
			// 判断应用的accessToken信息
			if (authentication != null) {
				_logger.trace("authentication " + authentication);
				return true;
			}
		} catch (Exception e) {
			_logger.error("load Authentication Exception ! ", e);
		}

		_logger.trace("No Authentication ... forward to /login");
		RequestDispatcher dispatcher = request.getRequestDispatcher("/login");
		dispatcher.forward(request, response);

		return false;
	}
}
