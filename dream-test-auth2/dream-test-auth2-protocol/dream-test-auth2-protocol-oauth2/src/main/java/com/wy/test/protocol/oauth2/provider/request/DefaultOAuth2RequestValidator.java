package com.wy.test.protocol.oauth2.provider.request;

import java.util.Set;

import org.springframework.security.oauth2.common.exceptions.InvalidScopeException;

import com.wy.test.core.entity.oauth2.ClientDetails;
import com.wy.test.protocol.oauth2.provider.AuthorizationRequest;
import com.wy.test.protocol.oauth2.provider.OAuth2RequestValidator;
import com.wy.test.protocol.oauth2.provider.TokenRequest;

/**
 * Default implementation of {@link OAuth2RequestValidator}.
 */
public class DefaultOAuth2RequestValidator implements OAuth2RequestValidator {

	@Override
	public void validateScope(AuthorizationRequest authorizationRequest, ClientDetails client)
			throws InvalidScopeException {
		validateScope(authorizationRequest.getScope(), client.getScope());
	}

	@Override
	public void validateScope(TokenRequest tokenRequest, ClientDetails client) throws InvalidScopeException {
		validateScope(tokenRequest.getScope(), client.getScope());
	}

	private void validateScope(Set<String> requestScopes, Set<String> clientScopes) {

		if (clientScopes != null && !clientScopes.isEmpty()) {
			for (String scope : requestScopes) {
				if (!clientScopes.contains(scope)) {
					throw new InvalidScopeException("Invalid scope: " + scope, clientScopes);
				}
			}
		}

		if (requestScopes.isEmpty()) {
			throw new InvalidScopeException(
					"Empty scope (either the client or the user is not allowed the requested scopes)");
		}
	}

}
