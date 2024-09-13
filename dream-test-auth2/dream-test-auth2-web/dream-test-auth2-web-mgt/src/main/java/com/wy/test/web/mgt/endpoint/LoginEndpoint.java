package com.wy.test.web.mgt.endpoint;

import java.util.HashMap;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wy.test.authentication.core.entity.LoginCredential;
import com.wy.test.authentication.core.jwt.AuthJwt;
import com.wy.test.authentication.core.jwt.AuthTokenService;
import com.wy.test.authentication.provider.provider.AbstractAuthenticationProvider;
import com.wy.test.core.constant.ConstAuthWeb;
import com.wy.test.core.entity.InstitutionEntity;
import com.wy.test.core.properties.DreamAuthLoginProperties;
import com.wy.test.core.web.AuthWebContext;

import dream.flying.flower.framework.web.controller.BaseResponseController;
import dream.flying.flower.result.Result;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 登录
 *
 * @author 飞花梦影
 * @date 2024-08-25 16:13:31
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@RestController
@RequestMapping(value = "/login")
@Slf4j
@AllArgsConstructor
public class LoginEndpoint implements BaseResponseController {

	private final AuthTokenService authTokenService;

	private final AbstractAuthenticationProvider authenticationProvider;

	private final DreamAuthLoginProperties dreamLoginProperties;

	@GetMapping("get")
	public ResponseEntity<?> get() {
		log.debug("/login.");

		HashMap<String, Object> model = new HashMap<>();
		model.put("isRemeberMe", dreamLoginProperties.isRememberMe());
		InstitutionEntity inst = (InstitutionEntity) AuthWebContext.getAttribute(ConstAuthWeb.CURRENT_INST);
		model.put("inst", inst);
		if (dreamLoginProperties.getCaptcha().isEnabled()) {
			model.put("captcha", "true");
		} else {
			model.put("captcha", inst.getCaptcha());
		}
		model.put("state", authTokenService.genRandomJwt());
		return ok(model);
	}

	@PostMapping("signin")
	public ResponseEntity<?> signin(@RequestBody LoginCredential loginCredential) {
		if (!authTokenService.validateJwtToken(loginCredential.getState())) {
			return ResponseEntity.ok(Result.error());
		}
		Authentication authentication = authenticationProvider.authenticate(loginCredential);
		if (authentication != null) {
			AuthJwt authJwt = authTokenService.genAuthJwt(authentication);
			return ResponseEntity.ok(Result.ok(authJwt));
		} else {
			String errorMsg = AuthWebContext.getAttribute(ConstAuthWeb.LOGIN_ERROR_SESSION_MESSAGE) == null ? ""
					: AuthWebContext.getAttribute(ConstAuthWeb.LOGIN_ERROR_SESSION_MESSAGE).toString();
			log.debug("login fail , message {}", errorMsg);
			return error(errorMsg);
		}
	}
}