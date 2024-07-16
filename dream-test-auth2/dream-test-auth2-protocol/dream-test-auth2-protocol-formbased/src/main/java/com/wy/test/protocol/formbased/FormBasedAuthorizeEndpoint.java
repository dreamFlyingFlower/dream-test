package com.wy.test.protocol.formbased;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import com.wy.test.authorize.endpoint.AuthorizeBaseEndpoint;
import com.wy.test.authorize.endpoint.adapter.AbstractAuthorizeAdapter;
import com.wy.test.constants.ConstsBoolean;
import com.wy.test.core.authn.annotation.CurrentUser;
import com.wy.test.core.authn.web.AuthorizationUtils;
import com.wy.test.entity.Accounts;
import com.wy.test.entity.UserInfo;
import com.wy.test.entity.apps.Apps;
import com.wy.test.entity.apps.AppsFormBasedDetails;
import com.wy.test.persistence.service.AppsFormBasedDetailsService;
import com.wy.test.protocol.formbased.adapter.FormBasedDefaultAdapter;
import com.wy.test.util.Instance;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "2-7-FormBased接口文档模块")
@Controller
public class FormBasedAuthorizeEndpoint extends AuthorizeBaseEndpoint {

	final static Logger _logger = LoggerFactory.getLogger(FormBasedAuthorizeEndpoint.class);

	@Autowired
	AppsFormBasedDetailsService formBasedDetailsService;

	FormBasedDefaultAdapter defaultFormBasedAdapter = new FormBasedDefaultAdapter();

	@Operation(summary = "FormBased认证地址接口", description = "参数应用ID", method = "GET")
	@GetMapping("/authz/formbased/{id}")
	public ModelAndView authorize(HttpServletRequest request, @PathVariable("id") String id,
			@CurrentUser UserInfo currentUser) {

		AppsFormBasedDetails formBasedDetails = formBasedDetailsService.getAppDetails(id, true);
		_logger.debug("formBasedDetails {}", formBasedDetails);
		Apps application = getApp(id);
		formBasedDetails.setAdapter(application.getAdapter());
		formBasedDetails.setIsAdapter(application.getIsAdapter());
		ModelAndView modelAndView = null;

		Accounts account = getAccounts(formBasedDetails, currentUser);
		_logger.debug("Accounts {}", account);

		if (account == null) {
			return initCredentialView(id, "/authz/formbased/" + id);
		} else {
			modelAndView = new ModelAndView();

			AbstractAuthorizeAdapter adapter;

			if (ConstsBoolean.isTrue(formBasedDetails.getIsAdapter())) {
				Object formBasedAdapter = Instance.newInstance(formBasedDetails.getAdapter());
				adapter = (AbstractAuthorizeAdapter) formBasedAdapter;
			} else {
				FormBasedDefaultAdapter formBasedDefaultAdapter = new FormBasedDefaultAdapter();
				adapter = (AbstractAuthorizeAdapter) formBasedDefaultAdapter;
			}
			adapter.setPrincipal(AuthorizationUtils.getPrincipal());
			adapter.setApp(formBasedDetails);
			adapter.setAccount(account);

			modelAndView = adapter.authorize(modelAndView);
		}

		_logger.debug("FormBased View Name {}", modelAndView.getViewName());

		return modelAndView;
	}
}
