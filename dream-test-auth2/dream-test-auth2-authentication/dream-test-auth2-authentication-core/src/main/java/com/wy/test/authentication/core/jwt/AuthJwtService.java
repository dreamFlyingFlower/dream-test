package com.wy.test.authentication.core.jwt;

import java.text.ParseException;
import java.util.Date;

import org.joda.time.DateTime;
import org.springframework.security.core.Authentication;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.wy.test.authentication.core.entity.SignPrincipal;
import com.wy.test.core.vo.UserVO;
import com.wy.test.core.web.AuthWebContext;

import dream.flying.flower.framework.web.crypto.jwt.HMAC512Service;
import dream.flying.flower.lang.StrHelper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AuthJwtService {

	HMAC512Service hmac512Service;

	/**
	 * JWT with Authentication
	 * 
	 * @param authentication
	 * @return
	 */
	public String genJwt(Authentication authentication, String issuer, int expires) {
		SignPrincipal principal = ((SignPrincipal) authentication.getPrincipal());
		UserVO userInfo = principal.getUserInfo();
		DateTime currentDateTime = DateTime.now();
		String subject = principal.getUsername();
		Date expirationTime = currentDateTime.plusSeconds(expires).toDate();
		log.trace("jwt subject : {} , expiration Time : {}", subject, expirationTime);

		JWTClaimsSet jwtClaims = new JWTClaimsSet.Builder().issuer(issuer).subject(subject)
				.jwtID(principal.getSession().getId()).issueTime(currentDateTime.toDate())
				.expirationTime(expirationTime).claim("locale", userInfo.getLocale())
				.claim("kid", HMAC512Service.DREAM_AUTH_JWK).claim("institution", userInfo.getInstId()).build();

		return signedJWT(jwtClaims);
	}

	/**
	 * JWT with subject
	 * 
	 * @param subject subject
	 * @return
	 */
	public String genJwt(String subject, String issuer, int expires) {
		DateTime currentDateTime = DateTime.now();
		Date expirationTime = currentDateTime.plusSeconds(expires).toDate();
		log.trace("jwt subject : {} , expiration Time : {}", subject, expirationTime);

		JWTClaimsSet jwtClaims = new JWTClaimsSet.Builder().issuer(issuer).subject(subject).jwtID(AuthWebContext.genId())
				.issueTime(currentDateTime.toDate()).expirationTime(expirationTime).build();

		return signedJWT(jwtClaims);
	}

	/**
	 * Random JWT
	 * 
	 * @return
	 */
	public String genRandomJwt(int expires) {
		Date expirationTime = DateTime.now().plusSeconds(expires).toDate();
		log.trace("expiration Time : {}", expirationTime);

		JWTClaimsSet jwtClaims =
				new JWTClaimsSet.Builder().jwtID(AuthWebContext.genId()).expirationTime(expirationTime).build();

		return signedJWT(jwtClaims);
	}

	public String signedJWT(JWTClaimsSet jwtClaims) {
		log.trace("jwt Claims : {}", jwtClaims);
		SignedJWT jwtToken = new SignedJWT(new JWSHeader(JWSAlgorithm.HS512), jwtClaims);
		return hmac512Service.sign(jwtToken.getPayload());
	}

	/**
	 * Verify with HMAC512 and check ExpirationTime
	 * 
	 * @param authToken
	 * @return true or false
	 */
	public boolean validateJwtToken(String authToken) {
		try {
			if (StrHelper.isNotBlank(authToken)) {
				JWTClaimsSet claims = resolve(authToken);
				boolean isExpiration = claims.getExpirationTime().after(DateTime.now().toDate());
				boolean isVerify = hmac512Service.verify(authToken);
				log.debug("JWT Validate {} ", isVerify && isExpiration);

				if (!(isVerify && isExpiration)) {
					log.debug("HMAC Verify {} , now {} , ExpirationTime {} , is not Expiration : {}", isVerify,
							DateTime.now().toDate(), claims.getExpirationTime(), isExpiration);
				}
				return isVerify && isExpiration;
			}
		} catch (ParseException e) {
			log.error("authToken {}", authToken);
			log.error("ParseException ", e);
		}
		return false;
	}

	public JWTClaimsSet resolve(String authToken) throws ParseException {
		SignedJWT signedJWT = SignedJWT.parse(authToken);
		log.trace("jwt Claims : {}", signedJWT.getJWTClaimsSet());
		return signedJWT.getJWTClaimsSet();
	}

	public String resolveJWTID(String authToken) throws ParseException {
		JWTClaimsSet claims = resolve(authToken);
		return claims.getJWTID();
	}
}