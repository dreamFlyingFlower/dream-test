package com.wy.test.persistence.service;

import java.sql.Types;
import java.util.Date;
import java.util.List;

import org.dromara.mybatis.jpa.JpaService;
import org.springframework.stereotype.Repository;

import com.wy.test.entity.Organizations;
import com.wy.test.entity.SynchroRelated;
import com.wy.test.entity.Synchronizers;
import com.wy.test.persistence.mapper.SynchroRelatedMapper;
import com.wy.test.util.DateUtils;

@Repository
public class SynchroRelatedService extends JpaService<SynchroRelated> {

	public SynchroRelatedService() {
		super(SynchroRelatedMapper.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.connsec.db.service.BaseService#getMapper()
	 */
	@Override
	public SynchroRelatedMapper getMapper() {
		return (SynchroRelatedMapper) super.getMapper();
	}

	public int updateSyncTime(SynchroRelated synchroRelated) {
		return getMapper().updateSyncTime(synchroRelated);
	}

	public List<SynchroRelated> findOrgs(Synchronizers synchronizer) {
		return find("instid = ? and syncid = ? and objecttype = ? ",
				new Object[] { synchronizer.getInstId(), synchronizer.getId(), Organizations.CLASS_TYPE },
				new int[] { Types.VARCHAR, Types.VARCHAR, Types.VARCHAR });
	}

	public SynchroRelated findByOriginId(Synchronizers synchronizer, String originId, String classType) {
		return findOne("instid = ? and syncId = ? and originid = ? and objecttype = ? ",
				new Object[] { synchronizer.getInstId(), synchronizer.getId(), originId, classType },
				new int[] { Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR });
	}

	public void updateSynchroRelated(Synchronizers synchronizer, SynchroRelated synchroRelated, String classType) {
		SynchroRelated loadSynchroRelated = findByOriginId(synchronizer, synchroRelated.getOriginId(), classType);
		if (loadSynchroRelated == null) {
			insert(synchroRelated);
		} else {
			synchroRelated.setId(loadSynchroRelated.getId());
			synchroRelated.setSyncTime(DateUtils.formatDateTime(new Date()));
			updateSyncTime(synchroRelated);
		}
	}
}
