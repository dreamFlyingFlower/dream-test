package com.wy.test.persistence.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.wy.test.core.entity.HistorySyncEntity;
import com.wy.test.core.query.HistorySyncQuery;
import com.wy.test.core.vo.HistorySyncVO;

import dream.flying.flower.framework.mybatis.plus.mapper.BaseMappers;

/**
 * 同步日志
 *
 * @author 飞花梦影
 * @date 2024-08-01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Mapper
public interface HistorySyncMapper extends BaseMappers<HistorySyncEntity, HistorySyncVO, HistorySyncQuery> {

}