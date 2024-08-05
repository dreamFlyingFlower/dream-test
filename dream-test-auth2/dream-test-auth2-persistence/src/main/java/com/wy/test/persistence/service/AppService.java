package com.wy.test.persistence.service;

import java.util.List;

import com.wy.test.core.entity.AppEntity;
import com.wy.test.core.query.AppQuery;
import com.wy.test.core.vo.AppVO;
import com.wy.test.core.vo.UserApps;

import dream.flying.flower.framework.mybatis.plus.service.BaseServices;

/**
 * 应用表
 *
 * @author 飞花梦影
 * @date 2024-08-01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public interface AppService extends BaseServices<AppEntity, AppVO, AppQuery> {

	boolean insertApp(AppEntity app);

	boolean updateApp(AppEntity app);

	boolean updateExtendAttr(AppEntity app);

	List<UserApps> queryMyApps(UserApps userApplications);

	void put(String appId, AppVO appDetails);

	AppVO get(String appId, boolean cached);
}