package com.wy.test.persistence.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.wy.test.core.entity.HistoryEventEntity;
import com.wy.test.core.query.HistoryEventQuery;
import com.wy.test.core.vo.HistoryEventVO;

import dream.flying.flower.framework.mybatis.plus.mapper.BaseMappers;

/**
 * 历史事件
 *
 * @author 飞花梦影
 * @date 2024-08-01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Mapper
public interface HistoryEventMapper extends BaseMappers<HistoryEventEntity, HistoryEventVO, HistoryEventQuery> {

}