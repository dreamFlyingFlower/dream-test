package com.wy.test.web.apps.contorller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.wy.test.core.entity.AppEntity;
import com.wy.test.core.password.PasswordReciprocal;
import com.wy.test.persistence.service.AppsService;
import com.wy.test.persistence.service.FileUploadService;
import com.wy.test.persistence.service.HistorySystemLogsService;

import dream.flying.flower.lang.StrHelper;

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

	protected void transform(AppEntity application) {
		encodeSharedPassword(application);
		encodeSecret(application);
		/*
		 * string field encoding
		 */
		encoding(application);
		/*
		 * upload icon Bytes
		 */
		if (StrHelper.isNotBlank(application.getIconId())) {
			application.setIcon(fileUploadService.get(application.getIconId()).getUploaded());
			fileUploadService.remove(application.getIconId());
		}

	}

	protected void encodeSharedPassword(AppEntity application) {
		if (StrHelper.isNotBlank(application.getSharedPassword())) {
			application.setSharedPassword(PasswordReciprocal.getInstance().encode(application.getSharedPassword()));
		}
	}

	protected void decoderSharedPassword(AppEntity application) {
		if (StrHelper.isNotBlank(application.getSharedPassword())) {
			application.setSharedPassword(PasswordReciprocal.getInstance().decoder(application.getSharedPassword()));
		}
	}

	protected void encoding(AppEntity application) {

	}

	protected void encodeSecret(AppEntity application) {
		if (StrHelper.isNotBlank(application.getSecret())) {
			String encodeSecret = passwordReciprocal.encode(application.getSecret());
			application.setSecret(encodeSecret);
		}
	}

	protected void decoderSecret(AppEntity application) {
		if (StrHelper.isNotBlank(application.getSecret())) {
			String decodeSecret = passwordReciprocal.decoder(application.getSecret());
			application.setSecret(decodeSecret);
		}
	}
}
