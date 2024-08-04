package com.wy.test.core.convert;

import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import com.wy.test.core.entity.AppOauthClientDetailEntity;
import com.wy.test.core.vo.AppOauthClientDetailVO;

import dream.flying.flower.framework.web.convert.BaseConvert;

/**
 * oauth_client_details
 *
 * @author 飞花梦影
 * @date 2024-08-01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
		unmappedSourcePolicy = ReportingPolicy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AppOauthClientDetailConvert extends BaseConvert<AppOauthClientDetailEntity, AppOauthClientDetailVO> {

	AppOauthClientDetailConvert INSTANCE = Mappers.getMapper(AppOauthClientDetailConvert.class);
}