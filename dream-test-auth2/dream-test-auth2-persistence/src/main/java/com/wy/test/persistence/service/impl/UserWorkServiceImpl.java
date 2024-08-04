package com.wy.test.persistence.service.impl;

import org.springframework.stereotype.Service;

import com.wy.test.core.convert.UserWorkConvert;
import com.wy.test.core.entity.UserWorkEntity;
import com.wy.test.core.query.UserWorkQuery;
import com.wy.test.core.vo.UserWorkVO;
import com.wy.test.persistence.mapper.UserWorkMapper;
import com.wy.test.persistence.service.UserWorkService;

import dream.flying.flower.framework.mybatis.plus.service.impl.AbstractServiceImpl;

/**
 * 用户信息
 *
 * @author 飞花梦影
 * @date 2024-08-01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Service
public class UserWorkServiceImpl
		extends AbstractServiceImpl<UserWorkEntity, UserWorkVO, UserWorkQuery, UserWorkConvert, UserWorkMapper>
		implements UserWorkService {

}