package com.wy.test.persistence.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.wy.test.core.entity.UserAdjunctEntity;
import com.wy.test.core.query.UserAdjunctQuery;
import com.wy.test.core.vo.UserAdjunctVO;

import dream.flying.flower.framework.mybatis.plus.mapper.BaseMappers;

/**
 * 用户扩展信息
 *
 * @author 飞花梦影
 * @date 2024-08-01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Mapper
public interface UserAdjunctMapper extends BaseMappers<UserAdjunctEntity, UserAdjunctVO, UserAdjunctQuery> {

}