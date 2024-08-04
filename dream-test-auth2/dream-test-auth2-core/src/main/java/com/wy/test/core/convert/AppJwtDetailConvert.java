package com.wy.test.core.convert;

import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import com.wy.test.core.entity.AppJwtDetailEntity;
import com.wy.test.core.vo.AppJwtDetailVO;

import dream.flying.flower.framework.web.convert.BaseConvert;

/**
 * JWT详情
 *
 * @author 飞花梦影
 * @date 2024-08-01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
		unmappedSourcePolicy = ReportingPolicy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AppJwtDetailConvert extends BaseConvert<AppJwtDetailEntity, AppJwtDetailVO> {

	AppJwtDetailConvert INSTANCE = Mappers.getMapper(AppJwtDetailConvert.class);
}