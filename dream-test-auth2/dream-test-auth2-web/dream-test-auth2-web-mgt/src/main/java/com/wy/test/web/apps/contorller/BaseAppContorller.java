package com.wy.test.web.apps.contorller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.wy.test.crypto.password.PasswordReciprocal;
import com.wy.test.entity.apps.Apps;
import com.wy.test.persistence.service.AppsService;
import com.wy.test.persistence.service.FileUploadService;
import com.wy.test.persistence.service.HistorySystemLogsService;
import com.wy.test.util.StringUtils;

public class BaseAppContorller {

	final static Logger _logger = LoggerFactory.getLogger(BaseAppContorller.class);

	@Autowired
	protected AppsService appsService;

	@Autowired
	protected PasswordReciprocal passwordReciprocal;

	@Autowired
	protected FileUploadService fileUploadService;

	@Autowired
	HistorySystemLogsService systemLog;

	public void setAppsService(AppsService appsService) {
		this.appsService = appsService;
	}

	protected void transform(Apps application) {
		encodeSharedPassword(application);
		encodeSecret(application);
		/*
		 * string field encoding
		 */
		encoding(application);
		/*
		 * upload icon Bytes
		 */
		if (StringUtils.isNotBlank(application.getIconId())) {
			application.setIcon(fileUploadService.get(application.getIconId()).getUploaded());
			fileUploadService.remove(application.getIconId());
		}

	}

	protected void encodeSharedPassword(Apps application) {
		if (StringUtils.isNotBlank(application.getSharedPassword())) {
			application.setSharedPassword(PasswordReciprocal.getInstance().encode(application.getSharedPassword()));
		}
	}

	protected void decoderSharedPassword(Apps application) {
		if (StringUtils.isNotBlank(application.getSharedPassword())) {
			application.setSharedPassword(PasswordReciprocal.getInstance().decoder(application.getSharedPassword()));
		}
	}

	protected void encoding(Apps application) {

	}

	protected void encodeSecret(Apps application) {
		if (StringUtils.isNotBlank(application.getSecret())) {
			String encodeSecret = passwordReciprocal.encode(application.getSecret());
			application.setSecret(encodeSecret);
		}
	}

	protected void decoderSecret(Apps application) {
		if (StringUtils.isNotBlank(application.getSecret())) {
			String decodeSecret = passwordReciprocal.decoder(application.getSecret());
			application.setSecret(decodeSecret);
		}
	}
}
