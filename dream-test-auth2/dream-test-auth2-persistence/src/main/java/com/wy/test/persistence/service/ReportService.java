package com.wy.test.persistence.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 报表
 *
 * @author 飞花梦影
 * @date 2024-08-04 23:25:10
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public interface ReportService {

	Integer analysisDay(HashMap<String, Object> reportParameter);

	Integer analysisNewUsers(HashMap<String, Object> reportParameter);

	Integer analysisOnlineUsers(HashMap<String, Object> reportParameter);

	Integer analysisActiveUsers(HashMap<String, Object> reportParameter);

	List<Map<String, Object>> analysisDayHour(HashMap<String, Object> reportParameter);

	List<Map<String, Object>> analysisMonth(HashMap<String, Object> reportParameter);

	List<Map<String, Object>> analysisBrowser(HashMap<String, Object> reportParameter);

	List<Map<String, Object>> analysisApp(HashMap<String, Object> reportParameter);
}