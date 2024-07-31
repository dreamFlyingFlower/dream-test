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
import com.wy.test.core.entity.Message;
import com.wy.test.core.entity.UserInfo;
import com.wy.test.core.entity.apps.AppsJwtDetails;
import com.wy.test.core.entity.apps.AppsTokenBasedDetails;
import com.wy.test.persistence.service.AppsTokenBasedDetailsService;

import dream.flying.flower.framework.web.crypto.ReciprocalHelpers;

@Controller
@RequestMapping(value = { "/apps/tokenbased" })
public class TokenBasedDetailsController extends BaseAppContorller {

	final static Logger _logger = LoggerFactory.getLogger(TokenBasedDetailsController.class);

	@Autowired
	AppsTokenBasedDetailsService tokenBasedDetailsService;

	@GetMapping(value = { "/init" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> init() {
		AppsTokenBasedDetails tokenBasedDetails = new AppsTokenBasedDetails();
		tokenBasedDetails.setId(tokenBasedDetails.generateId());
		tokenBasedDetails.setProtocol(ConstProtocols.TOKENBASED);
		tokenBasedDetails.setSecret(ReciprocalHelpers.generateKey(ReciprocalHelpers.Algorithm.AES));
		tokenBasedDetails.setAlgorithmKey(tokenBasedDetails.getSecret());
		tokenBasedDetails.setUserPropertys("userPropertys");
		return new Message<AppsTokenBasedDetails>(tokenBasedDetails).buildResponse();
	}

	@GetMapping(value = { "/get/{id}" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> get(@PathVariable("id") String id) {
		AppsTokenBasedDetails tokenBasedDetails = tokenBasedDetailsService.getAppDetails(id, false);
		decoderSecret(tokenBasedDetails);
		String algorithmKey = passwordReciprocal.decoder(tokenBasedDetails.getAlgorithmKey());
		tokenBasedDetails.setAlgorithmKey(algorithmKey);
		tokenBasedDetails.transIconBase64();
		return new Message<AppsTokenBasedDetails>(tokenBasedDetails).buildResponse();
	}

	@ResponseBody
	@PostMapping(value = { "/add" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> add(@RequestBody AppsTokenBasedDetails tokenBasedDetails,
			@CurrentUser UserInfo currentUser) {
		_logger.debug("-Add  :" + tokenBasedDetails);

		transform(tokenBasedDetails);

		tokenBasedDetails.setAlgorithmKey(tokenBasedDetails.getSecret());
		tokenBasedDetails.setInstId(currentUser.getInstId());
		if (tokenBasedDetailsService.insert(tokenBasedDetails) && appsService.insertApp(tokenBasedDetails)) {
			return new Message<AppsJwtDetails>(Message.SUCCESS).buildResponse();
		} else {
			return new Message<AppsJwtDetails>(Message.FAIL).buildResponse();
		}
	}

	@ResponseBody
	@PostMapping(value = { "/update" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> update(@RequestBody AppsTokenBasedDetails tokenBasedDetails,
			@CurrentUser UserInfo currentUser) {
		_logger.debug("-update  :" + tokenBasedDetails);
		transform(tokenBasedDetails);
		tokenBasedDetails.setAlgorithmKey(tokenBasedDetails.getSecret());
		tokenBasedDetails.setInstId(currentUser.getInstId());
		if (tokenBasedDetailsService.update(tokenBasedDetails) && appsService.updateApp(tokenBasedDetails)) {
			return new Message<AppsJwtDetails>(Message.SUCCESS).buildResponse();
		} else {
			return new Message<AppsJwtDetails>(Message.FAIL).buildResponse();
		}
	}

	@ResponseBody
	@PostMapping(value = { "/delete" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> delete(@RequestParam("ids") String ids, @CurrentUser UserInfo currentUser) {
		_logger.debug("-delete  ids : {} ", ids);
		if (tokenBasedDetailsService.deleteBatch(ids) && appsService.deleteBatch(ids)) {
			return new Message<AppsJwtDetails>(Message.SUCCESS).buildResponse();
		} else {
			return new Message<AppsJwtDetails>(Message.FAIL).buildResponse();
		}
	}

}
