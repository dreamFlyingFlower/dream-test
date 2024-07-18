package com.wy.test.openapi.web.interceptor;

import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.AsyncHandlerInterceptor;

import com.wy.test.authz.oauth2.provider.OAuth2Authentication;
import com.wy.test.authz.oauth2.provider.token.DefaultTokenServices;
import com.wy.test.core.authn.web.AuthorizationUtils;
import com.wy.test.util.AuthorizationHeader;
import com.wy.test.util.AuthorizationHeaderUtils;
import com.wy.test.util.StringUtils;

/**
 * basic认证Interceptor处理.
 */
@Component
public class RestApiPermissionAdapter implements AsyncHandlerInterceptor {

	private static final Logger _logger = LoggerFactory.getLogger(RestApiPermissionAdapter.class);

	@Autowired
	DefaultTokenServices oauth20TokenServices;

	@Autowired
	ProviderManager oauth20ClientAuthenticationManager;

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
		_logger.trace("Rest API Permission Adapter pre handle");
		AuthorizationHeader headerCredential = AuthorizationHeaderUtils.resolve(request);

		// 判断应用的AppId和Secret
		if (headerCredential != null) {
			UsernamePasswordAuthenticationToken authenticationToken = null;
			if (headerCredential.isBasic()) {
				if (StringUtils.isNotBlank(headerCredential.getUsername())
						&& StringUtils.isNotBlank(headerCredential.getCredential())) {
					UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(
							headerCredential.getUsername(), headerCredential.getCredential());
					authenticationToken = (UsernamePasswordAuthenticationToken) oauth20ClientAuthenticationManager
							.authenticate(authRequest);
				}
			} else {
				_logger.trace("Authentication bearer {}", headerCredential.getCredential());
				OAuth2Authentication oauth2Authentication =
						oauth20TokenServices.loadAuthentication(headerCredential.getCredential());

				if (oauth2Authentication != null) {
					_logger.trace("Authentication token {}", oauth2Authentication.getPrincipal().toString());
					authenticationToken = new UsernamePasswordAuthenticationToken(
							new User(oauth2Authentication.getPrincipal().toString(), "CLIENT_SECRET",
									oauth2Authentication.getAuthorities()),
							"PASSWORD", oauth2Authentication.getAuthorities());
				} else {
					_logger.trace("Authentication token is null ");
				}
			}

			if (authenticationToken != null && authenticationToken.isAuthenticated()) {
				AuthorizationUtils.setAuthentication(authenticationToken);
				return true;
			}
		}

		_logger.trace("No Authentication ... forward to /login");
		RequestDispatcher dispatcher = request.getRequestDispatcher("/login");
		dispatcher.forward(request, response);

		return false;
	}
}
