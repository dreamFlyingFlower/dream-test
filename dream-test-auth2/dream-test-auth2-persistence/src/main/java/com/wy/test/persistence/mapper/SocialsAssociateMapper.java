package com.wy.test.persistence.mapper;

import java.util.List;

import org.dromara.mybatis.jpa.IJpaMapper;

import com.wy.test.entity.SocialsAssociate;
import com.wy.test.entity.UserInfo;

public interface SocialsAssociateMapper extends IJpaMapper<SocialsAssociate> {

	public List<SocialsAssociate> queryByUser(UserInfo user);
}
