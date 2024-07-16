package com.wy.test.core.authn.web.interceptor;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.AsyncHandlerInterceptor;

import com.wy.test.configuration.ApplicationConfig;
import com.wy.test.core.authn.SignPrincipal;
import com.wy.test.core.authn.jwt.AuthTokenService;
import com.wy.test.core.authn.session.SessionManager;
import com.wy.test.core.authn.web.AuthorizationUtils;

/**
 * 权限Interceptor处理
 */
@Component
public class PermissionInterceptor implements AsyncHandlerInterceptor {

	private static final Logger _logger = LoggerFactory.getLogger(PermissionInterceptor.class);

	// 无需Interceptor url
	@Autowired
	ApplicationConfig applicationConfig;

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
		_logger.trace("Permission Interceptor .");
		AuthorizationUtils.authenticate(request, authTokenService, sessionManager);
		SignPrincipal principal = AuthorizationUtils.getPrincipal();
		// 判断用户是否登录,判断用户是否登录用户
		if (principal == null) {
			_logger.trace("No Authentication ... forward to /auth/entrypoint , request URI " + request.getRequestURI());
			RequestDispatcher dispatcher = request.getRequestDispatcher("/auth/entrypoint");
			dispatcher.forward(request, response);
			return false;
		}

		// 管理端必须使用管理员登录,非管理员用户直接注销
		if (this.mgmt && !principal.isRoleAdministrators()) {
			_logger.debug("Not ADMINISTRATORS Authentication .");
			RequestDispatcher dispatcher = request.getRequestDispatcher("/auth/entrypoint");
			dispatcher.forward(request, response);
			return false;
		}

		return true;
	}

	public void setMgmt(boolean mgmt) {
		this.mgmt = mgmt;
		_logger.debug("Permission for ADMINISTRATORS {}", this.mgmt);
	}

}
