package com.wy.test.provider.authn.provider.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import com.wy.test.core.authn.LoginCredential;
import com.wy.test.core.authn.jwt.AuthTokenService;
import com.wy.test.core.authn.session.SessionManager;
import com.wy.test.core.constants.ConstsLoginType;
import com.wy.test.core.entity.Institutions;
import com.wy.test.core.entity.UserInfo;
import com.wy.test.core.properties.DreamServerProperties;
import com.wy.test.core.web.WebConstants;
import com.wy.test.core.web.WebContext;
import com.wy.test.provider.authn.provider.AbstractAuthenticationProvider;
import com.wy.test.provider.authn.realm.AbstractAuthenticationRealm;

/**
 * database Authentication provider.
 */
public class MfaAuthenticationProvider extends AbstractAuthenticationProvider {

	private static final Logger _logger = LoggerFactory.getLogger(MfaAuthenticationProvider.class);

	@Override
	public String getProviderName() {
		return "mfa" + PROVIDER_SUFFIX;
	}

	public MfaAuthenticationProvider() {
		super();
	}

	public MfaAuthenticationProvider(AbstractAuthenticationRealm authenticationRealm,
			DreamServerProperties dreamServerProperties, SessionManager sessionManager,
			AuthTokenService authTokenService) {
		this.authenticationRealm = authenticationRealm;
		this.dreamServerProperties = dreamServerProperties;
		this.sessionManager = sessionManager;
		this.authTokenService = authTokenService;
	}

	@Override
	public Authentication doAuthenticate(LoginCredential loginCredential) {
		UsernamePasswordAuthenticationToken authenticationToken = null;
		_logger.debug("Trying to authenticate user '{}' via {}", loginCredential.getPrincipal(), getProviderName());
		try {

			_logger.debug("authentication " + loginCredential);

			@SuppressWarnings("unused")
			Institutions inst = (Institutions) WebContext.getAttribute(WebConstants.CURRENT_INST);

			emptyPasswordValid(loginCredential.getPassword());

			UserInfo userInfo = null;

			emptyUsernameValid(loginCredential.getUsername());

			userInfo = loadUserInfo(loginCredential.getUsername(), loginCredential.getPassword());

			statusValid(loginCredential, userInfo);
			// mfa
			mfacaptchaValid(loginCredential.getOtpCaptcha(), userInfo);

			// Validate PasswordPolicy
			authenticationRealm.getPasswordPolicyValidator().passwordPolicyValid(userInfo);

			// Match password
			authenticationRealm.passwordMatches(userInfo, loginCredential.getPassword());

			// apply PasswordSetType and resetBadPasswordCount
			authenticationRealm.getPasswordPolicyValidator().applyPasswordPolicy(userInfo);

			authenticationToken = createOnlineTicket(loginCredential, userInfo);
			// user authenticated
			_logger.debug("'{}' authenticated successfully by {}.", loginCredential.getPrincipal(), getProviderName());

			authenticationRealm.insertLoginHistory(userInfo, ConstsLoginType.LOCAL, "", "xe00000004",
					WebConstants.LOGIN_RESULT.SUCCESS);
		} catch (AuthenticationException e) {
			_logger.error("Failed to authenticate user {} via {}: {}",
					new Object[] { loginCredential.getPrincipal(), getProviderName(), e.getMessage() });
			WebContext.setAttribute(WebConstants.LOGIN_ERROR_SESSION_MESSAGE, e.getMessage());
		} catch (Exception e) {
			_logger.error("Login error Unexpected exception in {} authentication:\n{}", getProviderName(),
					e.getMessage());
		}

		return authenticationToken;
	}

	/**
	 * captcha validate.
	 * 
	 * @param otpCaptcha String
	 * @param authType String
	 * @param userInfo UserInfo
	 */
	protected void mfacaptchaValid(String otpCaptcha, UserInfo userInfo) {
		// for one time password 2 factor
		if (dreamLoginProperties.isMfa()) {
			UserInfo validUserInfo = new UserInfo();
			validUserInfo.setUsername(userInfo.getUsername());
			validUserInfo.setSharedSecret(userInfo.getSharedSecret());
			validUserInfo.setSharedCounter(userInfo.getSharedCounter());
			validUserInfo.setId(userInfo.getId());
			if (otpCaptcha == null || !tfaOtpAuthn.validate(validUserInfo, otpCaptcha)) {
				String message = WebContext.getI18nValue("login.error.captcha");
				_logger.debug("login captcha valid error.");
				throw new BadCredentialsException(message);
			}
		}
	}
}