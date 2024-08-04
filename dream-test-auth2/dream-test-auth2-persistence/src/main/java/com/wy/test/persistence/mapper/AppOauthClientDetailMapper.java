package com.wy.test.persistence.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.wy.test.core.entity.AppOauthClientDetailEntity;
import com.wy.test.core.query.AppOauthClientDetailQuery;
import com.wy.test.core.vo.AppOauthClientDetailVO;

import dream.flying.flower.framework.mybatis.plus.mapper.BaseMappers;

/**
 * oauth_client_details
 *
 * @author 飞花梦影
 * @date 2024-08-01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Mapper
public interface AppOauthClientDetailMapper
		extends BaseMappers<AppOauthClientDetailEntity, AppOauthClientDetailVO, AppOauthClientDetailQuery> {

}