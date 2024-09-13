package com.wy.test.authentication.core.web.interceptor;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.AsyncHandlerInterceptor;

import com.wy.test.authentication.core.entity.SignPrincipal;
import com.wy.test.authentication.core.jwt.AuthTokenService;
import com.wy.test.authentication.core.session.SessionManager;
import com.wy.test.authentication.core.web.AuthorizationUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * 拦截所有请求,检查请求头中是否有Authorization参数
 * 
 * @author 飞花梦影
 * @date 2024-09-10 22:53:53
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Component
@Slf4j
public class PermissionInterceptor implements AsyncHandlerInterceptor {

	@Autowired
	SessionManager sessionManager;

	@Autowired
	AuthTokenService authTokenService;

	boolean mgmt = false;

	/*
	 * 请求前处理 (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.web.servlet.handler.HandlerInterceptorAdapter#preHandle(
	 * javax.servlet.http. HttpServletRequest,
	 * javax.servlet.http.HttpServletResponse, java.lang.Object)
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		log.trace("Permission Interceptor .");
		AuthorizationUtils.authenticate(request, authTokenService, sessionManager);
		SignPrincipal principal = AuthorizationUtils.getPrincipal();
		// 判断用户是否登录,判断用户是否登录用户
		if (principal == null) {
			log.info("No Authentication ... forward to /auth/entrypoint , request URI " + request.getRequestURI());
			RequestDispatcher dispatcher = request.getRequestDispatcher("/auth/entrypoint");
			dispatcher.forward(request, response);
			return false;
		}

		// 管理端必须使用管理员登录,非管理员用户直接注销
		if (this.mgmt && !principal.isRoleAdministrators()) {
			log.debug("Not ADMINISTRATORS Authentication .");
			RequestDispatcher dispatcher = request.getRequestDispatcher("/auth/entrypoint");
			dispatcher.forward(request, response);
			return false;
		}

		return true;
	}

	public void setMgmt(boolean mgmt) {
		this.mgmt = mgmt;
		log.debug("Permission for ADMINISTRATORS {}", this.mgmt);
	}
}