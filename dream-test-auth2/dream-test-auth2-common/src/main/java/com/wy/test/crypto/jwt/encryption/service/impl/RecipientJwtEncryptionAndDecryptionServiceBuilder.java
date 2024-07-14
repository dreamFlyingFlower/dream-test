package com.wy.test.crypto.jwt.encryption.service.impl;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.text.ParseException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.JWKSet;
import com.wy.test.crypto.jose.keystore.JWKSetKeyStore;
import com.wy.test.crypto.jwt.encryption.service.JwtEncryptionAndDecryptionService;

public class RecipientJwtEncryptionAndDecryptionServiceBuilder {

	final static Logger _logger = LoggerFactory.getLogger(RecipientJwtEncryptionAndDecryptionServiceBuilder.class);

	// private HttpClient httpClient =
	// HttpClientBuilder.create().useSystemProperties().build();
	// private HttpComponentsClientHttpRequestFactory httpFactory = new
	// HttpComponentsClientHttpRequestFactory(httpClient);
	// private RestTemplate restTemplate = new RestTemplate(httpFactory);

	/**
	 * 
	 */
	public RecipientJwtEncryptionAndDecryptionServiceBuilder() {

	}

	public JwtEncryptionAndDecryptionService serviceBuilder(String jwksUri) {

		_logger.debug("jwksUri : {}", jwksUri);

		String jsonString = "";// = restTemplate.getForObject(jwksUri, String.class);

		_logger.debug("jwks json String : {}", jsonString);
		JwtEncryptionAndDecryptionService recipientJwtEncryptionAndDecryptionService;
		try {
			JWKSet jwkSet = JWKSet.parse(jsonString);

			JWKSetKeyStore keyStore = new JWKSetKeyStore(jwkSet);
			recipientJwtEncryptionAndDecryptionService = new DefaultJwtEncryptionAndDecryptionService(keyStore);

			return recipientJwtEncryptionAndDecryptionService;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		} catch (JOSEException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return null;
	}

}
