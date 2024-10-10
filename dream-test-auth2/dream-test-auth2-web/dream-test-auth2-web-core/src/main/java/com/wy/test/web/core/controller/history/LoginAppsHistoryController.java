package com.wy.test.web.core.controller.history;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wy.test.authentication.core.annotation.CurrentUser;
import com.wy.test.core.base.ResultResponse;
import com.wy.test.core.entity.HistoryLoginAppEntity;
import com.wy.test.core.entity.UserEntity;
import com.wy.test.persistence.service.HistoryLoginAppService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "1-11 APP登录日志API")
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(value = { "/historys" })
public class LoginAppsHistoryController {

	protected final HistoryLoginAppService historyLoginAppService;

	@Operation(summary = "查询列表", description = "查询列表", method = "POST")
	@PostMapping(value = { "/loginAppsHistory/fetch" })
	public ResponseEntity<?> fetch(@RequestBody HistoryLoginAppEntity historyLoginApp,
			@CurrentUser UserEntity currentUser) {
		log.debug("historys/loginAppsHistory/fetch/  {}", historyLoginApp);
		historyLoginApp.setId(null);
		historyLoginApp.setUserId(currentUser.getId());
		historyLoginApp.setInstId(currentUser.getInstId());
		return new ResultResponse<>(historyLoginAppService.list(historyLoginApp)).buildResponse();
	}

	// @InitBinder
	// public void initBinder(WebDataBinder binder) {
	// SimpleDateFormat dateFormat = new SimpleDateFormat(ConstDate.TIME);
	// dateFormat.setLenient(false);
	// binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat,
	// true));
	// }
}