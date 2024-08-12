package com.wy.test.mgt.web.apps.contorller;

import java.util.Arrays;

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
import com.wy.test.core.constant.ConstProtocols;
import com.wy.test.core.entity.AppFormDetailEntity;
import com.wy.test.core.entity.Message;
import com.wy.test.core.entity.UserEntity;
import com.wy.test.core.vo.AppFormDetailVO;
import com.wy.test.persistence.service.AppFormDetailService;

import dream.flying.flower.framework.web.crypto.ReciprocalHelpers;
import dream.flying.flower.generator.GeneratorStrategyContext;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping(value = { "/apps/formbased" })
@Slf4j
public class FormDetailController extends BaseAppContorller {

	@Autowired
	AppFormDetailService appFormDetailService;

	@GetMapping(value = { "/init" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> init() {
		AppFormDetailVO formBasedDetails = new AppFormDetailVO();
		GeneratorStrategyContext generatorStrategyContext = new GeneratorStrategyContext();
		formBasedDetails.setId(generatorStrategyContext.generate());
		formBasedDetails.setProtocol(ConstProtocols.FORMBASED);
		formBasedDetails.setSecret(ReciprocalHelpers.generateKey(""));
		return new Message<>(formBasedDetails).buildResponse();
	}

	@GetMapping(value = { "/get/{id}" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> get(@PathVariable("id") String id) {
		AppFormDetailVO formBasedDetails = appFormDetailService.getAppDetails(id, false);
		decoderSecret(formBasedDetails);
		decoderSharedPassword(formBasedDetails);
		formBasedDetails.transIconBase64();
		return new Message<>(formBasedDetails).buildResponse();
	}

	@ResponseBody
	@PostMapping(value = { "/add" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> add(@RequestBody AppFormDetailVO formBasedDetails, @CurrentUser UserEntity currentUser) {
		log.debug("-Add  :" + formBasedDetails);

		transform(formBasedDetails);
		formBasedDetails.setInstId(currentUser.getInstId());
		if (null != appFormDetailService.add(formBasedDetails) && null != appService.add(formBasedDetails)) {
			return new Message<AppFormDetailEntity>(Message.SUCCESS).buildResponse();
		} else {
			return new Message<AppFormDetailEntity>(Message.FAIL).buildResponse();
		}
	}

	@ResponseBody
	@PostMapping(value = { "/update" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> update(@RequestBody AppFormDetailVO formBasedDetails,
			@CurrentUser UserEntity currentUser) {
		log.debug("-update  :" + formBasedDetails);
		transform(formBasedDetails);
		formBasedDetails.setInstId(currentUser.getInstId());
		if (appFormDetailService.edit(formBasedDetails) && appService.edit(formBasedDetails)) {
			return new Message<AppFormDetailEntity>(Message.SUCCESS).buildResponse();
		} else {
			return new Message<AppFormDetailEntity>(Message.FAIL).buildResponse();
		}
	}

	@ResponseBody
	@PostMapping(value = { "/delete" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> delete(@RequestParam("ids") String ids, @CurrentUser UserEntity currentUser) {
		log.debug("-delete  ids : {} ", ids);
		if (appFormDetailService.removeByIds(Arrays.asList(ids.split(",")))
				&& appService.removeByIds(Arrays.asList(ids.split(",")))) {
			return new Message<AppFormDetailEntity>(Message.SUCCESS).buildResponse();
		} else {
			return new Message<AppFormDetailEntity>(Message.FAIL).buildResponse();
		}
	}
}