package com.wy.test.persistence.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.wy.test.core.entity.AppJwtDetailEntity;
import com.wy.test.core.query.AppJwtDetailQuery;
import com.wy.test.core.vo.AppJwtDetailVO;

import dream.flying.flower.framework.mybatis.plus.mapper.BaseMappers;

/**
 * JWT详情
 *
 * @author 飞花梦影
 * @date 2024-08-01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Mapper
public interface AppJwtDetailMapper extends BaseMappers<AppJwtDetailEntity, AppJwtDetailVO, AppJwtDetailQuery> {

	AppJwtDetailEntity getAppDetails(String id);
}