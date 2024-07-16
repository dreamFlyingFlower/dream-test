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

import com.wy.test.core.authn.annotation.CurrentUser;
import com.wy.test.entity.HistorySynchronizer;
import com.wy.test.entity.Message;
import com.wy.test.entity.UserInfo;
import com.wy.test.persistence.service.HistorySynchronizerService;
import com.wy.test.util.DateUtils;

/**
 * 同步器日志查询
 */
@Controller
@RequestMapping(value = { "/historys" })
public class SynchronizerHistoryController {

	final static Logger _logger = LoggerFactory.getLogger(SynchronizerHistoryController.class);

	@Autowired
	HistorySynchronizerService historySynchronizerService;

	/**
	 * @param historySynchronizer
	 * @return
	 */
	@PostMapping(value = { "/synchronizerHistory/fetch" })
	@ResponseBody
	public ResponseEntity<?> fetch(@ModelAttribute("historySynchronizer") HistorySynchronizer historySynchronizer,
			@CurrentUser UserInfo currentUser) {
		_logger.debug("historys/synchronizerHistory/fetch/ {}", historySynchronizer);
		historySynchronizer.setInstId(currentUser.getInstId());
		return new Message<JpaPageResults<HistorySynchronizer>>(
				historySynchronizerService.fetchPageResults(historySynchronizer)).buildResponse();
	}

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(DateUtils.FORMAT_DATE_HH_MM_SS);
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}
}
