package com.wy.test.persistence.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.wy.test.persistence.mapper.ReportMapper;
import com.wy.test.persistence.service.ReportService;

@Service
public class ReportServiceImpl implements ReportService {

	private ReportMapper reportMapper;

	@Override
	public Integer analysisDay(HashMap<String, Object> reportParameter) {
		return reportMapper.analysisDay(reportParameter);
	};

	@Override
	public Integer analysisNewUsers(HashMap<String, Object> reportParameter) {
		return reportMapper.analysisNewUsers(reportParameter);
	};

	@Override
	public Integer analysisOnlineUsers(HashMap<String, Object> reportParameter) {
		return reportMapper.analysisOnlineUsers(reportParameter);
	};

	@Override
	public Integer analysisActiveUsers(HashMap<String, Object> reportParameter) {
		return reportMapper.analysisActiveUsers(reportParameter);
	};

	@Override
	public List<Map<String, Object>> analysisDayHour(HashMap<String, Object> reportParameter) {
		return reportMapper.analysisDayHour(reportParameter);
	}

	@Override
	public List<Map<String, Object>> analysisMonth(HashMap<String, Object> reportParameter) {
		return reportMapper.analysisMonth(reportParameter);
	}

	@Override
	public List<Map<String, Object>> analysisBrowser(HashMap<String, Object> reportParameter) {
		return reportMapper.analysisBrowser(reportParameter);
	}

	@Override
	public List<Map<String, Object>> analysisApp(HashMap<String, Object> reportParameter) {
		return reportMapper.analysisApp(reportParameter);
	}
}