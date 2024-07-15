/*
 * Copyright [2020] [MaxKey of copyright http://www.maxkey.top]
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

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
