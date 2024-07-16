package com.wy.test.synchronizer.dingtalk;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taobao.api.ApiException;
import com.wy.test.entity.Synchronizers;
import com.wy.test.synchronizer.core.synchronizer.ISynchronizerService;

@Service
public class DingtalkSynchronizerService implements ISynchronizerService {

	final static Logger _logger = LoggerFactory.getLogger(DingtalkSynchronizerService.class);

	Synchronizers synchronizer;

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
		_logger.info("Sync ...");
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

	public Synchronizers getSynchronizer() {
		return synchronizer;
	}

	public DingtalkAccessTokenService getDingtalkAccessTokenService() {
		return dingtalkAccessTokenService;
	}

	public void setDingtalkAccessTokenService(DingtalkAccessTokenService dingtalkAccessTokenService) {
		this.dingtalkAccessTokenService = dingtalkAccessTokenService;
	}

	@Override
	public void setSynchronizer(Synchronizers synchronizer) {
		this.synchronizer = synchronizer;

	}

}
