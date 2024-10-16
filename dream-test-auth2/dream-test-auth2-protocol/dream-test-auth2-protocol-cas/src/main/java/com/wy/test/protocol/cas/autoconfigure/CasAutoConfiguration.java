package com.wy.test.protocol.cas.autoconfigure;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jdbc.core.JdbcTemplate;

import com.wy.test.core.properties.DreamAuthStoreProperties;
import com.wy.test.core.redis.RedisConnectionFactory;
import com.wy.test.protocol.cas.endpoint.ticket.TicketServices;
import com.wy.test.protocol.cas.endpoint.ticket.pgt.ProxyGrantingTicketServicesFactory;
import com.wy.test.protocol.cas.endpoint.ticket.st.TicketServicesFactory;
import com.wy.test.protocol.cas.endpoint.ticket.tgt.TicketGrantingTicketServicesFactory;

import lombok.extern.slf4j.Slf4j;

@AutoConfiguration
@ComponentScan(basePackages = { "com.wy.test.protocol.cas.endpoint" })
@Slf4j
public class CasAutoConfiguration implements InitializingBean {

	@Bean(name = "casTicketServices")
	TicketServices casTicketServices(DreamAuthStoreProperties dreamAuthStoreProperties, JdbcTemplate jdbcTemplate,
			RedisConnectionFactory redisConnFactory) {
		log.debug("init casTicketServices.");
		return new TicketServicesFactory().getService(dreamAuthStoreProperties.getStoreType(), jdbcTemplate,
				redisConnFactory);
	}

	@Bean(name = "casTicketGrantingTicketServices")
	TicketServices casTicketGrantingTicketServices(DreamAuthStoreProperties dreamAuthStoreProperties,
			JdbcTemplate jdbcTemplate, RedisConnectionFactory redisConnFactory) {
		log.debug("init casTicketGrantingTicketServices.");
		return new TicketGrantingTicketServicesFactory().getService(dreamAuthStoreProperties.getStoreType(),
				jdbcTemplate, redisConnFactory);
	}

	@Bean(name = "casProxyGrantingTicketServices")
	TicketServices casProxyGrantingTicketServices(DreamAuthStoreProperties dreamAuthStoreProperties,
			JdbcTemplate jdbcTemplate, RedisConnectionFactory redisConnFactory) {
		log.debug("init casTicketGrantingTicketServices.");
		return new ProxyGrantingTicketServicesFactory().getService(dreamAuthStoreProperties.getStoreType(),
				jdbcTemplate, redisConnFactory);
	}

	@Override
	public void afterPropertiesSet() throws Exception {

	}
}