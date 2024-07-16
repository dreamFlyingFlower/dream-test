package com.wy.test.web.config.contorller;

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

import com.wy.test.core.authn.annotation.CurrentUser;
import com.wy.test.entity.Institutions;
import com.wy.test.entity.Message;
import com.wy.test.entity.UserInfo;
import com.wy.test.persistence.service.InstitutionsService;

@Controller
@RequestMapping(value = { "/config/institutions" })
public class InstitutionsController {

	final static Logger _logger = LoggerFactory.getLogger(InstitutionsController.class);

	@Autowired
	private InstitutionsService institutionsService;

	@GetMapping(value = { "/get" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> get(@CurrentUser UserInfo currentUser) {
		Institutions institutions = institutionsService.get(currentUser.getInstId());
		return new Message<Institutions>(Message.SUCCESS, institutions).buildResponse();
	}

	@PostMapping(value = { "/update" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> update(@RequestBody Institutions institutions, @CurrentUser UserInfo currentUser,
			BindingResult result) {
		_logger.debug("updateRole institutions : " + institutions);
		if (institutionsService.update(institutions)) {
			return new Message<Institutions>(Message.SUCCESS).buildResponse();
		} else {
			return new Message<Institutions>(Message.FAIL).buildResponse();
		}
	}
}
