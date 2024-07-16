package com.wy.test.cas.authz.endpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.wy.test.authorize.endpoint.AuthorizeBaseEndpoint;
import com.wy.test.cas.authz.endpoint.ticket.TicketServices;
import com.wy.test.core.authn.session.SessionManager;
import com.wy.test.persistence.service.AppsCasDetailsService;
import com.wy.test.persistence.service.UserInfoService;
import com.wy.test.web.HttpRequestAdapter;
import com.wy.test.web.HttpResponseAdapter;

public class CasBaseAuthorizeEndpoint extends AuthorizeBaseEndpoint {

	final static Logger _logger = LoggerFactory.getLogger(CasBaseAuthorizeEndpoint.class);

	@Autowired
	protected AppsCasDetailsService casDetailsService;

	@Autowired
	protected UserInfoService userInfoService;

	@Autowired
	@Qualifier("casTicketServices")
	protected TicketServices ticketServices;

	@Autowired
	@Qualifier("casTicketGrantingTicketServices")
	protected TicketServices casTicketGrantingTicketServices;

	@Autowired
	protected SessionManager sessionManager;

	@Autowired
	@Qualifier("casProxyGrantingTicketServices")
	protected TicketServices casProxyGrantingTicketServices;

	@Autowired
	protected HttpResponseAdapter httpResponseAdapter;

	@Autowired
	protected HttpRequestAdapter httpRequestAdapter;

}
