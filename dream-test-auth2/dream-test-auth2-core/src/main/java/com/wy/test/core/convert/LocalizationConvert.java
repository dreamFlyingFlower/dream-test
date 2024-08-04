package com.wy.test.core.convert;

import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import com.wy.test.core.entity.LocalizationEntity;
import com.wy.test.core.vo.LocalizationVO;

import dream.flying.flower.framework.web.convert.BaseConvert;

/**
 * 国际化
 *
 * @author 飞花梦影
 * @date 2024-08-01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
		unmappedSourcePolicy = ReportingPolicy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LocalizationConvert extends BaseConvert<LocalizationEntity, LocalizationVO> {

	LocalizationConvert INSTANCE = Mappers.getMapper(LocalizationConvert.class);
}