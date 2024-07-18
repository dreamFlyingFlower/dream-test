package com.wy.test.persistence.mapper;

import java.util.List;

import org.dromara.mybatis.jpa.IJpaMapper;

import com.wy.test.core.entity.Register;
import com.wy.test.core.entity.UserInfo;


public interface RegisterMapper extends IJpaMapper<Register> {

	public List<UserInfo> findByEmail(String email);

}
