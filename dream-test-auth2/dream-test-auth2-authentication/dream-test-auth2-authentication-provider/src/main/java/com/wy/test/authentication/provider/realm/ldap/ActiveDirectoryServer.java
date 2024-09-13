package com.wy.test.authentication.provider.realm.ldap;

import com.wy.test.authentication.provider.realm.IAuthenticationServer;
import com.wy.test.core.ldap.ActiveDirectoryHelpers;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class ActiveDirectoryServer implements IAuthenticationServer {

	ActiveDirectoryHelpers activeDirectoryUtils;

	String filter;

	boolean mapping;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.connsec.web.authentication.realm.IAuthenticationServer#authenticate(java. lang.String, java.lang.String)
	 */
	@Override
	public boolean authenticate(String username, String password) {
		ActiveDirectoryHelpers ldapPassWordValid = new ActiveDirectoryHelpers(activeDirectoryUtils.getProviderUrl(),
				username, password, activeDirectoryUtils.getDomain());
		ldapPassWordValid.openConnection();
		if (ldapPassWordValid.getCtx() != null) {
			log.debug("Active Directory user " + username + "  is validate .");
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
