package com.wy.test.authz.oauth2.common.util;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;

public interface JdbcListFactory {

	/**
	 * @param sql
	 * @param parameters
	 * @return a list of {@link T}
	 */
	<T> List<T> getList(String sql, Map<String, Object> parameters, RowMapper<T> rowMapper);

}
