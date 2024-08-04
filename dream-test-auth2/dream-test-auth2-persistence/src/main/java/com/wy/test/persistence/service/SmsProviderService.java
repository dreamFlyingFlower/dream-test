package com.wy.test.persistence.service;

import com.wy.test.core.entity.SmsProviderEntity;
import com.wy.test.core.query.SmsProviderQuery;
import com.wy.test.core.vo.SmsProviderVO;

import dream.flying.flower.framework.mybatis.plus.service.BaseServices;

/**
 * 短信记录
 *
 * @author 飞花梦影
 * @date 2024-08-01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public interface SmsProviderService extends BaseServices<SmsProviderEntity, SmsProviderVO, SmsProviderQuery> {

}