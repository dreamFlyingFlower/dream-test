package com.wy.test.persistence.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.wy.test.core.entity.HistoryLoginAppEntity;
import com.wy.test.core.query.HistoryLoginAppQuery;
import com.wy.test.core.vo.HistoryLoginAppVO;

import dream.flying.flower.framework.mybatis.plus.mapper.BaseMappers;

/**
 * APP登录历史记录
 *
 * @author 飞花梦影
 * @date 2024-08-01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Mapper
public interface HistoryLoginAppMapper
		extends BaseMappers<HistoryLoginAppEntity, HistoryLoginAppVO, HistoryLoginAppQuery> {

}