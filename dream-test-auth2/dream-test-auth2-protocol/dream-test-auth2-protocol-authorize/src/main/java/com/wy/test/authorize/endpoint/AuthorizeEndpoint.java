package com.wy.test.authorize.endpoint;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import com.wy.test.constants.ConstsProtocols;
import com.wy.test.entity.apps.Apps;
import com.wy.test.persistence.service.AppsCasDetailsService;
import com.wy.test.web.WebConstants;
import com.wy.test.web.WebContext;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "1-2认证总地址文档模块")
@Controller
public class AuthorizeEndpoint extends AuthorizeBaseEndpoint {

	final static Logger _logger = LoggerFactory.getLogger(AuthorizeEndpoint.class);

	@Autowired
	AppsCasDetailsService casDetailsService;

	// all single sign on url
	@Operation(summary = "认证总地址接口", description = "参数应用ID，分发到不同应用的认证地址", method = "GET")
	@GetMapping("/authz/{id}")
	public ModelAndView authorize(HttpServletRequest request, @PathVariable("id") String id) {
		ModelAndView modelAndView = null;
		Apps app = getApp(id);
		WebContext.setAttribute(WebConstants.SINGLE_SIGN_ON_APP_ID, app.getId());

		if (app.getProtocol().equalsIgnoreCase(ConstsProtocols.EXTEND_API)) {
			modelAndView = WebContext.forward("/authz/api/" + app.getId());
		} else if (app.getProtocol().equalsIgnoreCase(ConstsProtocols.FORMBASED)) {
			modelAndView = WebContext.forward("/authz/formbased/" + app.getId());
		} else if (app.getProtocol().equalsIgnoreCase(ConstsProtocols.OAUTH20)) {
			modelAndView = WebContext.forward("/authz/oauth/v20/" + app.getId());
		} else if (app.getProtocol().equalsIgnoreCase(ConstsProtocols.OAUTH21)) {
			modelAndView = WebContext.redirect(app.getLoginUrl());
		} else if (app.getProtocol().equalsIgnoreCase(ConstsProtocols.OPEN_ID_CONNECT10)) {
			modelAndView = WebContext.forward("/authz/oauth/v20/" + app.getId());
		} else if (app.getProtocol().equalsIgnoreCase(ConstsProtocols.SAML20)) {
			modelAndView = WebContext.forward("/authz/saml20/idpinit/" + app.getId());
		} else if (app.getProtocol().equalsIgnoreCase(ConstsProtocols.TOKENBASED)) {
			modelAndView = WebContext.forward("/authz/tokenbased/" + app.getId());
		} else if (app.getProtocol().equalsIgnoreCase(ConstsProtocols.CAS)) {
			modelAndView = WebContext.forward("/authz/cas/" + app.getId());
		} else if (app.getProtocol().equalsIgnoreCase(ConstsProtocols.JWT)) {
			modelAndView = WebContext.forward("/authz/jwt/" + app.getId());
		} else if (app.getProtocol().equalsIgnoreCase(ConstsProtocols.BASIC)) {
			modelAndView = WebContext.redirect(app.getLoginUrl());
		}

		_logger.debug(modelAndView.getViewName());

		return modelAndView;
	}

	@GetMapping("/authz/refused")
	public ModelAndView refused() {
		ModelAndView modelAndView = new ModelAndView("authorize/authorize_refused");
		Apps app = (Apps) WebContext.getAttribute(WebConstants.AUTHORIZE_SIGN_ON_APP);
		app.transIconBase64();
		modelAndView.addObject("model", app);
		return modelAndView;
	}
}