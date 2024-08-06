package com.wy.test.persistence.service;

import com.wy.test.core.entity.AppTokenDetailEntity;
import com.wy.test.core.query.AppTokenDetailQuery;
import com.wy.test.core.vo.AppTokenDetailVO;

import dream.flying.flower.framework.mybatis.plus.service.BaseServices;

/**
 * token详情
 *
 * @author 飞花梦影
 * @date 2024-08-01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public interface AppTokenDetailService
		extends BaseServices<AppTokenDetailEntity, AppTokenDetailVO, AppTokenDetailQuery> {

	AppTokenDetailVO getAppDetails(String id, boolean cached);
}