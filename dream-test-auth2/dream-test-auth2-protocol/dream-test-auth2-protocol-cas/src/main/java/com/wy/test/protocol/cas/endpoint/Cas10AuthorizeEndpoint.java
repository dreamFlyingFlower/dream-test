package com.wy.test.protocol.cas.endpoint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wy.test.authentication.core.authn.SignPrincipal;
import com.wy.test.protocol.cas.endpoint.response.Service10ResponseBuilder;
import com.wy.test.protocol.cas.endpoint.ticket.CasConstants;
import com.wy.test.protocol.cas.endpoint.ticket.Ticket;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * https://apereo.github.io/cas/6.2.x/protocol/CAS-Protocol-Specification.html
 */
@Tag(name = "2-3-CAS API文档模块")
@Controller
@Slf4j
public class Cas10AuthorizeEndpoint extends CasBaseAuthorizeEndpoint {

	/**
	 * @param request
	 * @param response
	 * @param ticket
	 * @param service
	 * @param renew
	 * @return
	 * 
	 *         2.4. /validate [CAS 1.0] /validate checks the validity of a service ticket. /validate is part of the CAS
	 *         1.0 protocol and thus does not handle proxy authentication. CAS MUST respond with a ticket validation
	 *         failure response when a proxy ticket is passed to /validate.
	 * 
	 * 
	 *         2.4.1. parameters The following HTTP request parameters MAY be specified to /validate. They are case
	 *         sensitive and MUST all be handled by /validate.
	 * 
	 *         service [REQUIRED] - the identifier of the service for which the ticket was issued, as discussed in
	 *         Section 2.2.1. As a HTTP request parameter, the service value MUST be URL-encoded as described in Section
	 *         2.2 of RFC 1738 [4].
	 * 
	 *         Note: It is STRONGLY RECOMMENDED that all service urls be filtered via the service management tool, such
	 *         that only authorized and known client applications would be able to use the CAS server. Leaving the
	 *         service management tool open to allow lenient access to all applications will potentially increase the
	 *         risk of service attacks and other security vulnerabilities. Furthermore, it is RECOMMENDED that only
	 *         secure protocols such as https be allowed for client applications for further strengthen the
	 *         authenticating client.
	 * 
	 *         ticket [REQUIRED] - the service ticket issued by /login. Service tickets are described in Section 3.1.
	 * 
	 *         renew [OPTIONAL] - if this parameter is set, ticket validation will only succeed if the service ticket
	 *         was issued from the presentation of the user��s primary credentials. It will fail if the ticket was
	 *         issued from a single sign-on session.
	 * 
	 * 
	 *         2.4.2. response /validate will return one of the following two responses: On ticket validation success:
	 *         yes<LF> username<LF>
	 * 
	 *         On ticket validation failure: no<LF> <LF>
	 */
	@Operation(summary = "CAS 1.0 ticket验证接口", description = "通过ticket获取当前登录用户信息", method = "POST")
	@GetMapping(CasConstants.ENDPOINT.ENDPOINT_VALIDATE)
	@ResponseBody
	public String validate(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = CasConstants.PARAMETER.TICKET) String ticket,
			@RequestParam(value = CasConstants.PARAMETER.SERVICE) String service,
			@RequestParam(value = CasConstants.PARAMETER.RENEW, required = false) String renew) {
		log.debug("serviceValidate " + " ticket " + ticket + " , service " + service + " , renew " + renew);

		Ticket storedTicket = null;
		try {
			storedTicket = ticketServices.consumeTicket(ticket);
		} catch (Exception e) {
			log.error("consume Ticket error ", e);
		}

		if (storedTicket != null) {
			String principal = ((SignPrincipal) storedTicket.getAuthentication().getPrincipal()).getUsername();
			log.debug("principal " + principal);
			return new Service10ResponseBuilder().success().setUser(principal).serviceResponseBuilder();
		} else {
			log.debug("Ticket not found .");
			return new Service10ResponseBuilder().failure().serviceResponseBuilder();
		}
	}
}
