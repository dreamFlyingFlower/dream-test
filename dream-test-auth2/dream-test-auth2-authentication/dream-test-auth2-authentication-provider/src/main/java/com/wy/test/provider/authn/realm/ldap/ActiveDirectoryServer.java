package com.wy.test.provider.authn.realm.ldap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wy.test.core.persistence.ldap.ActiveDirectoryHelpers;
import com.wy.test.provider.authn.realm.IAuthenticationServer;

public final class ActiveDirectoryServer implements IAuthenticationServer {

	private final static Logger _logger = LoggerFactory.getLogger(ActiveDirectoryServer.class);

	ActiveDirectoryHelpers activeDirectoryUtils;

	String filter;

	boolean mapping;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.connsec.web.authentication.realm.IAuthenticationServer#authenticate(java.
	 * lang.String, java.lang.String)
	 */
	@Override
	public boolean authenticate(String username, String password) {
		ActiveDirectoryHelpers ldapPassWordValid = new ActiveDirectoryHelpers(activeDirectoryUtils.getProviderUrl(),
				username, password, activeDirectoryUtils.getDomain());
		ldapPassWordValid.openConnection();
		if (ldapPassWordValid.getCtx() != null) {
			_logger.debug("Active Directory user " + username + "  is validate .");
			ldapPassWordValid.close();
			return true;
		}

		ldapPassWordValid.close();
		return false;
	}

	public ActiveDirectoryHelpers getActiveDirectoryUtils() {
		return activeDirectoryUtils;
	}

	public void setActiveDirectoryUtils(ActiveDirectoryHelpers activeDirectoryUtils) {
		this.activeDirectoryUtils = activeDirectoryUtils;
	}

	public String getFilter() {
		return filter;
	}

	public void setFilter(String filter) {
		this.filter = filter;
	}

	@Override
	public boolean isMapping() {
		return mapping;
	}

	public void setMapping(boolean mapping) {
		this.mapping = mapping;
	}
}
