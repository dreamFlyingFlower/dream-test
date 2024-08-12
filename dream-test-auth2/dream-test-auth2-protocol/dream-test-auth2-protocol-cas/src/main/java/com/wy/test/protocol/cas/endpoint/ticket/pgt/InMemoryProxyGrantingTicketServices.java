package com.wy.test.protocol.cas.endpoint.ticket.pgt;

import java.util.concurrent.TimeUnit;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.wy.test.protocol.cas.endpoint.ticket.RandomServiceTicketServices;
import com.wy.test.protocol.cas.endpoint.ticket.Ticket;

public class InMemoryProxyGrantingTicketServices extends RandomServiceTicketServices {

	protected final static Cache<String, Ticket> casTicketStore =
			Caffeine.newBuilder().expireAfterWrite(60, TimeUnit.MINUTES).build();

	@Override
	public void store(String ticketId, Ticket ticket) {
		store(ticketId, ticket, 60 * 3);
	}

	@Override
	public void store(String ticketId, Ticket ticket, int validitySeconds) {
		casTicketStore.put(ticketId, ticket);
	}

	@Override
	public Ticket remove(String ticketId) {
		Ticket ticket = casTicketStore.getIfPresent(ticketId);
		casTicketStore.invalidate(ticketId);
		return ticket;
	}

	@Override
	public Ticket get(String ticket) {
		return casTicketStore.getIfPresent(ticket);
	}

}
