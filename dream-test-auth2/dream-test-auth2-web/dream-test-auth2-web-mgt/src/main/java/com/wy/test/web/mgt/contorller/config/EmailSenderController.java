package com.wy.test.web.mgt.contorller.config;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wy.test.authentication.core.annotation.CurrentUser;
import com.wy.test.core.base.ResultResponse;
import com.wy.test.core.entity.EmailSenderEntity;
import com.wy.test.core.entity.UserEntity;
import com.wy.test.core.password.PasswordReciprocal;
import com.wy.test.persistence.service.EmailSenderService;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping(value = { "/config/emailsenders" })
@Slf4j
public class EmailSenderController {

	@Autowired
	private EmailSenderService emailSendersService;

	@GetMapping(value = { "/get" })
	public ResponseEntity<?> get(@CurrentUser UserEntity currentUser) {
		EmailSenderEntity emailSenders = emailSendersService.getById(currentUser.getInstId());
		if (emailSenders != null && StringUtils.isNotBlank(emailSenders.getCredentials())) {
			emailSenders.setCredentials(PasswordReciprocal.getInstance().decoder(emailSenders.getCredentials()));
		} else {
			emailSenders = new EmailSenderEntity();
			emailSenders.setProtocol("smtp");
			emailSenders.setEncoding("utf-8");
		}
		return new ResultResponse<EmailSenderEntity>(emailSenders).buildResponse();
	}

	@PostMapping(value = { "/update" })
	@ResponseBody
	public ResponseEntity<?> update(@RequestBody EmailSenderEntity emailSenders, @CurrentUser UserEntity currentUser,
			BindingResult result) {
		log.debug("update emailSenders : " + emailSenders);
		emailSenders.setInstId(currentUser.getInstId());
		emailSenders.setCredentials(PasswordReciprocal.getInstance().encode(emailSenders.getCredentials()));
		if (StringUtils.isBlank(emailSenders.getId())) {
			emailSenders.setId(emailSenders.getInstId());
			if (emailSendersService.save(emailSenders)) {
				return new ResultResponse<EmailSenderEntity>(ResultResponse.SUCCESS).buildResponse();
			} else {
				return new ResultResponse<EmailSenderEntity>(ResultResponse.ERROR).buildResponse();
			}
		} else {
			if (emailSendersService.updateById(emailSenders)) {
				return new ResultResponse<EmailSenderEntity>(ResultResponse.SUCCESS).buildResponse();
			} else {
				return new ResultResponse<EmailSenderEntity>(ResultResponse.ERROR).buildResponse();
			}
		}
	}
}