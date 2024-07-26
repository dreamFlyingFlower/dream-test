package com.wy.test.web.web.access.contorller;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wy.test.core.authn.annotation.CurrentUser;
import com.wy.test.core.authn.session.SessionManager;
import com.wy.test.core.entity.HistoryLogin;
import com.wy.test.core.entity.UserInfo;
import com.wy.test.persistence.service.HistoryLoginService;

import dream.flying.flower.ConstDate;
import dream.flying.flower.lang.StrHelper;
import dream.flying.flower.result.Result;

/**
 * 登录会话管理.
 * 
 */
@RestController
@RequestMapping(value = { "/access/session" })
public class LoginSessionController {

	static final Logger _logger = LoggerFactory.getLogger(LoginSessionController.class);

	@Autowired
	HistoryLoginService historyLoginService;

	@Autowired
	SessionManager sessionManager;

	/**
	 * 查询登录日志
	 * 
	 * @param logsAuth
	 * @return
	 */
	@GetMapping(value = { "/fetch" })
	public ResponseEntity<?> fetch(@ModelAttribute("historyLogin") HistoryLogin historyLogin,
			@CurrentUser UserInfo currentUser) {
		_logger.debug("history/session/fetch {}", historyLogin);
		historyLogin.setUserId(currentUser.getId());
		historyLogin.setInstId(currentUser.getInstId());
		return ResponseEntity.ok(Result.ok(historyLoginService.queryOnlineSession(historyLogin)));
	}

	@GetMapping(value = "/terminate")
	public ResponseEntity<?> terminate(@RequestParam("ids") String ids, @CurrentUser UserInfo currentUser) {
		_logger.debug(ids);
		boolean isTerminated = false;
		try {
			for (String sessionId : StrHelper.split(ids, ",")) {
				_logger.trace("terminate session Id {} ", sessionId);
				if (currentUser.getSessionId().contains(sessionId)) {
					continue;// skip current session
				}

				sessionManager.terminate(sessionId, currentUser.getId(), currentUser.getUsername());
			}
			isTerminated = true;
		} catch (Exception e) {
			_logger.debug("terminate Exception .", e);
		}

		if (isTerminated) {
			return ResponseEntity.ok(Result.ok());
		} else {
			return ResponseEntity.ok(Result.error());
		}
	}

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(ConstDate.TIME);
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}
}