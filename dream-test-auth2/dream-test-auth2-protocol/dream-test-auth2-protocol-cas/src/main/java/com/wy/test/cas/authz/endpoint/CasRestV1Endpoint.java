package com.wy.test.cas.authz.endpoint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.wy.test.cas.authz.endpoint.response.ServiceResponseBuilder;
import com.wy.test.cas.authz.endpoint.ticket.CasConstants;
import com.wy.test.cas.authz.endpoint.ticket.ServiceTicketImpl;
import com.wy.test.cas.authz.endpoint.ticket.TicketGrantingTicketImpl;
import com.wy.test.core.authn.LoginCredential;
import com.wy.test.core.authn.web.AuthorizationUtils;
import com.wy.test.core.entity.UserInfo;
import com.wy.test.core.entity.apps.AppsCasDetails;
import com.wy.test.core.web.HttpResponseConstants;
import com.wy.test.provider.authn.provider.AbstractAuthenticationProvider;
import com.wy.test.util.StringUtils;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * https://apereo.github.io/cas/6.2.x/protocol/REST-Protocol.html
 */
@Tag(name = "2-4-CAS REST API文档模块")
@Controller
public class CasRestV1Endpoint extends CasBaseAuthorizeEndpoint {

	final static Logger _logger = LoggerFactory.getLogger(CasRestV1Endpoint.class);

	@Autowired
	@Qualifier("authenticationProvider")
	AbstractAuthenticationProvider authenticationProvider;

