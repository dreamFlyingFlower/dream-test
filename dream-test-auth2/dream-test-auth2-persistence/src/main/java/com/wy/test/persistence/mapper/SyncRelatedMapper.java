package com.wy.test.persistence.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

import com.wy.test.core.entity.SyncRelatedEntity;
import com.wy.test.core.query.SyncRelatedQuery;
import com.wy.test.core.vo.SyncRelatedVO;

import dream.flying.flower.framework.mybatis.plus.mapper.BaseMappers;

/**
 * 同步关联
 *
 * @author 飞花梦影
 * @date 2024-08-01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Mapper
public interface SyncRelatedMapper extends BaseMappers<SyncRelatedEntity, SyncRelatedVO, SyncRelatedQuery> {

	@Update("update auth_sync_related set synctime = #{syncTime} where id= #{id} ")
	int updateSyncTime(SyncRelatedEntity synchroRelated);
}