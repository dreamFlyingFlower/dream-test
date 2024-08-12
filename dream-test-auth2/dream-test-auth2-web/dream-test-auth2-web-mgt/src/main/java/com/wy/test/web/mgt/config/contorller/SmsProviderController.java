package com.wy.test.web.mgt.config.contorller;

import org.apache.commons.lang3.StringUtils;
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

import com.wy.test.authentication.core.authn.annotation.CurrentUser;
import com.wy.test.core.entity.Message;
import com.wy.test.core.entity.SmsProviderEntity;
import com.wy.test.core.entity.UserEntity;
import com.wy.test.core.password.PasswordReciprocal;
import com.wy.test.persistence.service.SmsProviderService;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping(value = { "/config/smsprovider" })
@Slf4j
public class SmsProviderController {

	@Autowired
	private SmsProviderService smsProviderService;

	@GetMapping(value = { "/get" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> get(@CurrentUser UserEntity currentUser) {
		SmsProviderEntity smsProvider = smsProviderService.getById(currentUser.getInstId());
		if (smsProvider != null && StringUtils.isNoneBlank(smsProvider.getId())) {
			smsProvider.setAppSecret(PasswordReciprocal.getInstance().decoder(smsProvider.getAppSecret()));
		}
		return new Message<SmsProviderEntity>(smsProvider).buildResponse();
	}

	@PostMapping(value = { "/update" })
	@ResponseBody
	public ResponseEntity<?> update(@RequestBody SmsProviderEntity smsProvider, @CurrentUser UserEntity currentUser,
			BindingResult result) {
		log.debug("update smsProvider : " + smsProvider);
		smsProvider.setAppSecret(PasswordReciprocal.getInstance().encode(smsProvider.getAppSecret()));
		smsProvider.setInstId(currentUser.getInstId());
		boolean updateResult = false;
		if (StringUtils.isBlank(smsProvider.getId())) {
			smsProvider.setId(smsProvider.getInstId());
			updateResult = smsProviderService.save(smsProvider);
		} else {
			updateResult = smsProviderService.updateById(smsProvider);
		}
		if (updateResult) {
			return new Message<SmsProviderEntity>(Message.SUCCESS).buildResponse();
		} else {
			return new Message<SmsProviderEntity>(Message.FAIL).buildResponse();
		}
	}
}