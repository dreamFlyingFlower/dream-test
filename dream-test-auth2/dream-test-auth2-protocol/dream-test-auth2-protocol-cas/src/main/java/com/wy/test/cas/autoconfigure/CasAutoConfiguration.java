package com.wy.test.cas.autoconfigure;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jdbc.core.JdbcTemplate;

import com.wy.test.cas.authz.endpoint.ticket.TicketServices;
import com.wy.test.cas.authz.endpoint.ticket.pgt.ProxyGrantingTicketServicesFactory;
import com.wy.test.cas.authz.endpoint.ticket.st.TicketServicesFactory;
import com.wy.test.cas.authz.endpoint.ticket.tgt.TicketGrantingTicketServicesFactory;
import com.wy.test.core.persistence.redis.RedisConnectionFactory;

@AutoConfiguration
@ComponentScan(basePackages = { "org.dream.authz.cas.endpoint" })
public class CasAutoConfiguration implements InitializingBean {

	private static final Logger _logger = LoggerFactory.getLogger(CasAutoConfiguration.class);

	/**
	 * TicketServices.
	 * 
	 * @param persistence int
	 * @param validity int
	 * @return casTicketServices
	 */
	@Bean(name = "casTicketServices")
	TicketServices casTicketServices(@Value("${dreamserver.persistence}") int persistence,
			@Value("${dream.login.remeberme.validity}") int validity, JdbcTemplate jdbcTemplate,
			RedisConnectionFactory redisConnFactory) {
		_logger.debug("init casTicketServices.");
		return new TicketServicesFactory().getService(persistence, jdbcTemplate, redisConnFactory);
	}

	/**
	 * TicketServices.
	 * 
	 * @param persistence int
	 * @param validity int
	 * @return casTicketServices
	 */
	@Bean(name = "casTicketGrantingTicketServices")
	TicketServices casTicketGrantingTicketServices(@Value("${dream.server.persistence}") int persistence,
			@Value("${dream.login.remeberme.validity}") int validity, JdbcTemplate jdbcTemplate,
			RedisConnectionFactory redisConnFactory) {
		_logger.debug("init casTicketGrantingTicketServices.");
		return new TicketGrantingTicketServicesFactory().getService(persistence, jdbcTemplate, redisConnFactory);
	}

	@Bean(name = "casProxyGrantingTicketServices")
	TicketServices casProxyGrantingTicketServices(@Value("${dream.server.persistence}") int persistence,
			@Value("${dream.login.remeberme.validity}") int validity, JdbcTemplate jdbcTemplate,
			RedisConnectionFactory redisConnFactory) {
		_logger.debug("init casTicketGrantingTicketServices.");
		return new ProxyGrantingTicketServicesFactory().getService(persistence, jdbcTemplate, redisConnFactory);
	}

	@Override
	public void afterPropertiesSet() throws Exception {

	}
}
