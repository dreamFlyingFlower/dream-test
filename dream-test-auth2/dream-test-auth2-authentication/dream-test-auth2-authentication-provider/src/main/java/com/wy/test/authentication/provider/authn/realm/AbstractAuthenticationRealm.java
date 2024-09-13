package com.wy.test.authentication.provider.authn.realm;

import java.util.Date;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import com.wy.test.authentication.core.authn.SignPrincipal;
import com.wy.test.authentication.provider.authn.realm.ldap.LdapAuthenticationRealmService;
import com.wy.test.core.entity.HistoryLoginEntity;
import com.wy.test.core.entity.RoleEntity;
import com.wy.test.core.repository.LoginHistoryRepository;
import com.wy.test.core.repository.PasswordPolicyValidator;
import com.wy.test.core.vo.UserVO;
import com.wy.test.core.web.WebConstants;
import com.wy.test.core.web.WebContext;
import com.wy.test.persistence.service.LoginService;
import com.wy.test.persistence.service.UserService;

import dream.flying.flower.framework.web.enums.AuthLoginType;
import lombok.extern.slf4j.Slf4j;

/**
 * AbstractAuthenticationRealm.
 */
@Slf4j
public abstract class AbstractAuthenticationRealm {

	protected JdbcTemplate jdbcTemplate;

	protected PasswordPolicyValidator passwordPolicyValidator;

	protected LoginService loginService;

	protected LoginHistoryRepository loginHistoryRepository;

	protected UserService userService;

	protected LdapAuthenticationRealmService ldapAuthenticationRealmService;

	public AbstractAuthenticationRealm() {

	}

	public AbstractAuthenticationRealm(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public PasswordPolicyValidator getPasswordPolicyValidator() {
		return passwordPolicyValidator;
	}

	public LoginService getLoginRepository() {
		return loginService;
	}

	public UserVO loadUserInfo(String username, String password) {
		return loginService.find(username, password);
	}

	public abstract boolean passwordMatches(UserVO userInfo, String password);

	public List<RoleEntity> queryGroups(UserVO userInfo) {
		return loginService.queryRoles(userInfo);
	}

	/**
	 * grant Authority by userinfo
	 * 
	 * @param userInfo
	 * @return ArrayList<GrantedAuthority>
	 */
	public List<GrantedAuthority> grantAuthority(UserVO userInfo) {
		return loginService.grantAuthority(userInfo);
	}

	/**
	 * grant Authority by grantedAuthoritys
	 * 
	 * @param grantedAuthoritys
	 * @return ArrayList<GrantedAuthority Apps>
	 */
	public List<GrantedAuthority> queryAuthorizedApps(List<GrantedAuthority> grantedAuthoritys) {
		return loginService.queryAuthorizedApps(grantedAuthoritys);
	}

	/**
	 * login log write to log db
	 * 
	 * @param uid
	 * @param j_username
	 * @param type
	 * @param code
	 * @param message
	 */
	public boolean insertLoginHistory(UserVO userInfo, AuthLoginType type, String provider, String code,
			String message) {
		HistoryLoginEntity historyLogin = new HistoryLoginEntity();
		historyLogin.setSessionId(WebContext.genId());
		historyLogin.setSessionStatus(7);
		Authentication authentication = (Authentication) WebContext.getAttribute(WebConstants.AUTHENTICATION);
		if (authentication != null && authentication.getPrincipal() instanceof SignPrincipal) {
			historyLogin.setSessionStatus(1);
			historyLogin.setSessionId(userInfo.getSessionId());
		}

		log.debug("user session id is {} . ", historyLogin.getSessionId());

		userInfo.setLastLoginTime(new Date());
		userInfo.setLastLoginIp(WebContext.getRequestIpAddress());

		Browser browser = resolveBrowser();
		historyLogin.setBrowser(browser.getName());
		historyLogin.setPlatform(browser.getPlatform());
		historyLogin.setSourceIp(userInfo.getLastLoginIp());
		historyLogin.setProvider(provider);
		historyLogin.setCode(code);
		historyLogin.setLoginType(type.getMsg());
		historyLogin.setMessage(message);
		historyLogin.setUserId(userInfo.getId());
		historyLogin.setUsername(userInfo.getUsername());
		historyLogin.setDisplayName(userInfo.getDisplayName());
		historyLogin.setInstId(userInfo.getInstId());

		loginHistoryRepository.login(historyLogin);

		loginService.updateLastLogin(userInfo);

		return true;
	}

	public Browser resolveBrowser() {
		Browser browser = new Browser();
		String userAgent = WebContext.getRequest().getHeader("User-Agent");
		String[] arrayUserAgent = null;
		if (userAgent.indexOf("MSIE") > 0) {
			arrayUserAgent = userAgent.split(";");
			browser.setName(arrayUserAgent[1].trim());
			browser.setPlatform(arrayUserAgent[2].trim());
		} else if (userAgent.indexOf("Trident") > 0) {
			arrayUserAgent = userAgent.split(";");
			browser.setName("MSIE/" + arrayUserAgent[3].split("\\)")[0]);

			browser.setPlatform(arrayUserAgent[0].split("\\(")[1]);
		} else if (userAgent.indexOf("Chrome") > 0) {
			arrayUserAgent = userAgent.split(" ");
			// browser=arrayUserAgent[8].trim();
			for (int i = 0; i < arrayUserAgent.length; i++) {
				if (arrayUserAgent[i].contains("Chrome")) {
					browser.setName(arrayUserAgent[i].trim());
					browser.setName(browser.getName().substring(0, browser.getName().indexOf('.')));
				}
			}
			browser.setPlatform((arrayUserAgent[1].substring(1) + " " + arrayUserAgent[2] + " "
					+ arrayUserAgent[3].substring(0, arrayUserAgent[3].length() - 1)).trim());
		} else if (userAgent.indexOf("Firefox") > 0) {
			arrayUserAgent = userAgent.split(" ");
			for (int i = 0; i < arrayUserAgent.length; i++) {
				if (arrayUserAgent[i].contains("Firefox")) {
					browser.setName(arrayUserAgent[i].trim());
					browser.setName(browser.getName().substring(0, browser.getName().indexOf('.')));
				}
			}
			browser.setPlatform((arrayUserAgent[1].substring(1) + " " + arrayUserAgent[2] + " "
					+ arrayUserAgent[3].substring(0, arrayUserAgent[3].length() - 1)).trim());

		}

		return browser;
	}

	public class Browser {

		private String platform;

		private String name;

		public String getPlatform() {
			return platform;
		}

		public void setPlatform(String platform) {
			this.platform = platform;
		}

		public String getName() {
			return name;
		}

		public void setName(String browser) {
			this.name = browser;
		}

	}

}
