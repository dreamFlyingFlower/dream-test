package com.wy.test.web.core.contorller.history;

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
import com.wy.test.core.entity.HistoryLoginEntity;
import com.wy.test.core.entity.Message;
import com.wy.test.core.entity.UserEntity;
import com.wy.test.persistence.service.HistoryLoginService;

import dream.flying.flower.ConstDate;
import lombok.extern.slf4j.Slf4j;

/**
 * 登录日志查询
 */
@Controller
@RequestMapping(value = { "/historys" })
@Slf4j
public class LoginHistoryController {

	@Autowired
	HistoryLoginService loginHistoryService;

	/**
	 * @param HistoryLoginEntity
	 * @return
	 */
	@PostMapping(value = { "/loginHistory/fetch" })
	@ResponseBody
	public ResponseEntity<?> fetch(@ModelAttribute("historyLogin") HistoryLoginEntity historyLogin,
			@CurrentUser UserEntity currentUser) {
		log.debug("historys/loginHistory/fetch/ {}", historyLogin);
		historyLogin.setInstId(currentUser.getInstId());
		historyLogin.setUserId(currentUser.getId());
		return new Message<>(loginHistoryService.list(historyLogin)).buildResponse();
	}

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(ConstDate.TIME);
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}
}