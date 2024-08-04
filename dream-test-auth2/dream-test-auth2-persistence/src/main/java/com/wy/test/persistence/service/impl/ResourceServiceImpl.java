package com.wy.test.persistence.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.wy.test.core.convert.ResourceConvert;
import com.wy.test.core.entity.ResourceEntity;
import com.wy.test.core.query.ResourceQuery;
import com.wy.test.core.vo.ResourceVO;
import com.wy.test.persistence.mapper.ResourceMapper;
import com.wy.test.persistence.service.ResourceService;

import dream.flying.flower.framework.mybatis.plus.service.impl.AbstractServiceImpl;

/**
 * 资源
 *
 * @author 飞花梦影
 * @date 2024-08-01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Service
public class ResourceServiceImpl
		extends AbstractServiceImpl<ResourceEntity, ResourceVO, ResourceQuery, ResourceConvert, ResourceMapper>
		implements ResourceService {

	@Override
	public List<ResourceEntity> queryResourcesTree(ResourceEntity resource) {
		return baseConvert.convert(list(resource));
	}
}