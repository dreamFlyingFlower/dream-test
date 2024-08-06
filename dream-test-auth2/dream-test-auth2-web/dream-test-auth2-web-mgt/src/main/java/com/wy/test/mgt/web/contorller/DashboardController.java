package com.wy.test.mgt.web.contorller;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.wy.test.core.authn.annotation.CurrentUser;
import com.wy.test.core.entity.Message;
import com.wy.test.core.entity.UserEntity;
import com.wy.test.persistence.service.ReportService;

/**
 * Index
 */
@Controller
public class DashboardController {

	private static Logger _logger = LoggerFactory.getLogger(DashboardController.class);

	@Autowired
	ReportService reportService;

	@GetMapping(value = { "/dashboard" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> dashboard(@CurrentUser UserEntity currentUser) {
		_logger.debug("IndexController /dashboard.");
		HashMap<String, Object> reportParameter = new HashMap<String, Object>();
		reportParameter.put("instId", currentUser.getInstId());

		reportParameter.put("dayCount", reportService.analysisDay(reportParameter));
		reportParameter.put("newUsers", reportService.analysisNewUsers(reportParameter));

		reportParameter.put("onlineUsers", reportService.analysisOnlineUsers(reportParameter));
		reportParameter.put("activeUsers", reportService.analysisActiveUsers(reportParameter));

		reportParameter.put("reportMonth", reportService.analysisMonth(reportParameter));
		reportParameter.put("reportDayHour", reportService.analysisDayHour(reportParameter));

		reportParameter.put("reportBrowser", reportService.analysisBrowser(reportParameter));
		reportParameter.put("reportApp", reportService.analysisApp(reportParameter));
		return new Message<HashMap<?, ?>>(reportParameter).buildResponse();
	}

}
