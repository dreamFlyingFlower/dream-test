package com.wy.test.core.persistence.ldap;

import java.util.Properties;

import javax.naming.Context;
import javax.naming.directory.DirContext;
import javax.naming.directory.SearchControls;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ActiveDirectoryHelpers extends LdapHelpers {

	private final static Logger _logger = LoggerFactory.getLogger(ActiveDirectoryHelpers.class);

	protected String domain;

	String activeDirectoryDomain;

	public ActiveDirectoryHelpers() {
		super();
	}

	public ActiveDirectoryHelpers(String providerUrl, String principal, String credentials, String baseDN,
			String domain) {
		this.providerUrl = providerUrl;
		this.principal = principal;
		this.credentials = credentials;
		this.searchScope = SearchControls.SUBTREE_SCOPE;
		this.baseDN = baseDN;
		this.domain = domain.toUpperCase();
	}

	public ActiveDirectoryHelpers(String providerUrl, String principal, String credentials, String domain) {
		this.providerUrl = providerUrl;
		this.principal = principal;
		this.credentials = credentials;
		this.searchScope = SearchControls.SUBTREE_SCOPE;
		this.domain = domain.toUpperCase();
	}

	public ActiveDirectoryHelpers(DirContext dirContext) {
		this.ctx = dirContext;
	}

	@Override
	protected void initEnvironment() {
		if (props == null) {
			_logger.debug("PROVIDER_URL {}", providerUrl);
			_logger.debug("SECURITY_PRINCIPAL {}", principal);
			// no log credentials
			// _logger.trace("SECURITY_CREDENTIALS {}" , credentials);
			// LDAP
			props = new Properties();
			props.setProperty(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
			props.setProperty(Context.URL_PKG_PREFIXES, "com.sun.jndi.url");
			props.setProperty(Context.REFERRAL, referral);
			props.setProperty(Context.SECURITY_AUTHENTICATION, "simple");

			props.setProperty(Context.PROVIDER_URL, providerUrl);

			if (domain.indexOf(".") > -1) {
				activeDirectoryDomain = domain.substring(0, domain.indexOf("."));
			} else {
				activeDirectoryDomain = domain;
			}

			_logger.info("PROVIDER_DOMAIN:" + activeDirectoryDomain + " for " + domain);
			String activeDirectoryPrincipal = activeDirectoryDomain + "\\" + principal;
			_logger.debug("Active Directory SECURITY_PRINCIPAL : " + activeDirectoryPrincipal);
			props.setProperty(Context.SECURITY_PRINCIPAL, activeDirectoryPrincipal);
			props.setProperty(Context.SECURITY_CREDENTIALS, credentials);

			if (ssl && providerUrl.toLowerCase().startsWith("ldaps")) {
				_logger.info("ldaps security protocol.");
				System.setProperty("javax.net.ssl.trustStore", trustStore);
				System.setProperty("javax.net.ssl.trustStorePassword", trustStorePassword);
				props.put(Context.SECURITY_PROTOCOL, "ssl");
			}
			props.put(Context.REFERRAL, "follow");
		}
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain.toUpperCase();
	}

}
