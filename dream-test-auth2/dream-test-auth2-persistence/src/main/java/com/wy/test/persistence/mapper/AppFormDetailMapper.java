package com.wy.test.persistence.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.wy.test.core.entity.AppFormDetailEntity;
import com.wy.test.core.query.AppFormDetailQuery;
import com.wy.test.core.vo.AppFormDetailVO;

import dream.flying.flower.framework.mybatis.plus.mapper.BaseMappers;

/**
 * 表单信息
 *
 * @author 飞花梦影
 * @date 2024-08-01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Mapper
public interface AppFormDetailMapper extends BaseMappers<AppFormDetailEntity, AppFormDetailVO, AppFormDetailQuery> {

	AppFormDetailVO getAppDetails(String id);
}