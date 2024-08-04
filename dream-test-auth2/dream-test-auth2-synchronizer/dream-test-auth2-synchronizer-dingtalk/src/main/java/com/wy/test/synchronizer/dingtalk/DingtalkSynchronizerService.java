package com.wy.test.synchronizer.dingtalk;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taobao.api.ApiException;
import com.wy.test.core.entity.SyncEntity;
import com.wy.test.synchronizer.core.synchronizer.ISynchronizerService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DingtalkSynchronizerService implements ISynchronizerService {

	SyncEntity synchronizer;

	@Autowired
	DingtalkUsersService dingtalkUsersService;

	@Autowired
	DingtalkOrganizationService dingtalkOrganizationService;

	DingtalkAccessTokenService dingtalkAccessTokenService = new DingtalkAccessTokenService();

	public DingtalkSynchronizerService() {
		super();
	}

	@Override
	public void sync() throws ApiException {
		log.info("Sync ...");
		dingtalkAccessTokenService.setAppkey(synchronizer.getPrincipal());
		dingtalkAccessTokenService.setAppsecret(synchronizer.getCredentials());
		String access_token = dingtalkAccessTokenService.requestToken();

		dingtalkOrganizationService.setSynchronizer(synchronizer);
		dingtalkOrganizationService.setAccess_token(access_token);
		dingtalkOrganizationService.sync();

		dingtalkUsersService.setSynchronizer(synchronizer);
		dingtalkUsersService.setAccess_token(access_token);
		dingtalkUsersService.sync();
	}

	public DingtalkUsersService getDingtalkUsersService() {
		return dingtalkUsersService;
	}

	public void setDingtalkUsersService(DingtalkUsersService dingtalkUsersService) {
		this.dingtalkUsersService = dingtalkUsersService;
	}

	public DingtalkOrganizationService getDingtalkOrganizationService() {
		return dingtalkOrganizationService;
	}

	public void setDingtalkOrganizationService(DingtalkOrganizationService dingtalkOrganizationService) {
		this.dingtalkOrganizationService = dingtalkOrganizationService;
	}

	public SyncEntity getSynchronizer() {
		return synchronizer;
	}

	public DingtalkAccessTokenService getDingtalkAccessTokenService() {
		return dingtalkAccessTokenService;
	}

	public void setDingtalkAccessTokenService(DingtalkAccessTokenService dingtalkAccessTokenService) {
		this.dingtalkAccessTokenService = dingtalkAccessTokenService;
	}

	@Override
	public void setSynchronizer(SyncEntity synchronizer) {
		this.synchronizer = synchronizer;

	}

}
