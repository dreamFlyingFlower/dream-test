package com.wy.test.provider.authn.realm.ldap;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.wy.test.common.crypto.password.PasswordReciprocal;
import com.wy.test.core.entity.LdapContext;
import com.wy.test.core.persistence.ldap.ActiveDirectoryUtils;
import com.wy.test.core.persistence.ldap.LdapUtils;
import com.wy.test.persistence.service.LdapContextService;
import com.wy.test.provider.authn.realm.IAuthenticationServer;

public class LdapAuthenticationRealmService {

	protected static final Cache<String, LdapAuthenticationRealm> ldapRealmStore =
			Caffeine.newBuilder().expireAfterWrite(60, TimeUnit.MINUTES).build();

	LdapContextService ldapContextService;

	public LdapAuthenticationRealmService(LdapContextService ldapContextService) {
		this.ldapContextService = ldapContextService;
	}

	public LdapAuthenticationRealm getByInstId(String instId) {
		LdapAuthenticationRealm authenticationRealm = ldapRealmStore.getIfPresent(instId);
		if (authenticationRealm == null) {
			List<LdapContext> ldapContexts = ldapContextService.find("where instid = ? and status = 1 ",
					new Object[] { instId }, new int[] { Types.VARCHAR });
			authenticationRealm = new LdapAuthenticationRealm(false);
			if (ldapContexts != null && ldapContexts.size() > 0) {
				authenticationRealm.setLdapSupport(true);
				List<IAuthenticationServer> ldapAuthenticationServers = new ArrayList<IAuthenticationServer>();
				for (LdapContext ldapContext : ldapContexts) {
					if (ldapContext.getProduct().equalsIgnoreCase("ActiveDirectory")) {
						ActiveDirectoryServer ldapServer = new ActiveDirectoryServer();
						ActiveDirectoryUtils ldapUtils =
								new ActiveDirectoryUtils(ldapContext.getProviderUrl(), ldapContext.getPrincipal(),
										PasswordReciprocal.getInstance().decoder(ldapContext.getCredentials()),
										ldapContext.getMsadDomain());
						ldapServer.setActiveDirectoryUtils(ldapUtils);
						if (ldapContext.getAccountMapping().equalsIgnoreCase("YES")) {
							ldapServer.setMapping(true);
						}
						ldapAuthenticationServers.add(ldapServer);

					} else {
						StandardLdapServer standardLdapServer = new StandardLdapServer();
						LdapUtils ldapUtils = new LdapUtils(ldapContext.getProviderUrl(), ldapContext.getPrincipal(),
								PasswordReciprocal.getInstance().decoder(ldapContext.getCredentials()),
								ldapContext.getBasedn());
						standardLdapServer.setLdapUtils(ldapUtils);
						standardLdapServer.setFilterAttribute(ldapContext.getFilters());
						if (ldapContext.getAccountMapping().equalsIgnoreCase("YES")) {
							standardLdapServer.setMapping(true);
						}
						ldapAuthenticationServers.add(standardLdapServer);
					}
				}
				authenticationRealm.setLdapServers(ldapAuthenticationServers);
			}
			ldapRealmStore.put(instId, authenticationRealm);
		}
		return authenticationRealm;

	}
}
