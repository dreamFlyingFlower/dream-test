package com.wy.test.sync.weixin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wy.test.core.entity.SyncEntity;
import com.wy.test.sync.core.synchronizer.ISynchronizerService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class WorkweixinSynchronizerService implements ISynchronizerService {

	SyncEntity synchronizer;

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
		log.info("Sync ...");
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
	public void setSynchronizer(SyncEntity synchronizer) {
		this.synchronizer = synchronizer;

	}

}
