package com.wy.test.web.web.historys.contorller;

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
import com.wy.test.core.entity.HistoryLoginAppEntity;
import com.wy.test.core.entity.Message;
import com.wy.test.core.entity.UserEntity;
import com.wy.test.persistence.service.HistoryLoginAppsService;

import dream.flying.flower.ConstDate;

/**
 * 单点登录日志查询
 * 
 *
 */
@Controller
@RequestMapping(value = { "/historys" })
public class LoginAppsHistoryController {

	final static Logger _logger = LoggerFactory.getLogger(LoginAppsHistoryController.class);

	@Autowired
	protected HistoryLoginAppsService historyLoginAppsService;

	/**
	 * @param loginAppsHistory
	 * @return
	 */
	@PostMapping(value = { "/loginAppsHistory/fetch" })
	@ResponseBody
	public ResponseEntity<?> fetch(@ModelAttribute("historyLoginApp") HistoryLoginAppEntity historyLoginApp,
			@CurrentUser UserEntity currentUser) {
		_logger.debug("historys/loginAppsHistory/fetch/  {}", historyLoginApp);
		historyLoginApp.setId(null);
		historyLoginApp.setUserId(currentUser.getId());
		historyLoginApp.setInstId(currentUser.getInstId());
		return new Message<JpaPageResults<HistoryLoginAppEntity>>(historyLoginAppsService.fetchPageResults(historyLoginApp))
				.buildResponse();
	}

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(ConstDate.TIME);
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}
}
