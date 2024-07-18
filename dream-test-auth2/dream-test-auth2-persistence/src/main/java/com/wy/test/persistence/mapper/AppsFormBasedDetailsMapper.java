package com.wy.test.persistence.mapper;

import org.dromara.mybatis.jpa.IJpaMapper;

import com.wy.test.core.entity.apps.AppsFormBasedDetails;

public interface AppsFormBasedDetailsMapper extends IJpaMapper<AppsFormBasedDetails> {

	public AppsFormBasedDetails getAppDetails(String id);
}
