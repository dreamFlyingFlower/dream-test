package com.wy.test.persistence.service.impl;

import org.springframework.stereotype.Service;

import com.wy.test.core.convert.UserAdjunctConvert;
import com.wy.test.core.entity.UserAdjunctEntity;
import com.wy.test.core.query.UserAdjunctQuery;
import com.wy.test.core.vo.UserAdjunctVO;
import com.wy.test.persistence.mapper.UserAdjunctMapper;
import com.wy.test.persistence.service.UserAdjunctService;

import dream.flying.flower.framework.mybatis.plus.service.impl.AbstractServiceImpl;

/**
 * 用户扩展信息
 *
 * @author 飞花梦影
 * @date 2024-08-01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Service
public class UserAdjunctServiceImpl extends
		AbstractServiceImpl<UserAdjunctEntity, UserAdjunctVO, UserAdjunctQuery, UserAdjunctConvert, UserAdjunctMapper>
		implements UserAdjunctService {

}