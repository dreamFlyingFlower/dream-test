package com.wy.test.persistence.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.wy.test.core.entity.ConnectorEntity;
import com.wy.test.core.query.ConnectorQuery;
import com.wy.test.core.vo.ConnectorVO;

import dream.flying.flower.framework.mybatis.plus.mapper.BaseMappers;

/**
 * 连接器
 *
 * @author 飞花梦影
 * @date 2024-08-01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Mapper
public interface ConnectorMapper extends BaseMappers<ConnectorEntity, ConnectorVO, ConnectorQuery> {

}