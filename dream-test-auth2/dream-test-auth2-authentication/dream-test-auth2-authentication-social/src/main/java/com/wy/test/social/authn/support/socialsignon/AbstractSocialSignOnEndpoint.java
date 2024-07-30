package com.wy.test.social.authn.support.socialsignon;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.wy.test.core.authn.jwt.AuthTokenService;
import com.wy.test.core.entity.SocialsAssociate;
import com.wy.test.core.entity.SocialsProvider;
import com.wy.test.core.properties.DreamServerProperties;
import com.wy.test.core.web.WebContext;
import com.wy.test.provider.authn.provider.AbstractAuthenticationProvider;
import com.wy.test.social.authn.support.socialsignon.service.SocialSignOnProviderService;
import com.wy.test.social.authn.support.socialsignon.service.SocialsAssociateService;

import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.model.AuthResponse;
import me.zhyd.oauth.request.AuthRequest;

public class AbstractSocialSignOnEndpoint {

	final static Logger _logger = LoggerFactory.getLogger(AbstractSocialSignOnEndpoint.class);

	protected AuthRequest authRequest;

	protected String accountJsonString;

	@Autowired
	protected SocialSignOnProviderService socialSignOnProviderService;

	@Autowired
	protected SocialsAssociateService socialsAssociateService;

	@Autowired
	@Qualifier("authenticationProvider")
	AbstractAuthenticationProvider authenticationProvider;

	@Autowired
	AuthTokenService authTokenService;

	@Autowired
	DreamServerProperties dreamServerProperties;

	protected AuthRequest buildAuthRequest(String instId, String provider, String baseUrl) {
		try {
			SocialsProvider socialSignOnProvider = socialSignOnProviderService.get(instId, provider);
			_logger.debug("socialSignOn Provider : " + socialSignOnProvider);

			if (socialSignOnProvider != null) {
				authRequest = socialSignOnProviderService.getAuthRequest(instId, provider, baseUrl);
				return authRequest;
			}
		} catch (Exception e) {
			_logger.debug("buildAuthRequest Exception ", e);
		}
		return null;
	}

	protected SocialsAssociate authCallback(String instId, String provider, String baseUrl) throws Exception {
		SocialsAssociate socialsAssociate = null;
		AuthCallback authCallback = new AuthCallback();
		authCallback.setCode(WebContext.getRequest().getParameter("code"));
		authCallback.setAuth_code(WebContext.getRequest().getParameter("auth_code"));
		authCallback.setOauth_token(WebContext.getRequest().getParameter("oauthToken"));
		authCallback.setAuthorization_code(WebContext.getRequest().getParameter("authorization_code"));
		authCallback.setOauth_verifier(WebContext.getRequest().getParameter("oauthVerifier"));
		authCallback.setState(WebContext.getRequest().getParameter("state"));
		_logger.debug(
				"Callback OAuth code {}, auth_code {}, oauthToken {}, authorization_code {}, oauthVerifier {} , state {}",
				authCallback.getCode(), authCallback.getAuth_code(), authCallback.getOauth_token(),
				authCallback.getAuthorization_code(), authCallback.getOauth_verifier(), authCallback.getState());

		if (authRequest == null) {// if authRequest is null renew one
			authRequest = socialSignOnProviderService.getAuthRequest(instId, provider, baseUrl);
			_logger.debug("session authRequest is null , renew one");
		}

		// State time out, re set
		if (authCallback.getState() != null) {
			authRequest.authorize(WebContext.getRequest().getSession().getId());
		}

		AuthResponse<?> authResponse = authRequest.login(authCallback);
		_logger.debug("Response  : " + authResponse.getData());
		socialsAssociate = new SocialsAssociate();
		socialsAssociate.setProvider(provider);
		socialsAssociate.setSocialUserId(socialSignOnProviderService.getAccountId(provider, authResponse));
		socialsAssociate.setInstId(instId);

		return socialsAssociate;
	}

}
