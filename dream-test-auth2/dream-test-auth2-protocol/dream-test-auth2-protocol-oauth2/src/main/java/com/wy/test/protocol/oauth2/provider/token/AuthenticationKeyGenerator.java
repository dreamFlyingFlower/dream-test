package com.wy.test.protocol.oauth2.provider.token;

import com.wy.test.protocol.oauth2.provider.OAuth2Authentication;

/**
 * Strategy interface for extracting a unique key from an
 * {@link OAuth2Authentication}.
 * 
 * @author Dave Syer
 * 
 */
public interface AuthenticationKeyGenerator {

	/**
	 * @param authentication an OAuth2Authentication
	 * @return a unique key identifying the authentication
	 */
	String extractKey(OAuth2Authentication authentication);

}
