package com.wy.test.persistence.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.wy.test.core.entity.HistoryConnectorEntity;
import com.wy.test.core.query.HistoryConnectorQuery;
import com.wy.test.core.vo.HistoryConnectorVO;

import dream.flying.flower.framework.mybatis.plus.mapper.BaseMappers;

/**
 * 历史连接器
 *
 * @author 飞花梦影
 * @date 2024-08-01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Mapper
public interface HistoryConnectorMapper
		extends BaseMappers<HistoryConnectorEntity, HistoryConnectorVO, HistoryConnectorQuery> {

}