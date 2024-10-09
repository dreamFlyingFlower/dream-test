package com.wy.test.authentication.core.web;

import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import com.wy.test.authentication.core.annotation.CurrentUser;
import com.wy.test.core.constant.ConstAuthWeb;
import com.wy.test.core.entity.UserEntity;
import com.wy.test.core.vo.UserVO;

public class CurrentUserMethodArgumentResolver implements HandlerMethodArgumentResolver {

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		Authentication authentication =
				(Authentication) webRequest.getAttribute(ConstAuthWeb.AUTHENTICATION, RequestAttributes.SCOPE_SESSION);
		UserVO userInfo = AuthorizationUtils.getUserInfo(authentication);
		if (userInfo != null) {
			return userInfo;
		}
		throw new MissingServletRequestPartException("currentUser");
	}

	/**
	 * 判断是否对参数进行解析
	 * 
	 * @param parameter 参数
	 * @return true->是;false->否
	 */
	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return (parameter.getParameterType().isAssignableFrom(UserEntity.class)
				|| parameter.getParameterType().isAssignableFrom(UserVO.class))
				&& parameter.hasParameterAnnotation(CurrentUser.class);
	}
}