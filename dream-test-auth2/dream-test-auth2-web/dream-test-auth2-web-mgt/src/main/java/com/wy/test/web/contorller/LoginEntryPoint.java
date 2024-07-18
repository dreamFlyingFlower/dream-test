package com.wy.test.web.contorller;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wy.test.core.authn.LoginCredential;
import com.wy.test.core.authn.jwt.AuthJwt;
import com.wy.test.core.authn.jwt.AuthTokenService;
import com.wy.test.core.configuration.ApplicationConfig;
import com.wy.test.core.entity.Institutions;
import com.wy.test.core.web.WebConstants;
import com.wy.test.core.web.WebContext;
import com.wy.test.entity.Message;
import com.wy.test.provider.authn.provider.AbstractAuthenticationProvider;

@Controller
@RequestMapping(value = "/login")
public class LoginEntryPoint {

	private static Logger _logger = LoggerFactory.getLogger(LoginEntryPoint.class);

	@Autowired
	AuthTokenService authTokenService;

	@Autowired
	protected ApplicationConfig applicationConfig;

	@Autowired
	AbstractAuthenticationProvider authenticationProvider;

	/**
	 * init login
	 * 
	 * @return
	 */
	@GetMapping(value = { "/get" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> get() {
		_logger.debug("/login.");

		HashMap<String, Object> model = new HashMap<String, Object>();
		model.put("isRemeberMe", applicationConfig.getLoginConfig().isRemeberMe());
		Institutions inst = (Institutions) WebContext.getAttribute(WebConstants.CURRENT_INST);
		model.put("inst", inst);
		if (applicationConfig.getLoginConfig().isCaptcha()) {
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
				_logger.debug("login fail , message {}", errorMsg);
			}
		}
		return authJwtMessage.buildResponse();
	}
}