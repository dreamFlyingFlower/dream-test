package com.wy.test.sync.weixin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wy.test.core.entity.SyncEntity;
import com.wy.test.sync.core.synchronizer.SyncProcessor;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class WeixinSynchronizerService implements SyncProcessor {

	SyncEntity synchronizer;

	@Autowired
	WeixinUsersService workweixinUsersService;

	@Autowired
	WeixinOrganizationService workweixinOrganizationService;

	WeixinAccessTokenService workweixinAccessTokenService = new WeixinAccessTokenService();

	public WeixinSynchronizerService() {
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

	public WeixinUsersService getWorkweixinUsersService() {
		return workweixinUsersService;
	}

	public void setWorkweixinUsersService(WeixinUsersService workweixinUsersService) {
		this.workweixinUsersService = workweixinUsersService;
	}

	public WeixinOrganizationService getWorkweixinOrganizationService() {
		return workweixinOrganizationService;
	}

	public void setWorkweixinOrganizationService(WeixinOrganizationService workweixinOrganizationService) {
		this.workweixinOrganizationService = workweixinOrganizationService;
	}

	public WeixinAccessTokenService getWorkweixinAccessTokenService() {
		return workweixinAccessTokenService;
	}

	public void setWorkweixinAccessTokenService(WeixinAccessTokenService workweixinAccessTokenService) {
		this.workweixinAccessTokenService = workweixinAccessTokenService;
	}

	@Override
	public void setSynchronizer(SyncEntity synchronizer) {
		this.synchronizer = synchronizer;

	}

}
