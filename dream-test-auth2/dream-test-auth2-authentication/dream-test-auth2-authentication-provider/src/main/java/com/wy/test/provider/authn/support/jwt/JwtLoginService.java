package com.wy.test.provider.authn.support.jwt;

import java.util.Date;
import java.util.UUID;

import org.joda.time.DateTime;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.PlainJWT;
import com.nimbusds.jwt.SignedJWT;
import com.wy.test.core.web.WebContext;

import dream.flying.flower.framework.web.crypto.jwt.sign.DefaultJwtSigningAndValidationHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JwtLoginService {

	String issuer;

	DefaultJwtSigningAndValidationHandler jwtSignerValidationService;

	public JwtLoginService(DefaultJwtSigningAndValidationHandler jwtSignerValidationService, String issuer) {
		this.jwtSignerValidationService = jwtSignerValidationService;
		this.issuer = issuer;
	}

	public String buildLoginJwt() {
		log.debug("build Login JWT .");

		DateTime currentDateTime = DateTime.now();
		Date expirationTime = currentDateTime.plusMinutes(5).toDate();
		log.debug("Expiration Time : " + expirationTime);
		JWTClaimsSet jwtClaims = new JWTClaimsSet.Builder().subject(WebContext.getSession().getId())
				.expirationTime(expirationTime).issuer(getIssuer()).issueTime(currentDateTime.toDate())
				.jwtID(UUID.randomUUID().toString()).build();

		JWT jwtToken = new PlainJWT(jwtClaims);

		log.info("JWT Claims : " + jwtClaims.toString());

		JWSAlgorithm signingAlg = jwtSignerValidationService.getDefaultSigningAlgorithm();

		jwtToken = new SignedJWT(new JWSHeader(signingAlg), jwtClaims);
		// sign it with the server's key
		jwtSignerValidationService.signJwt((SignedJWT) jwtToken);

		String tokenString = jwtToken.serialize();
		log.debug("JWT Token : " + tokenString);
		return tokenString;
	}

	public SignedJWT jwtTokenValidation(String jwt) {
		SignedJWT signedJWT = null;
		JWTClaimsSet jwtClaimsSet = null;
		try {
			RSASSAVerifier rsaSSAVerifier = new RSASSAVerifier(((RSAKey) jwtSignerValidationService.getAllPublicKeys()
					.get(jwtSignerValidationService.getDefaultSignerKeyId())).toRSAPublicKey());

			signedJWT = SignedJWT.parse(jwt);

			if (signedJWT.verify(rsaSSAVerifier)) {
				jwtClaimsSet = signedJWT.getJWTClaimsSet();
				boolean isIssuerMatches = jwtClaimsSet.getIssuer().equals(getIssuer());
				boolean isExpiration = (new DateTime()).isBefore(jwtClaimsSet.getExpirationTime().getTime());

				log.debug("Signed JWT {}", signedJWT.getPayload());
				log.debug("Subject is {}", jwtClaimsSet.getSubject());
				log.debug("ExpirationTime  Validation {}", isExpiration);
				log.debug("JWT ClaimsSet Issuer {}, Metadata Issuer {}, Issuer is matches {}", jwtClaimsSet.getIssuer(),
						getIssuer(), isIssuerMatches);

				if (isIssuerMatches && isExpiration) {
					return signedJWT;
				}
			} else {
				log.debug("JWT Signer Verify false.");
			}
		} catch (java.text.ParseException e) {
			// Invalid signed JWT encoding
			log.error("Invalid signed JWT encoding ", e);
		} catch (JOSEException e) {
			log.error("JOSEException ", e);
		}
		return null;
	}

	public void setJwtSignerValidationService(DefaultJwtSigningAndValidationHandler jwtSignerValidationService) {
		this.jwtSignerValidationService = jwtSignerValidationService;
	}

	public DefaultJwtSigningAndValidationHandler getJwtSignerValidationService() {
		return jwtSignerValidationService;
	}

	public String getIssuer() {
		return issuer;
	}

	public void setIssuer(String issuer) {
		this.issuer = issuer;
	}

}
