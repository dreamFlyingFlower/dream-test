package com.wy.test.persistence.service;

import java.util.List;

import com.wy.test.core.entity.SyncEntity;
import com.wy.test.core.entity.SyncRelatedEntity;
import com.wy.test.core.query.SyncRelatedQuery;
import com.wy.test.core.vo.SyncRelatedVO;

import dream.flying.flower.framework.mybatis.plus.service.BaseServices;

/**
 * 同步关联
 *
 * @author 飞花梦影
 * @date 2024-08-01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public interface SyncRelatedService extends BaseServices<SyncRelatedEntity, SyncRelatedVO, SyncRelatedQuery> {

	int updateSyncTime(SyncRelatedEntity synchroRelated);

	List<SyncRelatedEntity> findOrgs(SyncEntity synchronizer);

	SyncRelatedEntity findByOriginId(SyncEntity synchronizer, String originId, String classType);

	void updateSynchroRelated(SyncEntity synchronizer, SyncRelatedEntity synchroRelated, String classType);
}