package com.wy.test.authentication.provider.provider;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import com.wy.test.authentication.core.entity.LoginCredential;
import com.wy.test.authentication.core.entity.SignPrincipal;
import com.wy.test.authentication.core.jwt.AuthTokenService;
import com.wy.test.authentication.core.session.Session;
import com.wy.test.authentication.core.session.SessionManager;
import com.wy.test.authentication.core.web.AuthorizationUtils;
import com.wy.test.authentication.otp.onetimepwd.AbstractOtpAuthn;
import com.wy.test.authentication.otp.onetimepwd.MailOtpAuthnService;
import com.wy.test.authentication.provider.realm.AbstractAuthenticationRealm;
import com.wy.test.core.constant.ConstAuthWeb;
import com.wy.test.core.constant.ConstStatus;
import com.wy.test.core.properties.DreamAuthLoginProperties;
import com.wy.test.core.properties.DreamAuthServerProperties;
import com.wy.test.core.vo.UserVO;
import com.wy.test.core.web.AuthWebContext;

import dream.flying.flower.framework.web.enums.AuthLoginType;
import dream.flying.flower.generator.GeneratorStrategyContext;
import lombok.extern.slf4j.Slf4j;

/**
 * 登录认证抽象类
 *
 * @author 飞花梦影
 * @date 2024-09-29 23:36:47
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Slf4j
public abstract class AbstractAuthenticationProvider {

	public static final String PROVIDER_SUFFIX = "AuthenticationProvider";

	protected DreamAuthServerProperties dreamServerProperties;

	protected DreamAuthLoginProperties dreamLoginProperties;

	protected AbstractAuthenticationRealm authenticationRealm;

	protected AbstractOtpAuthn tfaOtpAuthn;

	protected MailOtpAuthnService otpAuthnService;

	protected SessionManager sessionManager;

	protected AuthTokenService authTokenService;

	public static List<GrantedAuthority> grantedAdministratorsAuthoritys = new ArrayList<GrantedAuthority>();

	static {
		grantedAdministratorsAuthoritys.add(new SimpleGrantedAuthority("ROLE_ADMINISTRATORS"));
	}

	public abstract String getProviderName();

	public abstract Authentication doAuthenticate(LoginCredential authentication);

	@SuppressWarnings("rawtypes")
	public boolean supports(Class authentication) {
		return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
	}

	public Authentication authenticate(LoginCredential authentication) {
		return null;
	}

	public Authentication authenticate(LoginCredential authentication, boolean trusted) {
		return null;
	}

	/**
	 * createOnlineSession
	 * 
	 * @param credential
	 * @param userInfo
	 * @return
	 */
	public UsernamePasswordAuthenticationToken createOnlineTicket(LoginCredential credential, UserVO userInfo) {
		// create session
		Session session = new Session();

		// set session with principal
		SignPrincipal principal = new SignPrincipal(userInfo, session);

		List<GrantedAuthority> grantedAuthoritys = authenticationRealm.grantAuthority(userInfo);
		principal.setAuthenticated(true);

		for (GrantedAuthority administratorsAuthority : grantedAdministratorsAuthoritys) {
			if (grantedAuthoritys.contains(administratorsAuthority)) {
				principal.setRoleAdministrators(true);
				log.trace("ROLE ADMINISTRATORS Authentication .");
			}
		}
		log.debug("Granted Authority {}", grantedAuthoritys);

		principal.setGrantedAuthorityApps(authenticationRealm.queryAuthorizedApps(grantedAuthoritys));

		UsernamePasswordAuthenticationToken authenticationToken =
				new UsernamePasswordAuthenticationToken(principal, "PASSWORD", grantedAuthoritys);

		authenticationToken.setDetails(new WebAuthenticationDetails(AuthWebContext.getRequest()));

		/*
		 * put Authentication to current session context
		 */
		session.setAuthentication(authenticationToken);

		// create session
		this.sessionManager.create(session.getId(), session);

		// set Authentication to http session
		AuthorizationUtils.setAuthentication(authenticationToken);

		return authenticationToken;
	}

	/**
	 * login user by j_username and j_cname first query user by j_cname if first
	 * step userinfo is null,query user from system.
	 * 
	 * @param username String
	 * @param password String
	 * @return
	 */
	public UserVO loadUserInfo(String username, String password) {
		UserVO userInfo = authenticationRealm.loadUserInfo(username, password);
		if (userInfo != null) {
			if (userInfo.getUserType() == "SYSTEM") {
				log.debug("SYSTEM User Login. ");
			} else {
				log.debug("User Login. ");
			}

		}

		return userInfo;
	}

	/**
	 * check input password empty.
	 * 
	 * @param password String
	 * @return
	 */
	protected boolean emptyPasswordValid(String password) {
		if (null == password || "".equals(password)) {
			throw new BadCredentialsException(AuthWebContext.getI18nValue("login.error.password.null"));
		}
		return true;
	}

	/**
	 * check input username or password empty.
	 * 
	 * @param email String
	 * @return
	 */
	protected boolean emptyEmailValid(String email) {
		if (null == email || "".equals(email)) {
			throw new BadCredentialsException("login.error.email.null");
		}
		return true;
	}

	/**
	 * check input username empty.
	 * 
	 * @param username String
	 * @return
	 */
	protected boolean emptyUsernameValid(String username) {
		if (null == username || "".equals(username)) {
			throw new BadCredentialsException(AuthWebContext.getI18nValue("login.error.username.null"));
		}
		return true;
	}

	protected boolean statusValid(LoginCredential loginCredential, UserVO userInfo) {
		if (null == userInfo) {
			String i18nMessage = AuthWebContext.getI18nValue("login.error.username");
			log.debug("login user  " + loginCredential.getUsername() + " not in this System ." + i18nMessage);
			UserVO loginUser = new UserVO(loginCredential.getUsername());
			GeneratorStrategyContext generatorStrategyContext = new GeneratorStrategyContext();
			loginUser.setId(generatorStrategyContext.generate());
			loginUser.setUsername(loginCredential.getUsername());
			loginUser.setDisplayName("not exist");
			loginUser.setLoginCount(0);
			authenticationRealm.insertLoginHistory(loginUser, AuthLoginType.LOCAL, "", i18nMessage,
					ConstAuthWeb.LOGIN_RESULT.USER_NOT_EXIST);
			throw new BadCredentialsException(i18nMessage);
		} else {
			if (userInfo.getIsLocked() == ConstStatus.LOCK) {
				authenticationRealm.insertLoginHistory(userInfo, AuthLoginType.getByMsg(loginCredential.getAuthType()),
						loginCredential.getProvider(), loginCredential.getCode(),
						ConstAuthWeb.LOGIN_RESULT.USER_LOCKED);
			} else if (userInfo.getStatus() != ConstStatus.ACTIVE) {
				authenticationRealm.insertLoginHistory(userInfo, AuthLoginType.getByMsg(loginCredential.getAuthType()),
						loginCredential.getProvider(), loginCredential.getCode(),
						ConstAuthWeb.LOGIN_RESULT.USER_INACTIVE);
			}
		}
		return true;
	}
}