package com.wy.test.provider.authn.realm.ldap;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wy.test.core.persistence.ldap.LdapHelpers;
import com.wy.test.provider.authn.realm.IAuthenticationServer;

public final class StandardLdapServer implements IAuthenticationServer {

	private final static Logger _logger = LoggerFactory.getLogger(StandardLdapServer.class);

	LdapHelpers ldapUtils;

	String filterAttribute;

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
		String queryFilter = String.format(filterAttribute, username);
		_logger.info(" filter : " + queryFilter);
		String dn = "";
		SearchControls constraints = new SearchControls();
		constraints.setSearchScope(ldapUtils.getSearchScope());
		try {
			NamingEnumeration<SearchResult> results =
					ldapUtils.getConnection().search(ldapUtils.getBaseDN(), queryFilter, constraints);

			if (results == null || !results.hasMore()) {
				_logger.error("Ldap user " + username + " not found . ");
				return false;
			} else {
				while (results != null && results.hasMore()) {
					SearchResult sr = (SearchResult) results.next();
					// String rdn = sr.getName();
					dn = sr.getNameInNamespace();
					_logger.debug("Directory user dn is " + dn + " .");
				}
			}
		} catch (NamingException e) {
			_logger.error("query throw NamingException:" + e.getMessage());
		} finally {
			// ldapUtils.close();
		}

		LdapHelpers ldapPassWordValid = new LdapHelpers(ldapUtils.getProviderUrl(), dn, password);
		ldapPassWordValid.openConnection();
		if (ldapPassWordValid.getCtx() != null) {
			_logger.debug("Directory user " + username + "  is validate .");
			ldapPassWordValid.close();
			return true;
		}
		return false;
	}

	public LdapHelpers getLdapUtils() {
		return ldapUtils;
	}

	public void setLdapUtils(LdapHelpers ldapUtils) {
		this.ldapUtils = ldapUtils;
	}

	public String getFilterAttribute() {
		return filterAttribute;
	}

	public void setFilterAttribute(String filterAttribute) {
		this.filterAttribute = filterAttribute;
	}

	@Override
	public boolean isMapping() {
		return mapping;
	}

	public void setMapping(boolean mapping) {
		this.mapping = mapping;
	}

}
