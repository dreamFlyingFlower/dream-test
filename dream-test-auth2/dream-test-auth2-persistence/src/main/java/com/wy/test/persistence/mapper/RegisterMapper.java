package com.wy.test.persistence.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.wy.test.core.entity.RegisterEntity;
import com.wy.test.core.entity.UserEntity;
import com.wy.test.core.query.RegisterQuery;
import com.wy.test.core.vo.RegisterVO;

import dream.flying.flower.framework.mybatis.plus.mapper.BaseMappers;

/**
 * 用户注册
 *
 * @author 飞花梦影
 * @date 2024-08-01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Mapper
public interface RegisterMapper extends BaseMappers<RegisterEntity, RegisterVO, RegisterQuery> {

	List<UserEntity> findByEmail(String email);
}