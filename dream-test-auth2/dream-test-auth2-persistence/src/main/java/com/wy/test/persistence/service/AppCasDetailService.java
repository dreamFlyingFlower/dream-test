package com.wy.test.persistence.service;

import com.wy.test.core.entity.AppCasDetailEntity;
import com.wy.test.core.query.AppCasDetailQuery;
import com.wy.test.core.vo.AppCasDetailVO;

import dream.flying.flower.framework.mybatis.plus.service.BaseServices;

/**
 * CAS详情
 *
 * @author 飞花梦影
 * @date 2024-08-01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public interface AppCasDetailService extends BaseServices<AppCasDetailEntity, AppCasDetailVO, AppCasDetailQuery> {

	AppCasDetailVO getAppDetails(String id, boolean cached);
}