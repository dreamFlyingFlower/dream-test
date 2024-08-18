package com.wy.test.web.mgt.contorller.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wy.test.authentication.core.authn.annotation.CurrentUser;
import com.wy.test.core.entity.InstitutionEntity;
import com.wy.test.core.entity.Message;
import com.wy.test.core.entity.UserEntity;
import com.wy.test.persistence.service.InstitutionService;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping(value = { "/config/institutions" })
@Slf4j
public class InstitutionController {

	@Autowired
	private InstitutionService institutionsService;

	@GetMapping(value = { "/get" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> get(@CurrentUser UserEntity currentUser) {
		InstitutionEntity institutions = institutionsService.getById(currentUser.getInstId());
		return new Message<InstitutionEntity>(Message.SUCCESS, institutions).buildResponse();
	}

	@PostMapping(value = { "/update" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> update(@RequestBody InstitutionEntity institutions, @CurrentUser UserEntity currentUser,
			BindingResult result) {
		log.debug("updateRole institutions : " + institutions);
		if (institutionsService.updateById(institutions)) {
			return new Message<InstitutionEntity>(Message.SUCCESS).buildResponse();
		} else {
			return new Message<InstitutionEntity>(Message.FAIL).buildResponse();
		}
	}
}