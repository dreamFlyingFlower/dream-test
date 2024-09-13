package com.wy.test.authentication.provider.autoconfigure;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;

import com.wy.test.authentication.core.jwt.AuthTokenService;
import com.wy.test.authentication.core.session.SessionManager;
import com.wy.test.authentication.provider.provider.AbstractAuthenticationProvider;
import com.wy.test.authentication.provider.provider.AuthenticationProviderFactory;
import com.wy.test.authentication.provider.provider.impl.MobileAuthenticationProvider;
import com.wy.test.authentication.provider.provider.impl.NormalAuthenticationProvider;
import com.wy.test.authentication.provider.provider.impl.TrustedAuthenticationProvider;
import com.wy.test.authentication.provider.realm.AbstractAuthenticationRealm;
import com.wy.test.authentication.provider.support.rememberme.AbstractRemeberMeManager;
import com.wy.test.authentication.provider.support.rememberme.JdbcRemeberMeManager;
import com.wy.test.authentication.sms.password.sms.SmsOtpAuthnService;
import com.wy.test.core.properties.DreamAuthLoginProperties;
import com.wy.test.core.properties.DreamAuthServerProperties;
import com.wy.test.core.repository.LoginHistoryRepository;
import com.wy.test.core.repository.PasswordPolicyValidator;

import lombok.extern.slf4j.Slf4j;

@AutoConfiguration
@Slf4j
public class AuthnProviderAutoConfiguration implements InitializingBean {

	@Bean
	AbstractAuthenticationProvider authenticationProvider(AbstractAuthenticationProvider normalAuthenticationProvider,
			AbstractAuthenticationProvider mobileAuthenticationProvider,
			AbstractAuthenticationProvider trustedAuthenticationProvider) {
		AuthenticationProviderFactory authenticationProvider = new AuthenticationProviderFactory();
		authenticationProvider.addAuthenticationProvider(normalAuthenticationProvider);
		authenticationProvider.addAuthenticationProvider(mobileAuthenticationProvider);
		authenticationProvider.addAuthenticationProvider(trustedAuthenticationProvider);

		return authenticationProvider;
	}

	@Bean
	AbstractAuthenticationProvider normalAuthenticationProvider(AbstractAuthenticationRealm authenticationRealm,
			DreamAuthServerProperties dreamServerProperties, SessionManager sessionManager,
			AuthTokenService authTokenService) {
		log.debug("init authentication Provider .");
		return new NormalAuthenticationProvider(authenticationRealm, dreamServerProperties, sessionManager,
				authTokenService);
	}

	@Bean
	AbstractAuthenticationProvider mobileAuthenticationProvider(AbstractAuthenticationRealm authenticationRealm,
			DreamAuthServerProperties dreamServerProperties, SmsOtpAuthnService smsAuthnService,
			SessionManager sessionManager) {
		log.debug("init Mobile authentication Provider .");
		return new MobileAuthenticationProvider(authenticationRealm, dreamServerProperties, smsAuthnService,
				sessionManager);
	}

	@Bean
	AbstractAuthenticationProvider trustedAuthenticationProvider(AbstractAuthenticationRealm authenticationRealm,
			DreamAuthServerProperties dreamServerProperties, SessionManager sessionManager) {
		log.debug("init Mobile authentication Provider .");
		return new TrustedAuthenticationProvider(authenticationRealm, dreamServerProperties, sessionManager);
	}

	@Bean
	@ConditionalOnMissingBean
	PasswordPolicyValidator passwordPolicyValidator(JdbcTemplate jdbcTemplate, MessageSource messageSource) {
		return new PasswordPolicyValidator(jdbcTemplate, messageSource);
	}

	@Bean
	@ConditionalOnMissingBean
	LoginHistoryRepository loginHistoryRepository(JdbcTemplate jdbcTemplate) {
		return new LoginHistoryRepository(jdbcTemplate);
	}

	/**
	 * remeberMeService .
	 * 
	 * @return
	 */
	@Bean
	AbstractRemeberMeManager remeberMeManager(DreamAuthLoginProperties dreamLoginProperties,
			AuthTokenService authTokenService, JdbcTemplate jdbcTemplate) {
		log.trace("init RemeberMeManager , validity {}.", dreamLoginProperties.getRememberMeValidity());
		return new JdbcRemeberMeManager(jdbcTemplate, dreamLoginProperties, authTokenService,
				dreamLoginProperties.getRememberMeValidity());
	}

	@Override
	public void afterPropertiesSet() throws Exception {

	}
}