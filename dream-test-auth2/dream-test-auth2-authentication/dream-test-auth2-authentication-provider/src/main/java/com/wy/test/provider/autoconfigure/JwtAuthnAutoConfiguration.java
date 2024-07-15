package com.wy.test.provider.autoconfigure;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;

import com.nimbusds.jose.JOSEException;
import com.wy.test.crypto.jose.keystore.JWKSetKeyStore;
import com.wy.test.crypto.jwt.signer.service.impl.DefaultJwtSigningAndValidationService;
import com.wy.test.provider.authn.support.jwt.JwtLoginService;

@AutoConfiguration
public class JwtAuthnAutoConfiguration implements InitializingBean {

	private static final Logger _logger = LoggerFactory.getLogger(JwtAuthnAutoConfiguration.class);

	/**
	 * jwt Login JwkSetKeyStore.
	 * 
	 * @return
	 */
	@Bean
	JWKSetKeyStore jwtLoginJwkSetKeyStore() {
		JWKSetKeyStore jwkSetKeyStore = new JWKSetKeyStore();
		ClassPathResource classPathResource = new ClassPathResource("/config/loginjwkkeystore.jwks");
		jwkSetKeyStore.setLocation(classPathResource);
		_logger.debug("JWT Login JwkSet KeyStore init.");
		return jwkSetKeyStore;
	}

	/**
	 * jwt Login ValidationService.
	 * 
	 * @return
	 * @throws JOSEException
	 * @throws InvalidKeySpecException
	 * @throws NoSuchAlgorithmException
	 */
	@Bean
	DefaultJwtSigningAndValidationService jwtLoginValidationService(JWKSetKeyStore jwtLoginJwkSetKeyStore)
			throws NoSuchAlgorithmException, InvalidKeySpecException, JOSEException {
		DefaultJwtSigningAndValidationService jwtSignerValidationService =
				new DefaultJwtSigningAndValidationService(jwtLoginJwkSetKeyStore);
		jwtSignerValidationService.setDefaultSignerKeyId("maxkey_rsa");
		jwtSignerValidationService.setDefaultSigningAlgorithmName("RS256");
		_logger.debug("JWT Login Signing and Validation init.");
		return jwtSignerValidationService;
	}

	/**
	 * Jwt LoginService.
	 * 
	 * @return
	 */
	@Bean
	JwtLoginService jwtLoginService(@Value("${maxkey.login.jwt.issuer}") String issuer,
			DefaultJwtSigningAndValidationService jwtLoginValidationService) {
		JwtLoginService jwtLoginService = new JwtLoginService(jwtLoginValidationService, issuer);
		_logger.debug("JWT Login Service init.");
		return jwtLoginService;
	}

	@Override
	public void afterPropertiesSet() throws Exception {

	}
}
