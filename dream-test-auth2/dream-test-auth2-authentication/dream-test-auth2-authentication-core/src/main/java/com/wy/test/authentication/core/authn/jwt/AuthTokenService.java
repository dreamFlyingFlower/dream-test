package com.wy.test.authentication.core.authn.jwt;

import java.text.ParseException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;

import com.nimbusds.jose.JOSEException;
import com.wy.test.core.cache.MomentaryService;
import com.wy.test.core.properties.DreamAuthJwkProperties;
import com.wy.test.core.web.WebContext;

import dream.flying.flower.framework.web.crypto.jwt.HMAC512Service;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AuthTokenService extends AuthJwtService {

	DreamAuthJwkProperties dreamJwkProperties;

	CongressService congressService;

	MomentaryService momentaryService;

	AuthRefreshTokenService refreshTokenService;

	public AuthTokenService(DreamAuthJwkProperties dreamJwkProperties, CongressService congressService,
			MomentaryService momentaryService, AuthRefreshTokenService refreshTokenService) throws JOSEException {

		this.dreamJwkProperties = dreamJwkProperties;

		this.congressService = congressService;

		this.momentaryService = momentaryService;

		this.refreshTokenService = refreshTokenService;

		this.hmac512Service = new HMAC512Service(dreamJwkProperties.getSecret());

	}

	/**
	 * create AuthJwt use Authentication JWT
	 * 
	 * @param authentication
	 * @return AuthJwt
	 */
	public AuthJwt genAuthJwt(Authentication authentication) {
		if (authentication != null) {
			String refreshToken = refreshTokenService.genRefreshToken(authentication);
			log.trace("generate JWT Token");
			String accessToken = genJwt(authentication);
			AuthJwt authJwt = new AuthJwt(accessToken, authentication, dreamJwkProperties.getExpires(), refreshToken);
			return authJwt;
		}
		return null;
	}

	public String genJwt(Authentication authentication) {
		return genJwt(authentication, dreamJwkProperties.getIssuer(), dreamJwkProperties.getExpires());
	}

	/**
	 * JWT with subject
	 * 
	 * @param subject subject
	 * @return
	 */
	public String genJwt(String subject) {
		return genJwt(subject, dreamJwkProperties.getIssuer(), dreamJwkProperties.getExpires());
	}

	/**
	 * Random JWT
	 * 
	 * @return
	 */
	public String genRandomJwt() {
		return genRandomJwt(dreamJwkProperties.getExpires());
	}

	public String createCongress(Authentication authentication) {
		String congress = WebContext.genId();
		String refreshToken = refreshTokenService.genRefreshToken(authentication);
		congressService.store(congress,
				new AuthJwt(genJwt(authentication), authentication, dreamJwkProperties.getExpires(), refreshToken));
		return congress;
	}

	public AuthJwt consumeCongress(String congress) {
		AuthJwt authJwt = congressService.consume(congress);
		return authJwt;
	}

	public boolean validateCaptcha(String state, String captcha) {
		try {
			String jwtId = resolveJWTID(state);
			if (StringUtils.isNotBlank(jwtId) && StringUtils.isNotBlank(captcha)) {
				Object momentaryCaptcha = momentaryService.get("", jwtId);
				log.debug("captcha : {}, momentary Captcha : {}", captcha, momentaryCaptcha);
				if (!StringUtils.isBlank(captcha) && captcha.equals(momentaryCaptcha.toString())) {
					momentaryService.remove("", jwtId);
					return true;
				}
			}
		} catch (ParseException e) {
			log.debug("Exception ", e);
		}
		return false;
	}
}