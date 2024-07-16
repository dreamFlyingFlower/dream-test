package com.wy.test.provider.authn.realm.jdbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.wy.test.constants.ConstsLoginType;
import com.wy.test.constants.ConstsStatus;
import com.wy.test.entity.ChangePassword;
import com.wy.test.entity.PasswordPolicy;
import com.wy.test.entity.UserInfo;
import com.wy.test.persistence.repository.LoginHistoryRepository;
import com.wy.test.persistence.repository.LoginRepository;
import com.wy.test.persistence.repository.PasswordPolicyValidator;
import com.wy.test.persistence.service.UserInfoService;
import com.wy.test.provider.authn.realm.AbstractAuthenticationRealm;
import com.wy.test.provider.authn.realm.ldap.LdapAuthenticationRealm;
import com.wy.test.provider.authn.realm.ldap.LdapAuthenticationRealmService;
import com.wy.test.web.WebConstants;
import com.wy.test.web.WebContext;

/**
 * JdbcAuthenticationRealm.
 */
public class JdbcAuthenticationRealm extends AbstractAuthenticationRealm {

	private static Logger _logger = LoggerFactory.getLogger(JdbcAuthenticationRealm.class);

	protected PasswordEncoder passwordEncoder;

	public JdbcAuthenticationRealm() {
		_logger.debug("init . ");
	}

	public JdbcAuthenticationRealm(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public JdbcAuthenticationRealm(PasswordEncoder passwordEncoder, PasswordPolicyValidator passwordPolicyValidator,
			LoginRepository loginRepository, LoginHistoryRepository loginHistoryRepository,
			UserInfoService userInfoService, JdbcTemplate jdbcTemplate) {

		this.passwordEncoder = passwordEncoder;
		this.passwordPolicyValidator = passwordPolicyValidator;
		this.loginRepository = loginRepository;
		this.loginHistoryRepository = loginHistoryRepository;
		this.userInfoService = userInfoService;
		this.jdbcTemplate = jdbcTemplate;
	}

	public JdbcAuthenticationRealm(PasswordEncoder passwordEncoder, PasswordPolicyValidator passwordPolicyValidator,
			LoginRepository loginRepository, LoginHistoryRepository loginHistoryRepository,
			UserInfoService userInfoService, JdbcTemplate jdbcTemplate,
			LdapAuthenticationRealmService ldapAuthenticationRealmService) {
		this.passwordEncoder = passwordEncoder;
		this.passwordPolicyValidator = passwordPolicyValidator;
		this.loginRepository = loginRepository;
		this.loginHistoryRepository = loginHistoryRepository;
		this.userInfoService = userInfoService;
		this.jdbcTemplate = jdbcTemplate;
		this.ldapAuthenticationRealmService = ldapAuthenticationRealmService;
	}

	/**
	 * passwordMatches.
	 */
	@Override
	public boolean passwordMatches(UserInfo userInfo, String password) {
		boolean passwordMatches = false;
		// jdbc password check
		// _logger.trace("password : "
		// + PasswordReciprocal.getInstance().rawPassword(userInfo.getUsername(),
		// password));
		passwordMatches = passwordEncoder.matches(password, userInfo.getPassword());

		if (ldapAuthenticationRealmService != null) {
			// passwordMatches == false and ldapSupport ==true
			// validate password with LDAP
			try {
				LdapAuthenticationRealm ldapRealm = ldapAuthenticationRealmService.getByInstId(userInfo.getInstId());
				if (!passwordMatches && ldapRealm != null && ldapRealm.isLdapSupport()
						&& userInfo.getIsLocked() == ConstsStatus.ACTIVE) {
					passwordMatches = ldapRealm.passwordMatches(userInfo, password);
					if (passwordMatches) {
						// write password to database Realm
						ChangePassword changePassword = new ChangePassword(userInfo);
						changePassword.setPassword(password);
						userInfoService.changePassword(changePassword, false);
					}
				}
			} catch (Exception e) {
				_logger.debug("passwordvalid Exception : {}", e);
			}
		}
		_logger.debug("passwordvalid : {}", passwordMatches);
		if (!passwordMatches) {
			passwordPolicyValidator.plusBadPasswordCount(userInfo);
			insertLoginHistory(userInfo, ConstsLoginType.LOCAL, "", "xe00000004",
					WebConstants.LOGIN_RESULT.PASSWORD_ERROE);
			PasswordPolicy passwordPolicy = passwordPolicyValidator.getPasswordPolicyRepository().getPasswordPolicy();
			if (userInfo.getBadPasswordCount() >= (passwordPolicy.getAttempts() / 2)) {
				throw new BadCredentialsException(WebContext.getI18nValue("login.error.password.attempts",
						new Object[] { userInfo.getBadPasswordCount() + 1, passwordPolicy.getAttempts(),
								passwordPolicy.getDuration() }));
			} else {
				throw new BadCredentialsException(WebContext.getI18nValue("login.error.password"));
			}
		}
		return passwordMatches;
	}

}
