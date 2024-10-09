package com.wy.test.protocol.oauth2.endpoint;

import java.security.Principal;
import java.util.Map;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wy.test.protocol.oauth2.common.OAuth2Constants;
import com.wy.test.protocol.oauth2.provider.token.store.JwtAccessTokenConverter;

import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * OAuth2 token services that produces JWT encoded token values.
 * 
 * @author Dave Syer
 * @author Luke Taylor
 * @author Joel D'sa
 */
@Tag(name = "OAuth2.0 Token Key API")
@RestController
public class TokenKeyEndpoint {

	private JwtAccessTokenConverter converter;

	public TokenKeyEndpoint(JwtAccessTokenConverter converter) {
		super();
		this.converter = converter;
	}

	/**
	 * Get the verification key for the token signatures. The principal has to be
	 * provided only if the key is secret (shared not public).
	 * 
	 * @param principal the currently authenticated user if there is one
	 * @return the key used to verify tokens
	 */
	@GetMapping(value = OAuth2Constants.ENDPOINT.ENDPOINT_TOKEN_KEY)
	public Map<String, String> getKey(Principal principal) {
		if ((principal == null || principal instanceof AnonymousAuthenticationToken) && !converter.isPublic()) {
			throw new AccessDeniedException("You need to authenticate to see a shared key");
		}
		Map<String, String> result = converter.getKey();
		return result;
	}
}