package com.wy.test.authentication.core.jwt;

import org.springframework.security.core.Authentication;

import com.nimbusds.jose.JOSEException;
import com.wy.test.core.properties.DreamAuthJwkProperties;

import dream.flying.flower.framework.web.crypto.jwt.HMAC512Service;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AuthRefreshTokenService extends AuthJwtService {

	DreamAuthJwkProperties dreamJwkProperties;

	public AuthRefreshTokenService(DreamAuthJwkProperties dreamJwkProperties) throws JOSEException {
		this.dreamJwkProperties = dreamJwkProperties;
		this.hmac512Service = new HMAC512Service(dreamJwkProperties.getRefreshSecret());
	}

	/**
	 * JWT Refresh Token with Authentication
	 * 
	 * @param authentication
	 * @return
	 */
	public String genRefreshToken(Authentication authentication) {
		log.trace("generate Refresh JWT Token");
		return genJwt(authentication, dreamJwkProperties.getIssuer(), dreamJwkProperties.getRefreshExpires());
	}
}