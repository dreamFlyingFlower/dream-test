package com.wy.test.authentication.provider.provider.impl;

import java.text.ParseException;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import com.wy.test.authentication.core.entity.LoginCredential;
import com.wy.test.authentication.core.jwt.AuthTokenService;
import com.wy.test.authentication.core.session.SessionManager;
import com.wy.test.authentication.provider.provider.AbstractAuthenticationProvider;
import com.wy.test.authentication.provider.realm.AbstractAuthenticationRealm;
import com.wy.test.core.constant.ConstAuthWeb;
import com.wy.test.core.entity.InstitutionEntity;
import com.wy.test.core.properties.DreamAuthServerProperties;
import com.wy.test.core.vo.UserVO;
import com.wy.test.core.web.AuthWebContext;

import dream.flying.flower.framework.web.enums.AuthLoginType;
import lombok.extern.slf4j.Slf4j;

/**
 * database Authentication provider.
 */
@Slf4j
public class NormalAuthenticationProvider extends AbstractAuthenticationProvider {

	@Override
	public String getProviderName() {
		return "normal" + PROVIDER_SUFFIX;
	}

	public NormalAuthenticationProvider() {
		super();
	}

	public NormalAuthenticationProvider(AbstractAuthenticationRealm authenticationRealm,
			DreamAuthServerProperties dreamServerProperties, SessionManager sessionManager,
			AuthTokenService authTokenService) {
		this.authenticationRealm = authenticationRealm;
		this.dreamServerProperties = dreamServerProperties;
		this.sessionManager = sessionManager;
		this.authTokenService = authTokenService;
	}

	@Override
	public Authentication doAuthenticate(LoginCredential loginCredential) {
		UsernamePasswordAuthenticationToken authenticationToken = null;
		log.debug("Trying to authenticate user '{}' via {}", loginCredential.getPrincipal(), getProviderName());
		try {

			log.debug("authentication " + loginCredential);

			InstitutionEntity inst = (InstitutionEntity) AuthWebContext.getAttribute(ConstAuthWeb.CURRENT_INST);

			if (dreamLoginProperties.getCaptcha().isEnabled()) {
				captchaValid(loginCredential.getState(), loginCredential.getCaptcha());

			} else if (!inst.getCaptcha().equalsIgnoreCase("NONE")) {

				captchaValid(loginCredential.getState(), loginCredential.getCaptcha());
			}

			emptyPasswordValid(loginCredential.getPassword());

			emptyUsernameValid(loginCredential.getUsername());

			UserVO userInfo = loadUserInfo(loginCredential.getUsername(), loginCredential.getPassword());

			statusValid(loginCredential, userInfo);

			// Validate PasswordPolicy
			authenticationRealm.getPasswordPolicyValidator().passwordPolicyValid(userInfo);

			// Match password
			authenticationRealm.passwordMatches(userInfo, loginCredential.getPassword());

			// apply PasswordSetType and resetBadPasswordCount
			authenticationRealm.getPasswordPolicyValidator().applyPasswordPolicy(userInfo);

			authenticationToken = createOnlineTicket(loginCredential, userInfo);
			// user authenticated
			log.debug("'{}' authenticated successfully by {}.", loginCredential.getPrincipal(), getProviderName());

			authenticationRealm.insertLoginHistory(userInfo, AuthLoginType.LOCAL, "", "xe00000004",
					ConstAuthWeb.LOGIN_RESULT.SUCCESS);
		} catch (AuthenticationException e) {
			log.error("Failed to authenticate user {} via {}: {}",
					new Object[] { loginCredential.getPrincipal(), getProviderName(), e.getMessage() });
			AuthWebContext.setAttribute(ConstAuthWeb.LOGIN_ERROR_SESSION_MESSAGE, e.getMessage());
		} catch (Exception e) {
			log.error("Login error Unexpected exception in {} authentication:\n{}", getProviderName(), e.getMessage());
		}

		return authenticationToken;
	}

	/**
	 * captcha validate .
	 * 
	 * @param authType String
	 * @param captcha String
	 * @throws ParseException
	 */
	protected void captchaValid(String state, String captcha) throws ParseException {
		// for basic
		if (!authTokenService.validateCaptcha(state, captcha)) {
			throw new BadCredentialsException(AuthWebContext.getI18nValue("login.error.captcha"));
		}
	}
}
