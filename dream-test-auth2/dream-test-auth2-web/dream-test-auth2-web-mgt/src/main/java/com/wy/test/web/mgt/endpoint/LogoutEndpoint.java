package com.wy.test.web.mgt.endpoint;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wy.test.authentication.core.annotation.CurrentUser;
import com.wy.test.authentication.core.session.SessionManager;
import com.wy.test.core.vo.UserVO;

import dream.flying.flower.framework.web.controller.BaseResponseController;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@AllArgsConstructor
public class LogoutEndpoint implements BaseResponseController {

	private final SessionManager sessionManager;

	@GetMapping("logout")
	public ResponseEntity<?> logout(HttpServletRequest request, @CurrentUser UserVO currentUser) {
		sessionManager.terminate(currentUser.getSessionId(), currentUser.getId(), currentUser.getUsername());
		log.debug("/logout invalidate http Session id {}", request.getSession().getId());
		request.getSession().invalidate();
		return ok();
	}
}