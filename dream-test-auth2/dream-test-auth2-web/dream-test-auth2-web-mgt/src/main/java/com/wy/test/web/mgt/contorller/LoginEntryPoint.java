package com.wy.test.web.mgt.contorller;

import java.util.HashMap;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wy.test.authentication.core.authn.LoginCredential;
import com.wy.test.authentication.core.authn.jwt.AuthJwt;
import com.wy.test.authentication.core.authn.jwt.AuthTokenService;
import com.wy.test.authentication.provider.authn.provider.AbstractAuthenticationProvider;
import com.wy.test.core.entity.InstitutionEntity;
import com.wy.test.core.entity.Message;
import com.wy.test.core.properties.DreamAuthLoginProperties;
import com.wy.test.core.web.WebConstants;
import com.wy.test.core.web.WebContext;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping(value = "/login")
@Slf4j
@AllArgsConstructor
public class LoginEntryPoint {

	private final AuthTokenService authTokenService;

	private final AbstractAuthenticationProvider authenticationProvider;

	private final DreamAuthLoginProperties dreamLoginProperties;

	/**
	 * init login
	 * 
	 * @return
	 */
	@GetMapping(value = { "/get" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> get() {
		log.debug("/login.");

		HashMap<String, Object> model = new HashMap<String, Object>();
		model.put("isRemeberMe", dreamLoginProperties.isRememberMe());
		InstitutionEntity inst = (InstitutionEntity) WebContext.getAttribute(WebConstants.CURRENT_INST);
		model.put("inst", inst);
		if (dreamLoginProperties.getCaptcha().isEnabled()) {
			model.put("captcha", "true");
		} else {
			model.put("captcha", inst.getCaptcha());
		}
		model.put("state", authTokenService.genRandomJwt());
		return new Message<HashMap<String, Object>>(model).buildResponse();
	}

	@PostMapping(value = { "/signin" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> signin(@RequestBody LoginCredential loginCredential) {
		Message<AuthJwt> authJwtMessage = new Message<AuthJwt>(Message.FAIL);
		if (authTokenService.validateJwtToken(loginCredential.getState())) {
			Authentication authentication = authenticationProvider.authenticate(loginCredential);
			if (authentication != null) {
				AuthJwt authJwt = authTokenService.genAuthJwt(authentication);
				authJwtMessage = new Message<AuthJwt>(authJwt);
			} else {// fail
				String errorMsg = WebContext.getAttribute(WebConstants.LOGIN_ERROR_SESSION_MESSAGE) == null ? ""
						: WebContext.getAttribute(WebConstants.LOGIN_ERROR_SESSION_MESSAGE).toString();
				authJwtMessage.setMessage(Message.FAIL, errorMsg);
				log.debug("login fail , message {}", errorMsg);
			}
		}
		return authJwtMessage.buildResponse();
	}
}