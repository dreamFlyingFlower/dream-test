package com.wy.test.protocol.cas.endpoint.ticket.st;

import org.springframework.jdbc.core.JdbcTemplate;

import com.wy.test.core.enums.StoreType;
import com.wy.test.core.redis.RedisConnectionFactory;
import com.wy.test.protocol.cas.endpoint.ticket.TicketServices;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TicketServicesFactory {

	public TicketServices getService(StoreType storeType, JdbcTemplate jdbcTemplate,
			RedisConnectionFactory redisConnFactory) {
		TicketServices casTicketServices = null;
		if (StoreType.INMEMORY == storeType) {
			casTicketServices = new InMemoryTicketServices();
			log.debug("InMemoryTicketServices");
		} else if (StoreType.JDBC == storeType) {
			// casTicketServices = new JdbcTicketServices(jdbcTemplate);
			log.debug("JdbcTicketServices not support ");
		} else if (StoreType.REDIS == storeType) {
			casTicketServices = new RedisTicketServices(redisConnFactory);
			log.debug("RedisTicketServices");
		}
		return casTicketServices;
	}
}