package com.wy.test.protocol.cas.endpoint.ticket;

import java.time.ZonedDateTime;

import org.springframework.security.core.Authentication;

public interface TicketState {

	/**
	 * Returns the number of times a ticket was used.
	 *
	 * @return the number of times the ticket was used.
	 */
	int getCountOfUses();

	/**
	 * Returns the last time the ticket was used.
	 *
	 * @return the last time the ticket was used.
	 */
	ZonedDateTime getLastTimeUsed();

	/**
	 * Get the second to last time used.
	 *
	 * @return the previous time used.
	 */
	ZonedDateTime getPreviousTimeUsed();

	/**
	 * Get the time the ticket was created.
	 *
	 * @return the creation time of the ticket.
	 */
	ZonedDateTime getCreationTime();

	/**
	 * Authentication information from the ticket. This may be null.
	 *
	 * @return the authentication information.
	 */
	Authentication getAuthentication();

	/**
	 * Method to retrieve the TicketGrantingTicket that granted this ticket.
	 *
	 * @return the ticket or null if it has no parent
	 */
	TicketGrantingTicket getTicketGrantingTicket();

	/**
	 * Records the <i>previous</i> last time this ticket was used as well as the
	 * last usage time. The ticket usage count is also incremented.
	 * <p>
	 * Tickets themselves are solely responsible to maintain their state. The
	 * determination of ticket usage is left up to the implementation and the
	 * specific ticket type.
	 *
	 * @see ExpirationPolicy
	 * @since 5.0.0
	 */
	void update();
}
