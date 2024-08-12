package com.wy.test.web.core.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.AsyncHandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.wy.test.authentication.core.authn.SignPrincipal;
import com.wy.test.authentication.core.authn.web.AuthorizationUtils;
import com.wy.test.core.entity.AppEntity;
import com.wy.test.core.entity.HistoryLoginAppEntity;
import com.wy.test.core.vo.UserVO;
import com.wy.test.core.web.WebConstants;
import com.wy.test.core.web.WebContext;
import com.wy.test.persistence.service.AppService;
import com.wy.test.persistence.service.HistoryLoginAppService;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class HistorySignOnAppInterceptor implements AsyncHandlerInterceptor {

	@Autowired
	private HistoryLoginAppService historyLoginAppsService;

	@Autowired
	protected AppService appsService;

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
		log.debug("postHandle");

		final AppEntity app = (AppEntity) WebContext.getAttribute(WebConstants.AUTHORIZE_SIGN_ON_APP);

		SignPrincipal principal = AuthorizationUtils.getPrincipal();
		if (principal != null && app != null) {
			final UserVO userInfo = principal.getUserInfo();
			String sessionId = principal.getSession().getId();
			log.debug("sessionId : " + sessionId + " ,appId : " + app.getId());
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