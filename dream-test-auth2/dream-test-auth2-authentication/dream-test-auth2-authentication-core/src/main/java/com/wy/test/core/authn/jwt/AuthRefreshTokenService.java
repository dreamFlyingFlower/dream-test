package com.wy.test.core.authn.jwt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;

import com.nimbusds.jose.JOSEException;
import com.wy.test.configuration.AuthJwkConfig;
import com.wy.test.crypto.jwt.HMAC512Service;

public class AuthRefreshTokenService extends AuthJwtService {

	private static final Logger _logger = LoggerFactory.getLogger(AuthRefreshTokenService.class);

	AuthJwkConfig authJwkConfig;

	public AuthRefreshTokenService(AuthJwkConfig authJwkConfig) throws JOSEException {
		this.authJwkConfig = authJwkConfig;

		this.hmac512Service = new HMAC512Service(authJwkConfig.getRefreshSecret());
	}

	/**
	 * JWT Refresh Token with Authentication
	 * 
	 * @param authentication
	 * @return
	 */
	public String genRefreshToken(Authentication authentication) {
		_logger.trace("generate Refresh JWT Token");
		return genJwt(authentication, authJwkConfig.getIssuer(), authJwkConfig.getRefreshExpires());
	}
}
