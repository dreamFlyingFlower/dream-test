package com.wy.test.persistence.mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dromara.mybatis.jpa.IJpaMapper;
import org.dromara.mybatis.jpa.entity.JpaEntity;

public interface ReportMapper extends IJpaMapper<JpaEntity> {

	public Integer analysisDay(HashMap<String, Object> reportParameter);

	public Integer analysisNewUsers(HashMap<String, Object> reportParameter);

	public Integer analysisOnlineUsers(HashMap<String, Object> reportParameter);

	public Integer analysisActiveUsers(HashMap<String, Object> reportParameter);

	public List<Map<String, Object>> analysisDayHour(HashMap<String, Object> reportParameter);

	public List<Map<String, Object>> analysisMonth(HashMap<String, Object> reportParameter);

	public List<Map<String, Object>> analysisBrowser(HashMap<String, Object> reportParameter);

	public List<Map<String, Object>> analysisApp(HashMap<String, Object> reportParameter);

}
