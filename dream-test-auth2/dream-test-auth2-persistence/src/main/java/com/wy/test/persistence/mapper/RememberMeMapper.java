package com.wy.test.persistence.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.wy.test.core.entity.RememberMeEntity;
import com.wy.test.core.query.RememberMeQuery;
import com.wy.test.core.vo.RememberMeVO;

import dream.flying.flower.framework.mybatis.plus.mapper.BaseMappers;

/**
 * 记住我
 *
 * @author 飞花梦影
 * @date 2024-08-01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Mapper
public interface RememberMeMapper extends BaseMappers<RememberMeEntity, RememberMeVO, RememberMeQuery> {

}