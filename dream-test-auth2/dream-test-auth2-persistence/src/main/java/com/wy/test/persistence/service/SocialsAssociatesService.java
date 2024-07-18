package com.wy.test.persistence.service;

import java.util.List;

import org.dromara.mybatis.jpa.JpaService;
import org.springframework.stereotype.Repository;

import com.wy.test.core.entity.SocialsAssociate;
import com.wy.test.core.entity.UserInfo;
import com.wy.test.persistence.mapper.SocialsAssociateMapper;

@Repository
public class SocialsAssociatesService extends JpaService<SocialsAssociate> {

	public SocialsAssociatesService() {
		super(SocialsAssociateMapper.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.connsec.db.service.BaseService#getMapper()
	 */
	@Override
	public SocialsAssociateMapper getMapper() {
		return (SocialsAssociateMapper) super.getMapper();
	}

	public List<SocialsAssociate> queryByUser(UserInfo user) {
		return getMapper().queryByUser(user);
	}

}
