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
import com.wy.test.core.convert.AppCasDetailConvert;
import com.wy.test.core.entity.AppCasDetailEntity;
import com.wy.test.core.entity.Message;
import com.wy.test.core.entity.UserEntity;
import com.wy.test.core.vo.AppCasDetailVO;
import com.wy.test.persistence.service.AppCasDetailService;

import dream.flying.flower.framework.web.crypto.ReciprocalHelpers;
import dream.flying.flower.generator.GeneratorStrategyContext;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping(value = { "/apps/cas" })
@Slf4j
public class CasDetailController extends BaseAppContorller {

	@Autowired
	private AppCasDetailService appCasDetailService;

	@Autowired
	private AppCasDetailConvert appCasDetailConvert;

	@GetMapping(value = { "/init" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> init() {
		AppCasDetailVO casDetails = new AppCasDetailVO();
		GeneratorStrategyContext generatorStrategyContext = new GeneratorStrategyContext();
		casDetails.setId(generatorStrategyContext.generate());
		casDetails.setProtocol(ConstProtocols.CAS);
		casDetails.setSecret(ReciprocalHelpers.generateKey(""));
		return new Message<>(casDetails).buildResponse();
	}

	@GetMapping(value = { "/get/{id}" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> get(@PathVariable("id") String id) {
		AppCasDetailVO casDetails = appCasDetailService.getAppDetails(id, false);
		super.decoderSecret(casDetails);
		casDetails.transIconBase64();
		return new Message<>(casDetails).buildResponse();
	}

	@ResponseBody
	@PostMapping(value = { "/add" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> insert(@RequestBody AppCasDetailVO casDetails, @CurrentUser UserEntity currentUser) {
		log.debug("-Add  :" + casDetails);
		transform(casDetails);
		casDetails.setInstId(currentUser.getInstId());
		if (appCasDetailService.save(appCasDetailConvert.convert(casDetails)) && null != appService.add(casDetails)) {
			return new Message<AppCasDetailEntity>(Message.SUCCESS).buildResponse();
		} else {
			return new Message<AppCasDetailEntity>(Message.FAIL).buildResponse();
		}
	}

	@ResponseBody
	@PostMapping(value = { "/update" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> update(@RequestBody AppCasDetailVO casDetails, @CurrentUser UserEntity currentUser) {
		log.debug("-update  :" + casDetails);
		transform(casDetails);
		casDetails.setInstId(currentUser.getInstId());
		if (appCasDetailService.updateById(appCasDetailConvert.convert(casDetails)) && appService.edit(casDetails)) {
			return new Message<AppCasDetailEntity>(Message.SUCCESS).buildResponse();
		} else {
			return new Message<AppCasDetailEntity>(Message.FAIL).buildResponse();
		}
	}

	@ResponseBody
	@PostMapping(value = { "/delete" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> delete(@RequestParam("ids") String ids, @CurrentUser UserEntity currentUser) {
		log.debug("-delete  ids : {} ", ids);
		if (appCasDetailService.removeByIds(Arrays.asList(ids.split(",")))
				&& appService.removeByIds(Arrays.asList(ids.split(",")))) {
			return new Message<>(Message.SUCCESS).buildResponse();
		} else {
			return new Message<>(Message.FAIL).buildResponse();
		}
	}
}