package com.wy.test.persistence.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.wy.test.core.entity.SocialAssociateEntity;
import com.wy.test.core.entity.UserEntity;
import com.wy.test.core.query.SocialAssociateQuery;
import com.wy.test.core.vo.SocialAssociateVO;

import dream.flying.flower.framework.mybatis.plus.mapper.BaseMappers;

/**
 * 第三方登录用户
 *
 * @author 飞花梦影
 * @date 2024-08-01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Mapper
public interface SocialAssociateMapper
		extends BaseMappers<SocialAssociateEntity, SocialAssociateVO, SocialAssociateQuery> {

	List<SocialAssociateEntity> queryByUser(UserEntity user);
}