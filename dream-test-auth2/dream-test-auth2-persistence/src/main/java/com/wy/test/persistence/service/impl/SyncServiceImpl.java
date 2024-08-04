package com.wy.test.persistence.service.impl;

import org.springframework.stereotype.Service;

import com.wy.test.core.convert.SyncConvert;
import com.wy.test.core.entity.SyncEntity;
import com.wy.test.core.query.SyncQuery;
import com.wy.test.core.vo.SyncVO;
import com.wy.test.persistence.mapper.SyncMapper;
import com.wy.test.persistence.service.SyncService;

import dream.flying.flower.framework.mybatis.plus.service.impl.AbstractServiceImpl;

/**
 * 同步器
 *
 * @author 飞花梦影
 * @date 2024-08-01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Service
public class SyncServiceImpl extends AbstractServiceImpl<SyncEntity, SyncVO, SyncQuery, SyncConvert, SyncMapper>
		implements SyncService {

}