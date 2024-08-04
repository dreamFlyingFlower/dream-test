package com.wy.test.persistence.service.impl;

import org.springframework.stereotype.Service;

import com.wy.test.core.convert.AppOauthClientDetailConvert;
import com.wy.test.core.entity.AppOauthClientDetailEntity;
import com.wy.test.core.query.AppOauthClientDetailQuery;
import com.wy.test.core.vo.AppOauthClientDetailVO;
import com.wy.test.persistence.mapper.AppOauthClientDetailMapper;
import com.wy.test.persistence.service.AppOauthClientDetailService;

import dream.flying.flower.framework.mybatis.plus.service.impl.AbstractServiceImpl;

/**
 * oauth_client_details
 *
 * @author 飞花梦影
 * @date 2024-08-01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Service
public class AppOauthClientDetailServiceImpl extends AbstractServiceImpl<AppOauthClientDetailEntity,
		AppOauthClientDetailVO, AppOauthClientDetailQuery, AppOauthClientDetailConvert, AppOauthClientDetailMapper>
		implements AppOauthClientDetailService {

}