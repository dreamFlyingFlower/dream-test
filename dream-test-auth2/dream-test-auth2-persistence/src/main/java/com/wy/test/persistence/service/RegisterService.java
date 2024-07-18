package com.wy.test.persistence.service;

import java.util.List;

import org.dromara.mybatis.jpa.JpaService;
import org.springframework.stereotype.Repository;

import com.wy.test.core.entity.Register;
import com.wy.test.core.entity.UserInfo;
import com.wy.test.persistence.mapper.RegisterMapper;

@Repository
public class RegisterService extends JpaService<Register> {

	public RegisterService() {
		super(RegisterMapper.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.connsec.db.service.BaseService#getMapper()
	 */
	@Override
	public RegisterMapper getMapper() {
		return (RegisterMapper) super.getMapper();
	}

	public UserInfo findByEmail(String email) {
		List<UserInfo> listUserInfo = getMapper().findByEmail(email);
		return listUserInfo.size() > 0 ? listUserInfo.get(0) : null;
	}

}
