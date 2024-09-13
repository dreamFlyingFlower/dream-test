package com.wy.test.protocol.cas.endpoint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.wy.test.core.web.AuthWebContext;
import com.wy.test.protocol.cas.endpoint.ticket.CasConstants;

import dream.flying.flower.lang.StrHelper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 
 *
 * @author 飞花梦影
 * @date 2024-07-18 22:15:41
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Tag(name = "2-3-CAS API文档模块")
@Controller
public class CasLogoutEndpoint extends CasBaseAuthorizeEndpoint {

	/**
	 * for cas logout then redirect to logout
	 * 
	 * @param request
	 * @param response
	 * @param casService
	 * @return
	 */
	@Operation(summary = "CAS注销接口", description = "CAS注销接口", method = "GET")
	@GetMapping(CasConstants.ENDPOINT.ENDPOINT_LOGOUT)
	public ModelAndView logout(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = CasConstants.PARAMETER.SERVICE, required = false) String casService) {
		StringBuffer logoutUrl = new StringBuffer("/force/logout");
		if (StrHelper.isNotBlank(casService)) {
			logoutUrl.append("?").append("redirect_uri=").append(casService);
		}
		return AuthWebContext.forward(logoutUrl.toString());
	}
}