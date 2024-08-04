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
import com.wy.test.core.constants.ConstProtocols;
import com.wy.test.core.entity.AppJwtDetailEntity;
import com.wy.test.core.entity.Message;
import com.wy.test.core.entity.UserEntity;
import com.wy.test.persistence.service.AppsJwtDetailsService;

import dream.flying.flower.framework.web.crypto.ReciprocalHelpers;

@Controller
@RequestMapping(value = { "/apps/jwt" })
public class JwtDetailsController extends BaseAppContorller {

	final static Logger _logger = LoggerFactory.getLogger(JwtDetailsController.class);

	@Autowired
	AppsJwtDetailsService jwtDetailsService;

	@GetMapping(value = { "/init" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> init() {
		AppJwtDetailEntity jwtDetails = new AppJwtDetailEntity();
		jwtDetails.setId(jwtDetails.generateId());
		jwtDetails.setProtocol(ConstProtocols.JWT);
		jwtDetails.setSecret(ReciprocalHelpers.generateKey(""));
		jwtDetails.setUserPropertys("userPropertys");
		return new Message<AppJwtDetailEntity>(jwtDetails).buildResponse();
	}

	@GetMapping(value = { "/get/{id}" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> get(@PathVariable("id") String id) {
		AppJwtDetailEntity jwtDetails = jwtDetailsService.getAppDetails(id, false);
		decoderSecret(jwtDetails);
		jwtDetails.transIconBase64();
		return new Message<AppJwtDetailEntity>(jwtDetails).buildResponse();
	}

	@ResponseBody
	@PostMapping(value = { "/add" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> insert(@RequestBody AppJwtDetailEntity jwtDetails, @CurrentUser UserEntity currentUser) {
		_logger.debug("-Add  :" + jwtDetails);

		transform(jwtDetails);

		jwtDetails.setInstId(currentUser.getInstId());
		if (jwtDetailsService.insert(jwtDetails) && appsService.insertApp(jwtDetails)) {
			return new Message<AppJwtDetailEntity>(Message.SUCCESS).buildResponse();
		} else {
			return new Message<AppJwtDetailEntity>(Message.FAIL).buildResponse();
		}
	}

	@ResponseBody
	@PostMapping(value = { "/update" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> update(@RequestBody AppJwtDetailEntity jwtDetails, @CurrentUser UserEntity currentUser) {
		_logger.debug("-update  :" + jwtDetails);
		transform(jwtDetails);
		jwtDetails.setInstId(currentUser.getInstId());
		if (jwtDetailsService.update(jwtDetails) && appsService.updateApp(jwtDetails)) {
			return new Message<AppJwtDetailEntity>(Message.SUCCESS).buildResponse();
		} else {
			return new Message<AppJwtDetailEntity>(Message.FAIL).buildResponse();
		}
	}

	@ResponseBody
	@PostMapping(value = { "/delete" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> delete(@RequestParam("ids") String ids, @CurrentUser UserEntity currentUser) {
		_logger.debug("-delete  ids : {} ", ids);
		if (jwtDetailsService.deleteBatch(ids) && appsService.deleteBatch(ids)) {
			return new Message<AppJwtDetailEntity>(Message.SUCCESS).buildResponse();
		} else {
			return new Message<AppJwtDetailEntity>(Message.FAIL).buildResponse();
		}
	}

}
