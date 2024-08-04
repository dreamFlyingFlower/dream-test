package com.wy.test.persistence.service.impl;

import org.springframework.stereotype.Service;

import com.wy.test.core.convert.LocalizationConvert;
import com.wy.test.core.entity.LocalizationEntity;
import com.wy.test.core.query.LocalizationQuery;
import com.wy.test.core.vo.LocalizationVO;
import com.wy.test.persistence.mapper.LocalizationMapper;
import com.wy.test.persistence.service.LocalizationService;

import dream.flying.flower.framework.mybatis.plus.service.impl.AbstractServiceImpl;

/**
 * 国际化
 *
 * @author 飞花梦影
 * @date 2024-08-01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Service
public class LocalizationServiceImpl extends AbstractServiceImpl<LocalizationEntity, LocalizationVO, LocalizationQuery,
		LocalizationConvert, LocalizationMapper> implements LocalizationService {

}