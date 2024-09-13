package com.wy.test.web.mgt.contorller;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wy.test.authentication.core.annotation.CurrentUser;
import com.wy.test.core.entity.UserEntity;
import com.wy.test.persistence.service.ReportService;

import dream.flying.flower.framework.web.controller.BaseResponseController;
import lombok.extern.slf4j.Slf4j;

/**
 * 首页
 *
 * @author 飞花梦影
 * @date 2024-08-08 11:49:37
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@RestController
@Slf4j
public class DashboardController implements BaseResponseController {

	@Autowired
	ReportService reportService;

	@GetMapping("dashboard")
	public ResponseEntity<?> dashboard(@CurrentUser UserEntity currentUser) {
		log.debug("IndexController /dashboard.");
		HashMap<String, Object> reportParameter = new HashMap<>();
		reportParameter.put("instId", currentUser.getInstId());

		reportParameter.put("dayCount", reportService.analysisDay(reportParameter));
		reportParameter.put("newUsers", reportService.analysisNewUsers(reportParameter));

		reportParameter.put("onlineUsers", reportService.analysisOnlineUsers(reportParameter));
		reportParameter.put("activeUsers", reportService.analysisActiveUsers(reportParameter));

		reportParameter.put("reportMonth", reportService.analysisMonth(reportParameter));
		reportParameter.put("reportDayHour", reportService.analysisDayHour(reportParameter));

		reportParameter.put("reportBrowser", reportService.analysisBrowser(reportParameter));
		reportParameter.put("reportApp", reportService.analysisApp(reportParameter));
		return ok(reportParameter);
	}
}