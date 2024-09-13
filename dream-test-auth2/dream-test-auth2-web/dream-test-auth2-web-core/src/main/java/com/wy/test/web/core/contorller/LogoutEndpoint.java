package com.wy.test.web.core.contorller;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.wy.test.authentication.core.annotation.CurrentUser;
import com.wy.test.authentication.core.session.Session;
import com.wy.test.authentication.core.session.SessionManager;
import com.wy.test.core.base.ResultResponse;
import com.wy.test.core.constant.ConstProtocols;
import com.wy.test.core.properties.DreamAuthServerProperties;
import com.wy.test.core.vo.AppVO;
import com.wy.test.core.vo.UserVO;
import com.wy.test.protocol.authorize.singlelogout.DefaultSingleLogout;
import com.wy.test.protocol.authorize.singlelogout.LogoutType;
import com.wy.test.protocol.authorize.singlelogout.SamlSingleLogout;
import com.wy.test.protocol.authorize.singlelogout.SingleLogout;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "1-3-单点注销接口文档模块")
@Controller
@Slf4j
public class LogoutEndpoint {

	@Autowired
	DreamAuthServerProperties dreamServerProperties;

	@Autowired
	SessionManager sessionManager;

	/**
	 * for front end
	 * 
	 * @param currentUser
	 * @return ResponseEntity
	 */
	@Operation(summary = "前端注销接口", description = "前端注销接口", method = "GET")
	@GetMapping(value = { "/logout" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	public ResponseEntity<?> logout(@CurrentUser UserVO currentUser) {
		// if logined in have onlineTicket ,need remove or logout back
		String sessionId = currentUser.getSessionId();
		Session session = sessionManager.get(sessionId);
		if (session != null) {
			log.debug("/logout frontend clean Session id {}", session.getId());
			Set<Entry<String, AppVO>> entrySet = session.getAuthorizedApps().entrySet();

			Iterator<Entry<String, AppVO>> iterator = entrySet.iterator();
			while (iterator.hasNext()) {
				Entry<String, AppVO> mapEntry = iterator.next();
				log.debug("App Id : " + mapEntry.getKey() + " , " + mapEntry.getValue());
				if (mapEntry.getValue().getLogoutType() == LogoutType.BACK_CHANNEL) {
					SingleLogout singleLogout;
					if (mapEntry.getValue().getProtocol().equalsIgnoreCase(ConstProtocols.CAS)) {
						singleLogout = new SamlSingleLogout();
					} else {
						singleLogout = new DefaultSingleLogout();
					}
					singleLogout.sendRequest(session.getAuthentication(), mapEntry.getValue());
				}
			}
			// terminate session
			sessionManager.terminate(session.getId(), currentUser.getId(), currentUser.getUsername());
		}
		return new ResultResponse<String>().buildResponse();
	}

	@Operation(summary = "单点注销接口", description = "redirect_uri跳转地址", method = "GET")
	@GetMapping(value = { "/force/logout" })
	public ModelAndView forceLogout(HttpServletRequest request,
			@RequestParam(value = "redirect_uri", required = false) String redirect_uri) {
		// invalidate http session
		log.debug("/force/logout http Session id {}", request.getSession().getId());
		request.getSession().invalidate();
		StringBuffer logoutUrl = new StringBuffer("");
		logoutUrl.append(dreamServerProperties.getFrontendUri()).append("/#/passport/logout");
		if (StringUtils.isNotBlank(redirect_uri)) {
			logoutUrl.append("?").append("redirect_uri=").append(redirect_uri);
		}
		ModelAndView modelAndView = new ModelAndView("redirect");
		modelAndView.addObject("redirect_uri", logoutUrl);
		return modelAndView;
	}
}