package com.wy.test.web.mgt.contorller.history;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wy.test.authentication.core.authn.annotation.CurrentUser;
import com.wy.test.core.entity.HistorySyncEntity;
import com.wy.test.core.entity.Message;
import com.wy.test.core.entity.UserEntity;
import com.wy.test.persistence.service.HistorySyncService;

import dream.flying.flower.ConstDate;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.extern.slf4j.Slf4j;

/**
 * 同步器日志查询
 */
@Controller
@RequestMapping(value = { "/historys" })
@Slf4j
public class SyncHistoryController {

	@Autowired
	HistorySyncService historySynchronizerService;

	/**
	 * @param historySynchronizer
	 * @return
	 */
	@PostMapping(value = { "/synchronizerHistory/fetch" })
	@ResponseBody
	public ResponseEntity<?> fetch(@RequestBody HistorySyncEntity historySynchronizer,
			@CurrentUser UserEntity currentUser) {
		log.debug("historys/synchronizerHistory/fetch/ {}", historySynchronizer);
		historySynchronizer.setInstId(currentUser.getInstId());
		return new Message<>(historySynchronizerService.list(historySynchronizer)).buildResponse();
	}

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(ConstDate.TIME);
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}
}
