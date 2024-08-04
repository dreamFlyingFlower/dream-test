package com.wy.test.provider.authn.provider.impl;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;

import com.wy.test.core.authn.LoginCredential;
import com.wy.test.core.authn.session.SessionManager;
import com.wy.test.core.entity.UserEntity;
import com.wy.test.core.properties.DreamAuthServerProperties;
import com.wy.test.core.web.WebContext;
import com.wy.test.provider.authn.provider.AbstractAuthenticationProvider;
import com.wy.test.provider.authn.realm.AbstractAuthenticationRealm;

import lombok.extern.slf4j.Slf4j;

/**
 * Trusted Authentication provider.
 */
@Slf4j
public class TrustedAuthenticationProvider extends AbstractAuthenticationProvider {

	@Override
	public String getProviderName() {
		return "trusted" + PROVIDER_SUFFIX;
	}

	public TrustedAuthenticationProvider() {
		super();
	}

	public TrustedAuthenticationProvider(AbstractAuthenticationRealm authenticationRealm,
			DreamAuthServerProperties dreamServerProperties, SessionManager sessionManager) {
		this.authenticationRealm = authenticationRealm;
		this.dreamServerProperties = dreamServerProperties;
		this.sessionManager = sessionManager;
	}

	@Override
	public Authentication doAuthenticate(LoginCredential loginCredential) {
		UserEntity loadeduserInfo = loadUserInfo(loginCredential.getUsername(), "");
		statusValid(loginCredential, loadeduserInfo);
		if (loadeduserInfo != null) {
			// Validate PasswordPolicy
			authenticationRealm.getPasswordPolicyValidator().passwordPolicyValid(loadeduserInfo);
			// apply PasswordSetType and resetBadPasswordCount
			authenticationRealm.getPasswordPolicyValidator().applyPasswordPolicy(loadeduserInfo);
			Authentication authentication = createOnlineTicket(loginCredential, loadeduserInfo);

			authenticationRealm.insertLoginHistory(loadeduserInfo, loginCredential.getAuthLoginType(),
					loginCredential.getProvider(), loginCredential.getCode(), loginCredential.getMessage());

			return authentication;
		} else {
			String i18nMessage = WebContext.getI18nValue("login.error.username");
			log.debug("login user {} not in this System . {}", loginCredential.getUsername(), i18nMessage);
			throw new BadCredentialsException(WebContext.getI18nValue("login.error.username"));
		}
	}
}