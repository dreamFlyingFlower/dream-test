package com.wy.test.synchronizer.activedirectory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wy.test.core.entity.Synchronizers;
import com.wy.test.core.persistence.ldap.ActiveDirectoryUtils;
import com.wy.test.synchronizer.core.synchronizer.ISynchronizerService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ActiveDirectorySynchronizerService implements ISynchronizerService {

	Synchronizers synchronizer;

	@Autowired
	ActiveDirectoryUsersService activeDirectoryUsersService;

	@Autowired
	ActiveDirectoryOrganizationService activeDirectoryOrganizationService;

	public ActiveDirectorySynchronizerService() {
		super();
	}

	@Override
	public void sync() {
		log.info("Sync ...");
		ActiveDirectoryUtils ldapUtils =
				new ActiveDirectoryUtils(synchronizer.getProviderUrl(), synchronizer.getPrincipal(),
						synchronizer.getCredentials(), synchronizer.getUserBasedn(), synchronizer.getMsadDomain());
		ldapUtils.openConnection();

		activeDirectoryOrganizationService.setSynchronizer(synchronizer);
		activeDirectoryOrganizationService.setLdapUtils(ldapUtils);
		activeDirectoryOrganizationService.sync();

		activeDirectoryUsersService.setSynchronizer(synchronizer);
		activeDirectoryUsersService.setLdapUtils(ldapUtils);
		activeDirectoryUsersService.sync();

		ldapUtils.close();
	}

	public ActiveDirectoryUsersService getActiveDirectoryUsersService() {
		return activeDirectoryUsersService;
	}

	public void setActiveDirectoryUsersService(ActiveDirectoryUsersService activeDirectoryUsersService) {
		this.activeDirectoryUsersService = activeDirectoryUsersService;
	}

	public ActiveDirectoryOrganizationService getActiveDirectoryOrganizationService() {
		return activeDirectoryOrganizationService;
	}

	public void setActiveDirectoryOrganizationService(
			ActiveDirectoryOrganizationService activeDirectoryOrganizationService) {
		this.activeDirectoryOrganizationService = activeDirectoryOrganizationService;
	}

	@Override
	public void setSynchronizer(Synchronizers synchronizer) {
		this.synchronizer = synchronizer;
	}

}
