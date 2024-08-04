package com.wy.test.persistence.service.impl;

import org.springframework.stereotype.Service;

import com.wy.test.core.convert.ConnectorConvert;
import com.wy.test.core.entity.ConnectorEntity;
import com.wy.test.core.query.ConnectorQuery;
import com.wy.test.core.vo.ConnectorVO;
import com.wy.test.persistence.mapper.ConnectorMapper;
import com.wy.test.persistence.service.ConnectorService;

import dream.flying.flower.framework.mybatis.plus.service.impl.AbstractServiceImpl;

/**
 * 连接器
 *
 * @author 飞花梦影
 * @date 2024-08-01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Service
public class ConnectorServiceImpl
		extends AbstractServiceImpl<ConnectorEntity, ConnectorVO, ConnectorQuery, ConnectorConvert, ConnectorMapper>
		implements ConnectorService {

}