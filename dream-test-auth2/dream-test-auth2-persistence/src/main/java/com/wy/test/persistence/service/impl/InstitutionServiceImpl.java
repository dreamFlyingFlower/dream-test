package com.wy.test.persistence.service.impl;

import org.springframework.stereotype.Service;

import com.wy.test.core.convert.InstitutionConvert;
import com.wy.test.core.entity.InstitutionEntity;
import com.wy.test.core.query.InstitutionQuery;
import com.wy.test.core.vo.InstitutionVO;
import com.wy.test.persistence.mapper.InstitutionMapper;
import com.wy.test.persistence.service.InstitutionService;

import dream.flying.flower.framework.mybatis.plus.service.impl.AbstractServiceImpl;

/**
 * 机构
 *
 * @author 飞花梦影
 * @date 2024-08-01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Service
public class InstitutionServiceImpl extends
		AbstractServiceImpl<InstitutionEntity, InstitutionVO, InstitutionQuery, InstitutionConvert, InstitutionMapper>
		implements InstitutionService {

	@Override
	public InstitutionEntity findByDomain(String domain) {
		return baseMapper.findByDomain(domain);
	}
}