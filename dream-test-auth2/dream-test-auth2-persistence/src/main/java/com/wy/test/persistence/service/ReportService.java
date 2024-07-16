package com.wy.test.persistence.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dromara.mybatis.jpa.JpaService;
import org.dromara.mybatis.jpa.entity.JpaEntity;
import org.springframework.stereotype.Repository;

import com.wy.test.persistence.mapper.ReportMapper;

@Repository
public class ReportService extends JpaService<JpaEntity> {

	public Integer analysisDay(HashMap<String, Object> reportParameter) {
		return getMapper().analysisDay(reportParameter);
	};

	public Integer analysisNewUsers(HashMap<String, Object> reportParameter) {
		return getMapper().analysisNewUsers(reportParameter);
	};

	public Integer analysisOnlineUsers(HashMap<String, Object> reportParameter) {
		return getMapper().analysisOnlineUsers(reportParameter);
	};

	public Integer analysisActiveUsers(HashMap<String, Object> reportParameter) {
		return getMapper().analysisActiveUsers(reportParameter);
	};

	public List<Map<String, Object>> analysisDayHour(HashMap<String, Object> reportParameter) {
		return getMapper().analysisDayHour(reportParameter);
	}

	public List<Map<String, Object>> analysisMonth(HashMap<String, Object> reportParameter) {
		return getMapper().analysisMonth(reportParameter);
	}

	public List<Map<String, Object>> analysisBrowser(HashMap<String, Object> reportParameter) {
		return getMapper().analysisBrowser(reportParameter);
	}

	public List<Map<String, Object>> analysisApp(HashMap<String, Object> reportParameter) {
		return getMapper().analysisApp(reportParameter);
	}

	public ReportService() {
		super(ReportMapper.class);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.connsec.db.service.BaseService#getMapper()
	 */
	@Override
	public ReportMapper getMapper() {
		return (ReportMapper) super.getMapper();
	}
}
