package com.wy.test.cas.authz.endpoint.ticket.tgt;

import org.springframework.jdbc.core.JdbcTemplate;

import com.wy.test.cas.authz.endpoint.ticket.TicketServices;
import com.wy.test.core.enums.StoreType;
import com.wy.test.core.persistence.redis.RedisConnectionFactory;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TicketGrantingTicketServicesFactory {

	public TicketServices getService(StoreType storeType, JdbcTemplate jdbcTemplate,
			RedisConnectionFactory redisConnFactory) {
		TicketServices casTicketServices = null;
		if (StoreType.INMEMORY == storeType) {
			casTicketServices = new InMemoryTicketGrantingTicketServices();
			log.debug("InMemoryTicketGrantingTicketServices");
		} else if (StoreType.JDBC == storeType) {
			// casTicketServices = new JdbcTicketGrantingTicketServices(jdbcTemplate);
			log.debug("JdbcTicketGrantingTicketServices not support ");
		} else if (StoreType.REDIS == storeType) {
			casTicketServices = new RedisTicketGrantingTicketServices(redisConnFactory);
			log.debug("RedisTicketServices");
		}
		return casTicketServices;
	}
}