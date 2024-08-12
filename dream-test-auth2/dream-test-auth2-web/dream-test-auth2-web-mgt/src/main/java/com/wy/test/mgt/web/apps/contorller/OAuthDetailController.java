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
import com.wy.test.core.entity.AppOauthClientDetailEntity;
import com.wy.test.core.entity.Message;
import com.wy.test.core.entity.UserEntity;
import com.wy.test.core.entity.oauth2.client.BaseClientDetails;
import com.wy.test.core.vo.AppOauthClientDetailVO;
import com.wy.test.core.vo.AppVO;
import com.wy.test.oauth2.common.OAuth2Constants;
import com.wy.test.oauth2.provider.client.JdbcClientDetailsService;

import dream.flying.flower.framework.web.crypto.ReciprocalHelpers;
import dream.flying.flower.generator.GeneratorStrategyContext;
import dream.flying.flower.lang.StrHelper;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping(value = { "/apps/oauth20" })
@Slf4j
public class OAuthDetailController extends BaseAppContorller {

	@Autowired
	JdbcClientDetailsService oauth20JdbcClientDetailsService;

	@GetMapping(value = { "/init" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> init() {
		AppOauthClientDetailVO oauth20Details = new AppOauthClientDetailVO();
		GeneratorStrategyContext generatorStrategyContext = new GeneratorStrategyContext();
		oauth20Details.setId(generatorStrategyContext.generate());
		oauth20Details.setSecret(ReciprocalHelpers.generateKey(""));
		oauth20Details.setClientId(oauth20Details.getId());
		oauth20Details.setClientSecret(oauth20Details.getSecret());
		oauth20Details.setProtocol(ConstProtocols.OAUTH20);
		return new Message<>(oauth20Details).buildResponse();
	}

	@GetMapping(value = { "/get/{id}" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> get(@PathVariable("id") String id) {
		BaseClientDetails baseClientDetails =
				(BaseClientDetails) oauth20JdbcClientDetailsService.loadClientByClientId(id, false);
		AppVO application = appService.getInfo(id);
		decoderSecret(application);
		AppOauthClientDetailVO oauth20Details = new AppOauthClientDetailVO(application, baseClientDetails);
		oauth20Details.setSecret(application.getSecret());
		oauth20Details.setClientSecret(application.getSecret());
		log.debug("forwardUpdate " + oauth20Details);
		oauth20Details.transIconBase64();
		return new Message<>(oauth20Details).buildResponse();
	}

	@ResponseBody
	@PostMapping(value = { "/add" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> add(@RequestBody AppOauthClientDetailVO oauth20Details,
			@CurrentUser UserEntity currentUser) {
		log.debug("-Add  :" + oauth20Details);

		if (oauth20Details.getProtocol().equalsIgnoreCase(ConstProtocols.OAUTH21)) {
			oauth20Details.setPkce(OAuth2Constants.PKCE_TYPE.PKCE_TYPE_YES);
		}
		transform(oauth20Details);

		oauth20Details.setClientSecret(oauth20Details.getSecret());
		oauth20Details.setInstId(currentUser.getInstId());

		oauth20JdbcClientDetailsService.addClientDetails(oauth20Details.clientDetailsRowMapper());
		if (null != appService.add(oauth20Details)) {
			return new Message<AppOauthClientDetailEntity>(Message.SUCCESS).buildResponse();
		} else {
			return new Message<AppOauthClientDetailEntity>(Message.FAIL).buildResponse();
		}
	}

	@ResponseBody
	@PostMapping(value = { "/update" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> update(@RequestBody AppOauthClientDetailVO oauth20Details,
			@CurrentUser UserEntity currentUser) {
		log.debug("-update  :" + oauth20Details);
		log.debug("-update  application :" + oauth20Details);
		log.debug("-update  oauth20Details use oauth20JdbcClientDetails");
		if (oauth20Details.getProtocol().equalsIgnoreCase(ConstProtocols.OAUTH21)) {
			oauth20Details.setPkce(OAuth2Constants.PKCE_TYPE.PKCE_TYPE_YES);
		}

		transform(oauth20Details);
		oauth20Details.setClientSecret(oauth20Details.getSecret());
		oauth20Details.setInstId(currentUser.getInstId());
		oauth20JdbcClientDetailsService.updateClientDetails(oauth20Details.clientDetailsRowMapper());
		oauth20JdbcClientDetailsService.updateClientSecret(oauth20Details.getClientId(),
				oauth20Details.getClientSecret());

		if (appService.edit(oauth20Details)) {
			return new Message<AppOauthClientDetailEntity>(Message.SUCCESS).buildResponse();
		} else {
			return new Message<AppOauthClientDetailEntity>(Message.FAIL).buildResponse();
		}
	}

	@ResponseBody
	@PostMapping(value = { "/delete" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> delete(@RequestParam("ids") String ids, @CurrentUser UserEntity currentUser) {
		log.debug("-delete  ids : {} ", ids);
		for (String id : StrHelper.split(ids, ",")) {
			oauth20JdbcClientDetailsService.removeClientDetails(id);
		}
		if (appService.removeByIds(Arrays.asList(ids.split(",")))) {
			return new Message<AppOauthClientDetailEntity>(Message.SUCCESS).buildResponse();
		} else {
			return new Message<AppOauthClientDetailEntity>(Message.FAIL).buildResponse();
		}
	}
}