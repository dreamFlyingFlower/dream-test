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
import com.wy.test.core.entity.HistoryLogin;
import com.wy.test.core.entity.UserInfo;
import com.wy.test.entity.Message;
import com.wy.test.persistence.service.HistoryLoginService;
import com.wy.test.util.DateUtils;

/**
 * 登录日志查询
 */
@Controller
@RequestMapping(value = { "/historys" })
public class LoginHistoryController {

	final static Logger _logger = LoggerFactory.getLogger(LoginHistoryController.class);

	@Autowired
	HistoryLoginService loginHistoryService;

	/**
	 * @param HistoryLogin
	 * @return
	 */
	@PostMapping(value = { "/loginHistory/fetch" })
	@ResponseBody
	public ResponseEntity<?> fetch(@ModelAttribute("historyLogin") HistoryLogin historyLogin,
			@CurrentUser UserInfo currentUser) {
		_logger.debug("historys/loginHistory/fetch/ {}", historyLogin);
		historyLogin.setInstId(currentUser.getInstId());
		return new Message<JpaPageResults<HistoryLogin>>(loginHistoryService.fetchPageResults(historyLogin))
				.buildResponse();
	}

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(DateUtils.FORMAT_DATE_HH_MM_SS);
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}
}
