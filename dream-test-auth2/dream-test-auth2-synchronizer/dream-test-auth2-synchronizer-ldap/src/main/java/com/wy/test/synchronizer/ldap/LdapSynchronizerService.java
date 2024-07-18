package com.wy.test.synchronizer.ldap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wy.test.core.entity.Synchronizers;
import com.wy.test.core.persistence.ldap.LdapUtils;
import com.wy.test.synchronizer.core.synchronizer.ISynchronizerService;

@Service
public class LdapSynchronizerService implements ISynchronizerService {

	final static Logger _logger = LoggerFactory.getLogger(LdapSynchronizerService.class);

	Synchronizers synchronizer;

	@Autowired
	LdapUsersService ldapUsersService;

	@Autowired
	LdapOrganizationService ldapOrganizationService;

	public LdapSynchronizerService() {
		super();
	}

	@Override
	public void sync() {
		_logger.info("Sync ...");
		LdapUtils ldapUtils = new LdapUtils(synchronizer.getProviderUrl(), synchronizer.getPrincipal(),
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
	public void setSynchronizer(Synchronizers synchronizer) {
		this.synchronizer = synchronizer;

	}

}
