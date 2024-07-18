package com.wy.test.authz.oauth2.provider.authentication;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.authentication.AuthenticationDetailsSource;

/**
 * A source for authentication details in an OAuth2 protected Resource.
 * 
 */
public class OAuth2AuthenticationDetailsSource
		implements AuthenticationDetailsSource<HttpServletRequest, OAuth2AuthenticationDetails> {

	@Override
	public OAuth2AuthenticationDetails buildDetails(HttpServletRequest context) {
		return new OAuth2AuthenticationDetails(context);
	}
}