package com.wy.test.core.convert;

import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import com.wy.test.core.entity.AppAdapterEntity;
import com.wy.test.core.vo.AppAdapterVO;

import dream.flying.flower.framework.web.convert.BaseConvert;

/**
 * 应用适配
 *
 * @author 飞花梦影
 * @date 2024-08-01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
		unmappedSourcePolicy = ReportingPolicy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AppAdapterConvert extends BaseConvert<AppAdapterEntity, AppAdapterVO> {

	AppAdapterConvert INSTANCE = Mappers.getMapper(AppAdapterConvert.class);
}