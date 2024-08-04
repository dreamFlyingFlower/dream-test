package com.wy.test.persistence.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.wy.test.core.entity.AppAdapterEntity;
import com.wy.test.core.query.AppAdapterQuery;
import com.wy.test.core.vo.AppAdapterVO;

import dream.flying.flower.framework.mybatis.plus.mapper.BaseMappers;

/**
 * 应用适配
 *
 * @author 飞花梦影
 * @date 2024-08-01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Mapper
public interface AppAdapterMapper extends BaseMappers<AppAdapterEntity, AppAdapterVO, AppAdapterQuery> {

}