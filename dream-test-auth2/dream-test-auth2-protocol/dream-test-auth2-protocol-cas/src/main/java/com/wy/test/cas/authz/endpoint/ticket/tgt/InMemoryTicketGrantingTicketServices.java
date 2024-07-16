package com.wy.test.cas.authz.endpoint.ticket.tgt;

import java.util.concurrent.TimeUnit;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.wy.test.cas.authz.endpoint.ticket.RandomServiceTicketServices;
import com.wy.test.cas.authz.endpoint.ticket.Ticket;

public class InMemoryTicketGrantingTicketServices extends RandomServiceTicketServices {

	protected final static Cache<String, Ticket> casTicketGrantingTicketStore =
			Caffeine.newBuilder().expireAfterWrite(2, TimeUnit.DAYS).build();

	@Override
	public void store(String ticketId, Ticket ticket) {
		store(ticketId, ticket, 60 * 3);
	}

	@Override
	public void store(String ticketId, Ticket ticket, int validitySeconds) {
		casTicketGrantingTicketStore.put(ticketId, ticket);

	}

	@Override
	public Ticket remove(String ticketId) {
		Ticket ticket = casTicketGrantingTicketStore.getIfPresent(ticketId);
		casTicketGrantingTicketStore.invalidate(ticketId);
		return ticket;
	}

	@Override
	public Ticket get(String ticketId) {
		Ticket ticket = casTicketGrantingTicketStore.getIfPresent(ticketId);
		return ticket;
	}

}
