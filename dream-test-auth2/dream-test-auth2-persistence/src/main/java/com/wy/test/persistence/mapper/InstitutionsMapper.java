package com.wy.test.persistence.mapper;

import org.apache.ibatis.annotations.Select;
import org.dromara.mybatis.jpa.IJpaMapper;

import com.wy.test.core.constants.ConstStatus;
import com.wy.test.core.entity.Institutions;

public interface InstitutionsMapper extends IJpaMapper<Institutions> {

	@Select("select * from  mxk_institutions where domain = #{value} and status = " + ConstStatus.ACTIVE)
	public Institutions findByDomain(String domain);
}
