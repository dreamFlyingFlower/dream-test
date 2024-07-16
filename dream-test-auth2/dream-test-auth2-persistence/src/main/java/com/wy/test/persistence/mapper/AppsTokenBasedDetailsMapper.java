package com.wy.test.persistence.mapper;

import org.dromara.mybatis.jpa.IJpaMapper;

import com.wy.test.entity.apps.AppsTokenBasedDetails;

public interface AppsTokenBasedDetailsMapper extends IJpaMapper<AppsTokenBasedDetails> {

	public AppsTokenBasedDetails getAppDetails(String id);
}
