package com.wy.test.persistence.service;

import com.wy.test.core.entity.RememberMeEntity;
import com.wy.test.core.query.RememberMeQuery;
import com.wy.test.core.vo.RememberMeVO;

import dream.flying.flower.framework.mybatis.plus.service.BaseServices;

/**
 * 记住我
 *
 * @author 飞花梦影
 * @date 2024-08-01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public interface RememberMeService extends BaseServices<RememberMeEntity, RememberMeVO, RememberMeQuery> {

}