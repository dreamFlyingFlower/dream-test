package com.wy.test.persistence.service.impl;

import org.springframework.stereotype.Service;

import com.wy.test.core.convert.OrgCastConvert;
import com.wy.test.core.entity.OrgCastEntity;
import com.wy.test.core.query.OrgCastQuery;
import com.wy.test.core.vo.OrgCastVO;
import com.wy.test.persistence.mapper.OrgCastMapper;
import com.wy.test.persistence.service.OrgCastService;

import dream.flying.flower.framework.mybatis.plus.service.impl.AbstractServiceImpl;

/**
 * 机构映射表
 *
 * @author 飞花梦影
 * @date 2024-08-01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Service
public class OrgCastServiceImpl
		extends AbstractServiceImpl<OrgCastEntity, OrgCastVO, OrgCastQuery, OrgCastConvert, OrgCastMapper>
		implements OrgCastService {

	@Override
	public boolean updateCast(OrgCastEntity organizationsCast) {
		return baseMapper.updateCast(organizationsCast) > 0;
	}
}