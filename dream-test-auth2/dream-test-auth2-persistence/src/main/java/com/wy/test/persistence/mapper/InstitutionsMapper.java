package com.wy.test.persistence.mapper;

import org.apache.ibatis.annotations.Select;
import org.dromara.mybatis.jpa.IJpaMapper;

import com.wy.test.constants.ConstsStatus;
import com.wy.test.entity.Institutions;

public interface InstitutionsMapper extends IJpaMapper<Institutions> {

	@Select("select * from  mxk_institutions where domain = #{value} and status = " + ConstsStatus.ACTIVE)
	public Institutions findByDomain(String domain);
}
