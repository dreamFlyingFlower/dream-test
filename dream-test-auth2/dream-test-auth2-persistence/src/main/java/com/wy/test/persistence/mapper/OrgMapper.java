package com.wy.test.persistence.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.wy.test.core.entity.OrgEntity;
import com.wy.test.core.query.OrgQuery;
import com.wy.test.core.vo.OrgVO;

import dream.flying.flower.framework.mybatis.plus.mapper.BaseMappers;

/**
 * 组织机构
 *
 * @author 飞花梦影
 * @date 2024-08-01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Mapper
public interface OrgMapper extends BaseMappers<OrgEntity, OrgVO, OrgQuery> {

	List<OrgEntity> queryOrgs(OrgEntity organization);
}