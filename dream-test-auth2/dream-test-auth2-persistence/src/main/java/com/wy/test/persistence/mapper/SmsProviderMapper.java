package com.wy.test.persistence.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.wy.test.core.entity.SmsProviderEntity;
import com.wy.test.core.query.SmsProviderQuery;
import com.wy.test.core.vo.SmsProviderVO;

import dream.flying.flower.framework.mybatis.plus.mapper.BaseMappers;

/**
 * 短信记录
 *
 * @author 飞花梦影
 * @date 2024-08-01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Mapper
public interface SmsProviderMapper extends BaseMappers<SmsProviderEntity, SmsProviderVO, SmsProviderQuery> {

}