package com.wy.test.protocol.cas.endpoint.ticket.st;

import com.wy.test.core.persistence.redis.RedisConnection;
import com.wy.test.core.persistence.redis.RedisConnectionFactory;
import com.wy.test.protocol.cas.endpoint.ticket.RandomServiceTicketServices;
import com.wy.test.protocol.cas.endpoint.ticket.Ticket;

public class RedisTicketServices extends RandomServiceTicketServices {

	protected int serviceTicketValiditySeconds = 60 * 10; // default 10 minutes.

	RedisConnectionFactory connectionFactory;

	public static String PREFIX = "AUTH_CAS_TICKET_ST_";

	/**
	 * @param connectionFactory
	 */
	public RedisTicketServices(RedisConnectionFactory connectionFactory) {
		super();
		this.connectionFactory = connectionFactory;
	}

	/**
	 * 
	 */
	public RedisTicketServices() {

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
		conn.setexObject(prefixTicketId(ticketId), validitySeconds, ticket);
		conn.close();

	}

	@Override
	public Ticket remove(String ticketId) {
		RedisConnection conn = connectionFactory.getConnection();
		Ticket ticket = conn.getObject(prefixTicketId(ticketId));
		conn.delete(prefixTicketId(ticketId));
		conn.close();
		return ticket;
	}

	@Override
	public Ticket get(String ticketId) {
		RedisConnection conn = connectionFactory.getConnection();
		Ticket ticket = conn.getObject(prefixTicketId(ticketId));
		conn.close();
		return ticket;
	}

	public String prefixTicketId(String ticketId) {
		return PREFIX + ticketId;
	}

}
