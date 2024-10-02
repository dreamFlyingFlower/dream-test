package com.wy.test.web.core.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.AsyncHandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.wy.test.authentication.core.entity.SignPrincipal;
import com.wy.test.authentication.core.web.AuthorizationUtils;
import com.wy.test.core.constant.ConstAuthWeb;
import com.wy.test.core.entity.HistoryLoginAppEntity;
import com.wy.test.core.vo.AppVO;
import com.wy.test.core.vo.UserVO;
import com.wy.test.core.web.AuthWebContext;
import com.wy.test.persistence.service.AppService;
import com.wy.test.persistence.service.HistoryLoginAppService;

import lombok.extern.slf4j.Slf4j;

/**
 * CAS单点登录成功后记录日志
 *
 * @author 飞花梦影
 * @date 2024-10-02 13:26:54
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Component
@Slf4j
public class HistorySignOnAppInterceptor implements AsyncHandlerInterceptor {

	@Autowired
	private HistoryLoginAppService historyLoginAppsService;

	@Autowired
	protected AppService appsService;

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		log.debug("postHandle");

		final AppVO appVo = (AppVO) AuthWebContext.getAttribute(ConstAuthWeb.AUTHORIZE_SIGN_ON_APP);

		SignPrincipal principal = AuthorizationUtils.getPrincipal();
		if (principal != null && appVo != null) {
			final UserVO userInfo = principal.getUserInfo();
			String sessionId = principal.getSession().getId();
			log.debug("sessionId : " + sessionId + " ,appId : " + appVo.getId());
			HistoryLoginAppEntity historyLoginApps = new HistoryLoginAppEntity();
			historyLoginApps.setAppId(appVo.getId());
			historyLoginApps.setSessionId(sessionId);
			historyLoginApps.setAppName(appVo.getAppName());
			historyLoginApps.setUserId(userInfo.getId());
			historyLoginApps.setUsername(userInfo.getUsername());
			historyLoginApps.setDisplayName(userInfo.getDisplayName());
			historyLoginApps.setInstId(userInfo.getInstId());
			historyLoginAppsService.insert(historyLoginApps);
			AuthWebContext.removeAttribute(ConstAuthWeb.CURRENT_SINGLESIGNON_URI);
			AuthWebContext.removeAttribute(ConstAuthWeb.SINGLE_SIGN_ON_APP_ID);
		}
	}
}