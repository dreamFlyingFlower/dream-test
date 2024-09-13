package com.wy.test.authentication.core.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.wy.test.authentication.core.jwt.AuthJwt;
import com.wy.test.authentication.core.jwt.AuthRefreshTokenService;
import com.wy.test.authentication.core.jwt.AuthTokenService;
import com.wy.test.authentication.core.session.Session;
import com.wy.test.authentication.core.session.SessionManager;
import com.wy.test.core.base.ResultResponse;
import com.wy.test.core.web.AuthWebContext;

import dream.flying.flower.lang.StrHelper;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping(value = "/auth")
@Slf4j
public class AuthTokenRefreshPoint {

	@Autowired
	AuthTokenService authTokenService;

	@Autowired
	AuthRefreshTokenService refreshTokenService;

	@Autowired
	SessionManager sessionManager;

	@GetMapping(value = { "/token/refresh" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> refresh(HttpServletRequest request,
			@RequestParam(name = "refresh_token", required = false) String refreshToken) {
		log.debug("try to refresh token ");
		log.trace("refresh token {} ", refreshToken);
		if (log.isTraceEnabled()) {
			AuthWebContext.printRequest(request);
		}
		try {
			if (StrHelper.isNotBlank(refreshToken) && refreshTokenService.validateJwtToken(refreshToken)) {
				String sessionId = refreshTokenService.resolveJWTID(refreshToken);
				log.trace("Try to  refresh sessionId [{}]", sessionId);
				Session session = sessionManager.refresh(sessionId);
				if (session != null) {
					AuthJwt authJwt = authTokenService.genAuthJwt(session.getAuthentication());
					log.trace("Grant new token {}", authJwt);
					return new ResultResponse<AuthJwt>(authJwt).buildResponse();
				} else {
					log.debug("Session is timeout , sessionId [{}]", sessionId);
				}
			} else {
				log.debug("refresh token is not validate .");
			}
		} catch (Exception e) {
			log.error("Refresh Exception !", e);
		}
		return new ResponseEntity<>("Refresh Token Fail !", HttpStatus.UNAUTHORIZED);
	}
}
