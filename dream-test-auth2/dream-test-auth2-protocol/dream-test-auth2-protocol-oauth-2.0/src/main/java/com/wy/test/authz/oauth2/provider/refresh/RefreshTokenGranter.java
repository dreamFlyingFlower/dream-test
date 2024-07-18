package com.wy.test.authz.oauth2.provider.refresh;

import com.wy.test.authz.oauth2.common.OAuth2AccessToken;
import com.wy.test.authz.oauth2.provider.ClientDetailsService;
import com.wy.test.authz.oauth2.provider.OAuth2RequestFactory;
import com.wy.test.authz.oauth2.provider.TokenRequest;
import com.wy.test.authz.oauth2.provider.token.AbstractTokenGranter;
import com.wy.test.authz.oauth2.provider.token.AuthorizationServerTokenServices;
import com.wy.test.core.entity.apps.oauth2.provider.ClientDetails;

/**
 * @author Dave Syer
 * 
 */
public class RefreshTokenGranter extends AbstractTokenGranter {

	private static final String GRANT_TYPE = "refresh_token";

	public RefreshTokenGranter(AuthorizationServerTokenServices tokenServices,
			ClientDetailsService clientDetailsService, OAuth2RequestFactory requestFactory) {
		super(tokenServices, clientDetailsService, requestFactory, GRANT_TYPE);
	}

	@Override
	protected OAuth2AccessToken getAccessToken(ClientDetails client, TokenRequest tokenRequest) {
		String refreshToken = tokenRequest.getRequestParameters().get("refresh_token");
		return getTokenServices().refreshAccessToken(refreshToken, tokenRequest);
	}

}
