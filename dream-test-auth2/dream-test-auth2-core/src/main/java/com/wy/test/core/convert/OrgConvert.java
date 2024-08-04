package com.wy.test.core.convert;

import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import com.wy.test.core.entity.OrgEntity;
import com.wy.test.core.vo.OrgVO;

import dream.flying.flower.framework.web.convert.BaseConvert;

/**
 * 组织机构
 *
 * @author 飞花梦影
 * @date 2024-08-01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
		unmappedSourcePolicy = ReportingPolicy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrgConvert extends BaseConvert<OrgEntity, OrgVO> {

	OrgConvert INSTANCE = Mappers.getMapper(OrgConvert.class);
}