package com.wy.test.protocol.cas.endpoint.ticket.tgt;

import com.wy.test.core.redis.RedisConnection;
import com.wy.test.core.redis.RedisConnectionFactory;
import com.wy.test.protocol.cas.endpoint.ticket.RandomServiceTicketServices;
import com.wy.test.protocol.cas.endpoint.ticket.Ticket;

public class RedisTicketGrantingTicketServices extends RandomServiceTicketServices {

	protected int serviceTicketValiditySeconds = 60 * 60 * 24 * 2; // default 2 day.

	RedisConnectionFactory connectionFactory;

	public static String PREFIX = "REDIS_CAS_TICKET_TGT_";

	/**
	 * @param connectionFactory
	 */
	public RedisTicketGrantingTicketServices(RedisConnectionFactory connectionFactory) {
		super();
		this.connectionFactory = connectionFactory;
	}

	/**
	 * 
	 */
	public RedisTicketGrantingTicketServices() {

	}

	public void setConnectionFactory(RedisConnectionFactory connectionFactory) {
		this.connectionFactory = connectionFactory;
	}

	@Override
	public void store(String ticketId, Ticket ticket) {
		store(ticketId, ticket, serviceTicketValiditySeconds);
	}

	@Override
	public void store(String ticketId, Ticket ticket, int validitySeconds) {
		RedisConnection conn = connectionFactory.getConnection();
		conn.setexObject(PREFIX + ticketId, validitySeconds, ticket);
		conn.close();
	}

	@Override
	public Ticket remove(String ticketId) {
		RedisConnection conn = connectionFactory.getConnection();
		Ticket ticket = conn.getObject(PREFIX + ticketId);
		conn.delete(PREFIX + ticketId);
		conn.close();
		return ticket;
	}

	@Override
	public Ticket get(String ticketId) {
		RedisConnection conn = connectionFactory.getConnection();
		Ticket ticket = conn.getObject(PREFIX + ticketId);
		conn.close();
		return ticket;
	}

}
