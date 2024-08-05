package com.wy.test.cas.authz.endpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

	final static Logger _logger = LoggerFactory.getLogger(CasBaseAuthorizeEndpoint.class);

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
