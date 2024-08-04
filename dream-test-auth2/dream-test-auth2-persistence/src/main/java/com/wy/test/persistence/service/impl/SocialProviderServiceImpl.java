package com.wy.test.persistence.service.impl;

import org.springframework.stereotype.Service;

import com.wy.test.core.convert.SocialProviderConvert;
import com.wy.test.core.entity.SocialProviderEntity;
import com.wy.test.core.query.SocialProviderQuery;
import com.wy.test.core.vo.SocialProviderVO;
import com.wy.test.persistence.mapper.SocialProviderMapper;
import com.wy.test.persistence.service.SocialProviderService;

import dream.flying.flower.framework.mybatis.plus.service.impl.AbstractServiceImpl;

/**
 * 第三方登录提供者
 *
 * @author 飞花梦影
 * @date 2024-08-01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Service
public class SocialProviderServiceImpl extends
		AbstractServiceImpl<SocialProviderEntity, SocialProviderVO, SocialProviderQuery, SocialProviderConvert, SocialProviderMapper >
		implements SocialProviderService {

}