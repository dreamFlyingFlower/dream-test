package com.wy.test.persistence.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.wy.test.core.entity.SyncEntity;
import com.wy.test.core.query.SyncQuery;
import com.wy.test.core.vo.SyncVO;

import dream.flying.flower.framework.mybatis.plus.mapper.BaseMappers;

/**
 * 同步器
 *
 * @author 飞花梦影
 * @date 2024-08-01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Mapper
public interface SyncMapper extends BaseMappers<SyncEntity, SyncVO, SyncQuery> {

}