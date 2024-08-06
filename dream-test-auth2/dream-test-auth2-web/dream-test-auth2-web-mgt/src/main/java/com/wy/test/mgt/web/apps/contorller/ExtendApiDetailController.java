package com.wy.test.mgt.web.apps.contorller;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
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
import com.wy.test.core.constants.ConstProtocols;
import com.wy.test.core.entity.AppExtendDetailEntity;
import com.wy.test.core.entity.Message;
import com.wy.test.core.entity.UserEntity;
import com.wy.test.core.vo.AppExtendDetailVO;
import com.wy.test.core.vo.AppVO;

import dream.flying.flower.framework.web.crypto.ReciprocalHelpers;
import dream.flying.flower.generator.GeneratorStrategyContext;

@Controller
@RequestMapping(value = { "/apps/extendapi" })
public class ExtendApiDetailController extends BaseAppContorller {

	final static Logger _logger = LoggerFactory.getLogger(ExtendApiDetailController.class);

	@GetMapping(value = { "/init" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> init() {
		AppExtendDetailVO extendApiDetails = new AppExtendDetailVO();
		GeneratorStrategyContext generatorStrategyContext = new GeneratorStrategyContext();
		extendApiDetails.setId(generatorStrategyContext.generate());
		extendApiDetails.setProtocol(ConstProtocols.EXTEND_API);
		extendApiDetails.setSecret(ReciprocalHelpers.generateKey(""));
		return new Message<>(extendApiDetails).buildResponse();
	}

	@GetMapping(value = { "/get/{id}" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> get(@PathVariable("id") String id) {
		AppVO application = appsService.getInfo(id);
		super.decoderSecret(application);
		AppExtendDetailVO extendApiDetails = new AppExtendDetailVO();
		BeanUtils.copyProperties(application, extendApiDetails);
		extendApiDetails.transIconBase64();
		return new Message<>(extendApiDetails).buildResponse();
	}

	@ResponseBody
	@PostMapping(value = { "/add" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> add(@RequestBody AppExtendDetailVO extendApiDetails, @CurrentUser UserEntity currentUser) {
		_logger.debug("-Add  :" + extendApiDetails);

		transform(extendApiDetails);
		extendApiDetails.setInstId(currentUser.getInstId());
		if (appsService.insertApp(extendApiDetails)) {
			return new Message<AppExtendDetailEntity>(Message.SUCCESS).buildResponse();
		} else {
			return new Message<AppExtendDetailEntity>(Message.FAIL).buildResponse();
		}
	}

	@ResponseBody
	@PostMapping(value = { "/update" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> update(@RequestBody AppExtendDetailVO extendApiDetails,
			@CurrentUser UserEntity currentUser) {
		_logger.debug("-update  :" + extendApiDetails);
		transform(extendApiDetails);
		extendApiDetails.setInstId(currentUser.getInstId());
		if (appsService.updateApp(extendApiDetails)) {
			return new Message<AppExtendDetailEntity>(Message.SUCCESS).buildResponse();
		} else {
			return new Message<AppExtendDetailEntity>(Message.FAIL).buildResponse();
		}
	}

	@ResponseBody
	@PostMapping(value = { "/delete" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> delete(@RequestParam("ids") String ids, @CurrentUser UserEntity currentUser) {
		_logger.debug("-delete  ids : {} ", ids);
		if (appsService.removeByIds(Arrays.asList(ids.split(",")))) {
			return new Message<AppExtendDetailEntity>(Message.SUCCESS).buildResponse();
		} else {
			return new Message<AppExtendDetailEntity>(Message.FAIL).buildResponse();
		}
	}
}