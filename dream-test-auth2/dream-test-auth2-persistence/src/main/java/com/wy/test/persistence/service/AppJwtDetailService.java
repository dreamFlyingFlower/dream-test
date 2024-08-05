package com.wy.test.persistence.service;

import com.wy.test.core.entity.AppJwtDetailEntity;
import com.wy.test.core.query.AppJwtDetailQuery;
import com.wy.test.core.vo.AppJwtDetailVO;

import dream.flying.flower.framework.mybatis.plus.service.BaseServices;

/**
 * JWT详情
 *
 * @author 飞花梦影
 * @date 2024-08-01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public interface AppJwtDetailService extends BaseServices<AppJwtDetailEntity, AppJwtDetailVO, AppJwtDetailQuery> {

	AppJwtDetailVO getAppDetails(String id, boolean cached);
}