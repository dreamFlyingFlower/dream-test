package com.wy.test.protocol.oauth2.provider.request;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.common.exceptions.InvalidClientException;
import org.springframework.security.oauth2.common.util.OAuth2Utils;
import org.springframework.security.oauth2.provider.SecurityContextAccessor;

import com.wy.test.core.entity.oauth2.ClientDetails;
import com.wy.test.protocol.oauth2.common.OAuth2Constants;
import com.wy.test.protocol.oauth2.provider.AuthorizationRequest;
import com.wy.test.protocol.oauth2.provider.ClientDetailsService;
import com.wy.test.protocol.oauth2.provider.DefaultSecurityContextAccessor;
import com.wy.test.protocol.oauth2.provider.OAuth2Request;
import com.wy.test.protocol.oauth2.provider.OAuth2RequestFactory;
import com.wy.test.protocol.oauth2.provider.TokenRequest;

/**
 * Default implementation of {@link OAuth2RequestFactory} which initializes
 * fields from the parameters map, validates grant types and scopes, and fills
 * in scopes with the default values from the client if they are missing.
 * 
 * @author Dave Syer
 * @author Amanda Anganes
 * 
 */
public class DefaultOAuth2RequestFactory implements OAuth2RequestFactory {

	private final ClientDetailsService clientDetailsService;

	private SecurityContextAccessor securityContextAccessor = new DefaultSecurityContextAccessor();

	private boolean checkUserScopes = false;

	public DefaultOAuth2RequestFactory(ClientDetailsService clientDetailsService) {
		this.clientDetailsService = clientDetailsService;
	}

	/**
	 * @param securityContextAccessor the security context accessor to set
	 */
	public void setSecurityContextAccessor(SecurityContextAccessor securityContextAccessor) {
		this.securityContextAccessor = securityContextAccessor;
	}

	/**
	 * Flag to indicate that scopes should be interpreted as valid authorities. No
	 * scopes will be granted to a user unless they are permitted as a granted
	 * authority to that user.
	 * 
	 * @param checkUserScopes the checkUserScopes to set (default false)
	 */
	public void setCheckUserScopes(boolean checkUserScopes) {
		this.checkUserScopes = checkUserScopes;
	}

	@Override
	public AuthorizationRequest createAuthorizationRequest(Map<String, String> authorizationParameters) {

		String clientId = authorizationParameters.get(OAuth2Utils.CLIENT_ID);
		String state = authorizationParameters.get(OAuth2Utils.STATE);
		String redirectUri = authorizationParameters.get(OAuth2Utils.REDIRECT_URI);
		// oauth 2.1 PKCE
		String codeChallenge = authorizationParameters.get(OAuth2Constants.PARAMETER.CODE_CHALLENGE);
		String codeChallengeMethod = authorizationParameters.get(OAuth2Constants.PARAMETER.CODE_CHALLENGE_METHOD);
		Set<String> responseTypes =
				OAuth2Utils.parseParameterList(authorizationParameters.get(OAuth2Utils.RESPONSE_TYPE));

		Set<String> scopes = extractScopes(authorizationParameters, clientId);

		AuthorizationRequest request = new AuthorizationRequest(authorizationParameters,
				Collections.<String, String>emptyMap(), clientId, scopes, null, null, false, state, redirectUri,
				responseTypes, codeChallenge, codeChallengeMethod);

		ClientDetails clientDetails = clientDetailsService.loadClientByClientId(clientId, true);
		request.setResourceIdsAndAuthoritiesFromClientDetails(clientDetails);

		return request;

	}

	@Override
	public OAuth2Request createOAuth2Request(AuthorizationRequest request) {
		return request.createOAuth2Request();
	}

	@Override
	public TokenRequest createTokenRequest(Map<String, String> requestParameters, ClientDetails authenticatedClient) {

		String clientId = requestParameters.get(OAuth2Utils.CLIENT_ID);
		if (clientId == null) {
			// if the clientId wasn't passed in in the map, we add pull it from the
			// authenticated client object
			clientId = authenticatedClient.getClientId();
		} else {
			// otherwise, make sure that they match
			if (!clientId.equals(authenticatedClient.getClientId())) {
				throw new InvalidClientException("Given client ID does not match authenticated client");
			}
		}
		String grantType = requestParameters.get(OAuth2Utils.GRANT_TYPE);
		if (StringUtils.isBlank(grantType)) {
			// default client_credentials
			grantType = OAuth2Constants.PARAMETER.GRANT_TYPE_CLIENT_CREDENTIALS;
		}

		Set<String> scopes = extractScopes(requestParameters, clientId);
		TokenRequest tokenRequest = new TokenRequest(requestParameters, clientId, scopes, grantType);

		return tokenRequest;
	}

	@Override
	public TokenRequest createTokenRequest(AuthorizationRequest authorizationRequest, String grantType) {
		TokenRequest tokenRequest = new TokenRequest(authorizationRequest.getRequestParameters(),
				authorizationRequest.getClientId(), authorizationRequest.getScope(), grantType);
		return tokenRequest;
	}

	@Override
	public OAuth2Request createOAuth2Request(ClientDetails client, TokenRequest tokenRequest) {
		return tokenRequest.createOAuth2Request(client);
	}

	private Set<String> extractScopes(Map<String, String> requestParameters, String clientId) {
		Set<String> scopes = OAuth2Utils.parseParameterList(requestParameters.get(OAuth2Utils.SCOPE));
		ClientDetails clientDetails = clientDetailsService.loadClientByClientId(clientId, true);

		if ((scopes == null || scopes.isEmpty())) {
			// If no scopes are specified in the incoming data, use the default values
			// registered with the client
			// (the spec allows us to choose between this option and rejecting the request
			// completely, so we'll take the
			// least obnoxious choice as a default).
			scopes = clientDetails.getScope();
		}

		if (checkUserScopes) {
			scopes = checkUserScopes(scopes, clientDetails);
		}
		return scopes;
	}

	private Set<String> checkUserScopes(Set<String> scopes, ClientDetails clientDetails) {
		if (!securityContextAccessor.isUser()) {
			return scopes;
		}
		Set<String> result = new LinkedHashSet<String>();
		Set<String> authorities = AuthorityUtils.authorityListToSet(securityContextAccessor.getAuthorities());
		for (String scope : scopes) {
			if (authorities.contains(scope) || authorities.contains(scope.toUpperCase())
					|| authorities.contains("ROLE_" + scope.toUpperCase())) {
				result.add(scope);
			}
		}
		return result;
	}

}
