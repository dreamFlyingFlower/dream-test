package com.wy.test.persistence.service.impl;

import org.springframework.stereotype.Service;

import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.wy.test.core.convert.AccountStrategyConvert;
import com.wy.test.core.entity.AccountStrategyEntity;
import com.wy.test.core.entity.AppEntity;
import com.wy.test.core.query.AccountStrategyQuery;
import com.wy.test.core.vo.AccountStrategyVO;
import com.wy.test.persistence.mapper.AccountStrategyMapper;
import com.wy.test.persistence.service.AccountStrategyService;

import dream.flying.flower.framework.mybatis.plus.service.impl.AbstractServiceImpl;
import lombok.AllArgsConstructor;

/**
 * 用户账号表
 *
 * @author 飞花梦影
 * @date 2024-08-01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Service
@AllArgsConstructor
public class AccountStrategyServiceImpl extends AbstractServiceImpl<AccountStrategyEntity, AccountStrategyVO,
		AccountStrategyQuery, AccountStrategyConvert, AccountStrategyMapper> implements AccountStrategyService {

	@Override
	protected MPJLambdaWrapper<AccountStrategyEntity> buildQueryWrapper(AccountStrategyQuery query) {
		MPJLambdaWrapper<AccountStrategyEntity> mpjLambdaWrapper = super.buildQueryWrapper(query);

		mpjLambdaWrapper.selectAll(AccountStrategyEntity.class)
				.innerJoin(AppEntity.class, AppEntity::getId, AccountStrategyEntity::getAppId)
				.selectAs(AppEntity::getIcon, "appIcon");

		return super.buildQueryWrapper(query);
	}
}