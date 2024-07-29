package com.wy.test.core.autoconfigure;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;

import com.wy.test.core.authn.session.SessionManager;
import com.wy.test.core.authn.session.SessionManagerFactory;
import com.wy.test.core.authn.web.HttpSessionListenerAdapter;
import com.wy.test.core.authn.web.SavedRequestAwareAuthenticationSuccessHandler;
import com.wy.test.core.persistence.redis.RedisConnectionFactory;

@AutoConfiguration
public class SessionAutoConfiguration implements InitializingBean {

	private static final Logger _logger = LoggerFactory.getLogger(SessionAutoConfiguration.class);

	@Bean(name = "savedRequestSuccessHandler")
	SavedRequestAwareAuthenticationSuccessHandler savedRequestAwareAuthenticationSuccessHandler() {
		return new SavedRequestAwareAuthenticationSuccessHandler();
	}

	@Bean
	SessionManager sessionManager(@Value("${dream.server.persistence}") int persistence, JdbcTemplate jdbcTemplate,
			RedisConnectionFactory redisConnFactory, @Value("${dream.session.timeout:1800}") int timeout) {
		_logger.debug("session timeout " + timeout);
		SessionManager sessionManager = new SessionManagerFactory(persistence, jdbcTemplate, redisConnFactory, timeout);
		return sessionManager;
	}

	@Bean
	HttpSessionListenerAdapter httpSessionListenerAdapter() {
		return new HttpSessionListenerAdapter();
	}

	@Override
	public void afterPropertiesSet() throws Exception {

	}
}
