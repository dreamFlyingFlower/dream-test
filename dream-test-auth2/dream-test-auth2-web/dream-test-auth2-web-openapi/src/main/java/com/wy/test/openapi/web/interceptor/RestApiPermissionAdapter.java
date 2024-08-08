package com.wy.test.openapi.web.interceptor;

import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.AsyncHandlerInterceptor;

import com.wy.test.core.authn.web.AuthorizationUtils;
import com.wy.test.oauth2.provider.OAuth2Authentication;
import com.wy.test.oauth2.provider.token.DefaultTokenServices;

import dream.flying.flower.framework.core.helper.TokenHeader;
import dream.flying.flower.framework.core.helper.TokenHelpers;
import dream.flying.flower.lang.StrHelper;
import lombok.extern.slf4j.Slf4j;

/**
 * basic认证Interceptor处理
 *
 * @author 飞花梦影
 * @date 2024-08-08 11:47:12
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Component
@Slf4j
public class RestApiPermissionAdapter implements AsyncHandlerInterceptor {

	@Autowired
	DefaultTokenServices oauth20TokenServices;

	@Autowired
	ProviderManager oauth20ClientAuthenticationManager;

	static ConcurrentHashMap<String, String> navigationsMap = null;

	/*
	 * 请求前处理 (non-Javadoc)
	 * 
	 * @see org.springframework.web.servlet.handler.HandlerInterceptorAdapter#preHandle(
	 * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object)
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		log.trace("Rest API Permission Adapter pre handle");
		TokenHeader headerCredential = TokenHelpers.resolve(request);

		// 判断应用的AppId和Secret
		if (headerCredential != null) {
			UsernamePasswordAuthenticationToken authenticationToken = null;
			if (headerCredential.isBasic()) {
				if (StrHelper.isNotBlank(headerCredential.getUsername())
						&& StrHelper.isNotBlank(headerCredential.getCredential())) {
					UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(
							headerCredential.getUsername(), headerCredential.getCredential());
					authenticationToken = (UsernamePasswordAuthenticationToken) oauth20ClientAuthenticationManager
							.authenticate(authRequest);
				}
			} else {
				log.trace("Authentication bearer {}", headerCredential.getCredential());
				OAuth2Authentication oauth2Authentication =
						oauth20TokenServices.loadAuthentication(headerCredential.getCredential());

				if (oauth2Authentication != null) {
					log.trace("Authentication token {}", oauth2Authentication.getPrincipal().toString());
					authenticationToken = new UsernamePasswordAuthenticationToken(
							new User(oauth2Authentication.getPrincipal().toString(), "CLIENT_SECRET",
									oauth2Authentication.getAuthorities()),
							"PASSWORD", oauth2Authentication.getAuthorities());
				} else {
					log.trace("Authentication token is null ");
				}
			}

			if (authenticationToken != null && authenticationToken.isAuthenticated()) {
				AuthorizationUtils.setAuthentication(authenticationToken);
				return true;
			}
		}

		log.trace("No Authentication ... forward to /login");
		RequestDispatcher dispatcher = request.getRequestDispatcher("/login");
		dispatcher.forward(request, response);

		return false;
	}
}