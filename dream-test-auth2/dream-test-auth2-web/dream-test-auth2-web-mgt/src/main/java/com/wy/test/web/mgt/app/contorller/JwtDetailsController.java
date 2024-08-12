package com.wy.test.web.mgt.app.contorller;

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

import com.wy.test.authentication.core.authn.annotation.CurrentUser;
import com.wy.test.core.constant.ConstProtocols;
import com.wy.test.core.entity.AppJwtDetailEntity;
import com.wy.test.core.entity.Message;
import com.wy.test.core.entity.UserEntity;
import com.wy.test.core.vo.AppJwtDetailVO;
import com.wy.test.persistence.service.AppJwtDetailService;

import dream.flying.flower.framework.web.crypto.ReciprocalHelpers;
import dream.flying.flower.generator.GeneratorStrategyContext;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping(value = { "/apps/jwt" })
@Slf4j
public class JwtDetailsController extends BaseAppContorller {

	@Autowired
	AppJwtDetailService jwtDetailsService;

	@GetMapping(value = { "/init" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> init() {
		AppJwtDetailVO jwtDetails = new AppJwtDetailVO();
		GeneratorStrategyContext generatorStrategyContext = new GeneratorStrategyContext();
		jwtDetails.setId(generatorStrategyContext.generate());
		jwtDetails.setProtocol(ConstProtocols.JWT);
		jwtDetails.setSecret(ReciprocalHelpers.generateKey(""));
		jwtDetails.setUserPropertys("userPropertys");
		return new Message<>(jwtDetails).buildResponse();
	}

	@GetMapping(value = { "/get/{id}" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> get(@PathVariable("id") String id) {
		AppJwtDetailVO jwtDetails = jwtDetailsService.getAppDetails(id, false);
		decoderSecret(jwtDetails);
		jwtDetails.transIconBase64();
		return new Message<>(jwtDetails).buildResponse();
	}

	@ResponseBody
	@PostMapping(value = { "/add" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> insert(@RequestBody AppJwtDetailVO jwtDetails, @CurrentUser UserEntity currentUser) {
		log.debug("-Add  :" + jwtDetails);
		transform(jwtDetails);
		jwtDetails.setInstId(currentUser.getInstId());
		if (null != jwtDetailsService.add(jwtDetails) && null != appService.add(jwtDetails)) {
			return new Message<AppJwtDetailEntity>(Message.SUCCESS).buildResponse();
		} else {
			return new Message<AppJwtDetailEntity>(Message.FAIL).buildResponse();
		}
	}

	@ResponseBody
	@PostMapping(value = { "/update" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> update(@RequestBody AppJwtDetailVO jwtDetails, @CurrentUser UserEntity currentUser) {
		log.debug("-update  :" + jwtDetails);
		transform(jwtDetails);
		jwtDetails.setInstId(currentUser.getInstId());
		if (jwtDetailsService.edit(jwtDetails) && appService.edit(jwtDetails)) {
			return new Message<AppJwtDetailEntity>(Message.SUCCESS).buildResponse();
		} else {
			return new Message<>(Message.FAIL).buildResponse();
		}
	}

	@ResponseBody
	@PostMapping(value = { "/delete" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> delete(@RequestParam("ids") String ids, @CurrentUser UserEntity currentUser) {
		log.debug("-delete  ids : {} ", ids);
		if (jwtDetailsService.removeByIds(Arrays.asList(ids.split(",")))
				&& appService.removeByIds(Arrays.asList(ids.split(",")))) {
			return new Message<AppJwtDetailEntity>(Message.SUCCESS).buildResponse();
		} else {
			return new Message<AppJwtDetailEntity>(Message.FAIL).buildResponse();
		}
	}
}