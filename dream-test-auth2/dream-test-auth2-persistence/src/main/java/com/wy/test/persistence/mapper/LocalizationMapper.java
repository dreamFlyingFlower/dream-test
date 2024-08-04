package com.wy.test.persistence.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.wy.test.core.entity.LocalizationEntity;
import com.wy.test.core.query.LocalizationQuery;
import com.wy.test.core.vo.LocalizationVO;

import dream.flying.flower.framework.mybatis.plus.mapper.BaseMappers;

/**
 * 国际化
 *
 * @author 飞花梦影
 * @date 2024-08-01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Mapper
public interface LocalizationMapper extends BaseMappers<LocalizationEntity, LocalizationVO, LocalizationQuery> {

}