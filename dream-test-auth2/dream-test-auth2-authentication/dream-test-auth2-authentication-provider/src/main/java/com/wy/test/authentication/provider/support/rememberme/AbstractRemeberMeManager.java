package com.wy.test.authentication.provider.support.rememberme;

import java.text.ParseException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.joda.time.DateTime;
import org.springframework.security.core.Authentication;

import com.nimbusds.jwt.JWTClaimsSet;
import com.wy.test.authentication.core.entity.SignPrincipal;
import com.wy.test.authentication.core.jwt.AuthTokenService;
import com.wy.test.core.entity.UserEntity;
import com.wy.test.core.properties.DreamAuthLoginProperties;
import com.wy.test.core.vo.UserVO;
import com.wy.test.core.web.AuthWebContext;

import dream.flying.flower.framework.web.crypto.jwt.HMAC512Service;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractRemeberMeManager {

	protected Long validity = 7L;

	protected DreamAuthLoginProperties dreamLoginProperties;

	AuthTokenService authTokenService;

	// follow function is for persist
	public abstract void save(RemeberMe remeberMe);

	public abstract void update(RemeberMe remeberMe);

	public abstract RemeberMe read(RemeberMe remeberMe);

	public abstract void remove(String username);
	// end persist

	public String createRemeberMe(Authentication authentication, HttpServletRequest request,
			HttpServletResponse response) {
		if (dreamLoginProperties.isRememberMe()) {
			SignPrincipal principal = ((SignPrincipal) authentication.getPrincipal());
			UserVO userInfo = principal.getUserInfo();
			log.debug("Remeber Me ...");
			RemeberMe remeberMe = new RemeberMe();
			remeberMe.setId(AuthWebContext.genId());
			remeberMe.setUserId(userInfo.getId());
			remeberMe.setUsername(userInfo.getUsername());
			remeberMe.setLastLoginTime(new Date());
			remeberMe.setExpirationTime(DateTime.now().plusDays(validity.intValue()).toDate());
			save(remeberMe);
			log.debug("Remeber Me " + remeberMe);
			return genRemeberMe(remeberMe);
		}
		return null;
	}

	public String updateRemeberMe(RemeberMe remeberMe) {
		remeberMe.setLastLoginTime(new Date());
		remeberMe.setExpirationTime(DateTime.now().plusDays(validity.intValue()).toDate());
		update(remeberMe);
		log.debug("update Remeber Me " + remeberMe);

		return genRemeberMe(remeberMe);
	}

	public boolean removeRemeberMe(HttpServletResponse response, UserEntity currentUser) {
		remove(currentUser.getUsername());

		return true;
	}

	public RemeberMe resolve(String rememberMeJwt) throws ParseException {
		JWTClaimsSet claims = authTokenService.resolve(rememberMeJwt);
		RemeberMe remeberMe = new RemeberMe();
		remeberMe.setId(claims.getJWTID());
		remeberMe.setUsername(claims.getSubject());
		return read(remeberMe);
	}

	public String genRemeberMe(RemeberMe remeberMe) {
		log.debug("expiration Time : {}", remeberMe.getExpirationTime());

		JWTClaimsSet remeberMeJwtClaims = new JWTClaimsSet.Builder().issuer("").subject(remeberMe.getUsername())
				.jwtID(remeberMe.getId()).issueTime(remeberMe.getLastLoginTime())
				.expirationTime(remeberMe.getExpirationTime()).claim("kid", HMAC512Service.DREAM_AUTH_JWK).build();

		return authTokenService.signedJWT(remeberMeJwtClaims);
	}

	public Long getValidity() {
		return validity;
	}

	public void setValidity(Long validity) {
		if (validity != 0) {
			this.validity = validity;
		}
	}
}