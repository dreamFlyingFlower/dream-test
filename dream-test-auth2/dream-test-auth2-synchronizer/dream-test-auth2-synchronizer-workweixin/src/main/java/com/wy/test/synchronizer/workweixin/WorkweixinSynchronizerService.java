package com.wy.test.synchronizer.workweixin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wy.test.entity.Synchronizers;
import com.wy.test.synchronizer.core.synchronizer.ISynchronizerService;

@Service
public class WorkweixinSynchronizerService implements ISynchronizerService {

	final static Logger _logger = LoggerFactory.getLogger(WorkweixinSynchronizerService.class);

	Synchronizers synchronizer;

	@Autowired
	WorkweixinUsersService workweixinUsersService;

	@Autowired
	WorkweixinOrganizationService workweixinOrganizationService;

	WorkweixinAccessTokenService workweixinAccessTokenService = new WorkweixinAccessTokenService();

	public WorkweixinSynchronizerService() {
		super();
	}

	@Override
	public void sync() throws Exception {
		_logger.info("Sync ...");
		workweixinAccessTokenService.setCorpid(synchronizer.getPrincipal());
		workweixinAccessTokenService.setCorpsecret(synchronizer.getCredentials());
		String access_token = workweixinAccessTokenService.requestToken();

		workweixinOrganizationService.setSynchronizer(synchronizer);
		workweixinOrganizationService.setAccess_token(access_token);
		workweixinOrganizationService.sync();

		workweixinUsersService.setSynchronizer(synchronizer);
		workweixinUsersService.setAccess_token(access_token);
		workweixinUsersService.sync();
	}

	public WorkweixinUsersService getWorkweixinUsersService() {
		return workweixinUsersService;
	}

	public void setWorkweixinUsersService(WorkweixinUsersService workweixinUsersService) {
		this.workweixinUsersService = workweixinUsersService;
	}

	public WorkweixinOrganizationService getWorkweixinOrganizationService() {
		return workweixinOrganizationService;
	}

	public void setWorkweixinOrganizationService(WorkweixinOrganizationService workweixinOrganizationService) {
		this.workweixinOrganizationService = workweixinOrganizationService;
	}

	public WorkweixinAccessTokenService getWorkweixinAccessTokenService() {
		return workweixinAccessTokenService;
	}

	public void setWorkweixinAccessTokenService(WorkweixinAccessTokenService workweixinAccessTokenService) {
		this.workweixinAccessTokenService = workweixinAccessTokenService;
	}

	@Override
	public void setSynchronizer(Synchronizers synchronizer) {
		this.synchronizer = synchronizer;

	}

}
