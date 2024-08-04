package com.wy.test.core.convert;

import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import com.wy.test.core.entity.HistorySyncEntity;
import com.wy.test.core.vo.HistorySyncVO;

import dream.flying.flower.framework.web.convert.BaseConvert;

/**
 * 同步日志
 *
 * @author 飞花梦影
 * @date 2024-08-01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
		unmappedSourcePolicy = ReportingPolicy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface HistorySyncConvert extends BaseConvert<HistorySyncEntity, HistorySyncVO> {

	HistorySyncConvert INSTANCE = Mappers.getMapper(HistorySyncConvert.class);
}