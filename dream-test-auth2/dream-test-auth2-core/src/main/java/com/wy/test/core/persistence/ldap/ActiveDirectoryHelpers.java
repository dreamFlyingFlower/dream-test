package com.wy.test.core.persistence.ldap;

import java.util.Properties;

import javax.naming.Context;
import javax.naming.directory.DirContext;
import javax.naming.directory.SearchControls;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ActiveDirectoryHelpers extends LdapHelpers {

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
			log.debug("PROVIDER_URL {}", providerUrl);
			log.debug("SECURITY_PRINCIPAL {}", principal);
			// no log credentials
			// log.trace("SECURITY_CREDENTIALS {}" , credentials);
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

			log.info("PROVIDER_DOMAIN:" + activeDirectoryDomain + " for " + domain);
			String activeDirectoryPrincipal = activeDirectoryDomain + "\\" + principal;
			log.debug("Active Directory SECURITY_PRINCIPAL : " + activeDirectoryPrincipal);
			props.setProperty(Context.SECURITY_PRINCIPAL, activeDirectoryPrincipal);
			props.setProperty(Context.SECURITY_CREDENTIALS, credentials);

			if (ssl && providerUrl.toLowerCase().startsWith("ldaps")) {
				log.info("ldaps security protocol.");
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
