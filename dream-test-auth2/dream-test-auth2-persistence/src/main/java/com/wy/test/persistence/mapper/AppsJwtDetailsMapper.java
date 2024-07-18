package com.wy.test.persistence.mapper;

import org.dromara.mybatis.jpa.IJpaMapper;

import com.wy.test.core.entity.apps.AppsJwtDetails;

public interface AppsJwtDetailsMapper extends IJpaMapper<AppsJwtDetails> {

	AppsJwtDetails getAppDetails(String id);
}
