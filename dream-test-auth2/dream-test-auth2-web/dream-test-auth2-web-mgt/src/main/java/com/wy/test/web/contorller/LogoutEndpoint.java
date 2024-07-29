package com.wy.test.web.contorller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.wy.test.core.authn.annotation.CurrentUser;
import com.wy.test.core.authn.session.SessionManager;
import com.wy.test.core.entity.Message;
import com.wy.test.core.entity.UserInfo;

@Controller
public class LogoutEndpoint {

	private static Logger _logger = LoggerFactory.getLogger(LogoutEndpoint.class);

	@Autowired
	protected SessionManager sessionManager;

	@GetMapping(value = { "/logout" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> logout(HttpServletRequest request, @CurrentUser UserInfo currentUser) {
		sessionManager.terminate(currentUser.getSessionId(), currentUser.getId(), currentUser.getUsername());
		// invalidate http session
		_logger.debug("/logout invalidate http Session id {}", request.getSession().getId());
		request.getSession().invalidate();
		return new Message<String>().buildResponse();
	}
}