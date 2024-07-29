package com.wy.test.web.apps.contorller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wy.test.core.authn.annotation.CurrentUser;
import com.wy.test.core.constants.ConstsProtocols;
import com.wy.test.core.entity.Message;
import com.wy.test.core.entity.UserInfo;
import com.wy.test.core.entity.apps.AppsFormBasedDetails;
import com.wy.test.persistence.service.AppsFormBasedDetailsService;

import dream.flying.flower.framework.web.crypto.ReciprocalHelpers;

@Controller
@RequestMapping(value = { "/apps/formbased" })
public class FormBasedDetailsController extends BaseAppContorller {

	final static Logger _logger = LoggerFactory.getLogger(FormBasedDetailsController.class);

	@Autowired
	AppsFormBasedDetailsService formBasedDetailsService;

	@GetMapping(value = { "/init" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> init() {
		AppsFormBasedDetails formBasedDetails = new AppsFormBasedDetails();
		formBasedDetails.setId(formBasedDetails.generateId());
		formBasedDetails.setProtocol(ConstsProtocols.FORMBASED);
		formBasedDetails.setSecret(ReciprocalHelpers.generateKey(""));
		return new Message<AppsFormBasedDetails>(formBasedDetails).buildResponse();
	}

	@GetMapping(value = { "/get/{id}" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> get(@PathVariable("id") String id) {
		AppsFormBasedDetails formBasedDetails = formBasedDetailsService.getAppDetails(id, false);
		decoderSecret(formBasedDetails);
		decoderSharedPassword(formBasedDetails);
		formBasedDetails.transIconBase64();
		return new Message<AppsFormBasedDetails>(formBasedDetails).buildResponse();
	}

	@ResponseBody
	@PostMapping(value = { "/add" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> add(@RequestBody AppsFormBasedDetails formBasedDetails,
			@CurrentUser UserInfo currentUser) {
		_logger.debug("-Add  :" + formBasedDetails);

		transform(formBasedDetails);
		formBasedDetails.setInstId(currentUser.getInstId());
		if (formBasedDetailsService.insert(formBasedDetails) && appsService.insertApp(formBasedDetails)) {
			return new Message<AppsFormBasedDetails>(Message.SUCCESS).buildResponse();
		} else {
			return new Message<AppsFormBasedDetails>(Message.FAIL).buildResponse();
		}
	}

	@ResponseBody
	@PostMapping(value = { "/update" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> update(@RequestBody AppsFormBasedDetails formBasedDetails,
			@CurrentUser UserInfo currentUser) {
		_logger.debug("-update  :" + formBasedDetails);
		transform(formBasedDetails);
		formBasedDetails.setInstId(currentUser.getInstId());
		if (formBasedDetailsService.update(formBasedDetails) && appsService.updateApp(formBasedDetails)) {
			return new Message<AppsFormBasedDetails>(Message.SUCCESS).buildResponse();
		} else {
			return new Message<AppsFormBasedDetails>(Message.FAIL).buildResponse();
		}
	}

	@ResponseBody
	@PostMapping(value = { "/delete" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> delete(@RequestParam("ids") String ids, @CurrentUser UserInfo currentUser) {
		_logger.debug("-delete  ids : {} ", ids);
		if (formBasedDetailsService.deleteBatch(ids) && appsService.deleteBatch(ids)) {
			return new Message<AppsFormBasedDetails>(Message.SUCCESS).buildResponse();
		} else {
			return new Message<AppsFormBasedDetails>(Message.FAIL).buildResponse();
		}
	}

}
