package com.wy.test.persistence.service;

import com.wy.test.core.entity.UserWorkEntity;
import com.wy.test.core.query.UserWorkQuery;
import com.wy.test.core.vo.UserWorkVO;

import dream.flying.flower.framework.mybatis.plus.service.BaseServices;

/**
 * 用户信息
 *
 * @author 飞花梦影
 * @date 2024-08-01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public interface UserWorkService extends BaseServices<UserWorkEntity, UserWorkVO, UserWorkQuery> {

}