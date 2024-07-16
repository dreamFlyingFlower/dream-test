package com.wy.test.persistence.mapper;

import org.apache.ibatis.annotations.Update;
import org.dromara.mybatis.jpa.IJpaMapper;

import com.wy.test.entity.SynchroRelated;

public interface SynchroRelatedMapper extends IJpaMapper<SynchroRelated> {

	@Update("update mxk_synchro_related set synctime = #{syncTime} where id= #{id} ")
	public int updateSyncTime(SynchroRelated synchroRelated);
}
