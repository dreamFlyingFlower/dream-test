package com.wy.test.protocol.oauth2.provider.code;

import java.util.concurrent.TimeUnit;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.wy.test.protocol.oauth2.provider.OAuth2Authentication;

/**
 * Implementation of authorization code services that stores the codes and
 * authentication in memory.
 * 
 * @author Ryan Heaton
 * @author Dave Syer
 */
public class InMemoryAuthorizationCodeServices extends RandomValueAuthorizationCodeServices {

	protected final static Cache<String, OAuth2Authentication> authorizationCodeStore =
			Caffeine.newBuilder().expireAfterWrite(3, TimeUnit.MINUTES).build();

	@Override
	protected void store(String code, OAuth2Authentication authentication) {
		authorizationCodeStore.put(code, authentication);
	}

	@Override
	public OAuth2Authentication remove(String code) {
		OAuth2Authentication auth = authorizationCodeStore.getIfPresent(code);
		authorizationCodeStore.invalidate(code);
		return auth;
	}

}
