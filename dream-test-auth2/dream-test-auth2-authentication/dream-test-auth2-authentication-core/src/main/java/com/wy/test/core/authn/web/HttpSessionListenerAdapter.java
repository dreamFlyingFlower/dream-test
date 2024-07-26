package com.wy.test.core.authn.web;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;

import com.wy.test.core.authn.SignPrincipal;
import com.wy.test.core.web.WebConstants;

import dream.flying.flower.helper.DateTimeHelper;

@WebListener
public class HttpSessionListenerAdapter implements HttpSessionListener {

	private static final Logger _logger = LoggerFactory.getLogger(HttpSessionListenerAdapter.class);

	public HttpSessionListenerAdapter() {
		super();
		_logger.debug("SessionListenerAdapter inited . ");
	}

	/**
	 * session Created
	 */
	@Override
	public void sessionCreated(HttpSessionEvent sessionEvent) {
		_logger.trace("new session Created :" + sessionEvent.getSession().getId());
	}

	/**
	 * session Destroyed
	 */
	@Override
	public void sessionDestroyed(HttpSessionEvent sessionEvent) {
		HttpSession session = sessionEvent.getSession();
		Authentication authentication = (Authentication) session.getAttribute(WebConstants.AUTHENTICATION);
		Object principal = authentication == null ? null : authentication.getPrincipal();
		_logger.trace("principal {}", principal);
		if (principal != null) {
			if (principal instanceof SignPrincipal && ((SignPrincipal) principal).getUserInfo() != null) {
				SignPrincipal signPrincipal = (SignPrincipal) principal;
				_logger.trace("{} HttpSession Id  {} for userId  {} , username {} @Ticket {} Destroyed",
						DateTimeHelper.formatDateTime(), session.getId(), signPrincipal.getUserInfo().getId(),
						signPrincipal.getUserInfo().getUsername(), signPrincipal.getSession().getId());
			} else if (principal instanceof User) {
				User user = (User) principal;
				_logger.trace("{} HttpSession Id  {} for username {} password {} Destroyed",
						DateTimeHelper.formatDateTime(), session.getId(), user.getUsername(), user.getPassword());
			} else {
				_logger.trace("{} HttpSession Id  {} for principal {} Destroyed", DateTimeHelper.formatDateTime(),
						session.getId(), principal);
			}
		} else {
			_logger.trace("{} HttpSession Id  {} Destroyed", DateTimeHelper.formatDateTime(), session.getId());
		}
	}

}
