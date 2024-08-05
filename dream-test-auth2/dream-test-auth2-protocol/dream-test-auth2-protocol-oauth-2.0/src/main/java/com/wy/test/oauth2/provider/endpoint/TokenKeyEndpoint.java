package com.wy.test.oauth2.provider.endpoint;

import java.security.Principal;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wy.test.oauth2.common.OAuth2Constants;
import com.wy.test.oauth2.provider.token.store.JwtAccessTokenConverter;

/**
 * OAuth2 token services that produces JWT encoded token values.
 * 
 * @author Dave Syer
 * @author Luke Taylor
 * @author Joel D'sa
 */
@Controller
public class TokenKeyEndpoint {

	protected final Log logger = LogFactory.getLog(getClass());

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
	@ResponseBody
	public Map<String, String> getKey(Principal principal) {
		if ((principal == null || principal instanceof AnonymousAuthenticationToken) && !converter.isPublic()) {
			throw new AccessDeniedException("You need to authenticate to see a shared key");
		}
		Map<String, String> result = converter.getKey();
		return result;
	}

}