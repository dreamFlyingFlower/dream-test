package com.wy.test.cas.authz.endpoint.ticket;

import org.springframework.security.core.Authentication;

import com.wy.test.cas.authz.endpoint.ticket.pgt.ProxyTicket;
import com.wy.test.core.entity.AppCasDetailEntity;

/**
 * The {@link ProxyGrantingTicketImpl} is a concrete implementation of the
 * {@link ProxyTicket}.
 *
 * @author Misagh Moayyed
 * @since 4.2
 */
public class ProxyGrantingTicketImpl extends ServiceTicketImpl implements Ticket {

	private static final long serialVersionUID = -4469960563289285371L;

	/**
	 * Instantiates a new Proxy ticket.
	 */
	public ProxyGrantingTicketImpl() {
	}

	/**
	 * Instantiates a new Proxy ticket.
	 *
	 * @param id the id
	 * @param ticket the ticket
	 * @param service the service
	 * @param credentialProvided the credential that prompted this ticket. Could be
	 *        false.
	 * @param policy the expiration policy
	 */
	public ProxyGrantingTicketImpl(final String id, final Service service, final boolean credentialProvided) {

	}

	public ProxyGrantingTicketImpl(Authentication authentication, AppCasDetailEntity casDetails) {
		this.authentication = authentication;
		this.casDetails = casDetails;
	}
}
