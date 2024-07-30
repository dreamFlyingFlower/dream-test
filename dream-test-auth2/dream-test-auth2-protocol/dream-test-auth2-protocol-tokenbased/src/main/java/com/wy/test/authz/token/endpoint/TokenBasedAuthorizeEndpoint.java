package com.wy.test.authz.token.endpoint;

import java.lang.reflect.InvocationTargetException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.wy.test.core.constants.ConstsBoolean;
import com.wy.test.core.entity.UserInfo;
import com.wy.test.core.entity.apps.Apps;
import com.wy.test.core.entity.apps.AppsTokenBasedDetails;
import com.wy.test.core.properties.DreamServerProperties;
import com.wy.test.core.web.WebContext;
import com.wy.test.persistence.service.AppsTokenBasedDetailsService;

import dream.flying.flower.reflect.ReflectHelper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "2-6-TokenBased接口文档模块")
@Controller
@Slf4j
public class TokenBasedAuthorizeEndpoint extends AuthorizeBaseEndpoint {

	@Autowired
	AppsTokenBasedDetailsService tokenBasedDetailsService;

	@Autowired
	DreamServerProperties dreamServerProperties;

	@Operation(summary = "TokenBased认证接口", description = "传递参数应用ID", method = "GET")
	@GetMapping("/authz/tokenbased/{id}")
	public ModelAndView authorize(HttpServletRequest request, HttpServletResponse response,
			@PathVariable("id") String id, @CurrentUser UserInfo currentUser)
			throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		ModelAndView modelAndView = new ModelAndView();

		AppsTokenBasedDetails tokenBasedDetails = null;
		tokenBasedDetails = tokenBasedDetailsService.getAppDetails(id, true);
		log.debug("" + tokenBasedDetails);

		Apps application = getApp(id);
		tokenBasedDetails.setAdapter(application.getAdapter());
		tokenBasedDetails.setIsAdapter(application.getIsAdapter());

		AbstractAuthorizeAdapter adapter;
		if (ConstsBoolean.isTrue(tokenBasedDetails.getIsAdapter())) {
			adapter = (AbstractAuthorizeAdapter) ReflectHelper.newInstance(tokenBasedDetails.getAdapter());
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
			log.debug("Cookie Name : {}", tokenBasedDetails.getCookieName());

			Cookie cookie = new Cookie(tokenBasedDetails.getCookieName(), adapter.serialize());

			Integer maxAge = tokenBasedDetails.getExpires();
			log.debug("Cookie Max Age : {} seconds.", maxAge);
			cookie.setMaxAge(maxAge);

			cookie.setPath("/");
			//
			// cookie.setDomain("."+applicationConfig.getBaseDomainName());
			// tomcat 8.5
			cookie.setDomain(dreamServerProperties.getBaseDomain());

			log.debug("Sub Domain Name : .{}", dreamServerProperties.getBaseDomain());
			response.addCookie(cookie);

			if (tokenBasedDetails.getRedirectUri().indexOf(dreamServerProperties.getBaseDomain()) > -1) {
				return WebContext.redirect(tokenBasedDetails.getRedirectUri());
			} else {
				log.error(
						tokenBasedDetails.getRedirectUri() + " not in domain " + dreamServerProperties.getBaseDomain());
				return null;
			}
		}
	}
}