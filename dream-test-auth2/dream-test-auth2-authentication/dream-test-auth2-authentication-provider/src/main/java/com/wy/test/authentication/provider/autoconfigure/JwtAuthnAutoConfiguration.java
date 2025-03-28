package com.wy.test.authentication.provider.autoconfigure;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;

import com.nimbusds.jose.JOSEException;
import com.wy.test.authentication.provider.support.jwt.JwtLoginService;
import com.wy.test.core.properties.DreamAuthLoginProperties;

import dream.flying.flower.framework.safe.jose.keystore.JWKSetKeyStore;
import dream.flying.flower.framework.safe.jwt.sign.DefaultJwtSigningAndValidationHandler;
import lombok.extern.slf4j.Slf4j;

@AutoConfiguration
@Slf4j
public class JwtAuthnAutoConfiguration implements InitializingBean {

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
		log.debug("JWT Login JwkSet KeyStore init.");
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
	DefaultJwtSigningAndValidationHandler jwtLoginValidationService(JWKSetKeyStore jwtLoginJwkSetKeyStore)
			throws NoSuchAlgorithmException, InvalidKeySpecException, JOSEException {
		DefaultJwtSigningAndValidationHandler jwtSignerValidationService =
				new DefaultJwtSigningAndValidationHandler(jwtLoginJwkSetKeyStore);
		jwtSignerValidationService.setDefaultSignerKeyId("dream_rsa");
		jwtSignerValidationService.setDefaultSigningAlgorithmName("RS256");
		log.debug("JWT Login Signing and Validation init.");
		return jwtSignerValidationService;
	}

	/**
	 * Jwt LoginService.
	 * 
	 * @return
	 */
	@Bean
	JwtLoginService jwtLoginService(DreamAuthLoginProperties dreamAuthLoginProperties,
			DefaultJwtSigningAndValidationHandler jwtLoginValidationService) {
		JwtLoginService jwtLoginService =
				new JwtLoginService(jwtLoginValidationService, dreamAuthLoginProperties.getJwt().getIssuer());
		log.debug("JWT Login Service init.");
		return jwtLoginService;
	}

	@Override
	public void afterPropertiesSet() throws Exception {

	}
}