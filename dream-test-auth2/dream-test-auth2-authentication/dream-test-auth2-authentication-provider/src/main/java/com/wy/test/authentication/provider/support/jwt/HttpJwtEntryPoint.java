package com.wy.test.authentication.provider.support.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.nimbusds.jwt.SignedJWT;
import com.wy.test.authentication.core.entity.LoginCredential;
import com.wy.test.authentication.core.jwt.AuthJwt;
import com.wy.test.authentication.core.jwt.AuthTokenService;
import com.wy.test.authentication.provider.provider.AbstractAuthenticationProvider;
import com.wy.test.core.base.ResultResponse;
import com.wy.test.core.constant.ConstAuthWeb;
import com.wy.test.core.properties.DreamAuthServerProperties;

import dream.flying.flower.framework.web.enums.AuthLoginType;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping(value = "/login")
@Slf4j
public class HttpJwtEntryPoint {

	@Autowired
	DreamAuthServerProperties dreamServerProperties;

	@Autowired
	AbstractAuthenticationProvider authenticationProvider;

	@Autowired
	AuthTokenService authTokenService;

	@Autowired
	JwtLoginService jwtLoginService;

	@GetMapping(value = { "/jwt" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> jwt(@RequestParam(value = ConstAuthWeb.JWT_TOKEN_PARAMETER, required = true) String jwt) {
		try {
			// for jwt Login
			log.debug("jwt : " + jwt);

			SignedJWT signedJWT = jwtLoginService.jwtTokenValidation(jwt);

			if (signedJWT != null) {
				String username = signedJWT.getJWTClaimsSet().getSubject();
				LoginCredential loginCredential = new LoginCredential(username, "", AuthLoginType.JWT);
				Authentication authentication = authenticationProvider.authenticate(loginCredential, true);
				log.debug("JWT Logined in , username " + username);
				AuthJwt authJwt = authTokenService.genAuthJwt(authentication);
				return new ResultResponse<AuthJwt>(authJwt).buildResponse();
			}
		} catch (Exception e) {
			log.error("Exception ", e);
		}

		return new ResultResponse<AuthJwt>(ResultResponse.FAIL).buildResponse();
	}

	/**
	 * trust same HS512
	 * 
	 * @param jwt
	 * @return
	 */
	@GetMapping(value = { "/jwt/trust" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?>
			jwtTrust(@RequestParam(value = ConstAuthWeb.JWT_TOKEN_PARAMETER, required = true) String jwt) {
		try {
			// for jwt Login
			log.debug("jwt : " + jwt);

			if (authTokenService.validateJwtToken(jwt)) {
				String username = authTokenService.resolve(jwt).getSubject();
				LoginCredential loginCredential = new LoginCredential(username, "", AuthLoginType.JWT);
				Authentication authentication = authenticationProvider.authenticate(loginCredential, true);
				log.debug("JWT Logined in , username " + username);
				AuthJwt authJwt = authTokenService.genAuthJwt(authentication);
				return new ResultResponse<AuthJwt>(authJwt).buildResponse();
			}
		} catch (Exception e) {
			log.error("Exception ", e);
		}

		return new ResultResponse<AuthJwt>(ResultResponse.FAIL).buildResponse();
	}

	public void setApplicationConfig(DreamAuthServerProperties dreamServerProperties) {
		this.dreamServerProperties = dreamServerProperties;
	}

	public void setAuthenticationProvider(AbstractAuthenticationProvider authenticationProvider) {
		this.authenticationProvider = authenticationProvider;
	}

	public void setJwtLoginService(JwtLoginService jwtLoginService) {
		this.jwtLoginService = jwtLoginService;
	}
}