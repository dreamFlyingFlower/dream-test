package com.wy.test.persistence.service;

import com.wy.test.core.entity.SyncEntity;
import com.wy.test.core.query.SyncQuery;
import com.wy.test.core.vo.SyncVO;

import dream.flying.flower.framework.mybatis.plus.service.BaseServices;

/**
 * 同步器
 *
 * @author 飞花梦影
 * @date 2024-08-01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public interface SyncService extends BaseServices<SyncEntity, SyncVO, SyncQuery> {

}