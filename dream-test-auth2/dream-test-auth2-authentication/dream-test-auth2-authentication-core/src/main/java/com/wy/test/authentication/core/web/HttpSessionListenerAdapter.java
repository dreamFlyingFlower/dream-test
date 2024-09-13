package com.wy.test.authentication.core.web;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;

import com.wy.test.authentication.core.entity.SignPrincipal;
import com.wy.test.core.constant.ConstAuthWeb;

import dream.flying.flower.helper.DateTimeHelper;
import lombok.extern.slf4j.Slf4j;

@WebListener
@Slf4j
public class HttpSessionListenerAdapter implements HttpSessionListener {

	public HttpSessionListenerAdapter() {
		super();
		log.debug("SessionListenerAdapter inited . ");
	}

	/**
	 * session Created
	 */
	@Override
	public void sessionCreated(HttpSessionEvent sessionEvent) {
		log.trace("new session Created :" + sessionEvent.getSession().getId());
	}

	/**
	 * session Destroyed
	 */
	@Override
	public void sessionDestroyed(HttpSessionEvent sessionEvent) {
		HttpSession session = sessionEvent.getSession();
		Authentication authentication = (Authentication) session.getAttribute(ConstAuthWeb.AUTHENTICATION);
		Object principal = authentication == null ? null : authentication.getPrincipal();
		log.trace("principal {}", principal);
		if (principal != null) {
			if (principal instanceof SignPrincipal && ((SignPrincipal) principal).getUserInfo() != null) {
				SignPrincipal signPrincipal = (SignPrincipal) principal;
				log.trace("{} HttpSession Id  {} for userId  {} , username {} @Ticket {} Destroyed",
						DateTimeHelper.formatDateTime(), session.getId(), signPrincipal.getUserInfo().getId(),
						signPrincipal.getUserInfo().getUsername(), signPrincipal.getSession().getId());
			} else if (principal instanceof User) {
				User user = (User) principal;
				log.trace("{} HttpSession Id  {} for username {} password {} Destroyed",
						DateTimeHelper.formatDateTime(), session.getId(), user.getUsername(), user.getPassword());
			} else {
				log.trace("{} HttpSession Id  {} for principal {} Destroyed", DateTimeHelper.formatDateTime(),
						session.getId(), principal);
			}
		} else {
			log.trace("{} HttpSession Id  {} Destroyed", DateTimeHelper.formatDateTime(), session.getId());
		}
	}
}