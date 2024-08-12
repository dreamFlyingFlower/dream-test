package com.wy.test.web.mgt.history.contorller;

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
import com.wy.test.core.entity.HistoryConnectorEntity;
import com.wy.test.core.entity.Message;
import com.wy.test.core.entity.UserEntity;
import com.wy.test.persistence.service.HistoryConnectorService;

import dream.flying.flower.ConstDate;
import lombok.extern.slf4j.Slf4j;

/**
 * 连接器日志查询
 * 
 */
@Controller
@RequestMapping(value = { "/historys" })
@Slf4j
public class ConnectorHistoryController {

	@Autowired
	HistoryConnectorService historyConnectorService;

	/**
	 * @param historySynchronizer
	 * @return
	 */
	@PostMapping(value = { "/connectorHistory/fetch" })
	@ResponseBody
	public ResponseEntity<?> fetch(@ModelAttribute("historyConnector") HistoryConnectorEntity historyConnector,
			@CurrentUser UserEntity currentUser) {
		log.debug("historys/historyConnector/fetch/ {}", historyConnector);
		historyConnector.setInstId(currentUser.getInstId());
		return new Message<>(historyConnectorService.list(historyConnector)).buildResponse();
	}

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(ConstDate.TIME);
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}
}