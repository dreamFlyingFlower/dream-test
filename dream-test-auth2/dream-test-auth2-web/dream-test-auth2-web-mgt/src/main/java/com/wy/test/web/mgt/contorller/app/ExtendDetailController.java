package com.wy.test.web.mgt.contorller.app;

import java.util.Arrays;

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

import com.wy.test.authentication.core.annotation.CurrentUser;
import com.wy.test.core.base.ResultResponse;
import com.wy.test.core.constant.ConstProtocols;
import com.wy.test.core.entity.AppExtendDetailEntity;
import com.wy.test.core.entity.UserEntity;
import com.wy.test.core.vo.AppExtendDetailVO;
import com.wy.test.core.vo.AppVO;

import dream.flying.flower.framework.safe.helper.ReciprocalHelpers;
import dream.flying.flower.generator.GeneratorStrategyContext;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping(value = { "/apps/extendapi" })
@Slf4j
public class ExtendDetailController extends BaseAppContorller {

	@GetMapping(value = { "/init" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> init() {
		AppExtendDetailVO extendApiDetails = new AppExtendDetailVO();
		GeneratorStrategyContext generatorStrategyContext = new GeneratorStrategyContext();
		extendApiDetails.setId(generatorStrategyContext.generate());
		extendApiDetails.setProtocol(ConstProtocols.EXTEND_API);
		extendApiDetails.setSecret(ReciprocalHelpers.generateKey(""));
		return new ResultResponse<>(extendApiDetails).buildResponse();
	}

	@GetMapping(value = { "/get/{id}" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> get(@PathVariable("id") String id) {
		AppVO application = appService.getInfo(id);
		super.decoderSecret(application);
		AppExtendDetailVO extendApiDetails = new AppExtendDetailVO();
		BeanUtils.copyProperties(application, extendApiDetails);
		extendApiDetails.transIconBase64();
		return new ResultResponse<>(extendApiDetails).buildResponse();
	}

	@ResponseBody
	@PostMapping(value = { "/add" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> add(@RequestBody AppExtendDetailVO extendApiDetails, @CurrentUser UserEntity currentUser) {
		log.debug("-Add  :" + extendApiDetails);

		transform(extendApiDetails);
		extendApiDetails.setInstId(currentUser.getInstId());
		if (null != appService.add(extendApiDetails)) {
			return new ResultResponse<AppExtendDetailEntity>(ResultResponse.SUCCESS).buildResponse();
		} else {
			return new ResultResponse<AppExtendDetailEntity>(ResultResponse.FAIL).buildResponse();
		}
	}

	@ResponseBody
	@PostMapping(value = { "/update" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> update(@RequestBody AppExtendDetailVO extendApiDetails,
			@CurrentUser UserEntity currentUser) {
		log.debug("-update  :" + extendApiDetails);
		transform(extendApiDetails);
		extendApiDetails.setInstId(currentUser.getInstId());
		if (appService.edit(extendApiDetails)) {
			return new ResultResponse<AppExtendDetailEntity>(ResultResponse.SUCCESS).buildResponse();
		} else {
			return new ResultResponse<AppExtendDetailEntity>(ResultResponse.FAIL).buildResponse();
		}
	}

	@ResponseBody
	@PostMapping(value = { "/delete" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> delete(@RequestParam("ids") String ids, @CurrentUser UserEntity currentUser) {
		log.debug("-delete  ids : {} ", ids);
		if (appService.removeByIds(Arrays.asList(ids.split(",")))) {
			return new ResultResponse<AppExtendDetailEntity>(ResultResponse.SUCCESS).buildResponse();
		} else {
			return new ResultResponse<AppExtendDetailEntity>(ResultResponse.FAIL).buildResponse();
		}
	}
}