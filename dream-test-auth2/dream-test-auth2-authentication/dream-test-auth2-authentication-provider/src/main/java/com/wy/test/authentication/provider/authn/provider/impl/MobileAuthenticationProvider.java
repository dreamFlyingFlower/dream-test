package com.wy.test.authentication.provider.authn.provider.impl;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import com.wy.test.authentication.core.authn.LoginCredential;
import com.wy.test.authentication.core.authn.session.SessionManager;
import com.wy.test.authentication.otp.password.onetimepwd.AbstractOtpAuthn;
import com.wy.test.authentication.provider.authn.provider.AbstractAuthenticationProvider;
import com.wy.test.authentication.provider.authn.realm.AbstractAuthenticationRealm;
import com.wy.test.authentication.sms.password.sms.SmsOtpAuthnService;
import com.wy.test.core.entity.UserEntity;
import com.wy.test.core.properties.DreamAuthServerProperties;
import com.wy.test.core.vo.UserVO;
import com.wy.test.core.web.WebConstants;
import com.wy.test.core.web.WebContext;

import dream.flying.flower.framework.web.enums.AuthLoginType;
import lombok.extern.slf4j.Slf4j;

/**
 * Mobile Authentication provider.
 */
@Slf4j
public class MobileAuthenticationProvider extends AbstractAuthenticationProvider {

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
		log.debug("Trying to authenticate user '{}' via {}", loginCredential.getPrincipal(), getProviderName());
		try {

			// 如果是验证码登录，设置mobile为username
			loginCredential.setUsername(loginCredential.getMobile());
			// 设置密码为验证码
			loginCredential.setPassword(loginCredential.getOtpCaptcha());

			log.debug("authentication " + loginCredential);

			emptyPasswordValid(loginCredential.getPassword());

			emptyUsernameValid(loginCredential.getUsername());

			UserVO userInfo = loadUserInfo(loginCredential.getUsername(), loginCredential.getPassword());

			statusValid(loginCredential, userInfo);

			// Validate PasswordPolicy 取消密码策略验证
			// authenticationRealm.getPasswordPolicyValidator().passwordPolicyValid(userInfo);

			mobileCaptchaValid(loginCredential.getPassword(), userInfo);

			// apply PasswordSetType and resetBadPasswordCount
			authenticationRealm.getPasswordPolicyValidator().applyPasswordPolicy(userInfo);

			authenticationToken = createOnlineTicket(loginCredential, userInfo);
			// user authenticated
			log.debug("'{}' authenticated successfully by {}.", loginCredential.getPrincipal(), getProviderName());

			authenticationRealm.insertLoginHistory(userInfo, AuthLoginType.LOCAL, "", "xe00000004",
					WebConstants.LOGIN_RESULT.SUCCESS);
		} catch (AuthenticationException e) {
			log.error("Failed to authenticate user {} via {}: {}",
					new Object[] { loginCredential.getPrincipal(), getProviderName(), e.getMessage() });
			WebContext.setAttribute(WebConstants.LOGIN_ERROR_SESSION_MESSAGE, e.getMessage());
		} catch (Exception e) {
			log.error("Login error Unexpected exception in {} authentication:\n{}", getProviderName(), e.getMessage());
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
	protected void mobileCaptchaValid(String password, UserVO userInfo) {
		// for mobile password
		if (dreamLoginProperties.getMfa().isEnabled()) {
			UserEntity validUserInfo = new UserEntity();
			validUserInfo.setUsername(userInfo.getUsername());
			validUserInfo.setId(userInfo.getId());
			AbstractOtpAuthn smsOtpAuthn = smsOtpAuthnService.getByInstId(userInfo.getInstId());
			if (password == null || !smsOtpAuthn.validate(validUserInfo, password)) {
				String message = WebContext.getI18nValue("login.error.captcha");
				log.debug("login captcha valid error.");
				throw new BadCredentialsException(message);
			}
		}
	}
}