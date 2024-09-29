package com.wy.test.protocol.jwt.jwt.endpoint.adapter;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWEHeader;
import com.nimbusds.jose.JWEObject;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.Payload;
import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.PlainJWT;
import com.nimbusds.jwt.SignedJWT;
import com.wy.test.core.constant.ConstAuthWeb;
import com.wy.test.core.vo.AppJwtDetailVO;
import com.wy.test.protocol.authorize.endpoint.adapter.AbstractAuthorizeAdapter;

import dream.flying.flower.framework.crypto.jwt.encryption.DefaultJwtEncryptionAndDecryptionHandler;
import dream.flying.flower.framework.crypto.jwt.sign.DefaultJwtSigningAndValidationHandler;

public class JwtAdapter extends AbstractAuthorizeAdapter {

	final static Logger log = LoggerFactory.getLogger(JwtAdapter.class);

	AppJwtDetailVO jwtDetails;

	JWT jwtToken;

	JWEObject jweObject;

	JWTClaimsSet jwtClaims;

	public JwtAdapter() {

	}

	public JwtAdapter(AppJwtDetailVO jwtDetails) {
		this.jwtDetails = jwtDetails;
	}

	@Override
	public Object generateInfo() {
		DateTime currentDateTime = DateTime.now();
		Date expirationTime = currentDateTime.plusSeconds(jwtDetails.getExpires()).toDate();
		log.debug("expiration Time : {}", expirationTime);
		String subject = getValueByUserAttr(userInfo, jwtDetails.getSubject());
		log.trace("jwt subject : {}", subject);

		jwtClaims = new JWTClaimsSet.Builder().issuer(jwtDetails.getIssuer()).subject(subject)
				.audience(Arrays.asList(jwtDetails.getId())).jwtID(UUID.randomUUID().toString())
				.issueTime(currentDateTime.toDate()).expirationTime(expirationTime)
				.claim("email", userInfo.getWorkEmail()).claim("name", userInfo.getUsername())
				.claim("user_id", userInfo.getId()).claim("external_id", userInfo.getId())
				.claim("locale", userInfo.getLocale())
				.claim(ConstAuthWeb.ONLINE_TICKET_NAME, principal.getSession().getFormattedId())
				.claim("kid", jwtDetails.getId() + "_sig").claim("institution", userInfo.getInstId()).build();

		log.trace("jwt Claims : {}", jwtClaims);

		jwtToken = new PlainJWT(jwtClaims);

		return jwtToken;
	}

	@Override
	public Object sign(Object data, String signatureKey, String signature) {
		if (!jwtDetails.getSignature().equalsIgnoreCase("none")) {
			try {
				DefaultJwtSigningAndValidationHandler jwtSignerService = new DefaultJwtSigningAndValidationHandler(
						jwtDetails.getSignatureKey(), jwtDetails.getId() + "_sig", jwtDetails.getSignature());

				jwtToken = new SignedJWT(new JWSHeader(jwtSignerService.getDefaultSigningAlgorithm()), jwtClaims);
				// sign it with the server's key
				jwtSignerService.signJwt((SignedJWT) jwtToken);
				return jwtToken;
			} catch (NoSuchAlgorithmException e) {
				log.error("NoSuchAlgorithmException", e);
			} catch (InvalidKeySpecException e) {
				log.error("InvalidKeySpecException", e);
			} catch (JOSEException e) {
				log.error("JOSEException", e);
			}
		}
		return data;
	}

	@Override
	public Object encrypt(Object data, String algorithmKey, String algorithm) {
		if (!jwtDetails.getAlgorithm().equalsIgnoreCase("none")) {
			try {
				DefaultJwtEncryptionAndDecryptionHandler jwtEncryptionService =
						new DefaultJwtEncryptionAndDecryptionHandler(jwtDetails.getAlgorithmKey(),
								jwtDetails.getId() + "_enc", jwtDetails.getAlgorithm());

				Payload payload;
				if (jwtToken instanceof SignedJWT) {
					payload = ((SignedJWT) jwtToken).getPayload();
				} else {
					payload = ((PlainJWT) jwtToken).getPayload();
				}
				// Example Request JWT encrypted with RSA-OAEP-256 and 128-bit AES/GCM
				// JWEHeader jweHeader = new JWEHeader(JWEAlgorithm.RSA1_5,
				// EncryptionMethod.A128GCM);
				JWEHeader jweHeader = new JWEHeader(jwtEncryptionService.getDefaultAlgorithm(jwtDetails.getAlgorithm()),
						jwtEncryptionService.parseEncryptionMethod(jwtDetails.getEncryptionType()));
				jweObject = new JWEObject(new JWEHeader.Builder(jweHeader).contentType("JWT") // required to indicate
																								// nested JWT
						.build(), payload);

				jwtEncryptionService.encryptJwt(jweObject);

			} catch (NoSuchAlgorithmException | InvalidKeySpecException | JOSEException e) {
				log.error("Encrypt Exception", e);
			}
		}
		return data;
	}

	@Override
	public ModelAndView authorize(ModelAndView modelAndView) {
		modelAndView.setViewName("authorize/jwt_sso_submint");
		modelAndView.addObject("action", jwtDetails.getRedirectUri());

		modelAndView.addObject("token", serialize());
		modelAndView.addObject("jwtName", jwtDetails.getJwtName());

		modelAndView.addObject("tokenType", jwtDetails.getTokenType().toLowerCase());

		return modelAndView;
	}

	public void setJwtDetails(AppJwtDetailVO jwtDetails) {
		this.jwtDetails = jwtDetails;
	}

	@Override
	public String serialize() {
		String tokenString = "";
		if (jweObject != null) {
			tokenString = jweObject.serialize();
		} else {
			tokenString = jwtToken.serialize();
		}
		log.debug("jwt Token : {}", tokenString);
		return tokenString;
	}
}