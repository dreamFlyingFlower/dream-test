package com.wy.test.sync.feishu;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wy.test.core.entity.SyncEntity;
import com.wy.test.sync.core.synchronizer.SyncProcessor;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FeishuSynchronizerService implements SyncProcessor {

	SyncEntity synchronizer;

	@Autowired
	FeishuUsersService feishuUsersService;

	@Autowired
	FeishuOrganizationService feishuOrganizationService;

	FeishuAccessTokenService feishuAccessTokenService = new FeishuAccessTokenService();

	public FeishuSynchronizerService() {
		super();
	}

	@Override
	public void sync() throws Exception {
		log.info("Sync ...");
		feishuAccessTokenService.setAppId(synchronizer.getPrincipal());
		feishuAccessTokenService.setAppSecret(synchronizer.getCredentials());
		String access_token = feishuAccessTokenService.requestToken();

		feishuOrganizationService.setSynchronizer(synchronizer);
		feishuOrganizationService.setAccess_token(access_token);
		feishuOrganizationService.sync();

		feishuUsersService.setSynchronizer(synchronizer);
		feishuUsersService.setAccess_token(access_token);
		feishuUsersService.sync();
	}

	public void setFeishuUsersService(FeishuUsersService feishuUsersService) {
		this.feishuUsersService = feishuUsersService;
	}

	public void setFeishuOrganizationService(FeishuOrganizationService feishuOrganizationService) {
		this.feishuOrganizationService = feishuOrganizationService;
	}

	@Override
	public void setSynchronizer(SyncEntity synchronizer) {
		this.synchronizer = synchronizer;

	}

}
