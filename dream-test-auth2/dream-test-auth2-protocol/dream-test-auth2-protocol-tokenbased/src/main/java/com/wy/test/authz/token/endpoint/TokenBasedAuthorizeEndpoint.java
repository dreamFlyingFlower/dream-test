package com.wy.test.authz.token.endpoint;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import com.wy.test.authorize.endpoint.AuthorizeBaseEndpoint;
import com.wy.test.authorize.endpoint.adapter.AbstractAuthorizeAdapter;
import com.wy.test.authz.token.endpoint.adapter.TokenBasedDefaultAdapter;
import com.wy.test.core.authn.annotation.CurrentUser;
import com.wy.test.core.authn.web.AuthorizationUtils;
import com.wy.test.core.configuration.ApplicationConfig;
import com.wy.test.core.constants.ConstsBoolean;
import com.wy.test.core.entity.UserInfo;
import com.wy.test.core.entity.apps.Apps;
import com.wy.test.core.entity.apps.AppsTokenBasedDetails;
import com.wy.test.core.web.WebContext;
import com.wy.test.persistence.service.AppsTokenBasedDetailsService;
import com.wy.test.util.Instance;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "2-6-TokenBased接口文档模块")
@Controller
public class TokenBasedAuthorizeEndpoint extends AuthorizeBaseEndpoint {

	final static Logger _logger = LoggerFactory.getLogger(TokenBasedAuthorizeEndpoint.class);

	@Autowired
	AppsTokenBasedDetailsService tokenBasedDetailsService;

	@Autowired
	ApplicationConfig applicationConfig;

	@Operation(summary = "TokenBased认证接口", description = "传递参数应用ID", method = "GET")
	@GetMapping("/authz/tokenbased/{id}")
	public ModelAndView authorize(HttpServletRequest request, HttpServletResponse response,
			@PathVariable("id") String id, @CurrentUser UserInfo currentUser) {
		ModelAndView modelAndView = new ModelAndView();

		AppsTokenBasedDetails tokenBasedDetails = null;
		tokenBasedDetails = tokenBasedDetailsService.getAppDetails(id, true);
		_logger.debug("" + tokenBasedDetails);

		Apps application = getApp(id);
		tokenBasedDetails.setAdapter(application.getAdapter());
		tokenBasedDetails.setIsAdapter(application.getIsAdapter());

		AbstractAuthorizeAdapter adapter;
		if (ConstsBoolean.isTrue(tokenBasedDetails.getIsAdapter())) {
			adapter = (AbstractAuthorizeAdapter) Instance.newInstance(tokenBasedDetails.getAdapter());
		} else {
			adapter = (AbstractAuthorizeAdapter) new TokenBasedDefaultAdapter();
		}
		adapter.setPrincipal(AuthorizationUtils.getPrincipal());
		adapter.setApp(tokenBasedDetails);

		adapter.generateInfo();

		adapter.encrypt(null, tokenBasedDetails.getAlgorithmKey(), tokenBasedDetails.getAlgorithm());

		if (tokenBasedDetails.getTokenType().equalsIgnoreCase("POST")) {
			return adapter.authorize(modelAndView);
		} else {
			_logger.debug("Cookie Name : {}", tokenBasedDetails.getCookieName());

			Cookie cookie = new Cookie(tokenBasedDetails.getCookieName(), adapter.serialize());

			Integer maxAge = tokenBasedDetails.getExpires();
			_logger.debug("Cookie Max Age : {} seconds.", maxAge);
			cookie.setMaxAge(maxAge);

			cookie.setPath("/");
			//
			// cookie.setDomain("."+applicationConfig.getBaseDomainName());
			// tomcat 8.5
			cookie.setDomain(applicationConfig.getBaseDomainName());

			_logger.debug("Sub Domain Name : .{}", applicationConfig.getBaseDomainName());
			response.addCookie(cookie);

			if (tokenBasedDetails.getRedirectUri().indexOf(applicationConfig.getBaseDomainName()) > -1) {
				return WebContext.redirect(tokenBasedDetails.getRedirectUri());
			} else {
				_logger.error(
						tokenBasedDetails.getRedirectUri() + " not in domain " + applicationConfig.getBaseDomainName());
				return null;
			}
		}

	}

}
