package com.wy.test.provider.autoconfigure;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;

import com.wy.test.core.authn.jwt.AuthTokenService;
import com.wy.test.core.authn.session.SessionManager;
import com.wy.test.core.persistence.repository.LoginHistoryRepository;
import com.wy.test.core.persistence.repository.LoginRepository;
import com.wy.test.core.persistence.repository.PasswordPolicyValidator;
import com.wy.test.core.properties.DreamLoginProperties;
import com.wy.test.core.properties.DreamServerProperties;
import com.wy.test.provider.authn.provider.AbstractAuthenticationProvider;
import com.wy.test.provider.authn.provider.AuthenticationProviderFactory;
import com.wy.test.provider.authn.provider.impl.MobileAuthenticationProvider;
import com.wy.test.provider.authn.provider.impl.NormalAuthenticationProvider;
import com.wy.test.provider.authn.provider.impl.TrustedAuthenticationProvider;
import com.wy.test.provider.authn.realm.AbstractAuthenticationRealm;
import com.wy.test.provider.authn.support.rememberme.AbstractRemeberMeManager;
import com.wy.test.provider.authn.support.rememberme.JdbcRemeberMeManager;
import com.wy.test.sms.password.sms.SmsOtpAuthnService;

@AutoConfiguration
public class AuthnProviderAutoConfiguration implements InitializingBean {

	private static final Logger _logger = LoggerFactory.getLogger(AuthnProviderAutoConfiguration.class);

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
			DreamServerProperties dreamServerProperties, SessionManager sessionManager,
			AuthTokenService authTokenService) {
		_logger.debug("init authentication Provider .");
		return new NormalAuthenticationProvider(authenticationRealm, dreamServerProperties, sessionManager,
				authTokenService);
	}

	@Bean
	AbstractAuthenticationProvider mobileAuthenticationProvider(AbstractAuthenticationRealm authenticationRealm,
			DreamServerProperties dreamServerProperties, SmsOtpAuthnService smsAuthnService,
			SessionManager sessionManager) {
		_logger.debug("init Mobile authentication Provider .");
		return new MobileAuthenticationProvider(authenticationRealm, dreamServerProperties, smsAuthnService,
				sessionManager);
	}

	@Bean
	AbstractAuthenticationProvider trustedAuthenticationProvider(AbstractAuthenticationRealm authenticationRealm,
			DreamServerProperties dreamServerProperties, SessionManager sessionManager) {
		_logger.debug("init Mobile authentication Provider .");
		return new TrustedAuthenticationProvider(authenticationRealm, dreamServerProperties, sessionManager);
	}

	@Bean
	PasswordPolicyValidator passwordPolicyValidator(JdbcTemplate jdbcTemplate, MessageSource messageSource) {
		return new PasswordPolicyValidator(jdbcTemplate, messageSource);
	}

	@Bean
	LoginRepository loginRepository(JdbcTemplate jdbcTemplate) {
		return new LoginRepository(jdbcTemplate);
	}

	@Bean
	LoginHistoryRepository loginHistoryRepository(JdbcTemplate jdbcTemplate) {
		return new LoginHistoryRepository(jdbcTemplate);
	}

	/**
	 * remeberMeService .
	 * 
	 * @return
	 */
	@Bean
	AbstractRemeberMeManager remeberMeManager(@Value("${dream.server.persistence}") int persistence,
			@Value("${dream.login.remeberme.validity}") int validity, DreamLoginProperties dreamLoginProperties,
			AuthTokenService authTokenService, JdbcTemplate jdbcTemplate) {
		_logger.trace("init RemeberMeManager , validity {}.", validity);
		return new JdbcRemeberMeManager(jdbcTemplate, dreamLoginProperties, authTokenService, validity);
	}

	@Override
	public void afterPropertiesSet() throws Exception {

	}
}