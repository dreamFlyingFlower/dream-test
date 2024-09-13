package com.wy.test.authentication.social.request;

import me.zhyd.oauth.cache.AuthStateCache;
import me.zhyd.oauth.config.AuthConfig;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.model.AuthToken;
import me.zhyd.oauth.model.AuthUser;
import me.zhyd.oauth.request.AuthDefaultRequest;

public class AuthDreamRequest extends AuthDefaultRequest {

	public static final String KEY = "dream";

	public AuthDreamRequest(AuthConfig config) {
		super(config, WeLinkAuthDefaultSource.HUAWEI_WELINK);
	}

	public AuthDreamRequest(AuthConfig config, AuthStateCache authStateCache) {
		super(config, DreamAuthDefaultSource.DREAM, authStateCache);
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
