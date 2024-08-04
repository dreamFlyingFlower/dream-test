package com.wy.test.persistence.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.wy.test.core.convert.RegisterConvert;
import com.wy.test.core.entity.RegisterEntity;
import com.wy.test.core.entity.UserEntity;
import com.wy.test.core.query.RegisterQuery;
import com.wy.test.core.vo.RegisterVO;
import com.wy.test.persistence.mapper.RegisterMapper;
import com.wy.test.persistence.service.RegisterService;

import dream.flying.flower.framework.mybatis.plus.service.impl.AbstractServiceImpl;

/**
 * 用户注册
 *
 * @author 飞花梦影
 * @date 2024-08-01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Service
public class RegisterServiceImpl
		extends AbstractServiceImpl<RegisterEntity, RegisterVO, RegisterQuery, RegisterConvert, RegisterMapper>
		implements RegisterService {

	@Override
	public UserEntity findByEmail(String email) {
		List<UserEntity> listUserInfo = baseMapper.findByEmail(email);
		return listUserInfo.size() > 0 ? listUserInfo.get(0) : null;
	}
}