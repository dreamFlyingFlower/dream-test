package com.wy.test.persistence.service;

import com.wy.test.core.entity.OrgCastEntity;
import com.wy.test.core.query.OrgCastQuery;
import com.wy.test.core.vo.OrgCastVO;

import dream.flying.flower.framework.mybatis.plus.service.BaseServices;

/**
 * 机构映射表
 *
 * @author 飞花梦影
 * @date 2024-08-01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public interface OrgCastService extends BaseServices<OrgCastEntity, OrgCastVO, OrgCastQuery> {

	boolean updateCast(OrgCastEntity orgCastEntity);
}