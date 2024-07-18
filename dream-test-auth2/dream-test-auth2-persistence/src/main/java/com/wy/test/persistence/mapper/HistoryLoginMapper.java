package com.wy.test.persistence.mapper;

import java.util.List;

import org.dromara.mybatis.jpa.IJpaMapper;

import com.wy.test.core.entity.HistoryLogin;

public interface HistoryLoginMapper extends IJpaMapper<HistoryLogin> {

	public List<HistoryLogin> queryOnlineSession(HistoryLogin historyLogin);

}
