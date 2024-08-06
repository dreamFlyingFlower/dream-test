package com.wy.test.mgt.web.historys.contorller;

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

import com.wy.test.core.authn.annotation.CurrentUser;
import com.wy.test.core.entity.HistoryLoginAppEntity;
import com.wy.test.core.entity.Message;
import com.wy.test.core.entity.UserEntity;
import com.wy.test.persistence.service.HistoryLoginAppService;

import dream.flying.flower.ConstDate;
import lombok.extern.slf4j.Slf4j;

/**
 * 单点登录日志查询
 */
@Controller
@RequestMapping(value = { "/historys" })
@Slf4j
public class LoginAppsHistoryController {

	@Autowired
	protected HistoryLoginAppService historyLoginAppsService;

	/**
	 * @param loginAppsHistory
	 * @return
	 */
	@PostMapping(value = { "/loginAppsHistory/fetch" })
	@ResponseBody
	public ResponseEntity<?> fetch(@ModelAttribute("historyLoginApp") HistoryLoginAppEntity historyLoginApp,
			@CurrentUser UserEntity currentUser) {
		log.debug("historys/loginAppsHistory/fetch/  {}", historyLoginApp);
		historyLoginApp.setId(null);
		historyLoginApp.setInstId(currentUser.getInstId());
		return new Message<>(historyLoginAppsService.list(historyLoginApp)).buildResponse();
	}

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(ConstDate.TIME);
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}
}