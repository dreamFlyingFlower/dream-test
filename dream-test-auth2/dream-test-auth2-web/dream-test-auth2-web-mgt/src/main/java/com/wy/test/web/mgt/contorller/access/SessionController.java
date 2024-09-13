package com.wy.test.web.mgt.contorller.access;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wy.test.authentication.core.annotation.CurrentUser;
import com.wy.test.authentication.core.session.SessionManager;
import com.wy.test.core.base.ResultResponse;
import com.wy.test.core.entity.HistoryLoginEntity;
import com.wy.test.core.entity.UserEntity;
import com.wy.test.core.vo.UserVO;
import com.wy.test.persistence.service.HistoryLoginService;
import com.wy.test.persistence.service.HistorySysLogService;

import dream.flying.flower.ConstDate;
import dream.flying.flower.lang.StrHelper;
import lombok.extern.slf4j.Slf4j;

/**
 * 登录会话管理.
 */
@Controller
@RequestMapping(value = { "/access/session" })
@Slf4j
public class SessionController {

	@Autowired
	HistoryLoginService historyLoginService;

	@Autowired
	SessionManager sessionManager;

	@Autowired
	HistorySysLogService systemLog;

	/**
	 * 查询登录日志.
	 * 
	 * @param logsAuth
	 * @return
	 */
	@GetMapping(value = { "/fetch" })
	@ResponseBody
	public ResponseEntity<?> fetch(HistoryLoginEntity historyLogin, @CurrentUser UserEntity currentUser) {
		log.debug("history/session/fetch {}", historyLogin);
		historyLogin.setInstId(currentUser.getInstId());
		return new ResultResponse<>(historyLoginService.queryOnlineSession(historyLogin)).buildResponse();
	}

	@ResponseBody
	@GetMapping(value = "/terminate")
	public ResponseEntity<?> terminate(@RequestParam("ids") String ids, @CurrentUser UserVO currentUser) {
		log.debug(ids);
		boolean isTerminated = false;
		try {
			for (String sessionId : StrHelper.split(ids, ",")) {
				log.trace("terminate session Id {} ", sessionId);
				if (currentUser.getSessionId().contains(sessionId)) {
					continue;// skip current session
				}
				sessionManager.terminate(sessionId, currentUser.getId(), currentUser.getUsername());
			}
			isTerminated = true;
		} catch (Exception e) {
			log.debug("terminate Exception .", e);
		}

		if (isTerminated) {
			return new ResultResponse<HistoryLoginEntity>(ResultResponse.SUCCESS).buildResponse();
		} else {
			return new ResultResponse<HistoryLoginEntity>(ResultResponse.ERROR).buildResponse();
		}
	}

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(ConstDate.TIME);
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}
}