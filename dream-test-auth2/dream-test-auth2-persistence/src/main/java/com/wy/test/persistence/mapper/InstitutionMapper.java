package com.wy.test.persistence.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.wy.test.core.constants.ConstStatus;
import com.wy.test.core.entity.InstitutionEntity;
import com.wy.test.core.query.InstitutionQuery;
import com.wy.test.core.vo.InstitutionVO;

import dream.flying.flower.framework.mybatis.plus.mapper.BaseMappers;

/**
 * 机构
 *
 * @author 飞花梦影
 * @date 2024-08-01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Mapper
public interface InstitutionMapper extends BaseMappers<InstitutionEntity, InstitutionVO, InstitutionQuery> {

	@Select("select * from  auth_institution where domain = #{value} and status = " + ConstStatus.ACTIVE)
	InstitutionEntity findByDomain(String domain);
}