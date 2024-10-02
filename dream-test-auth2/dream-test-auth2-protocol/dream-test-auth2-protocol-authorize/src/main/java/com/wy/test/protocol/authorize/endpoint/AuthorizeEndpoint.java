package com.wy.test.protocol.authorize.endpoint;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import com.wy.test.core.constant.ConstAuthView;
import com.wy.test.core.constant.ConstAuthWeb;
import com.wy.test.core.constant.ConstProtocols;
import com.wy.test.core.vo.AppVO;
import com.wy.test.core.web.AuthWebContext;
import com.wy.test.persistence.service.AppCasDetailService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "1-2认证总地址文档模块")
@Controller
@Slf4j
public class AuthorizeEndpoint extends AuthorizeBaseEndpoint {

	@Autowired
	AppCasDetailService appCasDetailService;

	// all single sign on url
	@Operation(summary = "认证总地址接口", description = "参数应用ID，分发到不同应用的认证地址", method = "GET")
	@GetMapping("/authz/{id}")
	public ModelAndView authorize(HttpServletRequest request, @PathVariable("id") String id) {
		ModelAndView modelAndView = null;
		AppVO app = getApp(id);
		AuthWebContext.setAttribute(ConstAuthWeb.SINGLE_SIGN_ON_APP_ID, app.getId());

		if (app.getProtocol().equalsIgnoreCase(ConstProtocols.EXTEND_API)) {
			modelAndView = AuthWebContext.forward("/authz/api/" + app.getId());
		} else if (app.getProtocol().equalsIgnoreCase(ConstProtocols.FORMBASED)) {
			modelAndView = AuthWebContext.forward("/authz/formbased/" + app.getId());
		} else if (app.getProtocol().equalsIgnoreCase(ConstProtocols.OAUTH20)) {
			modelAndView = AuthWebContext.forward("/authz/oauth/v20/" + app.getId());
		} else if (app.getProtocol().equalsIgnoreCase(ConstProtocols.OAUTH21)) {
			modelAndView = AuthWebContext.redirect(app.getLoginUrl());
		} else if (app.getProtocol().equalsIgnoreCase(ConstProtocols.OPEN_ID_CONNECT10)) {
			modelAndView = AuthWebContext.forward("/authz/oauth/v20/" + app.getId());
		} else if (app.getProtocol().equalsIgnoreCase(ConstProtocols.SAML20)) {
			modelAndView = AuthWebContext.forward("/authz/saml20/idpinit/" + app.getId());
		} else if (app.getProtocol().equalsIgnoreCase(ConstProtocols.TOKENBASED)) {
			modelAndView = AuthWebContext.forward("/authz/tokenbased/" + app.getId());
		} else if (app.getProtocol().equalsIgnoreCase(ConstProtocols.CAS)) {
			modelAndView = AuthWebContext.forward("/authz/cas/" + app.getId());
		} else if (app.getProtocol().equalsIgnoreCase(ConstProtocols.JWT)) {
			modelAndView = AuthWebContext.forward("/authz/jwt/" + app.getId());
		} else if (app.getProtocol().equalsIgnoreCase(ConstProtocols.BASIC)) {
			modelAndView = AuthWebContext.redirect(app.getLoginUrl());
		}

		log.debug(modelAndView.getViewName());

		return modelAndView;
	}

	@GetMapping(ConstAuthView.AUTHZ_REFUSED)
	public ModelAndView refused(HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView("authorize/authorize_refused");
		AppVO app = (AppVO) AuthWebContext.getAttribute(ConstAuthWeb.AUTHORIZE_SIGN_ON_APP);
		app.transIconBase64();
		// 删除Session缓存
		AuthWebContext.removeAttribute(ConstAuthWeb.AUTHORIZE_SIGN_ON_APP);
		modelAndView.addObject("model", app);
		return modelAndView;
	}
}