package com.wy.test.persistence.service;

import java.util.List;

import com.wy.test.core.entity.OrgEntity;
import com.wy.test.core.query.OrgQuery;
import com.wy.test.core.vo.OrgVO;

import dream.flying.flower.framework.mybatis.plus.service.BaseServices;

/**
 * 组织机构
 *
 * @author 飞花梦影
 * @date 2024-08-01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public interface OrgService extends BaseServices<OrgEntity, OrgVO, OrgQuery> {

	boolean insert(OrgEntity organization);

	boolean update(OrgEntity organization);

	boolean delete(OrgEntity organization);

	List<OrgEntity> queryOrgs(OrgEntity organization);
}