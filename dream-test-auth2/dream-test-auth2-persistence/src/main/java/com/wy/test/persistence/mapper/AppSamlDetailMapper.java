package com.wy.test.persistence.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.wy.test.core.entity.AppSamlDetailEntity;
import com.wy.test.core.query.AppSamlDetailQuery;
import com.wy.test.core.vo.AppSamlDetailVO;

import dream.flying.flower.framework.mybatis.plus.mapper.BaseMappers;

/**
 * SAML2详情
 *
 * @author 飞花梦影
 * @date 2024-08-01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Mapper
public interface AppSamlDetailMapper extends BaseMappers<AppSamlDetailEntity, AppSamlDetailVO, AppSamlDetailQuery> {

	AppSamlDetailEntity getAppDetails(String id);
}