package com.wy.test.persistence.service;

import java.util.List;

import com.wy.test.core.entity.SocialAssociateEntity;
import com.wy.test.core.entity.UserEntity;
import com.wy.test.core.query.SocialAssociateQuery;
import com.wy.test.core.vo.SocialAssociateVO;

import dream.flying.flower.framework.mybatis.plus.service.BaseServices;

/**
 * 第三方登录用户
 *
 * @author 飞花梦影
 * @date 2024-08-01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public interface SocialAssociateService
		extends BaseServices<SocialAssociateEntity, SocialAssociateVO, SocialAssociateQuery> {

	List<SocialAssociateEntity> queryByUser(UserEntity user);
}