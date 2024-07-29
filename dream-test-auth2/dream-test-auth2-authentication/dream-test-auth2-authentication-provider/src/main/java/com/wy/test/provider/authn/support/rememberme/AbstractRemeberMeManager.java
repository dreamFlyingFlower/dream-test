package com.wy.test.provider.authn.support.rememberme;

import java.text.ParseException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;

import com.nimbusds.jwt.JWTClaimsSet;
import com.wy.test.core.authn.SignPrincipal;
import com.wy.test.core.authn.jwt.AuthTokenService;
import com.wy.test.core.configuration.ApplicationConfig;
import com.wy.test.core.entity.UserInfo;
import com.wy.test.core.web.WebContext;

import dream.flying.flower.framework.web.crypto.jwt.HMAC512Service;

public abstract class AbstractRemeberMeManager {

	private static final Logger _logger = LoggerFactory.getLogger(AbstractRemeberMeManager.class);

	protected Integer validity = 7;

	protected ApplicationConfig applicationConfig;

	AuthTokenService authTokenService;

	// follow function is for persist
	public abstract void save(RemeberMe remeberMe);

	public abstract void update(RemeberMe remeberMe);

	public abstract RemeberMe read(RemeberMe remeberMe);

	public abstract void remove(String username);
	// end persist

	public String createRemeberMe(Authentication authentication, HttpServletRequest request,
			HttpServletResponse response) {
		if (applicationConfig.getLoginConfig().isRemeberMe()) {
			SignPrincipal principal = ((SignPrincipal) authentication.getPrincipal());
			UserInfo userInfo = principal.getUserInfo();
			_logger.debug("Remeber Me ...");
			RemeberMe remeberMe = new RemeberMe();
			remeberMe.setId(WebContext.genId());
			remeberMe.setUserId(userInfo.getId());
			remeberMe.setUsername(userInfo.getUsername());
			remeberMe.setLastLoginTime(new Date());
			remeberMe.setExpirationTime(DateTime.now().plusDays(validity).toDate());
			save(remeberMe);
			_logger.debug("Remeber Me " + remeberMe);
			return genRemeberMe(remeberMe);
		}
		return null;
	}

	public String updateRemeberMe(RemeberMe remeberMe) {
		remeberMe.setLastLoginTime(new Date());
		remeberMe.setExpirationTime(DateTime.now().plusDays(validity).toDate());
		update(remeberMe);
		_logger.debug("update Remeber Me " + remeberMe);

		return genRemeberMe(remeberMe);
	}

	public boolean removeRemeberMe(HttpServletResponse response, UserInfo currentUser) {
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
		_logger.debug("expiration Time : {}", remeberMe.getExpirationTime());

		JWTClaimsSet remeberMeJwtClaims = new JWTClaimsSet.Builder().issuer("").subject(remeberMe.getUsername())
				.jwtID(remeberMe.getId()).issueTime(remeberMe.getLastLoginTime())
				.expirationTime(remeberMe.getExpirationTime()).claim("kid", HMAC512Service.MXK_AUTH_JWK).build();

		return authTokenService.signedJWT(remeberMeJwtClaims);
	}

	public Integer getValidity() {
		return validity;
	}

	public void setValidity(Integer validity) {
		if (validity != 0) {
			this.validity = validity;
		}
	}

}
