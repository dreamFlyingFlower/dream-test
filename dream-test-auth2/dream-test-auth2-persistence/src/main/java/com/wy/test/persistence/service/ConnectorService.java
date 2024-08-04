package com.wy.test.persistence.service;

import com.wy.test.core.entity.ConnectorEntity;
import com.wy.test.core.query.ConnectorQuery;
import com.wy.test.core.vo.ConnectorVO;

import dream.flying.flower.framework.mybatis.plus.service.BaseServices;

/**
 * 连接器
 *
 * @author 飞花梦影
 * @date 2024-08-01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public interface ConnectorService extends BaseServices<ConnectorEntity, ConnectorVO, ConnectorQuery> {

}