package com.wy.test.openapi.autoconfigure;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.wy.test.core.persistence.repository.LoginHistoryRepository;
import com.wy.test.core.persistence.repository.LoginRepository;
import com.wy.test.core.persistence.repository.PasswordPolicyValidator;
import com.wy.test.otp.password.onetimepwd.AbstractOtpAuthn;
import com.wy.test.otp.password.onetimepwd.impl.TimeBasedOtpAuthn;
import com.wy.test.persistence.service.UserService;
import com.wy.test.provider.authn.realm.jdbc.JdbcAuthenticationRealm;

import lombok.extern.slf4j.Slf4j;

@AutoConfiguration
@Slf4j
public class DreamOpenApiConfig implements InitializingBean {

	// authenticationRealm for DreamMgtApplication
	@Bean
	JdbcAuthenticationRealm authenticationRealm(PasswordEncoder passwordEncoder,
			PasswordPolicyValidator passwordPolicyValidator, LoginRepository loginRepository,
			LoginHistoryRepository loginHistoryRepository, UserService userService, JdbcTemplate jdbcTemplate) {

		JdbcAuthenticationRealm authenticationRealm = new JdbcAuthenticationRealm(passwordEncoder,
				passwordPolicyValidator, loginRepository, loginHistoryRepository, userService, jdbcTemplate);

		log.debug("JdbcAuthenticationRealm inited.");
		return authenticationRealm;
	}

	@Bean
	AbstractOtpAuthn timeBasedOtpAuthn() {
		AbstractOtpAuthn tfaOtpAuthn = new TimeBasedOtpAuthn();
		log.debug("TimeBasedOtpAuthn inited.");
		return tfaOtpAuthn;
	}

	@Override
	public void afterPropertiesSet() throws Exception {

	}
}