package com.wy.test.provider.authn.provider.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;

import com.wy.test.configuration.ApplicationConfig;
import com.wy.test.core.authn.LoginCredential;
import com.wy.test.core.authn.session.SessionManager;
import com.wy.test.entity.UserInfo;
import com.wy.test.provider.authn.provider.AbstractAuthenticationProvider;
import com.wy.test.provider.authn.realm.AbstractAuthenticationRealm;
import com.wy.test.web.WebContext;

/**
 * Trusted Authentication provider.
 */
public class TrustedAuthenticationProvider extends AbstractAuthenticationProvider {

	private static final Logger _logger = LoggerFactory.getLogger(TrustedAuthenticationProvider.class);

	@Override
	public String getProviderName() {
		return "trusted" + PROVIDER_SUFFIX;
	}

	public TrustedAuthenticationProvider() {
		super();
	}

	public TrustedAuthenticationProvider(AbstractAuthenticationRealm authenticationRealm,
			ApplicationConfig applicationConfig, SessionManager sessionManager) {
		this.authenticationRealm = authenticationRealm;
		this.applicationConfig = applicationConfig;
		this.sessionManager = sessionManager;
	}

	@Override
	public Authentication doAuthenticate(LoginCredential loginCredential) {
		UserInfo loadeduserInfo = loadUserInfo(loginCredential.getUsername(), "");
		statusValid(loginCredential, loadeduserInfo);
		if (loadeduserInfo != null) {
			// Validate PasswordPolicy
			authenticationRealm.getPasswordPolicyValidator().passwordPolicyValid(loadeduserInfo);
			// apply PasswordSetType and resetBadPasswordCount
			authenticationRealm.getPasswordPolicyValidator().applyPasswordPolicy(loadeduserInfo);
			Authentication authentication = createOnlineTicket(loginCredential, loadeduserInfo);

			authenticationRealm.insertLoginHistory(loadeduserInfo, loginCredential.getAuthType(),
					loginCredential.getProvider(), loginCredential.getCode(), loginCredential.getMessage());

			return authentication;
		} else {
			String i18nMessage = WebContext.getI18nValue("login.error.username");
			_logger.debug("login user {} not in this System . {}", loginCredential.getUsername(), i18nMessage);
			throw new BadCredentialsException(WebContext.getI18nValue("login.error.username"));
		}
	}

}
