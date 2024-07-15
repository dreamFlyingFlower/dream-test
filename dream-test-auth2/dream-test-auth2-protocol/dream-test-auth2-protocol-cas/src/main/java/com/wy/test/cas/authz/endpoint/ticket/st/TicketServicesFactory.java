package com.wy.test.cas.authz.endpoint.ticket.st;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import com.wy.test.cas.authz.endpoint.ticket.TicketServices;
import com.wy.test.constants.ConstsPersistence;
import com.wy.test.persistence.redis.RedisConnectionFactory;

public class TicketServicesFactory {

	private static final Logger _logger = LoggerFactory.getLogger(TicketServicesFactory.class);

	public TicketServices getService(int persistence, JdbcTemplate jdbcTemplate,
			RedisConnectionFactory redisConnFactory) {
		TicketServices casTicketServices = null;
		if (persistence == ConstsPersistence.INMEMORY) {
			casTicketServices = new InMemoryTicketServices();
			_logger.debug("InMemoryTicketServices");
		} else if (persistence == ConstsPersistence.JDBC) {
			// casTicketServices = new JdbcTicketServices(jdbcTemplate);
			_logger.debug("JdbcTicketServices not support ");
		} else if (persistence == ConstsPersistence.REDIS) {
			casTicketServices = new RedisTicketServices(redisConnFactory);
			_logger.debug("RedisTicketServices");
		}
		return casTicketServices;
	}
}
