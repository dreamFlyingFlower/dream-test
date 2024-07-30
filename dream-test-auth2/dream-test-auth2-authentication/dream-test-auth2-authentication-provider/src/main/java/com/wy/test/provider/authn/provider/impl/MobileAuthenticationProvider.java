package com.wy.test.provider.authn.provider.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import com.wy.test.core.authn.LoginCredential;
import com.wy.test.core.authn.session.SessionManager;
import com.wy.test.core.constants.ConstsLoginType;
import com.wy.test.core.entity.UserInfo;
import com.wy.test.core.properties.DreamAuthServerProperties;
import com.wy.test.core.web.WebConstants;
import com.wy.test.core.web.WebContext;
import com.wy.test.otp.password.onetimepwd.AbstractOtpAuthn;
import com.wy.test.provider.authn.provider.AbstractAuthenticationProvider;
import com.wy.test.provider.authn.realm.AbstractAuthenticationRealm;
import com.wy.test.sms.password.sms.SmsOtpAuthnService;

/**
 * Mobile Authentication provider.
 */
public class MobileAuthenticationProvider extends AbstractAuthenticationProvider {

	private static final Logger _logger = LoggerFactory.getLogger(MobileAuthenticationProvider.class);

	SmsOtpAuthnService smsOtpAuthnService;

	@Override
	public String getProviderName() {
		return "mobile" + PROVIDER_SUFFIX;
	}

	public MobileAuthenticationProvider() {
		super();
	}

	public MobileAuthenticationProvider(AbstractAuthenticationRealm authenticationRealm,
			DreamAuthServerProperties dreamServerProperties, SmsOtpAuthnService smsOtpAuthnService,
			SessionManager sessionManager) {
		this.authenticationRealm = authenticationRealm;
		this.dreamServerProperties = dreamServerProperties;
		this.smsOtpAuthnService = smsOtpAuthnService;
		this.sessionManager = sessionManager;
	}

	@Override
	public Authentication doAuthenticate(LoginCredential loginCredential) {
		UsernamePasswordAuthenticationToken authenticationToken = null;
		_logger.debug("Trying to authenticate user '{}' via {}", loginCredential.getPrincipal(), getProviderName());
		try {

			// 如果是验证码登录，设置mobile为username
			loginCredential.setUsername(loginCredential.getMobile());
			// 设置密码为验证码
			loginCredential.setPassword(loginCredential.getOtpCaptcha());

			_logger.debug("authentication " + loginCredential);

			emptyPasswordValid(loginCredential.getPassword());

			emptyUsernameValid(loginCredential.getUsername());

			UserInfo userInfo = loadUserInfo(loginCredential.getUsername(), loginCredential.getPassword());

			statusValid(loginCredential, userInfo);

			// Validate PasswordPolicy 取消密码策略验证
			// authenticationRealm.getPasswordPolicyValidator().passwordPolicyValid(userInfo);

			mobileCaptchaValid(loginCredential.getPassword(), userInfo);

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
	 * mobile validate.
	 *
	 * @param otpCaptcha String
	 * @param authType String
	 * @param userInfo UserInfo
	 */
	protected void mobileCaptchaValid(String password, UserInfo userInfo) {
		// for mobile password
		if (dreamLoginProperties.isMfa()) {
			UserInfo validUserInfo = new UserInfo();
			validUserInfo.setUsername(userInfo.getUsername());
			validUserInfo.setId(userInfo.getId());
			AbstractOtpAuthn smsOtpAuthn = smsOtpAuthnService.getByInstId(userInfo.getInstId());
			if (password == null || !smsOtpAuthn.validate(validUserInfo, password)) {
				String message = WebContext.getI18nValue("login.error.captcha");
				_logger.debug("login captcha valid error.");
				throw new BadCredentialsException(message);
			}
		}
	}
}