package com.wy.test.protocol.oauth2.provider;

import org.springframework.security.oauth2.common.exceptions.InvalidScopeException;

import com.wy.test.core.entity.oauth2.ClientDetails;
import com.wy.test.protocol.oauth2.endpoint.AuthorizationEndpoint;
import com.wy.test.protocol.oauth2.endpoint.TokenEndpoint;

/**
 * Validation interface for OAuth2 requests to the {@link AuthorizationEndpoint}
 * and {@link TokenEndpoint}.
 * 
 * @author Amanda Anganes
 *
 */
public interface OAuth2RequestValidator {

	/**
	 * Ensure that the client has requested a valid set of scopes.
	 * 
	 * @param authorizationRequest the AuthorizationRequest to be validated
	 * @param client the client that is making the request
	 * @throws InvalidScopeException if a requested scope is invalid
	 */
	public void validateScope(AuthorizationRequest authorizationRequest, ClientDetails client)
			throws InvalidScopeException;

	/**
	 * Ensure that the client has requested a valid set of scopes.
	 * 
	 * @param tokenRequest the TokenRequest to be validated
	 * @param client the client that is making the request
	 * @throws InvalidScopeException if a requested scope is invalid
	 */
	public void validateScope(TokenRequest tokenRequest, ClientDetails client) throws InvalidScopeException;

}
