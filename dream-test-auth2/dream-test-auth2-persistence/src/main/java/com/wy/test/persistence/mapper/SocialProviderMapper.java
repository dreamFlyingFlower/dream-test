package com.wy.test.persistence.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.wy.test.core.entity.SocialProviderEntity;
import com.wy.test.core.query.SocialProviderQuery;
import com.wy.test.core.vo.SocialProviderVO;

import dream.flying.flower.framework.mybatis.plus.mapper.BaseMappers;

/**
 * 第三方登录提供者
 *
 * @author 飞花梦影
 * @date 2024-08-01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Mapper
public interface SocialProviderMapper extends BaseMappers<SocialProviderEntity, SocialProviderVO, SocialProviderQuery> {

}