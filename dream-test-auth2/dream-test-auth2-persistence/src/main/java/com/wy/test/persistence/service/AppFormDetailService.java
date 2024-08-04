package com.wy.test.persistence.service;

import com.wy.test.core.entity.AppFormDetailEntity;
import com.wy.test.core.query.AppFormDetailQuery;
import com.wy.test.core.vo.AppFormDetailVO;

import dream.flying.flower.framework.mybatis.plus.service.BaseServices;

/**
 * 表单信息
 *
 * @author 飞花梦影
 * @date 2024-08-01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public interface AppFormDetailService extends BaseServices<AppFormDetailEntity, AppFormDetailVO, AppFormDetailQuery> {

	AppFormDetailEntity getAppDetails(String id, boolean cached);
}