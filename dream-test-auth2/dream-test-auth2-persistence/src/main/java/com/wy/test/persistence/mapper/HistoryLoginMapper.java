package com.wy.test.persistence.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.wy.test.core.entity.HistoryLoginEntity;
import com.wy.test.core.query.HistoryLoginQuery;
import com.wy.test.core.vo.HistoryLoginVO;

import dream.flying.flower.framework.mybatis.plus.mapper.BaseMappers;

/**
 * 登录历史
 *
 * @author 飞花梦影
 * @date 2024-08-01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Mapper
public interface HistoryLoginMapper extends BaseMappers<HistoryLoginEntity, HistoryLoginVO, HistoryLoginQuery> {

	List<HistoryLoginEntity> queryOnlineSession(HistoryLoginEntity historyLogin);
}