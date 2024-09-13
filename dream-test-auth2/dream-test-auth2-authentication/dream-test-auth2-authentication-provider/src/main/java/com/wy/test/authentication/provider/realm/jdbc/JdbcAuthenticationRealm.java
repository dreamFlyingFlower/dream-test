package com.wy.test.authentication.provider.realm.jdbc;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.wy.test.authentication.provider.realm.AbstractAuthenticationRealm;
import com.wy.test.authentication.provider.realm.ldap.LdapAuthenticationRealm;
import com.wy.test.authentication.provider.realm.ldap.LdapAuthenticationRealmService;
import com.wy.test.core.constant.ConstStatus;
import com.wy.test.core.constant.ConstAuthWeb;
import com.wy.test.core.entity.ChangePassword;
import com.wy.test.core.entity.PasswordPolicyEntity;
import com.wy.test.core.repository.LoginHistoryRepository;
import com.wy.test.core.repository.PasswordPolicyValidator;
import com.wy.test.core.vo.UserVO;
import com.wy.test.core.web.AuthWebContext;
import com.wy.test.persistence.service.LoginService;
import com.wy.test.persistence.service.UserService;

import dream.flying.flower.framework.web.enums.AuthLoginType;
import lombok.extern.slf4j.Slf4j;

/**
 * JdbcAuthenticationRealm.
 */
@Slf4j
public class JdbcAuthenticationRealm extends AbstractAuthenticationRealm {

	protected PasswordEncoder passwordEncoder;

	public JdbcAuthenticationRealm() {
		log.debug("init . ");
	}

	public JdbcAuthenticationRealm(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public JdbcAuthenticationRealm(PasswordEncoder passwordEncoder, PasswordPolicyValidator passwordPolicyValidator,
			LoginService loginService, LoginHistoryRepository loginHistoryRepository, UserService userService,
			JdbcTemplate jdbcTemplate) {

		this.passwordEncoder = passwordEncoder;
		this.passwordPolicyValidator = passwordPolicyValidator;
		this.loginService = loginService;
		this.loginHistoryRepository = loginHistoryRepository;
		this.userService = userService;
		this.jdbcTemplate = jdbcTemplate;
	}

	public JdbcAuthenticationRealm(PasswordEncoder passwordEncoder, PasswordPolicyValidator passwordPolicyValidator,
			LoginService loginService, LoginHistoryRepository loginHistoryRepository, UserService userService,
			JdbcTemplate jdbcTemplate, LdapAuthenticationRealmService ldapAuthenticationRealmService) {
		this.passwordEncoder = passwordEncoder;
		this.passwordPolicyValidator = passwordPolicyValidator;
		this.loginService = loginService;
		this.loginHistoryRepository = loginHistoryRepository;
		this.userService = userService;
		this.jdbcTemplate = jdbcTemplate;
		this.ldapAuthenticationRealmService = ldapAuthenticationRealmService;
	}

	/**
	 * passwordMatches.
	 */
	@Override
	public boolean passwordMatches(UserVO userInfo, String password) {
		boolean passwordMatches = false;
		// jdbc password check
		// log.trace("password : "
		// + PasswordReciprocal.getInstance().rawPassword(userInfo.getUsername(),
		// password));
		passwordMatches = passwordEncoder.matches(password, userInfo.getPassword());

		if (ldapAuthenticationRealmService != null) {
			// passwordMatches == false and ldapSupport ==true
			// validate password with LDAP
			try {
				LdapAuthenticationRealm ldapRealm = ldapAuthenticationRealmService.getByInstId(userInfo.getInstId());
				if (!passwordMatches && ldapRealm != null && ldapRealm.isLdapSupport()
						&& userInfo.getIsLocked() == ConstStatus.ACTIVE) {
					passwordMatches = ldapRealm.passwordMatches(userInfo, password);
					if (passwordMatches) {
						// write password to database Realm
						ChangePassword changePassword = new ChangePassword(userInfo);
						changePassword.setPassword(password);
						userService.changePassword(changePassword, false);
					}
				}
			} catch (Exception e) {
				log.debug("passwordvalid Exception : {}", e);
			}
		}
		log.debug("passwordvalid : {}", passwordMatches);
		if (!passwordMatches) {
			passwordPolicyValidator.plusBadPasswordCount(userInfo);
			insertLoginHistory(userInfo, AuthLoginType.LOCAL, "", "xe00000004",
					ConstAuthWeb.LOGIN_RESULT.PASSWORD_ERROE);
			PasswordPolicyEntity passwordPolicy =
					passwordPolicyValidator.getPasswordPolicyRepository().getPasswordPolicy();
			if (userInfo.getBadPasswordCount() >= (passwordPolicy.getAttempts() / 2)) {
				throw new BadCredentialsException(AuthWebContext.getI18nValue("login.error.password.attempts",
						new Object[] { userInfo.getBadPasswordCount() + 1, passwordPolicy.getAttempts(),
								passwordPolicy.getDuration() }));
			} else {
				throw new BadCredentialsException(AuthWebContext.getI18nValue("login.error.password"));
			}
		}
		return passwordMatches;
	}
}