	@Operation(summary = "CAS REST认证接口", description = "通过用户名密码获取TGT", method = "POST")
	@PostMapping(value = CasConstants.ENDPOINT.ENDPOINT_REST_TICKET_V1,
			consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseEntity<String> casLoginRestTickets(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = CasConstants.PARAMETER.SERVICE, required = false) String casService,
			@RequestParam(value = CasConstants.PARAMETER.REST_USERNAME, required = true) String username,
			@RequestParam(value = CasConstants.PARAMETER.REST_PASSWORD, required = true) String password) {
		try {
			if (StringUtils.isBlank(password)) {
				throw new BadCredentialsException(
						"No credentials are provided or extracted to authenticate the REST request");
			}

			LoginCredential loginCredential = new LoginCredential(username, password, "normal");

			// authenticationProvider.authenticate(loginCredential, false);
			Authentication authentication = authenticationProvider.authenticate(loginCredential);
			if (authentication == null) {
				_logger.debug("Bad Credentials Exception");
				return new ResponseEntity<>("Bad Credentials", HttpStatus.BAD_REQUEST);
			}

			TicketGrantingTicketImpl ticketGrantingTicket =
					new TicketGrantingTicketImpl("Random", AuthorizationUtils.getAuthentication(), null);

			String ticket = casTicketGrantingTicketServices.createTicket(ticketGrantingTicket);
			String location =
					applicationConfig.getServerPrefix() + CasConstants.ENDPOINT.ENDPOINT_REST_TICKET_V1 + "/" + ticket;
			HttpHeaders headers = new HttpHeaders();
			headers.add("location", location);
			_logger.trace("ticket {}", ticket);
			_logger.trace("location {}", location);
			return new ResponseEntity<>("Location: " + location, headers, HttpStatus.CREATED);

		} catch (final AuthenticationException e) {
			_logger.error("BadCredentialsException ", e);
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		} catch (final Exception e) {

			_logger.error("Exception ", e);
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Operation(summary = "CAS REST认证接口", description = "通过TGT获取ST", method = "POST")
	@PostMapping(value = CasConstants.ENDPOINT.ENDPOINT_REST_TICKET_V1 + "/{ticketGrantingTicket}",
			consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseEntity<String> requestServiceTicket(HttpServletRequest request, HttpServletResponse response,
			@PathVariable("ticketGrantingTicket") String ticketGrantingTicket,
			@RequestParam(value = CasConstants.PARAMETER.SERVICE, required = false) String casService,
			@RequestParam(value = CasConstants.PARAMETER.RENEW, required = false) String renew,
			@RequestParam(value = CasConstants.PARAMETER.REST_USERNAME, required = false) String username,
			@RequestParam(value = CasConstants.PARAMETER.REST_PASSWORD, required = false) String password) {
		try {
			TicketGrantingTicketImpl ticketGrantingTicketImpl =
					(TicketGrantingTicketImpl) casTicketGrantingTicketServices.get(ticketGrantingTicket);

			AppsCasDetails casDetails = casDetailsService.getAppDetails(casService, true);

			ServiceTicketImpl serviceTicket =
					new ServiceTicketImpl(ticketGrantingTicketImpl.getAuthentication(), casDetails);
			String ticket = ticketServices.createTicket(serviceTicket);
			return new ResponseEntity<>(ticket, HttpStatus.OK);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>("", HttpStatus.BAD_REQUEST);
	}

	@Operation(summary = "CAS REST认证接口", description = "检查TGT状态", method = "GET")
	@GetMapping(value = CasConstants.ENDPOINT.ENDPOINT_REST_TICKET_V1 + "/{ticketGrantingTicket}")
	public ResponseEntity<String> verifyTicketGrantingTicketStatus(
			@PathVariable("ticketGrantingTicket") String ticketGrantingTicket, HttpServletRequest request,
			HttpServletResponse response) {
		try {
			TicketGrantingTicketImpl ticketGrantingTicketImpl =
					(TicketGrantingTicketImpl) casTicketGrantingTicketServices.get(ticketGrantingTicket);
			if (ticketGrantingTicketImpl != null) {
				return new ResponseEntity<>("", HttpStatus.OK);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>("", HttpStatus.NOT_FOUND);
	}

	@Operation(summary = "CAS REST认证接口", description = "注销TGT状态", method = "DELETE")
	@DeleteMapping(value = CasConstants.ENDPOINT.ENDPOINT_REST_TICKET_V1 + "/{ticketGrantingTicket}")
	public ResponseEntity<String> destroyTicketGrantingTicket(
			@PathVariable("ticketGrantingTicket") String ticketGrantingTicket, HttpServletRequest request,
			HttpServletResponse response) {
		try {
			TicketGrantingTicketImpl ticketGrantingTicketImpl =
					(TicketGrantingTicketImpl) casTicketGrantingTicketServices.remove(ticketGrantingTicket);
			if (ticketGrantingTicketImpl != null) {
				return new ResponseEntity<>("", HttpStatus.OK);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>("", HttpStatus.NOT_FOUND);
	}

	@Operation(summary = "CAS REST认证接口", description = "用户名密码登录接口", method = "POST")
	@PostMapping(value = CasConstants.ENDPOINT.ENDPOINT_REST_USERS_V1,
			consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseEntity<String> casLoginRestUsers(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = CasConstants.PARAMETER.SERVICE, required = false) String casService,
			@RequestParam(value = CasConstants.PARAMETER.REST_USERNAME, required = true) String username,
			@RequestParam(value = CasConstants.PARAMETER.REST_PASSWORD, required = true) String password) {
		try {
			if (password == null || password.isEmpty()) {
				throw new BadCredentialsException(
						"No credentials are provided or extracted to authenticate the REST request");
			}

			LoginCredential loginCredential = new LoginCredential(username, password, "CASREST");

			authenticationProvider.authenticate(loginCredential, false);
			UserInfo userInfo = AuthorizationUtils.getUserInfo();
			TicketGrantingTicketImpl ticketGrantingTicket =
					new TicketGrantingTicketImpl("Random", AuthorizationUtils.getAuthentication(), null);

			String ticket = casTicketGrantingTicketServices.createTicket(ticketGrantingTicket);
			String location =
					applicationConfig.getServerPrefix() + CasConstants.ENDPOINT.ENDPOINT_REST_TICKET_V1 + ticket;
			HttpHeaders headers = new HttpHeaders();
			headers.add("location", location);
			ServiceResponseBuilder serviceResponseBuilder = new ServiceResponseBuilder();
			serviceResponseBuilder.setFormat(HttpResponseConstants.FORMAT_TYPE.JSON);
			// for user
			serviceResponseBuilder.setAttribute("userId", userInfo.getId());
			serviceResponseBuilder.setAttribute("displayName", userInfo.getDisplayName());
			serviceResponseBuilder.setAttribute("firstName", userInfo.getGivenName());
			serviceResponseBuilder.setAttribute("lastname", userInfo.getFamilyName());
			serviceResponseBuilder.setAttribute("mobile", userInfo.getMobile());
			serviceResponseBuilder.setAttribute("birthday", userInfo.getBirthDate());
			serviceResponseBuilder.setAttribute("gender", userInfo.getGender() + "");

			// for work
			serviceResponseBuilder.setAttribute("employeeNumber", userInfo.getEmployeeNumber());
			serviceResponseBuilder.setAttribute("title", userInfo.getJobTitle());
			serviceResponseBuilder.setAttribute("email", userInfo.getWorkEmail());
			serviceResponseBuilder.setAttribute("department", userInfo.getDepartment());
			serviceResponseBuilder.setAttribute("departmentId", userInfo.getDepartmentId());
			serviceResponseBuilder.setAttribute("workRegion", userInfo.getWorkRegion());

			serviceResponseBuilder.success().setUser(userInfo.getUsername());

			return new ResponseEntity<>(serviceResponseBuilder.serviceResponseBuilder(), headers, HttpStatus.OK);
		} catch (final AuthenticationException e) {
			_logger.error("BadCredentialsException ", e);
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		} catch (final Exception e) {

			_logger.error("Exception ", e);
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
