package com.wy.test.provider.authn.realm.ldap;

import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import com.wy.test.core.entity.UserEntity;
import com.wy.test.provider.authn.realm.AbstractAuthenticationRealm;
import com.wy.test.provider.authn.realm.IAuthenticationServer;

public class LdapAuthenticationRealm extends AbstractAuthenticationRealm {

	private final static Logger _logger = LoggerFactory.getLogger(LdapAuthenticationRealm.class);

	@NotNull
	@Size(min = 1)
	private List<IAuthenticationServer> ldapServers;

	private boolean ldapSupport;

	/**
	 * 
	 */
	public LdapAuthenticationRealm() {

	}

	public LdapAuthenticationRealm(boolean ldapSupport) {
		this.ldapSupport = ldapSupport;
	}

	/**
	 * @param jdbcTemplate
	 */
	public LdapAuthenticationRealm(JdbcTemplate jdbcTemplate) {
		super(jdbcTemplate);
	}

	@Override
	public boolean passwordMatches(UserEntity userInfo, String password) {
		boolean isAuthenticated = false;
		for (final IAuthenticationServer ldapServer : this.ldapServers) {
			String username = userInfo.getUsername();
			if (ldapServer.isMapping()) {// if ldap Context accountMapping equals YES
				username = userInfo.getWindowsAccount();
			}
			_logger.debug("Attempting to authenticate {} at {}", username, ldapServer);
			try {
				isAuthenticated = ldapServer.authenticate(username, password);
			} catch (Exception e) {
				_logger.debug("Attempting Authenticated fail .");
			}
			if (isAuthenticated) {
				return true;
			}
		}
		return false;
	}

	public void setLdapServers(List<IAuthenticationServer> ldapServers) {
		this.ldapServers = ldapServers;
	}

	public boolean isLdapSupport() {
		return ldapSupport;
	}

	public void setLdapSupport(boolean ldapSupport) {
		this.ldapSupport = ldapSupport;
	}

}
