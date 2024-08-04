package com.wy.test.persistence.service;

import com.wy.test.core.entity.InstitutionEntity;
import com.wy.test.core.query.InstitutionQuery;
import com.wy.test.core.vo.InstitutionVO;

import dream.flying.flower.framework.mybatis.plus.service.BaseServices;

/**
 * 机构
 *
 * @author 飞花梦影
 * @date 2024-08-01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public interface InstitutionService extends BaseServices<InstitutionEntity, InstitutionVO, InstitutionQuery> {

	InstitutionEntity findByDomain(String domain);
}