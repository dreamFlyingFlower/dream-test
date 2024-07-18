package com.wy.test.persistence.mapper;

import org.dromara.mybatis.jpa.IJpaMapper;

import com.wy.test.core.entity.apps.AppsSAML20Details;

public interface AppsSaml20DetailsMapper extends IJpaMapper<AppsSAML20Details> {

	public AppsSAML20Details getAppDetails(String id);
}
