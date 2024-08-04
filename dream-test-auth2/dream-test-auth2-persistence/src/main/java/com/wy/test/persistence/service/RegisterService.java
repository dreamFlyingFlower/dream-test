package com.wy.test.persistence.service;

import com.wy.test.core.entity.RegisterEntity;
import com.wy.test.core.entity.UserEntity;
import com.wy.test.core.query.RegisterQuery;
import com.wy.test.core.vo.RegisterVO;

import dream.flying.flower.framework.mybatis.plus.service.BaseServices;

/**
 * 用户注册
 *
 * @author 飞花梦影
 * @date 2024-08-01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public interface RegisterService extends BaseServices<RegisterEntity, RegisterVO, RegisterQuery> {

	UserEntity findByEmail(String email);
}