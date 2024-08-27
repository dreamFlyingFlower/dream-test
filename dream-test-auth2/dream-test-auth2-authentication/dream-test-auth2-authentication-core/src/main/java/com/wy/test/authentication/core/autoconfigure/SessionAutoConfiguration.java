package com.wy.test.authentication.core.autoconfigure;

import java.time.Duration;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;

import com.wy.test.authentication.core.authn.session.SessionManager;
import com.wy.test.authentication.core.authn.session.SessionManagerFactory;
import com.wy.test.authentication.core.authn.web.HttpSessionListenerAdapter;
import com.wy.test.authentication.core.authn.web.SavedRequestAwareAuthenticationSuccessHandler;
import com.wy.test.core.persistence.redis.RedisConnectionFactory;
import com.wy.test.core.properties.DreamAuthStoreProperties;

import lombok.extern.slf4j.Slf4j;

@AutoConfiguration
@EnableConfigurationProperties(DreamAuthStoreProperties.class)
@Slf4j
public class SessionAutoConfiguration implements InitializingBean {

	@Bean(name = "savedRequestSuccessHandler")
	SavedRequestAwareAuthenticationSuccessHandler savedRequestAwareAuthenticationSuccessHandler() {
		return new SavedRequestAwareAuthenticationSuccessHandler();
	}

	@Bean
	SessionManager sessionManager(DreamAuthStoreProperties dreamAuthRedisProperties, JdbcTemplate jdbcTemplate,
			RedisConnectionFactory redisConnFactory, ServerProperties serverProperties) {
		Duration duration = serverProperties.getServlet().getSession().getTimeout();
		log.debug("session timeout " + (null == duration ? null : duration.getSeconds()));
		SessionManager sessionManager = new SessionManagerFactory(dreamAuthRedisProperties.getStoreType(), jdbcTemplate,
				redisConnFactory, null == duration ? 1800 : (int) duration.getSeconds());
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