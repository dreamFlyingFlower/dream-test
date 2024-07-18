package com.wy.test.persistence.mapper;

import java.util.List;

import org.dromara.mybatis.jpa.IJpaMapper;

import com.wy.test.core.entity.AccountsStrategy;
import com.wy.test.core.entity.Roles;

public interface AccountsStrategyMapper extends IJpaMapper<AccountsStrategy> {

	public List<Roles> queryDynamicGroups(Roles groups);
}
