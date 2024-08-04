package com.wy.test.persistence.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.wy.test.core.convert.SyncRelatedConvert;
import com.wy.test.core.entity.OrgEntity;
import com.wy.test.core.entity.SyncEntity;
import com.wy.test.core.entity.SyncRelatedEntity;
import com.wy.test.core.query.SyncRelatedQuery;
import com.wy.test.core.vo.SyncRelatedVO;
import com.wy.test.persistence.mapper.SyncRelatedMapper;
import com.wy.test.persistence.service.SyncRelatedService;

import dream.flying.flower.collection.CollectionHelper;
import dream.flying.flower.framework.mybatis.plus.service.impl.AbstractServiceImpl;
import dream.flying.flower.helper.DateTimeHelper;

/**
 * 同步关联
 *
 * @author 飞花梦影
 * @date 2024-08-01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Service
public class SyncRelatedServiceImpl extends
		AbstractServiceImpl<SyncRelatedEntity, SyncRelatedVO, SyncRelatedQuery, SyncRelatedConvert, SyncRelatedMapper>
		implements SyncRelatedService {

	@Override
	public int updateSyncTime(SyncRelatedEntity synchroRelated) {
		return baseMapper.updateSyncTime(synchroRelated);
	}

	@Override
	public List<SyncRelatedEntity> findOrgs(SyncEntity synchronizer) {
		return list(lambdaQuery().eq(SyncRelatedEntity::getInstId, synchronizer.getInstId())
				.eq(SyncRelatedEntity::getSyncId, synchronizer.getId())
				.eq(SyncRelatedEntity::getObjectType, OrgEntity.CLASS_TYPE));
	}

	@Override
	public SyncRelatedEntity findByOriginId(SyncEntity synchronizer, String originId, String classType) {
		List<SyncRelatedEntity> syncRelatedEntities =
				list(lambdaQuery().eq(SyncRelatedEntity::getInstId, synchronizer.getInstId())
						.eq(SyncRelatedEntity::getSyncId, synchronizer.getId())
						.eq(SyncRelatedEntity::getOriginId, originId).eq(SyncRelatedEntity::getObjectType, classType));
		return CollectionHelper.isEmpty(syncRelatedEntities) ? null : syncRelatedEntities.get(0);
	}

	@Override
	public void updateSynchroRelated(SyncEntity synchronizer, SyncRelatedEntity synchroRelated, String classType) {
		SyncRelatedEntity loadSynchroRelated = findByOriginId(synchronizer, synchroRelated.getOriginId(), classType);
		if (loadSynchroRelated == null) {
			save(synchroRelated);
		} else {
			synchroRelated.setId(loadSynchroRelated.getId());
			synchroRelated.setSyncTime(DateTimeHelper.formatDateTime());
			updateSyncTime(synchroRelated);
		}
	}
}