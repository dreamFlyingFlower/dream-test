package com.wy.test.protocol.cas.endpoint.ticket.pgt;

import org.springframework.jdbc.core.JdbcTemplate;

import com.wy.test.core.enums.StoreType;
import com.wy.test.core.redis.RedisConnectionFactory;
import com.wy.test.protocol.cas.endpoint.ticket.TicketServices;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ProxyGrantingTicketServicesFactory {

	public TicketServices getService(StoreType storeType, JdbcTemplate jdbcTemplate,
			RedisConnectionFactory redisConnFactory) {
		TicketServices casTicketServices = null;
		if (StoreType.INMEMORY == storeType) {
			casTicketServices = new InMemoryProxyGrantingTicketServices();
			log.debug("InMemoryTicketServices");
		} else if (StoreType.JDBC == storeType) {
			// casTicketServices = new JdbcTicketServices(jdbcTemplate);
			log.debug("JdbcTicketServices not support ");
		} else if (StoreType.REDIS == storeType) {
			casTicketServices = new RedisProxyGrantingTicketServices(redisConnFactory);
			log.debug("RedisTicketServices");
		}
		return casTicketServices;
	}
}