package com.wy.test.web.core.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.wy.test.persistence.service.HistoryLoginAppService;
import com.wy.test.web.core.autoconfigure.DreamAuthMvcConfig;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Cas,OAuth2.0,OAuth2.1等APP登录成功后记录日志,由{@link DreamAuthMvcConfig}注入
 *
 * @author 飞花梦影
 * @date 2024-10-02 13:26:54
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Slf4j
@Component
@AllArgsConstructor
public class HistorySignOnAppInterceptor implements AsyncHandlerInterceptor {

	private final HistoryLoginAppService historyLoginAppService;

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
			HistoryLoginAppEntity historyLoginAppEntity = HistoryLoginAppEntity.builder()
					.appId(appVo.getId())
					.sessionId(sessionId)
					.appName(appVo.getAppName())
					.userId(userInfo.getId())
					.username(userInfo.getUsername())
					.displayName(userInfo.getDisplayName())
					.instId(userInfo.getInstId())
					.build();
			historyLoginAppService.save(historyLoginAppEntity);
			AuthWebContext.removeAttribute(ConstAuthWeb.CURRENT_SINGLESIGNON_URI);
			AuthWebContext.removeAttribute(ConstAuthWeb.SINGLE_SIGN_ON_APP_ID);
		}
	}
}