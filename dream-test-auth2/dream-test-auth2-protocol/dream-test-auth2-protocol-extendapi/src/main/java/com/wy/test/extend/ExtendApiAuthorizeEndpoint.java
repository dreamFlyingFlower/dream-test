package com.wy.test.extend;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import com.wy.test.authorize.endpoint.AuthorizeBaseEndpoint;
import com.wy.test.authorize.endpoint.adapter.AbstractAuthorizeAdapter;
import com.wy.test.core.authn.annotation.CurrentUser;
import com.wy.test.core.authn.web.AuthorizationUtils;
import com.wy.test.core.constants.ConstsBoolean;
import com.wy.test.core.entity.Accounts;
import com.wy.test.core.entity.UserInfo;
import com.wy.test.core.entity.apps.Apps;
import com.wy.test.util.Instance;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "2-8-ExtendApi接口文档模块")
@Controller
public class ExtendApiAuthorizeEndpoint extends AuthorizeBaseEndpoint {

	final static Logger _logger = LoggerFactory.getLogger(ExtendApiAuthorizeEndpoint.class);

	@Operation(summary = "ExtendApi认证地址接口", description = "参数应用ID", method = "GET")
	@GetMapping("/authz/api/{id}")
	public ModelAndView authorize(HttpServletRequest request, @PathVariable("id") String id,
			@CurrentUser UserInfo currentUser) {

		ModelAndView modelAndView = new ModelAndView("authorize/redirect_sso_submit");
		modelAndView.addObject("errorCode", 0);
		modelAndView.addObject("errorMessage", "");

		Apps apps = getApp(id);
		_logger.debug("" + apps);
		if (ConstsBoolean.isTrue(apps.getIsAdapter())) {
			_logger.debug("Adapter {}", apps.getAdapter());
			AbstractAuthorizeAdapter adapter = (AbstractAuthorizeAdapter) Instance.newInstance(apps.getAdapter());
			Accounts account = getAccounts(apps, currentUser);
			if (apps.getCredential().equalsIgnoreCase(Apps.CREDENTIALS.USER_DEFINED) && account == null) {
				return initCredentialView(id, "/authorize/api/" + id);
			}

			adapter.setPrincipal(AuthorizationUtils.getPrincipal());
			adapter.setApp(apps);
			adapter.setAccount(account);

			return adapter.authorize(modelAndView);
		} else {
			_logger.debug("redirect_uri {}", apps.getLoginUrl());
			modelAndView.addObject("redirect_uri", apps.getLoginUrl());
			return modelAndView;
		}

	}
}
