package com.wy.test.persistence.service;

import java.util.List;

import com.wy.test.core.entity.ResourceEntity;
import com.wy.test.core.query.ResourceQuery;
import com.wy.test.core.vo.ResourceVO;

import dream.flying.flower.framework.mybatis.plus.service.BaseServices;

/**
 * 资源
 *
 * @author 飞花梦影
 * @date 2024-08-01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public interface ResourceService extends BaseServices<ResourceEntity, ResourceVO, ResourceQuery> {

	List<ResourceEntity> queryResourcesTree(ResourceEntity resource);
}