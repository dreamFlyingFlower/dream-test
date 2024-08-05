package com.wy.test.web.apps.contorller;

import org.springframework.beans.factory.annotation.Autowired;

import com.wy.test.core.entity.AppEntity;
import com.wy.test.core.password.PasswordReciprocal;
import com.wy.test.core.vo.AppVO;
import com.wy.test.persistence.service.AppService;
import com.wy.test.persistence.service.FileUploadService;
import com.wy.test.persistence.service.HistorySysLogService;

import dream.flying.flower.lang.StrHelper;

public class BaseAppContorller {

	@Autowired
	protected AppService appsService;

	@Autowired
	protected PasswordReciprocal passwordReciprocal;

	@Autowired
	protected FileUploadService fileUploadService;

	@Autowired
	HistorySysLogService systemLog;

	protected void transform(AppVO application) {
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
			application.setIcon(fileUploadService.getById(application.getIconId()).getUploaded());
			fileUploadService.removeById(application.getIconId());
		}

	}

	protected void encodeSharedPassword(AppVO application) {
		if (StrHelper.isNotBlank(application.getSharedPassword())) {
			application.setSharedPassword(PasswordReciprocal.getInstance().encode(application.getSharedPassword()));
		}
	}

	protected void decoderSharedPassword(AppVO application) {
		if (StrHelper.isNotBlank(application.getSharedPassword())) {
			application.setSharedPassword(PasswordReciprocal.getInstance().decoder(application.getSharedPassword()));
		}
	}

	protected void encoding(AppVO application) {

	}

	protected void encodeSecret(AppVO application) {
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