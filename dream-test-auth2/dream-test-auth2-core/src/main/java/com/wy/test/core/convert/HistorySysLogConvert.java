package com.wy.test.core.convert;

import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import com.wy.test.core.entity.HistorySysLogEntity;
import com.wy.test.core.vo.HistorySysLogVO;

import dream.flying.flower.framework.web.convert.BaseConvert;

/**
 * 系统操作日志
 *
 * @author 飞花梦影
 * @date 2024-08-01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
		unmappedSourcePolicy = ReportingPolicy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface HistorySysLogConvert extends BaseConvert<HistorySysLogEntity, HistorySysLogVO> {

	HistorySysLogConvert INSTANCE = Mappers.getMapper(HistorySysLogConvert.class);
}