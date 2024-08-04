package com.wy.test.persistence.service;

import com.wy.test.core.entity.AppOauthClientDetailEntity;
import com.wy.test.core.query.AppOauthClientDetailQuery;
import com.wy.test.core.vo.AppOauthClientDetailVO;

import dream.flying.flower.framework.mybatis.plus.service.BaseServices;

/**
 * oauth_client_details
 *
 * @author 飞花梦影
 * @date 2024-08-01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public interface AppOauthClientDetailService extends BaseServices<AppOauthClientDetailEntity, AppOauthClientDetailVO, AppOauthClientDetailQuery> {

}