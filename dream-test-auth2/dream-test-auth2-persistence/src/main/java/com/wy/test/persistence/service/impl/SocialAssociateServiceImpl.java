package com.wy.test.persistence.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.wy.test.core.convert.SocialAssociateConvert;
import com.wy.test.core.entity.SocialAssociateEntity;
import com.wy.test.core.entity.UserEntity;
import com.wy.test.core.query.SocialAssociateQuery;
import com.wy.test.core.vo.SocialAssociateVO;
import com.wy.test.persistence.mapper.SocialAssociateMapper;
import com.wy.test.persistence.service.SocialAssociateService;

import dream.flying.flower.framework.mybatis.plus.service.impl.AbstractServiceImpl;

/**
 * 第三方登录用户
 *
 * @author 飞花梦影
 * @date 2024-08-01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Service
public class SocialAssociateServiceImpl extends AbstractServiceImpl<SocialAssociateEntity, SocialAssociateVO,
		SocialAssociateQuery, SocialAssociateConvert, SocialAssociateMapper> implements SocialAssociateService {

	@Override
	public List<SocialAssociateEntity> queryByUser(UserEntity user) {
		return baseMapper.queryByUser(user);
	}
}