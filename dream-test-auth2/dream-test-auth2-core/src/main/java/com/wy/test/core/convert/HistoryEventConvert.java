package com.wy.test.core.convert;

import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import com.wy.test.core.entity.HistoryEventEntity;
import com.wy.test.core.vo.HistoryEventVO;

import dream.flying.flower.framework.web.convert.BaseConvert;

/**
 * 历史事件
 *
 * @author 飞花梦影
 * @date 2024-08-01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
		unmappedSourcePolicy = ReportingPolicy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface HistoryEventConvert extends BaseConvert<HistoryEventEntity, HistoryEventVO> {

	HistoryEventConvert INSTANCE = Mappers.getMapper(HistoryEventConvert.class);
}