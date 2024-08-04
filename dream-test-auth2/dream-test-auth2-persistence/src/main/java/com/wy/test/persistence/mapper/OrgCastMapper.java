package com.wy.test.persistence.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.wy.test.core.entity.OrgCastEntity;
import com.wy.test.core.entity.OrgEntity;
import com.wy.test.core.query.OrgCastQuery;
import com.wy.test.core.vo.OrgCastVO;

import dream.flying.flower.framework.mybatis.plus.mapper.BaseMappers;

/**
 * 机构映射表
 *
 * @author 飞花梦影
 * @date 2024-08-01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Mapper
public interface OrgCastMapper extends BaseMappers<OrgCastEntity, OrgCastVO, OrgCastQuery> {

	List<OrgEntity> queryOrgs(OrgEntity organization);

	long updateCast(OrgCastEntity organizationsCast);
}