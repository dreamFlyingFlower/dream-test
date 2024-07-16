package com.wy.test.synchronizer.feishu;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wy.test.entity.Synchronizers;
import com.wy.test.synchronizer.core.synchronizer.ISynchronizerService;

@Service
public class FeishuSynchronizerService implements ISynchronizerService {

	final static Logger _logger = LoggerFactory.getLogger(FeishuSynchronizerService.class);

	Synchronizers synchronizer;

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
		_logger.info("Sync ...");
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
	public void setSynchronizer(Synchronizers synchronizer) {
		this.synchronizer = synchronizer;

	}

}
