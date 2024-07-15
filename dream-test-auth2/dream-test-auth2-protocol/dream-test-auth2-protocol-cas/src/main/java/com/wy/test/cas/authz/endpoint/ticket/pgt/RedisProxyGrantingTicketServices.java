package com.wy.test.cas.authz.endpoint.ticket.pgt;

import com.wy.test.cas.authz.endpoint.ticket.RandomServiceTicketServices;
import com.wy.test.cas.authz.endpoint.ticket.Ticket;
import com.wy.test.persistence.redis.RedisConnection;
import com.wy.test.persistence.redis.RedisConnectionFactory;

public class RedisProxyGrantingTicketServices extends RandomServiceTicketServices {

	protected int serviceTicketValiditySeconds = 60 * 60; // default 60 minutes.

	RedisConnectionFactory connectionFactory;

	public static String PREFIX = "MXK_CAS_TICKET_PGT_";

	/**
	 * @param connectionFactory
	 */
	public RedisProxyGrantingTicketServices(RedisConnectionFactory connectionFactory) {
		super();
		this.connectionFactory = connectionFactory;
	}

	/**
	 * 
	 */
	public RedisProxyGrantingTicketServices() {

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
