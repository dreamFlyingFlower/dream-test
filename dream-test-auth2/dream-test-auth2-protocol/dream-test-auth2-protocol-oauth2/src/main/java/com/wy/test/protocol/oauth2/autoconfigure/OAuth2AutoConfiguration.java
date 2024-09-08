package com.wy.test.protocol.oauth2.autoconfigure;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.servlet.Filter;
import javax.sql.DataSource;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWEAlgorithm;
import com.wy.test.core.entity.oidc.OidcProviderMetadataDetail;
import com.wy.test.core.persistence.redis.RedisConnectionFactory;
import com.wy.test.core.properties.DreamAuthOidcProperties;
import com.wy.test.core.properties.DreamAuthStoreProperties;
import com.wy.test.persistence.service.LoginService;
import com.wy.test.protocol.oauth2.common.OAuth2Constants;
import com.wy.test.protocol.oauth2.oidc.OidcIdTokenEnhancer;
import com.wy.test.protocol.oauth2.provider.ClientDetailsService;
import com.wy.test.protocol.oauth2.provider.OAuth2UserDetailsService;
import com.wy.test.protocol.oauth2.provider.approval.TokenApprovalStore;
import com.wy.test.protocol.oauth2.provider.approval.endpoint.OAuth20UserApprovalHandler;
import com.wy.test.protocol.oauth2.provider.client.ClientDetailsUserDetailsService;
import com.wy.test.protocol.oauth2.provider.client.JdbcClientDetailsService;
import com.wy.test.protocol.oauth2.provider.code.AuthorizationCodeServices;
import com.wy.test.protocol.oauth2.provider.code.AuthorizationCodeServicesFactory;
import com.wy.test.protocol.oauth2.provider.endpoint.TokenEndpointAuthenticationFilter;
import com.wy.test.protocol.oauth2.provider.request.DefaultOAuth2RequestFactory;
import com.wy.test.protocol.oauth2.provider.token.DefaultTokenServices;
import com.wy.test.protocol.oauth2.provider.token.TokenStore;
import com.wy.test.protocol.oauth2.provider.token.store.JwtAccessTokenConverter;
import com.wy.test.protocol.oauth2.provider.token.store.TokenStoreFactory;

import dream.flying.flower.framework.web.crypto.jose.keystore.JWKSetKeyStore;
import dream.flying.flower.framework.web.crypto.jwt.encryption.DefaultJwtEncryptionAndDecryptionHandler;
import dream.flying.flower.framework.web.crypto.jwt.sign.DefaultJwtSigningAndValidationHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * 参照OAuth2AuthorizationServerConfiguration
 *
 * @author 飞花梦影
 * @date 2024-09-08 16:39:00
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@AutoConfiguration
@ComponentScan(
		basePackages = { "com.wy.authz.oauth2.provider.endpoint", "com.wy.authz.oauth2.provider.userinfo.endpoint",
				"com.wy.authz.oauth2.provider.approval.controller", "com.wy.authz.oauth2.provider.wellknown.endpoint" })
@EnableConfigurationProperties(DreamAuthOidcProperties.class)
@Slf4j
public class OAuth2AutoConfiguration implements InitializingBean {

	@Bean
	FilterRegistrationBean<Filter> tokenEndpointAuthenticationFilter() {
		log.debug("TokenEndpointAuthenticationFilter init ");
		FilterRegistrationBean<Filter> registration = new FilterRegistrationBean<Filter>();
		registration.setFilter(new TokenEndpointAuthenticationFilter());
		registration.addUrlPatterns(OAuth2Constants.ENDPOINT.ENDPOINT_TOKEN + "/*",
				OAuth2Constants.ENDPOINT.ENDPOINT_TENCENT_IOA_TOKEN + "/*");
		registration.setName("TokenEndpointAuthenticationFilter");
		registration.setOrder(1);
		return registration;
	}

	/**
	 * OIDCProviderMetadataDetails. Self-issued Provider Metadata
	 * http://openid.net/specs/openid-connect-core-1_0.html#SelfIssued
	 */
	@Bean(name = "oidcProviderMetadata")
	OidcProviderMetadataDetail oidcProviderMetadataDetails(DreamAuthOidcProperties dreamAuthOidcProperties) {
		log.debug("OIDC Provider Metadata Details init .");
		OidcProviderMetadataDetail oidcProviderMetadata = new OidcProviderMetadataDetail();
		oidcProviderMetadata.setIssuer(dreamAuthOidcProperties.getMetadata().getIssuer());
		oidcProviderMetadata.setAuthorizationEndpoint(dreamAuthOidcProperties.getMetadata().getAuthorizationEndpoint());
		oidcProviderMetadata.setTokenEndpoint(dreamAuthOidcProperties.getMetadata().getTokenEndpoint());
		oidcProviderMetadata.setUserinfoEndpoint(dreamAuthOidcProperties.getMetadata().getUserinfoEndpoint());
		return oidcProviderMetadata;
	}

