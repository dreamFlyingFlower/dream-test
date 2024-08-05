package com.wy.test.cas.authz.endpoint;

import java.security.Principal;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.wy.test.authorize.singlelogout.LogoutType;
import com.wy.test.cas.authz.endpoint.ticket.CasConstants;
import com.wy.test.cas.authz.endpoint.ticket.ServiceTicketImpl;
import com.wy.test.core.authn.session.Session;
import com.wy.test.core.authn.web.AuthorizationUtils;
import com.wy.test.core.vo.AppCasDetailVO;
import com.wy.test.core.web.WebConstants;
import com.wy.test.core.web.WebContext;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * https://apereo.github.io/cas/6.2.x/protocol/CAS-Protocol.html
 */
@Tag(name = "2-3-CAS API文档模块")
@Controller
public class CasAuthorizeEndpoint extends CasBaseAuthorizeEndpoint {

	final static Logger _logger = LoggerFactory.getLogger(CasAuthorizeEndpoint.class);

	@Operation(summary = "CAS页面跳转service认证接口", description = "传递参数service", method = "GET")
	@GetMapping(CasConstants.ENDPOINT.ENDPOINT_LOGIN)
	public ModelAndView casLogin(
			@RequestParam(value = CasConstants.PARAMETER.SERVICE, required = false) String casService,
			HttpServletRequest request, HttpServletResponse response) {

		AppCasDetailVO casDetails = appCasDetailService.getAppDetails(casService, true);

		return buildCasModelAndView(request, response, casDetails, casService);
	}

	@Operation(summary = "CAS页面跳转应用ID认证接口", description = "传递参数应用ID", method = "GET")
	@GetMapping(CasConstants.ENDPOINT.ENDPOINT_BASE + "/{id}")
	public ModelAndView authorize(@PathVariable("id") String id, HttpServletRequest request,
			HttpServletResponse response) {
		AppCasDetailVO casDetails = appCasDetailService.getAppDetails(id, true);
		return buildCasModelAndView(request, response, casDetails,
				casDetails == null ? id : casDetails.getCallbackUrl());
	}

	private ModelAndView buildCasModelAndView(HttpServletRequest request, HttpServletResponse response,
			AppCasDetailVO casDetails, String casService) {
		if (casDetails == null) {
			_logger.debug("service {} not registered  ", casService);
			ModelAndView modelAndView = new ModelAndView("authorize/cas_sso_submint");
			modelAndView.addObject("errorMessage", casService);
			return modelAndView;
		}

		_logger.debug("Detail {}", casDetails);
		Map<String, String> parameterMap = WebContext.getRequestParameterMap(request);
		String service = casService;
		_logger.debug("CAS Parameter service = {}", service);
		if (casService.indexOf("?") > -1) {
			service = casService.substring(casService.indexOf("?") + 1);
			if (service.indexOf("=") > -1) {
				String[] parameterValues = service.split("=");
				if (parameterValues.length == 2) {
					parameterMap.put(parameterValues[0], parameterValues[1]);
				}
			}
			_logger.debug("CAS service with Parameter : {}", parameterMap);
		}
		WebContext.setAttribute(CasConstants.PARAMETER.PARAMETER_MAP, parameterMap);
		WebContext.setAttribute(CasConstants.PARAMETER.ENDPOINT_CAS_DETAILS, casDetails);
		WebContext.setAttribute(WebConstants.SINGLE_SIGN_ON_APP_ID, casDetails.getId());
		WebContext.setAttribute(WebConstants.AUTHORIZE_SIGN_ON_APP, casDetails);
		return WebContext.redirect(CasConstants.ENDPOINT.ENDPOINT_SERVICE_TICKET_GRANTING);

	}

	@GetMapping(CasConstants.ENDPOINT.ENDPOINT_SERVICE_TICKET_GRANTING)
	public ModelAndView grantingTicket(Principal principal, HttpServletRequest request, HttpServletResponse response) {
		ModelAndView modelAndView = new ModelAndView("authorize/cas_sso_submint");
		AppCasDetailVO casDetails =
				(AppCasDetailVO) WebContext.getAttribute(CasConstants.PARAMETER.ENDPOINT_CAS_DETAILS);

		ServiceTicketImpl serviceTicket = new ServiceTicketImpl(AuthorizationUtils.getAuthentication(), casDetails);

		_logger.trace("CAS start create ticket ... ");
		String ticket = ticketServices.createTicket(serviceTicket, casDetails.getExpires());
		_logger.trace("CAS ticket {} created . ", ticket);

		StringBuffer callbackUrl = new StringBuffer(casDetails.getCallbackUrl());
		if (casDetails.getCallbackUrl().indexOf("?") == -1) {
			callbackUrl.append("?");
		}

		if (callbackUrl.indexOf("&") != -1 || callbackUrl.indexOf("=") != -1) {
			callbackUrl.append("&");
		}

		// append ticket
		callbackUrl.append(CasConstants.PARAMETER.TICKET).append("=").append(ticket);

		callbackUrl.append("&");
		// append service
		callbackUrl.append(CasConstants.PARAMETER.SERVICE).append("=").append(casDetails.getService());

		// 增加可自定义的参数
		if (WebContext.getAttribute(CasConstants.PARAMETER.PARAMETER_MAP) != null) {
			@SuppressWarnings("unchecked")
			Map<String, String> parameterMap =
					(Map<String, String>) WebContext.getAttribute(CasConstants.PARAMETER.PARAMETER_MAP);
			parameterMap.remove(CasConstants.PARAMETER.TICKET);
			parameterMap.remove(CasConstants.PARAMETER.SERVICE);
			for (String key : parameterMap.keySet()) {
				callbackUrl.append("&").append(key).append("=").append(parameterMap.get(key));
			}
		}

		if (casDetails.getLogoutType() == LogoutType.BACK_CHANNEL) {
			_logger.debug("CAS LogoutType BACK_CHANNEL ... ");
			String sessionId = AuthorizationUtils.getPrincipal().getSession().getId();
			_logger.trace("get session by id {} . ", sessionId);
			Session session = sessionManager.get(sessionId);
			_logger.trace("current session {}  ", session);
			// set cas ticket as OnlineTicketId
			casDetails.setOnlineTicket(ticket);
			session.setAuthorizedApp(casDetails);
			_logger.trace("session store ticket  {} .", ticket);
			sessionManager.create(sessionId, session);
			_logger.debug("CAS LogoutType session store ticket to AuthorizedApp .");
		}

		_logger.debug("redirect to CAS Client URL {}", callbackUrl);
		modelAndView.addObject("callbackUrl", callbackUrl.toString());
		return modelAndView;
	}
}