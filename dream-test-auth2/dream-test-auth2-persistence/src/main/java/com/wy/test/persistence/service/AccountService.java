package com.wy.test.persistence.service;

import java.util.List;

import com.wy.test.core.entity.AccountEntity;
import com.wy.test.core.entity.AccountStrategyEntity;
import com.wy.test.core.entity.UserEntity;
import com.wy.test.core.query.AccountQuery;
import com.wy.test.core.vo.AccountVO;

import dream.flying.flower.framework.mybatis.plus.service.BaseServices;

/**
 * 用户账号表
 *
 * @author 飞花梦影
 * @date 2024-08-01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public interface AccountService extends BaseServices<AccountEntity, AccountVO, AccountQuery> {

	boolean insert(AccountEntity account);

	boolean update(AccountEntity account);

	boolean updateStatus(AccountEntity accounts);

	boolean remove(String id);

	void refreshByStrategy(AccountStrategyEntity strategy);

	void refreshAllByStrategy();

	List<UserEntity> queryUserNotInStrategy(AccountStrategyEntity strategy);

	long deleteByStrategy(AccountStrategyEntity strategy);

	List<AccountEntity> queryByAppIdAndDate(AccountEntity account);

	List<AccountEntity> queryByAppIdAndAccount(String appId, String relatedUsername);

	String generateAccount(UserEntity userInfo, AccountStrategyEntity accountsStrategy);
}