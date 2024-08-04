package com.wy.test.web.config.contorller;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wy.test.core.authn.annotation.CurrentUser;
import com.wy.test.core.entity.EmailSenderEntity;
import com.wy.test.core.entity.Message;
import com.wy.test.core.entity.UserEntity;
import com.wy.test.core.password.PasswordReciprocal;
import com.wy.test.persistence.service.EmailSendersService;

@Controller
@RequestMapping(value = { "/config/emailsenders" })
public class EmailSendersController {

	final static Logger _logger = LoggerFactory.getLogger(EmailSendersController.class);

	@Autowired
	private EmailSendersService emailSendersService;

	@GetMapping(value = { "/get" })
	public ResponseEntity<?> get(@CurrentUser UserEntity currentUser) {
		EmailSenderEntity emailSenders = emailSendersService.get(currentUser.getInstId());
		if (emailSenders != null && StringUtils.isNotBlank(emailSenders.getCredentials())) {
			emailSenders.setCredentials(PasswordReciprocal.getInstance().decoder(emailSenders.getCredentials()));
		} else {
			emailSenders = new EmailSenderEntity();
			emailSenders.setProtocol("smtp");
			emailSenders.setEncoding("utf-8");
		}
		return new Message<EmailSenderEntity>(emailSenders).buildResponse();
	}

	@PostMapping(value = { "/update" })
	@ResponseBody
	public ResponseEntity<?> update(@RequestBody EmailSenderEntity emailSenders, @CurrentUser UserEntity currentUser,
			BindingResult result) {
		_logger.debug("update emailSenders : " + emailSenders);
		emailSenders.setInstId(currentUser.getInstId());
		emailSenders.setCredentials(PasswordReciprocal.getInstance().encode(emailSenders.getCredentials()));
		if (StringUtils.isBlank(emailSenders.getId())) {
			emailSenders.setId(emailSenders.getInstId());
			if (emailSendersService.insert(emailSenders)) {
				return new Message<EmailSenderEntity>(Message.SUCCESS).buildResponse();
			} else {
				return new Message<EmailSenderEntity>(Message.ERROR).buildResponse();
			}
		} else {
			if (emailSendersService.update(emailSenders)) {
				return new Message<EmailSenderEntity>(Message.SUCCESS).buildResponse();
			} else {
				return new Message<EmailSenderEntity>(Message.ERROR).buildResponse();
			}
		}

	}
}
