package com.wy.test.persistence.service;

import org.dromara.mybatis.jpa.JpaService;
import org.dromara.mybatis.jpa.entity.JpaPageResults;
import org.springframework.stereotype.Repository;

import com.wy.test.entity.HistoryLogin;
import com.wy.test.persistence.mapper.HistoryLoginMapper;

@Repository
public class HistoryLoginService extends JpaService<HistoryLogin> {

	public HistoryLoginService() {
		super(HistoryLoginMapper.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.connsec.db.service.BaseService#getMapper()
	 */
	@Override
	public HistoryLoginMapper getMapper() {
		return (HistoryLoginMapper) super.getMapper();
	}

	public JpaPageResults<HistoryLogin> queryOnlineSession(HistoryLogin historyLogin) {
		return this.fetchPageResults("queryOnlineSession", historyLogin);
	}
}
