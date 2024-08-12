package com.wy.test.authentication.provider.authn.realm.ldap;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.wy.test.authentication.provider.authn.realm.IAuthenticationServer;
import com.wy.test.core.entity.LdapContextEntity;
import com.wy.test.core.password.PasswordReciprocal;
import com.wy.test.core.persistence.ldap.ActiveDirectoryHelpers;
import com.wy.test.core.persistence.ldap.LdapHelpers;
import com.wy.test.persistence.service.LdapContextService;

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
			List<LdapContextEntity> ldapContexts = ldapContextService.list(new LambdaQueryWrapper<LdapContextEntity>()
					.eq(LdapContextEntity::getInstId, instId).eq(LdapContextEntity::getStatus, 1));
			authenticationRealm = new LdapAuthenticationRealm(false);
			if (ldapContexts != null && ldapContexts.size() > 0) {
				authenticationRealm.setLdapSupport(true);
				List<IAuthenticationServer> ldapAuthenticationServers = new ArrayList<IAuthenticationServer>();
				for (LdapContextEntity ldapContext : ldapContexts) {
					if (ldapContext.getProduct().equalsIgnoreCase("ActiveDirectory")) {
						ActiveDirectoryServer ldapServer = new ActiveDirectoryServer();
						ActiveDirectoryHelpers ldapUtils =
								new ActiveDirectoryHelpers(ldapContext.getProviderUrl(), ldapContext.getPrincipal(),
										PasswordReciprocal.getInstance().decoder(ldapContext.getCredentials()),
										ldapContext.getMsadDomain());
						ldapServer.setActiveDirectoryUtils(ldapUtils);
						if (ldapContext.getAccountMapping().equalsIgnoreCase("YES")) {
							ldapServer.setMapping(true);
						}
						ldapAuthenticationServers.add(ldapServer);

					} else {
						StandardLdapServer standardLdapServer = new StandardLdapServer();
						LdapHelpers ldapUtils =
								new LdapHelpers(ldapContext.getProviderUrl(), ldapContext.getPrincipal(),
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