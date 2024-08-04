package com.wy.test.web.web.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.AsyncHandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.wy.test.core.authn.SignPrincipal;
import com.wy.test.core.authn.web.AuthorizationUtils;
import com.wy.test.core.entity.AppEntity;
import com.wy.test.core.entity.HistoryLoginAppEntity;
import com.wy.test.core.entity.UserEntity;
import com.wy.test.core.web.WebConstants;
import com.wy.test.core.web.WebContext;
import com.wy.test.persistence.service.AppsService;
import com.wy.test.persistence.service.HistoryLoginAppsService;

@Component
public class HistorySignOnAppInterceptor implements AsyncHandlerInterceptor {

	private static final Logger _logger = LoggerFactory.getLogger(HistorySignOnAppInterceptor.class);

	@Autowired
	HistoryLoginAppsService historyLoginAppsService;

	@Autowired
	protected AppsService appsService;

	/**
	 * postHandle .
	 * 
	 * @see org.springframework.web.servlet.handler.HandlerInterceptorAdapter#preHandle(
	 *      javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse, java.lang.Object)
	 */
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		_logger.debug("postHandle");

		final AppEntity app = (AppEntity) WebContext.getAttribute(WebConstants.AUTHORIZE_SIGN_ON_APP);

		SignPrincipal principal = AuthorizationUtils.getPrincipal();
		if (principal != null && app != null) {
			final UserEntity userInfo = principal.getUserInfo();
			String sessionId = principal.getSession().getId();
			_logger.debug("sessionId : " + sessionId + " ,appId : " + app.getId());
			HistoryLoginAppEntity historyLoginApps = new HistoryLoginAppEntity();
			historyLoginApps.setAppId(app.getId());
			historyLoginApps.setSessionId(sessionId);
			historyLoginApps.setAppName(app.getAppName());
			historyLoginApps.setUserId(userInfo.getId());
			historyLoginApps.setUsername(userInfo.getUsername());
			historyLoginApps.setDisplayName(userInfo.getDisplayName());
			historyLoginApps.setInstId(userInfo.getInstId());
			historyLoginAppsService.insert(historyLoginApps);
			WebContext.removeAttribute(WebConstants.CURRENT_SINGLESIGNON_URI);
			WebContext.removeAttribute(WebConstants.SINGLE_SIGN_ON_APP_ID);
		}

	}
}
