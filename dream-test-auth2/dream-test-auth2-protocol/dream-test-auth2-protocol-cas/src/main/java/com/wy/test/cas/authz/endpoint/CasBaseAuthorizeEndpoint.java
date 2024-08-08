package com.wy.test.cas.authz.endpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.wy.test.authorize.endpoint.AuthorizeBaseEndpoint;
import com.wy.test.cas.authz.endpoint.ticket.TicketServices;
import com.wy.test.core.authn.session.SessionManager;
import com.wy.test.core.web.HttpRequestAdapter;
import com.wy.test.core.web.HttpResponseAdapter;
import com.wy.test.persistence.service.AppCasDetailService;
import com.wy.test.persistence.service.UserService;

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
