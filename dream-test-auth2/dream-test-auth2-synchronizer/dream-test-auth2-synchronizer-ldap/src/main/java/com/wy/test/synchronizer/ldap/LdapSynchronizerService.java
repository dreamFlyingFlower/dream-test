package com.wy.test.synchronizer.ldap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wy.test.core.entity.SyncEntity;
import com.wy.test.core.persistence.ldap.LdapHelpers;
import com.wy.test.synchronizer.core.synchronizer.ISynchronizerService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class LdapSynchronizerService implements ISynchronizerService {

	SyncEntity synchronizer;

	@Autowired
	LdapUsersService ldapUsersService;

	@Autowired
	LdapOrganizationService ldapOrganizationService;

	public LdapSynchronizerService() {
		super();
	}

	@Override
	public void sync() {
		log.info("Sync ...");
		LdapHelpers ldapUtils = new LdapHelpers(synchronizer.getProviderUrl(), synchronizer.getPrincipal(),
				synchronizer.getCredentials(), synchronizer.getUserBasedn());
		ldapUtils.openConnection();

		ldapOrganizationService.setSynchronizer(synchronizer);
		ldapUsersService.setSynchronizer(synchronizer);

		ldapOrganizationService.setLdapUtils(ldapUtils);
		ldapUsersService.setLdapUtils(ldapUtils);

		ldapOrganizationService.sync();
		ldapUsersService.sync();

		ldapUtils.close();
	}

	public LdapUsersService getLdapUsersService() {
		return ldapUsersService;
	}

	public void setLdapUsersService(LdapUsersService ldapUsersService) {
		this.ldapUsersService = ldapUsersService;
	}

	public LdapOrganizationService getLdapOrganizationService() {
		return ldapOrganizationService;
	}

	public void setLdapOrganizationService(LdapOrganizationService ldapOrganizationService) {
		this.ldapOrganizationService = ldapOrganizationService;
	}

	@Override
	public void setSynchronizer(SyncEntity synchronizer) {
		this.synchronizer = synchronizer;

	}

}
