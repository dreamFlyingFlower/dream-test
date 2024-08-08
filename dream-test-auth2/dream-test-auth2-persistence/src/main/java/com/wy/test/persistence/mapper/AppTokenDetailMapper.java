package com.wy.test.persistence.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.wy.test.core.entity.AppTokenDetailEntity;
import com.wy.test.core.query.AppTokenDetailQuery;
import com.wy.test.core.vo.AppTokenDetailVO;

import dream.flying.flower.framework.mybatis.plus.mapper.BaseMappers;

/**
 * token详情
 *
 * @author 飞花梦影
 * @date 2024-08-01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Mapper
public interface AppTokenDetailMapper extends BaseMappers<AppTokenDetailEntity, AppTokenDetailVO, AppTokenDetailQuery> {

}