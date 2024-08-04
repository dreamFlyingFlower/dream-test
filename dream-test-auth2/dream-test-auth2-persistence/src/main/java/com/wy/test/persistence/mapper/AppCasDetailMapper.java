package com.wy.test.persistence.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.wy.test.core.entity.AppCasDetailEntity;
import com.wy.test.core.query.AppCasDetailQuery;
import com.wy.test.core.vo.AppCasDetailVO;

import dream.flying.flower.framework.mybatis.plus.mapper.BaseMappers;

/**
 * CAS详情
 *
 * @author 飞花梦影
 * @date 2024-08-01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Mapper
public interface AppCasDetailMapper extends BaseMappers<AppCasDetailEntity, AppCasDetailVO, AppCasDetailQuery> {

	AppCasDetailEntity getAppDetails(String id);
}