package com.wy.test.protocol.cas.endpoint;

import java.security.Principal;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.wy.test.authentication.core.session.Session;
import com.wy.test.authentication.core.web.AuthorizationUtils;
import com.wy.test.core.constant.ConstAuthWeb;
import com.wy.test.core.vo.AppCasDetailVO;
import com.wy.test.core.web.AuthWebContext;
import com.wy.test.protocol.authorize.singlelogout.LogoutType;
import com.wy.test.protocol.cas.endpoint.ticket.CasConstants;
import com.wy.test.protocol.cas.endpoint.ticket.ServiceTicketImpl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * https://apereo.github.io/cas/6.2.x/protocol/CAS-Protocol.html
 */
@Tag(name = "2-3-CAS API文档模块")
@Controller
@Slf4j
public class CasAuthorizeEndpoint extends CasBaseAuthorizeEndpoint {

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
			log.debug("service {} not registered  ", casService);
			ModelAndView modelAndView = new ModelAndView("authorize/cas_sso_submint");
			modelAndView.addObject("errorMessage", casService);
			return modelAndView;
		}

		log.debug("Detail {}", casDetails);
		Map<String, String> parameterMap = AuthWebContext.getRequestParameterMap(request);
		String service = casService;
		log.debug("CAS Parameter service = {}", service);
		if (casService.indexOf("?") > -1) {
			service = casService.substring(casService.indexOf("?") + 1);
			if (service.indexOf("=") > -1) {
				String[] parameterValues = service.split("=");
				if (parameterValues.length == 2) {
					parameterMap.put(parameterValues[0], parameterValues[1]);
				}
			}
			log.debug("CAS service with Parameter : {}", parameterMap);
		}
		AuthWebContext.setAttribute(CasConstants.PARAMETER.PARAMETER_MAP, parameterMap);
		AuthWebContext.setAttribute(CasConstants.PARAMETER.ENDPOINT_CAS_DETAILS, casDetails);
		AuthWebContext.setAttribute(ConstAuthWeb.SINGLE_SIGN_ON_APP_ID, casDetails.getId());
		AuthWebContext.setAttribute(ConstAuthWeb.AUTHORIZE_SIGN_ON_APP, casDetails);
		return AuthWebContext.redirect(CasConstants.ENDPOINT.ENDPOINT_SERVICE_TICKET_GRANTING);

	}

	@GetMapping(CasConstants.ENDPOINT.ENDPOINT_SERVICE_TICKET_GRANTING)
	public ModelAndView grantingTicket(Principal principal, HttpServletRequest request, HttpServletResponse response) {
		ModelAndView modelAndView = new ModelAndView("authorize/cas_sso_submint");
		AppCasDetailVO casDetails =
				(AppCasDetailVO) AuthWebContext.getAttribute(CasConstants.PARAMETER.ENDPOINT_CAS_DETAILS);

		ServiceTicketImpl serviceTicket = new ServiceTicketImpl(AuthorizationUtils.getAuthentication(), casDetails);

		log.trace("CAS start create ticket ... ");
		String ticket = ticketServices.createTicket(serviceTicket, casDetails.getExpires());
		log.trace("CAS ticket {} created . ", ticket);

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
		if (AuthWebContext.getAttribute(CasConstants.PARAMETER.PARAMETER_MAP) != null) {
			@SuppressWarnings("unchecked")
			Map<String, String> parameterMap =
					(Map<String, String>) AuthWebContext.getAttribute(CasConstants.PARAMETER.PARAMETER_MAP);
			parameterMap.remove(CasConstants.PARAMETER.TICKET);
			parameterMap.remove(CasConstants.PARAMETER.SERVICE);
			for (String key : parameterMap.keySet()) {
				callbackUrl.append("&").append(key).append("=").append(parameterMap.get(key));
			}
		}

		if (casDetails.getLogoutType() == LogoutType.BACK_CHANNEL) {
			log.debug("CAS LogoutType BACK_CHANNEL ... ");
			String sessionId = AuthorizationUtils.getPrincipal().getSession().getId();
			log.trace("get session by id {} . ", sessionId);
			Session session = sessionManager.get(sessionId);
			log.trace("current session {}  ", session);
			// set cas ticket as OnlineTicketId
			casDetails.setOnlineTicket(ticket);
			session.setAuthorizedApp(casDetails);
			log.trace("session store ticket  {} .", ticket);
			sessionManager.create(sessionId, session);
			log.debug("CAS LogoutType session store ticket to AuthorizedApp .");
		}

		log.debug("redirect to CAS Client URL {}", callbackUrl);
		modelAndView.addObject("callbackUrl", callbackUrl.toString());
		return modelAndView;
	}
}