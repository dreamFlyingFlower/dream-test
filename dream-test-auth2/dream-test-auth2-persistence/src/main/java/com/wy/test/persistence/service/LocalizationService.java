package com.wy.test.persistence.service;

import com.wy.test.core.entity.LocalizationEntity;
import com.wy.test.core.query.LocalizationQuery;
import com.wy.test.core.vo.LocalizationVO;

import dream.flying.flower.framework.mybatis.plus.service.BaseServices;

/**
 * 国际化
 *
 * @author 飞花梦影
 * @date 2024-08-01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public interface LocalizationService extends BaseServices<LocalizationEntity, LocalizationVO, LocalizationQuery> {

}