package com.wy.test.persistence.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.wy.test.core.entity.HistorySysLogEntity;
import com.wy.test.core.query.HistorySysLogQuery;
import com.wy.test.core.vo.HistorySysLogVO;

import dream.flying.flower.framework.mybatis.plus.mapper.BaseMappers;

/**
 * 系统操作日志
 *
 * @author 飞花梦影
 * @date 2024-08-01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Mapper
public interface HistorySysLogMapper extends BaseMappers<HistorySysLogEntity, HistorySysLogVO, HistorySysLogQuery> {

}