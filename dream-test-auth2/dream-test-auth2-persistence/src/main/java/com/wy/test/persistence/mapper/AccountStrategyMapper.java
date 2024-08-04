package com.wy.test.persistence.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.wy.test.core.entity.AccountStrategyEntity;
import com.wy.test.core.entity.RoleEntity;
import com.wy.test.core.query.AccountStrategyQuery;
import com.wy.test.core.vo.AccountStrategyVO;

import dream.flying.flower.framework.mybatis.plus.mapper.BaseMappers;

/**
 * 帐号策略
 *
 * @author 飞花梦影
 * @date 2024-08-01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Mapper
public interface AccountStrategyMapper
		extends BaseMappers<AccountStrategyEntity, AccountStrategyVO, AccountStrategyQuery> {

	List<RoleEntity> queryDynamicGroups(RoleEntity groups);
}