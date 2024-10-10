package com.wy.test.web.mgt.contorller.history;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wy.test.authentication.core.annotation.CurrentUser;
import com.wy.test.core.base.ResultResponse;
import com.wy.test.core.entity.HistoryLoginEntity;
import com.wy.test.core.entity.UserEntity;
import com.wy.test.persistence.service.HistoryLoginService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "1-10 用户登录日志API")
@Slf4j
@RestController
@RequestMapping(value = { "/historys" })
public class LoginHistoryController {

	@Autowired
	HistoryLoginService loginHistoryService;

	@Operation(summary = "查询列表", description = "查询列表", method = "POST")
	@PostMapping(value = { "/loginHistory/fetch" })
	public ResponseEntity<?> fetch(@RequestBody HistoryLoginEntity historyLogin, @CurrentUser UserEntity currentUser) {
		log.debug("historys/loginHistory/fetch/ {}", historyLogin);
		historyLogin.setInstId(currentUser.getInstId());
		return new ResultResponse<>(loginHistoryService.list(historyLogin)).buildResponse();
	}

	// @InitBinder
	// public void initBinder(WebDataBinder binder) {
	// SimpleDateFormat dateFormat = new SimpleDateFormat(ConstDate.TIME);
	// dateFormat.setLenient(false);
	// binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat,
	// true));
	// }
}