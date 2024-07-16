package com.wy.test.social.zhyd.request;

import me.zhyd.oauth.cache.AuthStateCache;
import me.zhyd.oauth.config.AuthConfig;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.model.AuthToken;
import me.zhyd.oauth.model.AuthUser;
import me.zhyd.oauth.request.AuthDefaultRequest;

public class AuthMaxkeyRequest extends AuthDefaultRequest {

	public static final String KEY = "maxkey";

	public AuthMaxkeyRequest(AuthConfig config) {
		super(config, WeLinkAuthDefaultSource.HUAWEI_WELINK);
	}

	public AuthMaxkeyRequest(AuthConfig config, AuthStateCache authStateCache) {
		super(config, MaxkeyAuthDefaultSource.MAXKEY, authStateCache);
	}

	@Override
	protected AuthToken getAccessToken(AuthCallback authCallback) {
		return null;
	}

	@Override
	protected AuthUser getUserInfo(AuthToken authToken) {
		return null;
	}
}
