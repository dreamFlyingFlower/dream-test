package com.wy.test.web.mgt.autoconfigure;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.wy.test.authentication.otp.password.onetimepwd.AbstractOtpAuthn;
import com.wy.test.authentication.otp.password.onetimepwd.impl.TimeBasedOtpAuthn;
import com.wy.test.authentication.provider.authn.realm.jdbc.JdbcAuthenticationRealm;
import com.wy.test.core.properties.DreamAuthLoginProperties;
import com.wy.test.core.repository.LoginHistoryRepository;
import com.wy.test.core.repository.PasswordPolicyValidator;
import com.wy.test.persistence.service.LoginService;
import com.wy.test.persistence.service.UserService;

import lombok.extern.slf4j.Slf4j;

@EnableConfigurationProperties(DreamAuthLoginProperties.class)
@AutoConfiguration
@Slf4j
public class DreamMgtConfig implements InitializingBean {

	// authenticationRealm for DreamMgtApplication
	@Bean
	JdbcAuthenticationRealm authenticationRealm(PasswordEncoder passwordEncoder,
			PasswordPolicyValidator passwordPolicyValidator, LoginService loginService,
			LoginHistoryRepository loginHistoryRepository, UserService userService, JdbcTemplate jdbcTemplate) {

		JdbcAuthenticationRealm authenticationRealm = new JdbcAuthenticationRealm(passwordEncoder,
				passwordPolicyValidator, loginService, loginHistoryRepository, userService, jdbcTemplate);

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