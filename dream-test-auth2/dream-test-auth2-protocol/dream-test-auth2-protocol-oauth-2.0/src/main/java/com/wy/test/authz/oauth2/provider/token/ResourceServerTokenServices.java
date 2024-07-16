package com.wy.test.authz.oauth2.provider.token;

import org.springframework.security.core.AuthenticationException;

import com.wy.test.authz.oauth2.common.OAuth2AccessToken;
import com.wy.test.authz.oauth2.common.exceptions.InvalidTokenException;
import com.wy.test.authz.oauth2.provider.OAuth2Authentication;

public interface ResourceServerTokenServices {

	/**
	 * Load the credentials for the specified access token.
	 *
	 * @param accessToken The access token value.
	 * @return The authentication for the access token.
	 * @throws AuthenticationException If the access token is expired
	 * @throws InvalidTokenException if the token isn't valid
	 */
	OAuth2Authentication loadAuthentication(String accessToken) throws AuthenticationException, InvalidTokenException;

	/**
	 * Retrieve the full access token details from just the value.
	 * 
	 * @param accessToken the token value
	 * @return the full access token with client id etc.
	 */
	OAuth2AccessToken readAccessToken(String accessToken);

}
