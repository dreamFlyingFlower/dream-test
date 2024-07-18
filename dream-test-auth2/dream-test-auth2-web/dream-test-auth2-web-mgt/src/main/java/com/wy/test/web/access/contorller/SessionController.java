package com.wy.test.web.access.contorller;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.dromara.mybatis.jpa.entity.JpaPageResults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wy.test.core.authn.annotation.CurrentUser;
import com.wy.test.core.authn.session.SessionManager;
import com.wy.test.core.entity.HistoryLogin;
import com.wy.test.core.entity.UserInfo;
import com.wy.test.entity.Message;
import com.wy.test.persistence.service.HistoryLoginService;
import com.wy.test.persistence.service.HistorySystemLogsService;
import com.wy.test.util.DateUtils;

import dream.flying.flower.lang.StrHelper;

/**
 * 登录会话管理.
 *
 */
@Controller
@RequestMapping(value = { "/access/session" })
public class SessionController {

	static final Logger _logger = LoggerFactory.getLogger(SessionController.class);

	@Autowired
	HistoryLoginService historyLoginService;

	@Autowired
	SessionManager sessionManager;

	@Autowired
	HistorySystemLogsService systemLog;

	/**
	 * 查询登录日志.
	 * 
	 * @param logsAuth
	 * @return
	 */
	@GetMapping(value = { "/fetch" })
	@ResponseBody
	public ResponseEntity<?> fetch(@ModelAttribute("historyLogin") HistoryLogin historyLogin,
			@CurrentUser UserInfo currentUser) {
		_logger.debug("history/session/fetch {}", historyLogin);
		historyLogin.setInstId(currentUser.getInstId());
		return new Message<JpaPageResults<HistoryLogin>>(historyLoginService.queryOnlineSession(historyLogin))
				.buildResponse();
	}

	@ResponseBody
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
			return new Message<HistoryLogin>(Message.SUCCESS).buildResponse();
		} else {
			return new Message<HistoryLogin>(Message.ERROR).buildResponse();
		}
	}

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(DateUtils.FORMAT_DATE_HH_MM_SS);
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}
}