	/**
	 * jwtSetKeyStore.
	 * 
	 * @return
	 */
	@Bean(name = "jwkSetKeyStore")
	JWKSetKeyStore jwkSetKeyStore() {
		JWKSetKeyStore jwkSetKeyStore = new JWKSetKeyStore();
		ClassPathResource classPathResource = new ClassPathResource("/config/keystore.jwks");
		jwkSetKeyStore.setLocation(classPathResource);
		log.debug("JWKSet KeyStore init.");
		return jwkSetKeyStore;
	}

	/**
	 * jwtSetKeyStore.
	 * 
	 * @return
	 * @throws JOSEException
	 * @throws InvalidKeySpecException
	 * @throws NoSuchAlgorithmException
	 */
	@Bean(name = "jwtSignerValidationService")
	DefaultJwtSigningAndValidationHandler jwtSignerValidationService(JWKSetKeyStore jwkSetKeyStore)
			throws NoSuchAlgorithmException, InvalidKeySpecException, JOSEException {
		DefaultJwtSigningAndValidationHandler jwtSignerValidationService =
				new DefaultJwtSigningAndValidationHandler(jwkSetKeyStore);
		jwtSignerValidationService.setDefaultSignerKeyId("dream_rsa");
		jwtSignerValidationService.setDefaultSigningAlgorithmName("RS256");
		log.debug("JWT Signer and Validation Service init.");
		return jwtSignerValidationService;
	}

	/**
	 * jwtSetKeyStore.
	 * 
	 * @return
	 * @throws JOSEException
	 * @throws InvalidKeySpecException
	 * @throws NoSuchAlgorithmException
	 */
	@Bean(name = "jwtEncryptionService")
	DefaultJwtEncryptionAndDecryptionHandler jwtEncryptionService(JWKSetKeyStore jwkSetKeyStore)
			throws NoSuchAlgorithmException, InvalidKeySpecException, JOSEException {
		DefaultJwtEncryptionAndDecryptionHandler jwtEncryptionService =
				new DefaultJwtEncryptionAndDecryptionHandler(jwkSetKeyStore);
		jwtEncryptionService.setDefaultAlgorithm(JWEAlgorithm.RSA_OAEP_256);// RSA1_5
		jwtEncryptionService.setDefaultDecryptionKeyId("dream_rsa");
		jwtEncryptionService.setDefaultEncryptionKeyId("dream_rsa");
		log.debug("JWT Encryption and Decryption Service init.");
		return jwtEncryptionService;
	}

	/**
	 * tokenEnhancer.
	 * 
	 * @return
	 */
	@Bean(name = "tokenEnhancer")
	OidcIdTokenEnhancer tokenEnhancer(OidcProviderMetadataDetail oidcProviderMetadata,
			ClientDetailsService oauth20JdbcClientDetailsService) {
		OidcIdTokenEnhancer tokenEnhancer = new OidcIdTokenEnhancer();
		tokenEnhancer.setClientDetailsService(oauth20JdbcClientDetailsService);
		tokenEnhancer.setProviderMetadata(oidcProviderMetadata);
		log.debug("OIDC IdToken Enhancer init.");
		return tokenEnhancer;
	}
	// 以上部分为了支持OpenID Connect 1.0

	/**
	 * AuthorizationCodeServices.
	 * 
	 * @param persistence int
	 * @return oauth20AuthorizationCodeServices
	 */
	@Bean(name = "oauth20AuthorizationCodeServices")
	AuthorizationCodeServices oauth20AuthorizationCodeServices(DreamAuthStoreProperties dreamAuthStoreProperties,
			JdbcTemplate jdbcTemplate, RedisConnectionFactory redisConnFactory) {
		log.debug("OAuth 2 Authorization Code Services init.");
		return new AuthorizationCodeServicesFactory().getService(dreamAuthStoreProperties.getStoreType(), jdbcTemplate,
				redisConnFactory);
	}

	/**
	 * TokenStore.
	 * 
	 * @param persistence int
	 * @return oauth20TokenStore
	 */
	@Bean(name = "oauth20TokenStore")
	TokenStore oauth20TokenStore(DreamAuthStoreProperties dreamAuthStoreProperties, JdbcTemplate jdbcTemplate,
			RedisConnectionFactory redisConnFactory) {
		log.debug("OAuth 2 TokenStore init.");
		return new TokenStoreFactory().getTokenStore(dreamAuthStoreProperties.getStoreType(), jdbcTemplate,
				redisConnFactory);
	}

	/**
	 * jwtAccessTokenConverter.
	 * 
	 * @return converter
	 */
	@Bean(name = "converter")
	JwtAccessTokenConverter jwtAccessTokenConverter() {
		JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
		log.debug("OAuth 2 Jwt AccessToken Converter init.");
		return jwtAccessTokenConverter;
	}

