package com.wy.test.persistence.service;

import com.wy.test.core.entity.AccountStrategyEntity;
import com.wy.test.core.query.AccountStrategyQuery;
import com.wy.test.core.vo.AccountStrategyVO;

import dream.flying.flower.framework.mybatis.plus.service.BaseServices;

/**
 * 帐号策略
 *
 * @author 飞花梦影
 * @date 2024-08-01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public interface AccountStrategyService
		extends BaseServices<AccountStrategyEntity, AccountStrategyVO, AccountStrategyQuery> {

}