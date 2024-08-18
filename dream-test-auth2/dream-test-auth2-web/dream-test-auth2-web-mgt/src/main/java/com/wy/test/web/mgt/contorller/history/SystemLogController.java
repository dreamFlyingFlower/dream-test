package com.wy.test.web.mgt.contorller.history;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wy.test.authentication.core.authn.annotation.CurrentUser;
import com.wy.test.core.entity.HistorySysLogEntity;
import com.wy.test.core.entity.Message;
import com.wy.test.core.entity.UserEntity;
import com.wy.test.persistence.service.HistorySysLogService;

import dream.flying.flower.ConstDate;
import lombok.extern.slf4j.Slf4j;

/**
 * 系统操作日志查询
 *
 */
@Controller
@RequestMapping(value = { "/historys" })
@Slf4j
public class SystemLogController {

	@Autowired
	HistorySysLogService historySystemLogsService;

	/**
	 * 查询操作日志
	 * 
	 * @param logs
	 * @return
	 */
	@PostMapping(value = { "/systemLogs/fetch" })
	@ResponseBody
	public ResponseEntity<?> fetch(@ModelAttribute("historyLog") HistorySysLogEntity historyLog,
			@CurrentUser UserEntity currentUser) {
		log.debug("historys/historyLog/fetch {} ", historyLog);
		historyLog.setInstId(currentUser.getInstId());
		return new Message<>(historySystemLogsService.list(historyLog)).buildResponse();
	}

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(ConstDate.TIME);
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}
}
