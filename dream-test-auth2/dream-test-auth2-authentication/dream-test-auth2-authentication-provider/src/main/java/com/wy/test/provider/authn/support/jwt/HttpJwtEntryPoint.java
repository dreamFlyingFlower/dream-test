package com.wy.test.provider.authn.support.jwt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.nimbusds.jwt.SignedJWT;
import com.wy.test.core.authn.LoginCredential;
import com.wy.test.core.authn.jwt.AuthJwt;
import com.wy.test.core.authn.jwt.AuthTokenService;
import com.wy.test.core.constants.ConstsLoginType;
import com.wy.test.core.entity.Message;
import com.wy.test.core.properties.DreamServerProperties;
import com.wy.test.core.web.WebConstants;
import com.wy.test.provider.authn.provider.AbstractAuthenticationProvider;

@Controller
@RequestMapping(value = "/login")
public class HttpJwtEntryPoint {

	private static final Logger _logger = LoggerFactory.getLogger(HttpJwtEntryPoint.class);

	@Autowired
	DreamServerProperties dreamServerProperties;

	@Autowired
	AbstractAuthenticationProvider authenticationProvider;

	@Autowired
	AuthTokenService authTokenService;

	@Autowired
	JwtLoginService jwtLoginService;

	@GetMapping(value = { "/jwt" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> jwt(@RequestParam(value = WebConstants.JWT_TOKEN_PARAMETER, required = true) String jwt) {
		try {
			// for jwt Login
			_logger.debug("jwt : " + jwt);

			SignedJWT signedJWT = jwtLoginService.jwtTokenValidation(jwt);

			if (signedJWT != null) {
				String username = signedJWT.getJWTClaimsSet().getSubject();
				LoginCredential loginCredential = new LoginCredential(username, "", ConstsLoginType.JWT);
				Authentication authentication = authenticationProvider.authenticate(loginCredential, true);
				_logger.debug("JWT Logined in , username " + username);
				AuthJwt authJwt = authTokenService.genAuthJwt(authentication);
				return new Message<AuthJwt>(authJwt).buildResponse();
			}
		} catch (Exception e) {
			_logger.error("Exception ", e);
		}

		return new Message<AuthJwt>(Message.FAIL).buildResponse();
	}

	/**
	 * trust same HS512
	 * 
	 * @param jwt
	 * @return
	 */
	@GetMapping(value = { "/jwt/trust" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?>
			jwtTrust(@RequestParam(value = WebConstants.JWT_TOKEN_PARAMETER, required = true) String jwt) {
		try {
			// for jwt Login
			_logger.debug("jwt : " + jwt);

			if (authTokenService.validateJwtToken(jwt)) {
				String username = authTokenService.resolve(jwt).getSubject();
				LoginCredential loginCredential = new LoginCredential(username, "", ConstsLoginType.JWT);
				Authentication authentication = authenticationProvider.authenticate(loginCredential, true);
				_logger.debug("JWT Logined in , username " + username);
				AuthJwt authJwt = authTokenService.genAuthJwt(authentication);
				return new Message<AuthJwt>(authJwt).buildResponse();
			}
		} catch (Exception e) {
			_logger.error("Exception ", e);
		}

		return new Message<AuthJwt>(Message.FAIL).buildResponse();
	}

	public void setApplicationConfig(DreamServerProperties dreamServerProperties) {
		this.dreamServerProperties = dreamServerProperties;
	}

	public void setAuthenticationProvider(AbstractAuthenticationProvider authenticationProvider) {
		this.authenticationProvider = authenticationProvider;
	}

	public void setJwtLoginService(JwtLoginService jwtLoginService) {
		this.jwtLoginService = jwtLoginService;
	}
}