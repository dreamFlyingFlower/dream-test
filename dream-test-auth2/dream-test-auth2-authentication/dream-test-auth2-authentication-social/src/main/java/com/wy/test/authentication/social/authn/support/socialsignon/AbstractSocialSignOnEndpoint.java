package com.wy.test.authentication.social.authn.support.socialsignon;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.wy.test.authentication.core.authn.jwt.AuthTokenService;
import com.wy.test.authentication.provider.authn.provider.AbstractAuthenticationProvider;
import com.wy.test.authentication.social.authn.support.socialsignon.service.SocialSignOnProviderService;
import com.wy.test.authentication.social.authn.support.socialsignon.service.SocialsAssociateService;
import com.wy.test.core.entity.SocialAssociateEntity;
import com.wy.test.core.entity.SocialProviderEntity;
import com.wy.test.core.properties.DreamAuthServerProperties;
import com.wy.test.core.web.WebContext;

import lombok.extern.slf4j.Slf4j;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.model.AuthResponse;
import me.zhyd.oauth.request.AuthRequest;

@Slf4j
public class AbstractSocialSignOnEndpoint {

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
	DreamAuthServerProperties dreamServerProperties;

	protected AuthRequest buildAuthRequest(String instId, String provider, String baseUrl) {
		try {
			SocialProviderEntity socialSignOnProvider = socialSignOnProviderService.get(instId, provider);
			log.debug("socialSignOn Provider : " + socialSignOnProvider);

			if (socialSignOnProvider != null) {
				authRequest = socialSignOnProviderService.getAuthRequest(instId, provider, baseUrl);
				return authRequest;
			}
		} catch (Exception e) {
			log.debug("buildAuthRequest Exception ", e);
		}
		return null;
	}

	protected SocialAssociateEntity authCallback(String instId, String provider, String baseUrl) throws Exception {
		SocialAssociateEntity socialsAssociate = null;
		AuthCallback authCallback = new AuthCallback();
		authCallback.setCode(WebContext.getRequest().getParameter("code"));
		authCallback.setAuth_code(WebContext.getRequest().getParameter("auth_code"));
		authCallback.setOauth_token(WebContext.getRequest().getParameter("oauthToken"));
		authCallback.setAuthorization_code(WebContext.getRequest().getParameter("authorization_code"));
		authCallback.setOauth_verifier(WebContext.getRequest().getParameter("oauthVerifier"));
		authCallback.setState(WebContext.getRequest().getParameter("state"));
		log.debug(
				"Callback OAuth code {}, auth_code {}, oauthToken {}, authorization_code {}, oauthVerifier {} , state {}",
				authCallback.getCode(), authCallback.getAuth_code(), authCallback.getOauth_token(),
				authCallback.getAuthorization_code(), authCallback.getOauth_verifier(), authCallback.getState());

		if (authRequest == null) {// if authRequest is null renew one
			authRequest = socialSignOnProviderService.getAuthRequest(instId, provider, baseUrl);
			log.debug("session authRequest is null , renew one");
		}

		// State time out, re set
		if (authCallback.getState() != null) {
			authRequest.authorize(WebContext.getRequest().getSession().getId());
		}

		AuthResponse<?> authResponse = authRequest.login(authCallback);
		log.debug("Response  : " + authResponse.getData());
		socialsAssociate = new SocialAssociateEntity();
		socialsAssociate.setProvider(provider);
		socialsAssociate.setSocialUserId(socialSignOnProviderService.getAccountId(provider, authResponse));
		socialsAssociate.setInstId(instId);

		return socialsAssociate;
	}
}