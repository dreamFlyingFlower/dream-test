package com.wy.test.web.historys.contorller;

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
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wy.test.common.entity.Message;
import com.wy.test.common.util.DateUtils;
import com.wy.test.core.authn.annotation.CurrentUser;
import com.wy.test.core.entity.HistorySystemLogs;
import com.wy.test.core.entity.UserInfo;
import com.wy.test.persistence.service.HistorySystemLogsService;

/**
 * 系统操作日志查询
 *
 */
@Controller
@RequestMapping(value = { "/historys" })
public class SystemLogsController {

	final static Logger _logger = LoggerFactory.getLogger(SystemLogsController.class);

	@Autowired
	HistorySystemLogsService historySystemLogsService;

	/**
	 * 查询操作日志
	 * 
	 * @param logs
	 * @return
	 */
	@PostMapping(value = { "/systemLogs/fetch" })
	@ResponseBody
	public ResponseEntity<?> fetch(@ModelAttribute("historyLog") HistorySystemLogs historyLog,
			@CurrentUser UserInfo currentUser) {
		_logger.debug("historys/historyLog/fetch {} ", historyLog);
		historyLog.setInstId(currentUser.getInstId());
		return new Message<JpaPageResults<HistorySystemLogs>>(historySystemLogsService.fetchPageResults(historyLog))
				.buildResponse();
	}

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(DateUtils.FORMAT_DATE_HH_MM_SS);
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}
}
