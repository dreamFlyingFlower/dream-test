package com.wy.test.web.config.contorller;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wy.test.core.authn.annotation.CurrentUser;
import com.wy.test.core.entity.SmsProvider;
import com.wy.test.core.entity.UserInfo;
import com.wy.test.crypto.password.PasswordReciprocal;
import com.wy.test.entity.Message;
import com.wy.test.persistence.service.SmsProviderService;

@Controller
@RequestMapping(value = { "/config/smsprovider" })
public class SmsProviderController {

	final static Logger _logger = LoggerFactory.getLogger(SmsProviderController.class);

	@Autowired
	private SmsProviderService smsProviderService;

	@GetMapping(value = { "/get" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> get(@CurrentUser UserInfo currentUser) {
		SmsProvider smsProvider = smsProviderService.get(currentUser.getInstId());
		if (smsProvider != null && StringUtils.isNoneBlank(smsProvider.getId())) {
			smsProvider.setAppSecret(PasswordReciprocal.getInstance().decoder(smsProvider.getAppSecret()));
		}
		return new Message<SmsProvider>(smsProvider).buildResponse();
	}

	@PostMapping(value = { "/update" })
	@ResponseBody
	public ResponseEntity<?> update(@RequestBody SmsProvider smsProvider, @CurrentUser UserInfo currentUser,
			BindingResult result) {
		_logger.debug("update smsProvider : " + smsProvider);
		smsProvider.setAppSecret(PasswordReciprocal.getInstance().encode(smsProvider.getAppSecret()));
		smsProvider.setInstId(currentUser.getInstId());
		boolean updateResult = false;
		if (StringUtils.isBlank(smsProvider.getId())) {
			smsProvider.setId(smsProvider.getInstId());
			updateResult = smsProviderService.insert(smsProvider);
		} else {
			updateResult = smsProviderService.update(smsProvider);
		}
		if (updateResult) {
			return new Message<SmsProvider>(Message.SUCCESS).buildResponse();
		} else {
			return new Message<SmsProvider>(Message.FAIL).buildResponse();
		}
	}
}
