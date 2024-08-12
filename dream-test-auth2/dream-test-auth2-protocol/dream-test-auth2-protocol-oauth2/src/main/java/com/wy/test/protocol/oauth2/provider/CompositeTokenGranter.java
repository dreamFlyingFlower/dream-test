package com.wy.test.protocol.oauth2.provider;

import java.util.ArrayList;
import java.util.List;

import com.wy.test.protocol.oauth2.common.OAuth2AccessToken;

public class CompositeTokenGranter implements TokenGranter {

	private final List<TokenGranter> tokenGranters;

	public CompositeTokenGranter(List<TokenGranter> tokenGranters) {
		this.tokenGranters = new ArrayList<TokenGranter>(tokenGranters);
	}

	@Override
	public OAuth2AccessToken grant(String grantType, TokenRequest tokenRequest) {
		for (TokenGranter granter : tokenGranters) {
			OAuth2AccessToken grant = granter.grant(grantType, tokenRequest);
			if (grant != null) {
				return grant;
			}
		}
		return null;
	}

}
