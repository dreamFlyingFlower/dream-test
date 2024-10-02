package com.wy.test.web.core.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.common.util.OAuth2Utils;
import org.springframework.security.web.util.UrlUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.AsyncHandlerInterceptor;

import com.wy.test.authentication.core.entity.SignPrincipal;
import com.wy.test.authentication.core.jwt.AuthTokenService;
import com.wy.test.authentication.core.session.SessionManager;
import com.wy.test.authentication.core.web.AuthorizationUtils;
import com.wy.test.core.constant.ConstAuthView;
import com.wy.test.core.constant.ConstAuthWeb;
import com.wy.test.core.properties.DreamAuthServerProperties;
import com.wy.test.core.vo.AppVO;
import com.wy.test.core.web.AuthWebContext;
import com.wy.test.persistence.service.AppCasDetailService;
import com.wy.test.persistence.service.AppService;
import com.wy.test.protocol.cas.endpoint.ticket.CasConstants;

import dream.flying.flower.binary.Base64Helper;
import lombok.extern.slf4j.Slf4j;

/**
 * 拦截CAS,OAuth2.0,OAuth2.1登录
 *
 * @author 飞花梦影
 * @date 2024-10-02 10:14:13
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Component
@Slf4j
public class SingleSignOnInterceptor implements AsyncHandlerInterceptor {

	@Autowired
	DreamAuthServerProperties dreamServerProperties;

	@Autowired
	SessionManager sessionManager;

	@Autowired
	AuthTokenService authTokenService;

	@Autowired
	AppService appsService;

	@Autowired
	AppCasDetailService casDetailsService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		log.trace("Single Sign On Interceptor");

		AuthorizationUtils.authenticateWithCookie(request, authTokenService, sessionManager);

		// 未登录或登录过期
		if (AuthorizationUtils.isNotAuthenticated()) {
			String loginUrl = dreamServerProperties.getFrontendUri() + "/#/passport/login?redirect_uri=%s";
			String redirect_uri = UrlUtils.buildFullRequestUrl(request);
			String base64RequestUrl = Base64Helper.encodeUrlString(redirect_uri.getBytes());
			log.debug("No Authentication ... Redirect to /passport/login , redirect_uri {} , base64 {}", redirect_uri,
					base64RequestUrl);
			response.sendRedirect(String.format(loginUrl, base64RequestUrl));
			return false;
		}

		// 已经登录,判断应用访问权限
		if (AuthorizationUtils.isAuthenticated()) {
			log.debug("preHandle {}", request.getRequestURI());
			AppVO app = (AppVO) AuthWebContext.getAttribute(ConstAuthWeb.AUTHORIZE_SIGN_ON_APP);
			if (app == null) {
				String requestURI = request.getRequestURI();
				if (requestURI.contains("/authz/cas/login")) {
					// CAS登录回调,根据service参数获取APP信息
					app = casDetailsService.getAppDetails(request.getParameter(CasConstants.PARAMETER.SERVICE), true);
					// 防止用户登录但没有权限,跳转到/authz/refused时渲染报错
					AuthWebContext.setAttribute(ConstAuthWeb.AUTHORIZE_SIGN_ON_APP, app);
				} else if (requestURI.contains("/authz/jwt/") || requestURI.contains("/authz/api/")
						|| requestURI.contains("/authz/formbased/") || requestURI.contains("/authz/tokenbased/")
						|| requestURI.contains("/authz/api/") || requestURI.contains("/authz/saml20/consumer/")
						|| requestURI.contains("/authz/saml20/idpinit/") || requestURI.contains("/authz/cas/")) {// for
																													// id
																													// end
																													// of
																													// URL
					String[] requestURIs = requestURI.split("/");
					String appId = requestURIs[requestURIs.length - 1];
					log.debug("appId {}", appId);
					app = appsService.get(appId, true);
				} else if (requestURI.contains("/authz/oauth/v20/authorize")) {
					// OAuth2,OAuth2.1认证回调
					app = appsService.get(request.getParameter(OAuth2Utils.CLIENT_ID), true);
					// 防止用户登录但没有权限,跳转到/authz/refused时渲染报错
					AuthWebContext.setAttribute(ConstAuthWeb.AUTHORIZE_SIGN_ON_APP, app);
				}
			}

			if (app == null) {
				log.debug("preHandle app is not exist . ");
				return true;
			}

			// 获取当前用户登录信息,判断是否有登录当前APP的权限
			SignPrincipal principal = AuthorizationUtils.getPrincipal();
			if (principal != null && app != null) {
				if (principal.getGrantedAuthorityApps().contains(new SimpleGrantedAuthority(app.getId()))) {
					log.trace("preHandle have authority access {}", app);
					return true;
				}
			}
			log.debug("preHandle not have authority access " + app);

			response.sendRedirect(request.getContextPath() + ConstAuthView.AUTHZ_REFUSED);
			return false;
		}
		return true;
	}
}