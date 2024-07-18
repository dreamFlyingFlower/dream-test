package com.wy.test.core.authn.web;

import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import com.wy.test.core.authn.annotation.CurrentUser;
import com.wy.test.core.entity.UserInfo;
import com.wy.test.core.web.WebConstants;

public class CurrentUserMethodArgumentResolver implements HandlerMethodArgumentResolver {

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		Authentication authentication =
				(Authentication) webRequest.getAttribute(WebConstants.AUTHENTICATION, RequestAttributes.SCOPE_SESSION);
		UserInfo userInfo = AuthorizationUtils.getUserInfo(authentication);
		if (userInfo != null) {
			return userInfo;
		}
		throw new MissingServletRequestPartException("currentUser");
	}

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.getParameterType().isAssignableFrom(UserInfo.class)
				&& parameter.hasParameterAnnotation(CurrentUser.class);
	}

}
