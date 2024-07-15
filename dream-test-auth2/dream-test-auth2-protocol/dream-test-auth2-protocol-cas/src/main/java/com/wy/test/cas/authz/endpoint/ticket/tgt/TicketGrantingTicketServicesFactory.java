package com.wy.test.cas.authz.endpoint.ticket.tgt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import com.wy.test.cas.authz.endpoint.ticket.TicketServices;
import com.wy.test.constants.ConstsPersistence;
import com.wy.test.persistence.redis.RedisConnectionFactory;

public class TicketGrantingTicketServicesFactory {

	private static final Logger _logger = LoggerFactory.getLogger(TicketGrantingTicketServicesFactory.class);

	public TicketServices getService(int persistence, JdbcTemplate jdbcTemplate,
			RedisConnectionFactory redisConnFactory) {
		TicketServices casTicketServices = null;
		if (persistence == ConstsPersistence.INMEMORY) {
			casTicketServices = new InMemoryTicketGrantingTicketServices();
			_logger.debug("InMemoryTicketGrantingTicketServices");
		} else if (persistence == ConstsPersistence.JDBC) {
			//
			// casTicketServices = new JdbcTicketGrantingTicketServices(jdbcTemplate);
			_logger.debug("JdbcTicketGrantingTicketServices not support ");
		} else if (persistence == ConstsPersistence.REDIS) {
			casTicketServices = new RedisTicketGrantingTicketServices(redisConnFactory);
			_logger.debug("RedisTicketServices");
		}
		return casTicketServices;
	}
}
