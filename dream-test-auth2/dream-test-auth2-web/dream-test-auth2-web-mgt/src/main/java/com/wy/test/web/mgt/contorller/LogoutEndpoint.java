package com.wy.test.web.mgt.contorller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.wy.test.authentication.core.authn.annotation.CurrentUser;
import com.wy.test.authentication.core.authn.session.SessionManager;
import com.wy.test.core.entity.Message;
import com.wy.test.core.vo.UserVO;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class LogoutEndpoint {

	@Autowired
	protected SessionManager sessionManager;

	@GetMapping(value = { "/logout" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> logout(HttpServletRequest request, @CurrentUser UserVO currentUser) {
		sessionManager.terminate(currentUser.getSessionId(), currentUser.getId(), currentUser.getUsername());
		// invalidate http session
		log.debug("/logout invalidate http Session id {}", request.getSession().getId());
		request.getSession().invalidate();
		return new Message<String>().buildResponse();
	}
}