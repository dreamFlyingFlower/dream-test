package com.wy.test.web.mgt.contorller.app;

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

import com.wy.test.authentication.core.annotation.CurrentUser;
import com.wy.test.core.base.ResultResponse;
import com.wy.test.core.constant.ConstProtocols;
import com.wy.test.core.convert.AppTokenDetailConvert;
import com.wy.test.core.entity.AppJwtDetailEntity;
import com.wy.test.core.entity.UserEntity;
import com.wy.test.core.vo.AppTokenDetailVO;
import com.wy.test.persistence.service.AppTokenDetailService;

import dream.flying.flower.framework.crypto.helper.ReciprocalHelpers;
import dream.flying.flower.generator.GeneratorStrategyContext;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping(value = { "/apps/tokenbased" })
@Slf4j
public class TokenDetailController extends BaseAppContorller {

	@Autowired
	AppTokenDetailService tokenBasedDetailsService;

	@Autowired
	AppTokenDetailConvert appTokenDetailConvert;

	@GetMapping(value = { "/init" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> init() {
		AppTokenDetailVO tokenBasedDetails = new AppTokenDetailVO();
		GeneratorStrategyContext generatorStrategyContext = new GeneratorStrategyContext();
		tokenBasedDetails.setId(generatorStrategyContext.generate());
		tokenBasedDetails.setProtocol(ConstProtocols.TOKENBASED);
		tokenBasedDetails.setSecret(ReciprocalHelpers.generateKey(ReciprocalHelpers.Algorithm.AES));
		tokenBasedDetails.setAlgorithmKey(tokenBasedDetails.getSecret());
		tokenBasedDetails.setUserPropertys("userPropertys");
		return new ResultResponse<>(tokenBasedDetails).buildResponse();
	}

	@GetMapping(value = { "/get/{id}" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> get(@PathVariable("id") String id) {
		AppTokenDetailVO tokenBasedDetails = tokenBasedDetailsService.getAppDetails(id, false);
		decoderSecret(tokenBasedDetails);
		String algorithmKey = passwordReciprocal.decoder(tokenBasedDetails.getAlgorithmKey());
		tokenBasedDetails.setAlgorithmKey(algorithmKey);
		tokenBasedDetails.transIconBase64();
		return new ResultResponse<>(tokenBasedDetails).buildResponse();
	}

	@ResponseBody
	@PostMapping(value = { "/add" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> add(@RequestBody AppTokenDetailVO tokenBasedDetails, @CurrentUser UserEntity currentUser) {
		log.debug("-Add  :" + tokenBasedDetails);
		transform(tokenBasedDetails);
		tokenBasedDetails.setAlgorithmKey(tokenBasedDetails.getSecret());
		tokenBasedDetails.setInstId(currentUser.getInstId());
		if (null != tokenBasedDetailsService.add(tokenBasedDetails) && null != appService.add(tokenBasedDetails)) {
			return new ResultResponse<AppJwtDetailEntity>(ResultResponse.SUCCESS).buildResponse();
		} else {
			return new ResultResponse<AppJwtDetailEntity>(ResultResponse.FAIL).buildResponse();
		}
	}

	@ResponseBody
	@PostMapping(value = { "/update" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> update(@RequestBody AppTokenDetailVO tokenBasedDetails,
			@CurrentUser UserEntity currentUser) {
		log.debug("-update  :" + tokenBasedDetails);
		transform(tokenBasedDetails);
		tokenBasedDetails.setAlgorithmKey(tokenBasedDetails.getSecret());
		tokenBasedDetails.setInstId(currentUser.getInstId());
		if (tokenBasedDetailsService.edit(tokenBasedDetails) && appService.edit(tokenBasedDetails)) {
			return new ResultResponse<AppJwtDetailEntity>(ResultResponse.SUCCESS).buildResponse();
		} else {
			return new ResultResponse<AppJwtDetailEntity>(ResultResponse.FAIL).buildResponse();
		}
	}

	@ResponseBody
	@PostMapping(value = { "/delete" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> delete(@RequestParam("ids") String ids, @CurrentUser UserEntity currentUser) {
		log.debug("-delete  ids : {} ", ids);
		if (tokenBasedDetailsService.removeByIds(Arrays.asList(ids.split(",")))
				&& appService.removeByIds(Arrays.asList(ids.split(",")))) {
			return new ResultResponse<AppJwtDetailEntity>(ResultResponse.SUCCESS).buildResponse();
		} else {
			return new ResultResponse<AppJwtDetailEntity>(ResultResponse.FAIL).buildResponse();
		}
	}
}