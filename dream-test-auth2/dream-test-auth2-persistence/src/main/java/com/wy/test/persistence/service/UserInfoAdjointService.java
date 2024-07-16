package com.wy.test.persistence.service;

import org.dromara.mybatis.jpa.JpaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.wy.test.entity.UserInfoAdjoint;
import com.wy.test.persistence.mapper.UserInfoAdjointMapper;

@Repository
public class UserInfoAdjointService extends JpaService<UserInfoAdjoint> {

	final static Logger _logger = LoggerFactory.getLogger(UserInfoAdjointService.class);

	public UserInfoAdjointService() {
		super(UserInfoAdjointMapper.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.connsec.db.service.BaseService#getMapper()
	 */
	@Override
	public UserInfoAdjointMapper getMapper() {
		return (UserInfoAdjointMapper) super.getMapper();
	}

}
