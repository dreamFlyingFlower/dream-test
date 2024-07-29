package com.wy.test.autoconfigure;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.wy.test.persistence.service.UserInfoService;
import com.wy.test.provider.authn.realm.jdbc.JdbcAuthenticationRealm;

@AutoConfiguration
public class DreamMgtConfig implements InitializingBean {

	private static final Logger _logger = LoggerFactory.getLogger(DreamMgtConfig.class);

	// authenticationRealm for DreamMgtApplication
	@Bean
	JdbcAuthenticationRealm authenticationRealm(PasswordEncoder passwordEncoder,
			PasswordPolicyValidator passwordPolicyValidator, LoginRepository loginRepository,
			LoginHistoryRepository loginHistoryRepository, UserInfoService userInfoService, JdbcTemplate jdbcTemplate) {

		JdbcAuthenticationRealm authenticationRealm = new JdbcAuthenticationRealm(passwordEncoder,
				passwordPolicyValidator, loginRepository, loginHistoryRepository, userInfoService, jdbcTemplate);

		_logger.debug("JdbcAuthenticationRealm inited.");
		return authenticationRealm;
	}

	@Bean
	AbstractOtpAuthn timeBasedOtpAuthn() {
		AbstractOtpAuthn tfaOtpAuthn = new TimeBasedOtpAuthn();
		_logger.debug("TimeBasedOtpAuthn inited.");
		return tfaOtpAuthn;
	}

	@Override
	public void afterPropertiesSet() throws Exception {

	}

}
