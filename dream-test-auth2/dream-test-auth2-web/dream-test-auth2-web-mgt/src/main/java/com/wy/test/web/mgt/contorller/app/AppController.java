package com.wy.test.web.mgt.contorller.app;

import java.util.Arrays;

import org.apache.commons.collections4.CollectionUtils;
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

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWEAlgorithm;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.Requirement;
import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jose.jwk.OctetSequenceKey;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.gen.OctetSequenceKeyGenerator;
import com.nimbusds.jose.jwk.gen.RSAKeyGenerator;
import com.wy.test.authentication.core.authn.annotation.CurrentUser;
import com.wy.test.core.constant.ConstProtocols;
import com.wy.test.core.entity.AppEntity;
import com.wy.test.core.entity.Message;
import com.wy.test.core.entity.UserEntity;
import com.wy.test.core.query.AppQuery;
import com.wy.test.core.vo.AppVO;

import dream.flying.flower.framework.web.crypto.ReciprocalHelpers;
import dream.flying.flower.generator.GeneratorStrategyContext;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping(value = { "/apps" })
@Slf4j
public class AppController extends BaseAppContorller {

	@GetMapping(value = { "/init" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> init() {
		AppEntity app = new AppEntity();
		GeneratorStrategyContext generatorStrategyContext = new GeneratorStrategyContext();
		app.setId(generatorStrategyContext.generate());
		app.setProtocol(ConstProtocols.BASIC);
		app.setSecret(ReciprocalHelpers.generateKey(""));
		return new Message<AppEntity>(app).buildResponse();
	}

	@GetMapping(value = { "/fetch" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	public ResponseEntity<?> fetch(AppQuery apps, @CurrentUser UserEntity currentUser) {
		apps.setInstId(currentUser.getInstId());
		Page<AppVO> appsList = appService.page(apps);
		for (AppVO app : appsList.getRecords()) {
			app.transIconBase64();
			app.setSecret(null);
			app.setSharedPassword(null);
		}
		log.debug("List " + appsList);
		return new Message<>(appsList).buildResponse();
	}

	@ResponseBody
	@GetMapping(value = { "/query" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> query(AppEntity apps, @CurrentUser UserEntity currentUser) {
		log.debug("-query  :" + apps);
		if (CollectionUtils.isNotEmpty(appService.list(apps))) {
			return new Message<AppEntity>(Message.SUCCESS).buildResponse();
		} else {
			return new Message<AppEntity>(Message.SUCCESS).buildResponse();
		}
	}

	@GetMapping(value = { "/get/{id}" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> get(@PathVariable("id") String id) {
		AppVO apps = appService.getInfo(id);
		decoderSecret(apps);
		apps.transIconBase64();
		return new Message<>(apps).buildResponse();
	}

	@ResponseBody
	@PostMapping(value = { "/add" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> insert(@RequestBody AppVO apps, @CurrentUser UserEntity currentUser) {
		log.debug("-Add  :" + apps);
		transform(apps);
		apps.setInstId(currentUser.getInstId());
		if (null != appService.add(apps)) {
			return new Message<>(Message.SUCCESS).buildResponse();
		} else {
			return new Message<>(Message.FAIL).buildResponse();
		}
	}

	@ResponseBody
	@PostMapping(value = { "/update" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> update(@RequestBody AppVO apps, @CurrentUser UserEntity currentUser) {
		log.debug("-update  :" + apps);
		transform(apps);
		apps.setInstId(currentUser.getInstId());
		if (appService.edit(apps)) {
			return new Message<AppEntity>(Message.SUCCESS).buildResponse();
		} else {
			return new Message<AppEntity>(Message.FAIL).buildResponse();
		}
	}

	@ResponseBody
	@PostMapping(value = { "/delete" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> delete(@RequestParam("ids") String ids, @CurrentUser UserEntity currentUser) {
		log.debug("-delete  ids : {} ", ids);
		if (appService.removeByIds(Arrays.asList(ids.split(",")))) {
			return new Message<AppEntity>(Message.SUCCESS).buildResponse();
		} else {
			return new Message<AppEntity>(Message.FAIL).buildResponse();
		}
	}

	@ResponseBody
	@PostMapping(value = { "/updateExtendAttr" })
	public ResponseEntity<?> updateExtendAttr(@RequestBody AppEntity app) {
		log.debug("-updateExtendAttr  id : {} , ExtendAttr : {}", app.getId(), app.getExtendAttr());
		if (appService.updateExtendAttr(app)) {
			return new Message<AppEntity>(Message.SUCCESS).buildResponse();
		} else {
			return new Message<AppEntity>(Message.FAIL).buildResponse();
		}
	}

	@ResponseBody
	@GetMapping(value = { "/generate/secret/{type}" })
	public ResponseEntity<?> generateSecret(@PathVariable("type") String type,
			@RequestParam(name = "id", required = false) String id) throws JOSEException {
		String secret = "";
		type = type.toLowerCase();
		if (type.equals("des")) {
			secret = ReciprocalHelpers.generateKey(ReciprocalHelpers.Algorithm.DES);
		} else if (type.equals("desede")) {
			secret = ReciprocalHelpers.generateKey(ReciprocalHelpers.Algorithm.DESede);
		} else if (type.equals("aes")) {
			secret = ReciprocalHelpers.generateKey(ReciprocalHelpers.Algorithm.AES);
		} else if (type.equals("blowfish")) {
			secret = ReciprocalHelpers.generateKey(ReciprocalHelpers.Algorithm.Blowfish);
		} else if (type.equalsIgnoreCase("RS256") || type.equalsIgnoreCase("RS384") || type.equalsIgnoreCase("RS512")) {
			RSAKey rsaJWK = new RSAKeyGenerator(2048).keyID(id + "_sig")
					.keyUse(KeyUse.SIGNATURE)
					.algorithm(new JWSAlgorithm(type.toUpperCase(), Requirement.OPTIONAL))
					.generate();
			secret = rsaJWK.toJSONString();
		} else if (type.equalsIgnoreCase("HS256") || type.equalsIgnoreCase("HS384") || type.equalsIgnoreCase("HS512")) {
			OctetSequenceKey octKey = new OctetSequenceKeyGenerator(2048).keyID(id + "_sig")
					.keyUse(KeyUse.SIGNATURE)
					.algorithm(new JWSAlgorithm(type.toUpperCase(), Requirement.OPTIONAL))
					.generate();
			secret = octKey.toJSONString();
		} else if (type.equalsIgnoreCase("RSA1_5") || type.equalsIgnoreCase("RSA_OAEP")
				|| type.equalsIgnoreCase("RSA-OAEP-256")) {
			RSAKey rsaJWK = new RSAKeyGenerator(2048).keyID(id + "_enc")
					.keyUse(KeyUse.ENCRYPTION)
					.algorithm(new JWEAlgorithm(type.toUpperCase(), Requirement.OPTIONAL))
					.generate();
			secret = rsaJWK.toJSONString();
		} else if (type.equalsIgnoreCase("A128KW") || type.equalsIgnoreCase("A192KW") || type.equalsIgnoreCase("A256KW")
				|| type.equalsIgnoreCase("A128GCMKW") || type.equalsIgnoreCase("A192GCMKW")
				|| type.equalsIgnoreCase("A256GCMKW")) {
			int keyLength = Integer.parseInt(type.substring(1, 4));
			OctetSequenceKey octKey = new OctetSequenceKeyGenerator(keyLength).keyID(id + "_enc")
					.keyUse(KeyUse.ENCRYPTION)
					.algorithm(new JWEAlgorithm(type.toUpperCase(), Requirement.OPTIONAL))
					.generate();
			secret = octKey.toJSONString();
		} else {
			secret = ReciprocalHelpers.generateKey("");
		}

		return new Message<Object>(Message.SUCCESS, (Object) secret).buildResponse();
	}
}