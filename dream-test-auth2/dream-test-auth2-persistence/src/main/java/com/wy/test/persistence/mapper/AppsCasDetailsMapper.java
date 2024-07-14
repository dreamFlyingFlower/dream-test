package com.wy.test.persistence.mapper;

import org.dromara.mybatis.jpa.IJpaMapper;

import com.wy.test.entity.apps.AppsCasDetails;

/**
 * @author Crystal.sea
 *
 */
public interface AppsCasDetailsMapper extends IJpaMapper<AppsCasDetails> {

	public AppsCasDetails getAppDetails(String id);
}
