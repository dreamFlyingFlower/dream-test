package com.wy.test.persistence.mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ReportMapper {

	Integer analysisDay(HashMap<String, Object> reportParameter);

	Integer analysisNewUsers(HashMap<String, Object> reportParameter);

	Integer analysisOnlineUsers(HashMap<String, Object> reportParameter);

	Integer analysisActiveUsers(HashMap<String, Object> reportParameter);

	List<Map<String, Object>> analysisDayHour(HashMap<String, Object> reportParameter);

	List<Map<String, Object>> analysisMonth(HashMap<String, Object> reportParameter);

	List<Map<String, Object>> analysisBrowser(HashMap<String, Object> reportParameter);

	List<Map<String, Object>> analysisApp(HashMap<String, Object> reportParameter);
}