	/**
	 * clientDetailsService.
	 * 
	 * @return oauth20JdbcClientDetailsService
	 */
	@Bean(name = "oauth20JdbcClientDetailsService")
	JdbcClientDetailsService jdbcClientDetailsService(DataSource dataSource, PasswordEncoder passwordReciprocal) {
		JdbcClientDetailsService clientDetailsService = new JdbcClientDetailsService(dataSource);
		// clientDetailsService.setPasswordEncoder(passwordReciprocal);
		log.debug("OAuth 2 Jdbc ClientDetails Service init.");
		return clientDetailsService;
	}

	/**
	 * clientDetailsUserDetailsService.
	 * 
	 * @return oauth20TokenServices
	 */
	@Bean(name = "oauth20TokenServices")
	DefaultTokenServices defaultTokenServices(JdbcClientDetailsService oauth20JdbcClientDetailsService,
			TokenStore oauth20TokenStore, OidcIdTokenEnhancer tokenEnhancer) {
		DefaultTokenServices tokenServices = new DefaultTokenServices();
		tokenServices.setClientDetailsService(oauth20JdbcClientDetailsService);
		tokenServices.setTokenEnhancer(tokenEnhancer);
		tokenServices.setTokenStore(oauth20TokenStore);
		tokenServices.setSupportRefreshToken(true);
		log.debug("OAuth 2 Token Services init.");
		return tokenServices;
	}

	/**
	 * TokenApprovalStore.
	 * 
	 * @return oauth20ApprovalStore
	 */
	@Bean(name = "oauth20ApprovalStore")
	TokenApprovalStore tokenApprovalStore(TokenStore oauth20TokenStore) {
		TokenApprovalStore tokenApprovalStore = new TokenApprovalStore();
		tokenApprovalStore.setTokenStore(oauth20TokenStore);
		log.debug("OAuth 2 Approval Store init.");
		return tokenApprovalStore;
	}

	/**
	 * OAuth2RequestFactory.
	 * 
	 * @return oAuth2RequestFactory
	 */
	@Bean(name = "oAuth2RequestFactory")
	DefaultOAuth2RequestFactory oauth2RequestFactory(JdbcClientDetailsService oauth20JdbcClientDetailsService) {
		DefaultOAuth2RequestFactory oauth2RequestFactory =
				new DefaultOAuth2RequestFactory(oauth20JdbcClientDetailsService);
		log.debug("OAuth 2 Request Factory init.");
		return oauth2RequestFactory;
	}

	/**
	 * OAuth20UserApprovalHandler.
	 * 
	 * @return oauth20UserApprovalHandler
	 */
	@Bean(name = "oauth20UserApprovalHandler")
	OAuth20UserApprovalHandler oauth20UserApprovalHandler(JdbcClientDetailsService oauth20JdbcClientDetailsService,
			DefaultOAuth2RequestFactory oAuth2RequestFactory, TokenApprovalStore oauth20ApprovalStore) {
		OAuth20UserApprovalHandler userApprovalHandler = new OAuth20UserApprovalHandler();
		userApprovalHandler.setApprovalStore(oauth20ApprovalStore);
		userApprovalHandler.setRequestFactory(oAuth2RequestFactory);
		userApprovalHandler.setClientDetailsService(oauth20JdbcClientDetailsService);
		log.debug("OAuth 2 User Approval Handler init.");
		return userApprovalHandler;
	}

	/**
	 * 本地用户登录处理器
	 * 
	 * @return oauth2UserAuthenticationManager
	 */
	@Bean(name = "oauth2UserAuthenticationManager")
	@Primary
	ProviderManager oauth2UserAuthenticationManager(PasswordEncoder passwordEncoder, LoginService loginService) {

		OAuth2UserDetailsService userDetailsService = new OAuth2UserDetailsService();
		userDetailsService.setLoginService(loginService);

		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
		daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
		daoAuthenticationProvider.setUserDetailsService(userDetailsService);
		ProviderManager authenticationManager = new ProviderManager(daoAuthenticationProvider);
		log.debug("OAuth 2 User Authentication Manager init.");
		return authenticationManager;
	}

	/**
	 * OAuth2客户端登录处理器
	 * 
	 * @return oauth2ClientAuthenticationManager
	 */
	@Bean(name = "oauth2ClientAuthenticationManager")
	ProviderManager oauth2ClientAuthenticationManager(JdbcClientDetailsService oauth20JdbcClientDetailsService,
			PasswordEncoder passwordReciprocal) {

		ClientDetailsUserDetailsService cientDetailsUserDetailsService =
				new ClientDetailsUserDetailsService(oauth20JdbcClientDetailsService);

		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
		daoAuthenticationProvider.setPasswordEncoder(passwordReciprocal);
		daoAuthenticationProvider.setUserDetailsService(cientDetailsUserDetailsService);
		ProviderManager authenticationManager = new ProviderManager(daoAuthenticationProvider);
		log.debug("OAuth 2 Client Authentication Manager init.");

		// FIXME 改造成多个manager
		// List<AuthenticationProvider> providers = authenticationManager.getProviders();
		// providers.add(daoAuthenticationProvider);
		//
		// ProviderManager providerManager = new ProviderManager(providers);

		return authenticationManager;
	}

	@Override
	public void afterPropertiesSet() throws Exception {

	}
}