package com.wy.test.authz.token.endpoint;

import java.lang.reflect.InvocationTargetException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import com.wy.test.authorize.endpoint.AuthorizeBaseEndpoint;
import com.wy.test.authorize.endpoint.adapter.AbstractAuthorizeAdapter;
import com.wy.test.authz.token.endpoint.adapter.TokenBasedDefaultAdapter;
import com.wy.test.core.authn.annotation.CurrentUser;
import com.wy.test.core.authn.web.AuthorizationUtils;
import com.wy.test.core.convert.AppTokenDetailConvert;
import com.wy.test.core.entity.UserEntity;
import com.wy.test.core.properties.DreamAuthServerProperties;
import com.wy.test.core.vo.AppTokenDetailVO;
import com.wy.test.core.vo.AppVO;
import com.wy.test.core.web.WebContext;
import com.wy.test.persistence.service.AppTokenDetailService;

import dream.flying.flower.framework.core.enums.BooleanEnum;
import dream.flying.flower.reflect.ReflectHelper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "2-6-TokenBased接口文档模块")
@Controller
@Slf4j
@AllArgsConstructor
public class TokenBasedAuthorizeEndpoint extends AuthorizeBaseEndpoint {

	AppTokenDetailService appTokenDetailService;

	AppTokenDetailConvert appTokenDetailConvert;

	DreamAuthServerProperties dreamServerProperties;

	@Operation(summary = "TokenBased认证接口", description = "传递参数应用ID", method = "GET")
	@GetMapping("/authz/tokenbased/{id}")
	public ModelAndView authorize(HttpServletRequest request, HttpServletResponse response,
			@PathVariable("id") String id, @CurrentUser UserEntity currentUser)
			throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		ModelAndView modelAndView = new ModelAndView();

		AppVO application = getApp(id);
		AppTokenDetailVO appTokenDetailVO = appTokenDetailService.getAppDetails(id, true);
		log.debug("" + appTokenDetailVO);

		appTokenDetailVO.setAdapter(application.getAdapter());
		appTokenDetailVO.setIsAdapter(application.getIsAdapter());

		AbstractAuthorizeAdapter adapter;
		if (BooleanEnum.isTrue(appTokenDetailVO.getIsAdapter())) {
			adapter = (AbstractAuthorizeAdapter) ReflectHelper.newInstance(appTokenDetailVO.getAdapter());
		} else {
			adapter = (AbstractAuthorizeAdapter) new TokenBasedDefaultAdapter();
		}
		adapter.setPrincipal(AuthorizationUtils.getPrincipal());
		adapter.setApp(appTokenDetailVO);

		adapter.generateInfo();

		adapter.encrypt(null, appTokenDetailVO.getAlgorithmKey(), appTokenDetailVO.getAlgorithm());

		if (appTokenDetailVO.getTokenType().equalsIgnoreCase("POST")) {
			return adapter.authorize(modelAndView);
		} else {
			log.debug("Cookie Name : {}", appTokenDetailVO.getCookieName());

			Cookie cookie = new Cookie(appTokenDetailVO.getCookieName(), adapter.serialize());

			Integer maxAge = appTokenDetailVO.getExpires();
			log.debug("Cookie Max Age : {} seconds.", maxAge);
			cookie.setMaxAge(maxAge);

			cookie.setPath("/");
			//
			// cookie.setDomain("."+applicationConfig.getBaseDomainName());
			// tomcat 8.5
			cookie.setDomain(dreamServerProperties.getBaseDomain());

			log.debug("Sub Domain Name : .{}", dreamServerProperties.getBaseDomain());
			response.addCookie(cookie);

			if (appTokenDetailVO.getRedirectUri().indexOf(dreamServerProperties.getBaseDomain()) > -1) {
				return WebContext.redirect(appTokenDetailVO.getRedirectUri());
			} else {
				log.error(
						appTokenDetailVO.getRedirectUri() + " not in domain " + dreamServerProperties.getBaseDomain());
				return null;
			}
		}
	}
}