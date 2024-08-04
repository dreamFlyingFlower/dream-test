package com.wy.test.persistence.service;

import com.wy.test.core.entity.AppSamlDetailEntity;
import com.wy.test.core.query.AppSamlDetailQuery;
import com.wy.test.core.vo.AppSamlDetailVO;

import dream.flying.flower.framework.mybatis.plus.service.BaseServices;

/**
 * SAML2详情
 *
 * @author 飞花梦影
 * @date 2024-08-01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public interface AppSamlDetailService extends BaseServices<AppSamlDetailEntity, AppSamlDetailVO, AppSamlDetailQuery> {

	AppSamlDetailEntity getAppDetails(String id, boolean cached);
}