package com.wy.test.protocol.cas.endpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.wy.test.authentication.core.session.SessionManager;
import com.wy.test.core.web.HttpRequestAdapter;
import com.wy.test.core.web.HttpResponseAdapter;
import com.wy.test.persistence.service.AppCasDetailService;
import com.wy.test.persistence.service.UserService;
import com.wy.test.protocol.authorize.endpoint.AuthorizeBaseEndpoint;
import com.wy.test.protocol.cas.endpoint.ticket.TicketServices;

public class CasBaseAuthorizeEndpoint extends AuthorizeBaseEndpoint {

	@Autowired
	protected AppCasDetailService appCasDetailService;

	@Autowired
	protected UserService userInfoService;

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
